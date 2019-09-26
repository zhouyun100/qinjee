package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomTableDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentChangeDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.StatusChangeVo;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.masterdata.service.staff.IStaffPreEmploymentService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.RegexpUtils;
import com.qinjee.utils.SendManyMailsUtil;
import com.qinjee.utils.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
public class StaffPreEmploymentServiceImpl implements IStaffPreEmploymentService {
    private static final Logger logger = LoggerFactory.getLogger(StaffPreEmploymentServiceImpl.class);
    private static final String CHANGSTATUS_DELAY="已延期";
    private static final String CHANGSTATUS_READY="已入职";
    private static final String CHANGSTATUS_BLACKLIST="黑名单";
    private static final String CHANGSTATUS_GIVEUP="放弃入职";
    private static final String PRE_EMPLOYMENT="预入职表";
    @Autowired
    private PreEmploymentDao preEmploymentDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private PreEmploymentChangeDao preEmploymentChangeDao;
    @Autowired
    private IStaffArchiveService staffArchiveService;
    @Autowired
    private BlacklistDao blacklistDao;
    @Autowired
    private CustomFieldDao customFieldDao;
    @Autowired
    private CustomTableDao customTableDao;

    @Override
    public ResponseResult sendMessage(List<Integer> list, Integer templateId, String[] params) {
        try {
            String[] phonenumbers = null;
            Integer max = preEmploymentDao.selectMaxId();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.FAIL);
                }
                String phoneNumber = preEmploymentDao.getPhoneNumber(list.get(i));
                phonenumbers[i] = phoneNumber;
            }
            //TODO 固定字段需要配置，现已写死
            SendMessage.sendMessageMany(1, "1", templateId, "勤杰软件", phonenumbers, params);
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("发送短信失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult sendManyMail(List<Integer> prelist, List<Integer> conList, String content, String subject, String[] filepath) {
        String[] tomails = null;
        String[] mails = null;
        //根据预入职id查找邮箱
        try {
            for (int i = 0; i < prelist.size(); i++) {
                if (preEmploymentDao.selectMaxId() < prelist.get(i)) {
                    return new ResponseResult(false, CommonCode.FAIL);
                }
                String tomail = preEmploymentDao.getMail(prelist.get(i));
                tomails[i] = tomail;
            }
        } catch (Exception e) {
            logger.error("查询发送人邮箱失败");
        }
        //根据档案id查询邮箱
        try {
            Integer max = userArchiveDao.selectMaxId();
            for (int i = 0; i < conList.size(); i++) {
                if (max < conList.get(i)) {
                    return new ResponseResult(false, CommonCode.FAIL);
                }
                String mail = userArchiveDao.selectMail(conList.get(i));
                mails[i] = mail;
            }
            //TODO 邮箱工具类中还需要确定模板，这里的发件人也需要再配置，现已写死
            SendManyMailsUtil.getInstance().sendMail("123", tomails, mails, subject, content, filepath);
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询抄送人失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult checkPhone(String phoneNumber) {
        boolean b = RegexpUtils.checkPhone(phoneNumber);
        if (b) {
            return new ResponseResult(true, CommonCode.SUCCESS);
        }
        logger.error("号码验证失败");
        return new ResponseResult(false, CommonCode.FAIL);
    }

    @Override
    public ResponseResult checkMail(String mail) {
        boolean b = RegexpUtils.checkEmail(mail);
        if (b) {
            return new ResponseResult(true, CommonCode.SUCCESS);
        }
        logger.error("邮箱验证失败");
        return new ResponseResult(false, CommonCode.FAIL);
    }

    @Override
    @Transactional
    public ResponseResult insertStatusChange(Integer companyId, Integer archiveId,Integer preEmploymentId,
                                             StatusChangeVo statusChangeVo,String reason) {
        /**
         * 说明：这一张表对应两个页面，一个为延期入职页面，一个为放弃入职页面，在PreEmploymentChange中进行了整合
         * 梳理：预入职状态分为未入职，已入职，延期入职，放弃入职，黑名单。
         * 当变更表的状态为放弃入职时，将放弃入职原因入库，描述为变更描述
         * 为延期入职时，需要将页面的延期入职时间传过来，存到预入职表中
         * 黑名单时需要将黑名单同步到黑名单表
         */
        String changeState = statusChangeVo.getChangeState();
        PreEmployment preEmployment = preEmploymentDao.selectByPrimaryKey(preEmploymentId);
        try {
            if(CHANGSTATUS_READY.equals(changeState)){
                //新增变更表
                getPreEmploymentChange(archiveId, preEmploymentId, statusChangeVo);
                //根据预入职id查找预入职对象
                //预入职信息转化为档案信息
                //新增档案表
                UserArchive userArchive=new UserArchive();
                preEmployment.setEmploymentState(CHANGSTATUS_READY);
                BeanUtils.copyProperties(userArchive,preEmployment);

                //删除预入职表
                preEmploymentDao.deletePreEmployment(preEmploymentId);
            }
            if(CHANGSTATUS_DELAY.equals(changeState)){
                //新增变更表
                getPreEmploymentChange(archiveId, preEmploymentId, statusChangeVo);
                preEmployment.setEmploymentState(CHANGSTATUS_DELAY);
            }
            if(CHANGSTATUS_GIVEUP.equals(changeState)){
                //新增变更表
                getPreEmploymentChange(archiveId, preEmploymentId, statusChangeVo);
                preEmployment.setEmploymentState(CHANGSTATUS_GIVEUP);
            }

            if(CHANGSTATUS_BLACKLIST.equals(changeState)){
                //新增变更表
                getPreEmploymentChange(archiveId, preEmploymentId, statusChangeVo);
                preEmployment.setEmploymentState(CHANGSTATUS_BLACKLIST);
                //加入黑名单
                Blacklist blacklist = new Blacklist();
                blacklist.setBlockReason(reason);
                blacklist.setDataSource("预入职");
                blacklist.setCompanyId(companyId);
                blacklist.setOperatorId(archiveId);
                blacklistDao.insertSelective(blacklist);
            }
        } catch (Exception e) {
            logger.error("更改状态失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }

        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    private void getPreEmploymentChange(Integer archiveId, Integer preEmploymentId, StatusChangeVo statusChangeVo) {
        PreEmploymentChange preEmploymentChange = new PreEmploymentChange();
        BeanUtils.copyProperties(statusChangeVo, preEmploymentChange);
        preEmploymentChange.setEmploymentId(preEmploymentId);
        preEmploymentChange.setOperatorId(archiveId);
        preEmploymentChangeDao.insertSelective(preEmploymentChange);
    }


    @Override
    public ResponseResult insertPreEmployment(PreEmployment preEmployment) {
        try {
            if(preEmployment instanceof  PreEmployment){
                preEmploymentDao.insertSelective(preEmployment);
            }
        } catch (Exception e) {
            logger.error("新增预入职失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
        return  new ResponseResult(false,CommonCode.INVALID_PARAM);
    }

    @Override
    @Transactional
    public ResponseResult deletePreEmployment(List<Integer> list) {
        Integer integer =null;
        try {
            integer=preEmploymentDao.selectMaxId();
            for (Integer integer1 : list) {
                if(integer1>integer){
                    return new ResponseResult(false,CommonCode.INVALID_PARAM);
                }
                preEmploymentDao.deletePreEmployment(integer1);
            }
            return  new ResponseResult<>(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除预入职失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult updatePreEmployment(PreEmployment preEmployment) {
        try {
            if(preEmployment instanceof PreEmployment) {
                preEmploymentDao.updateByPrimaryKey(preEmployment);
                return new ResponseResult<>(true, CommonCode.SUCCESS);
            }
        } catch (Exception e) {
            logger.error("更新预入职物理表失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
        return new ResponseResult(false,CommonCode.INVALID_PARAM);
    }
    @Override
    public ResponseResult updatePreEmploymentField(Map<Integer, String> map) {
        if(map!=null){
            try {
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    customFieldDao.updatePreEmploymentField(entry.getKey(),entry.getValue());
                    return new ResponseResult<>(true, CommonCode.SUCCESS);
                }
            } catch (Exception e) {
                logger.error("更新预入职字段表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(false,CommonCode.INVALID_PARAM);
    }
    @Override
    public ResponseResult<PageResult<PreEmployment>> selectPreEmployment(Integer companyId, Integer currentPage, Integer pageSize) {
        try {
            PageHelper.startPage(currentPage,pageSize);
            PageResult<PreEmployment> pageResult=new PageResult<>();
            List<PreEmployment> list=preEmploymentDao.selectPreEmployment(companyId);
            pageResult.setList(list);
            return  new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查找预入职失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }
    @Override
    public ResponseResult<Map<String,String>>selectPreEmploymentField(Integer companyId) {
        Map<String,String> map=new HashMap<>();
        //先找到对应的表id
        try {
            Integer id=customTableDao.selectPreEmploymentTable(companyId,PRE_EMPLOYMENT);
            //找到table的字段对象
            List<CustomField> list=customFieldDao.selectPreEmploymentField(id);
            for (CustomField customField : list) {
                map.put(customField.getFieldCode(),customField.getFieldName());
            }
            return new ResponseResult<>(map,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查找预入职字段失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }


}
