package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentChangeDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.StatusChangeVo;
import com.qinjee.masterdata.service.staff.IStaffPreEmploymentService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.utils.RegexpUtils;
import com.qinjee.utils.SendManyMailsUtil;
import com.qinjee.utils.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private static final String PRE_EMPLOYMENT = "预入职表";
    private static final String APPKEY = "91c94cbe664487bbfb072e717957e08f";
    private static final Integer APPID = 1400249114;
    private static final String SENDER = "huangkt@qinjee.cn";
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessage(List<Integer> list, Integer templateId, List<String> params) throws Exception {
        List<String> phonenumbers=new ArrayList<>();
        String[] strings = new String[params.size()];
        params.toArray(strings);
        Integer max = preEmploymentDao.selectMaxId();
        for (Integer integer : list) {
            if (max < integer) {
                throw new Exception("id出错");
            }
            String phoneNumber = preEmploymentDao.getPhoneNumber(integer);
            phonenumbers.add(phoneNumber);
        }
        //TODO 固定字段需要配置，现已写死
        SendMessage.sendMessageMany(APPID, APPKEY, templateId, "勤杰软件", phonenumbers, strings);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendManyMail(List<Integer> prelist, List<Integer> conList, String content,
                             String subject, List<String> filepath) throws Exception {
        //根据预入职id查找邮箱
        for (Integer integer1 : prelist) {
            if (preEmploymentDao.selectMaxId() < integer1) {
                throw new Exception("id出错");
            }
        }
        List<String> tomails = preEmploymentDao.getMail(prelist);

        //根据档案id查询邮箱
        Integer max = userArchiveDao.selectMaxId();
        for (Integer integer : conList) {
            if (max < integer) {
                throw new Exception("id出错");
            }
        }
        List<String> mails = userArchiveDao.selectMail(conList);

        //TODO 邮箱工具类中还需要确定模板，这里的发件人也需要再配置，现已写死
        SendManyMailsUtil.getInstance().sendMail(SENDER, tomails, mails, subject, content, filepath);
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
                                   StatusChangeVo statusChangeVo, String reason) {
        String changeState = statusChangeVo.getChangeState();
        PreEmployment preEmployment = preEmploymentDao.selectByPrimaryKey(preEmploymentId);

        if (CHANGSTATUS_READY.equals(changeState)) {
            //新增变更表
            getPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo);
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
            //新增变更表
            getPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo);
            preEmployment.setEmploymentState(CHANGSTATUS_DELAY);
        }
        if (CHANGSTATUS_GIVEUP.equals(changeState)) {
            //新增变更表
            getPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo);
            preEmployment.setEmploymentState(CHANGSTATUS_GIVEUP);
        }

        if (CHANGSTATUS_BLACKLIST.equals(changeState)) {
            //新增变更表
            getPreEmploymentChange(userSession.getArchiveId(), preEmploymentId, statusChangeVo);
            preEmployment.setEmploymentState(CHANGSTATUS_BLACKLIST);
            //加入黑名单
            Blacklist blacklist = new Blacklist();
            blacklist.setBlockReason(reason);
            blacklist.setDataSource("预入职");
            blacklist.setCompanyId(userSession.getCompanyId());
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


    @Override
    public void insertPreEmployment(PreEmployment preEmployment) {
        preEmploymentDao.insertSelective(preEmployment);
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
    public void updatePreEmployment(PreEmployment preEmployment) {
        preEmploymentDao.updateByPrimaryKey(preEmployment);
    }

    @Override
    public void updatePreEmploymentField(Map<Integer, String> map) {
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
        List<CustomField> list = customArchiveFieldDao.selectFieldByTableiId(id);
        for (CustomField customField : list) {
            map.put(customField.getFieldCode(), customField.getFieldName());
        }
        return map;
    }


}
