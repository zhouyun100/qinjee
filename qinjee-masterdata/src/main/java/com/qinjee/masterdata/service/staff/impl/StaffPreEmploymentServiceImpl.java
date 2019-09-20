package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentChangeDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.vo.staff.StatusChange;
import com.qinjee.masterdata.service.staff.IStaffPreEmploymentService;
import com.qinjee.model.response.CommonCode;
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
    private static final String CHANGSTATUS="已延期";
    @Autowired
    private PreEmploymentDao preEmploymentDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private PreEmploymentChangeDao preEmploymentChangeDao;

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
        return new ResponseResult(false, CommonCode.FAIL);
    }

    @Override
    public ResponseResult checkMail(String mail) {
        boolean b = RegexpUtils.checkEmail(mail);
        if (b) {
            return new ResponseResult(true, CommonCode.SUCCESS);
        }
        return new ResponseResult(false, CommonCode.FAIL);
    }

    @Override
    public ResponseResult insertStatusChange(StatusChange statusChange) {
        /**
         * 说明：这一张表对应两个页面，一个为延期入职页面，一个为放弃入职页面，在PreEmploymentChange中进行了整合
         * 梳理：预入职状态分为延期入职，放弃入职，黑名单三种状态。当变更表的状态为放弃入职时，将放弃入职原因入库，描述为变更描述
         * 为延期入职时，需要将页面的延期入职时间传过来，存到预入职表中
         * 黑名单时需要将黑名单同步到黑名单表(待定，因为黑名单理论上是不会入职的)
         */
        try {
            int i = preEmploymentChangeDao.insertStatusChange(statusChange);
        } catch (Exception e) {
            logger.error("新增预入职变更类失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
        try {
            if (CHANGSTATUS.equals(statusChange.getChangeState())) {
                /**
                 * 自定义表更改数据都需要拥有表id这个属性，在自定义数据表里面查找到这个表的记录，然后再解析数据大字段，找到属性进行更改
                 * 然而现在预入职表特殊，会存在一个单独的物理表维护。这里等新建了VO类再来完善
                 *
                 */
                //TODO 是否同步到黑名单表
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("更新延期入职时间失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }

    }
}
