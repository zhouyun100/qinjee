package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentChangeDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.staff.StatusChange;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ResponseResult insertStatusChange(PreEmployment preEmployment, StatusChange statusChange, Blacklist blacklist) {
        /**
         * 说明：这一张表对应两个页面，一个为延期入职页面，一个为放弃入职页面，在PreEmploymentChange中进行了整合
         * 梳理：预入职状态分为未入职，已入职，延期入职，放弃入职，黑名单。
         * 当变更表的状态为放弃入职时，将放弃入职原因入库，描述为变更描述
         * 为延期入职时，需要将页面的延期入职时间传过来，存到预入职表中
         * 为已入职时，需要将预入职的信息存到档案中（现如今表都是自定义表，如何保证两个表的高度契合）
         * 黑名单时需要将黑名单同步到黑名单表
         */
        String employmentState = preEmployment.getEmploymentState();
        try {
            if(CHANGSTATUS_READY.equals(employmentState)){
                //新增变更表
                preEmploymentChangeDao.insertStatusChange(statusChange);
                //预入职信息转化为档案信息
                //新增档案表
                //TODO 预入职信息转化为档案信息 新增档案表
            }
            if(CHANGSTATUS_DELAY.equals(employmentState) || CHANGSTATUS_GIVEUP.equals(employmentState)){
                //新增变更表
                preEmploymentChangeDao.insertStatusChange(statusChange);
            }
            if(CHANGSTATUS_BLACKLIST.equals(employmentState)){
                //新增变更表
                preEmploymentChangeDao.insertStatusChange(statusChange);
                //加入黑名单
                blacklistDao.insert(blacklist);
            }
        } catch (Exception e) {
            logger.error("更改状态失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }

        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult<PageResult<PreEmployment>> selectPreEmployment(Integer companyId, Integer currentPage, Integer pageSize) {
        try {
            PageHelper.startPage(currentPage,pageSize);
            PageResult<PreEmployment> pageResult=new PageResult<>();
            //数据库中没有机构id
            List<PreEmployment> list=preEmploymentDao.selectPreEmployment(companyId);
            pageResult.setList(list);
            return  new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查找预入职失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

}
