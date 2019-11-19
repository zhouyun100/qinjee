package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentChangeDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import com.qinjee.masterdata.model.vo.staff.StatusChangeVo;
import com.qinjee.masterdata.service.staff.IStaffPreEmploymentService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.utils.RegexpUtils;
import com.qinjee.utils.SendManyMailsUtil;
import entity.MailConfig;
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
    private static final String CHANGSTATUS_DELAY = "已延期";
    private static final String CHANGSTATUS_READY = "已入职";
    private static final String CHANGSTATUS_BLACKLIST = "黑名单";
    private static final String CHANGSTATUS_GIVEUP = "放弃入职";
    private static final String PRE_EMPLOYMENT = "t_pre_employment";
    private static final String PRE_DATASOURSE= "PRE";

    @Autowired
    private PreEmploymentDao preEmploymentDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private PreEmploymentChangeDao preEmploymentChangeDao;
    @Autowired
    private BlacklistDao blacklistDao;
    @Autowired
    private CustomArchiveFieldDao customArchiveFieldDao;
    @Autowired
    private CustomArchiveTableDao customArchiveTableDao;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendManyMail(List<Integer> preList, List<Integer> conList, String content,
                             String subject, List<String> filepath) throws Exception {
        //根据预入职id查找邮箱
        for (Integer integer1 : preList) {
            if (preEmploymentDao.selectMaxId() < integer1) {
                throw new Exception("id出错");
            }
        }
        List<String> tomails = preEmploymentDao.getMail(preList);

        //根据档案id查询邮箱
        Integer max = userArchiveDao.selectMaxId();
        for (Integer integer : conList) {
            if (max < integer) {
                throw new Exception("id出错");
            }
        }
        List<String> mails = userArchiveDao.selectMail(conList);
        MailConfig mailConfig = new MailConfig();
        SendManyMailsUtil.sendMail(mailConfig,tomails, mails, subject, content, filepath);
    }

    @Override
    public boolean checkPhone(String phoneNumber) {
        return RegexpUtils.checkPhone(phoneNumber);
    }

    @Override
    public boolean checkMail(String mail) {
        return RegexpUtils.checkEmail(mail);
    }

    /**
     * 说明：这一张表对应两个页面，一个为延期入职页面，一个为放弃入职页面，在PreEmploymentChange中进行了整合
     * 梳理：预入职状态分为未入职，已入职，延期入职，放弃入职，黑名单。
     * 当变更表的状态为放弃入职时，将放弃入职原因入库，描述为变更描述
     * 为延期入职时，需要将页面的延期入职时间传过来，存到预入职表中
     * 黑名单时需要将黑名单同步到黑名单表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertStatusChange(UserSession userSession, Integer preEmploymentId,
                                   StatusChangeVo statusChangeVo) {
        String changeState = statusChangeVo.getChangeState();
        PreEmployment preEmployment = preEmploymentDao.selectByPrimaryKey(preEmploymentId);
        Integer preChangeId = getPreChangeId(preEmploymentId);

        if (CHANGSTATUS_READY.equals(changeState)) {
            //如果存在就更新
            if(preChangeId !=null && preChangeId != 0) {
               gotPreEmploymentChange(userSession.getArchiveId(),preEmploymentId,statusChangeVo,preChangeId);
            }else {
                //新增变更表
                getPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo);
            }
                //根据预入职id查找预入职对象
                //预入职信息转化为档案信息
                //新增档案表
                UserArchive userArchive = new UserArchive();
                preEmployment.setEmploymentState(CHANGSTATUS_READY);
                BeanUtils.copyProperties(userArchive, preEmployment);
                //删除预入职表
                preEmploymentDao.deletePreEmployment(preEmploymentId);
        }
        if (CHANGSTATUS_DELAY.equals(changeState)) {
            if(preChangeId !=null && preChangeId != 0) {
                gotPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo,preChangeId);
            }else {
                //新增变更表
                getPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo);
            }
            //将预入职的入职时间重新设置
            preEmployment.setHireDate(statusChangeVo.getDelayTime());
            preEmploymentDao.updateByPrimaryKey(preEmployment);
            preEmployment.setEmploymentState(CHANGSTATUS_DELAY);
        }
        if (CHANGSTATUS_GIVEUP.equals(changeState)) {
            if(preChangeId !=null && preChangeId != 0) {
                gotPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo,preChangeId);
            }
            else {
                //新增变更表
                getPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo);
            }
                preEmployment.setEmploymentState(CHANGSTATUS_GIVEUP);
        }

        if (CHANGSTATUS_BLACKLIST.equals(changeState)) {
            if(preChangeId !=null && preChangeId != 0) {
                gotPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo,preChangeId);
            }else {
                //新增变更表
                getPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo);
            }
            preEmployment.setEmploymentState(CHANGSTATUS_BLACKLIST);
            //加入黑名单
            Blacklist blacklist = new Blacklist();
            blacklist.setBlockReason(statusChangeVo.getAbandonReason());
            blacklist.setDataSource(PRE_DATASOURSE);
            BeanUtils.copyProperties(preEmployment,blacklist);
            blacklist.setOperatorId(userSession.getArchiveId());
            blacklistDao.insertSelective(blacklist);
        }
    }


    private void getPreEmploymentChange(Integer archiveId, Integer preEmploymentId, StatusChangeVo statusChangeVo) {
        PreEmploymentChange preEmploymentChange = new PreEmploymentChange();
        BeanUtils.copyProperties(statusChangeVo, preEmploymentChange);
        preEmploymentChange.setEmploymentId(preEmploymentId);
        preEmploymentChange.setOperatorId(archiveId);
        preEmploymentChangeDao.insertSelective(preEmploymentChange);
    }
    private void gotPreEmploymentChange(Integer archiveId, Integer preEmploymentId, StatusChangeVo statusChangeVo,Integer changId) {
        PreEmploymentChange preEmploymentChange = new PreEmploymentChange();
        preEmploymentChange.setChangeId(changId);
        BeanUtils.copyProperties(statusChangeVo, preEmploymentChange);
        preEmploymentChange.setEmploymentId(preEmploymentId);
        preEmploymentChange.setOperatorId(archiveId);
        preEmploymentChangeDao.updateByPrimaryKeySelective(preEmploymentChange);
    }


    @Override
    public void insertPreEmployment(PreEmploymentVo preEmploymentVo,UserSession userSession) {
        PreEmployment preEmployment=new PreEmployment();
        BeanUtils.copyProperties(preEmploymentVo,preEmployment);
        preEmployment.setCompanyId(userSession.getCompanyId());
        preEmployment.setOperatorId(userSession.getArchiveId());
        preEmployment.setIsDelete((short) 0);
        preEmploymentDao.insert(preEmployment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePreEmployment(List<Integer> list) throws Exception {

        Integer max = preEmploymentDao.selectMaxId();
        for (Integer integer : list) {
            if (integer > max) {
                throw new Exception("id不合法");
            }
        }
        preEmploymentDao.deletePreEmploymentList(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreEmployment(PreEmploymentVo preEmploymentVo,UserSession userSession) {
        PreEmployment preEmployment=new PreEmployment();
        BeanUtils.copyProperties(preEmploymentVo,preEmployment);
        preEmployment.setCompanyId(userSession.getCompanyId());
        preEmployment.setOperatorId(userSession.getArchiveId());
        preEmployment.setIsDelete((short) 0);
        preEmploymentDao.updateByPrimaryKey(preEmployment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreEmploymentField(Map<Integer, String> map)  {
        customArchiveFieldDao.updatePreEmploymentField(map);
    }

    @Override
    public PageResult<PreEmployment> selectPreEmployment(Integer companyId, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<PreEmployment> list = preEmploymentDao.selectPreEmployment(companyId);
        return new PageResult<>(list);
    }

    @Override
    public Map<String, String> selectPreEmploymentField(UserSession userSession) {
        Map<String, String> map = new HashMap<>();
        //先找到对应的表id
        Integer id = customArchiveTableDao.selectByComIdAndPhyName(userSession.getCompanyId(), PRE_EMPLOYMENT);
        //找到table的字段对象
        List<CustomArchiveField> list = customArchiveFieldDao.selectFieldByTableId(id);
        for (CustomArchiveField customArchiveField : list) {
            map.put(customArchiveField.getFieldCode(), customArchiveField.getFieldName());
        }
        return map;
    }
    /**
     *
     查询是否已经存在预入职变更表
     */
    private Integer getPreChangeId(Integer preEmploymentId){
        return preEmploymentChangeDao.selectIdByPreId(preEmploymentId);
    }



}
