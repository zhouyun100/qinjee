package com.qinjee.masterdata.service.email.impl;

import com.alibaba.fastjson.JSON;
import com.qinjee.masterdata.dao.email.EmailConfigDao;
import com.qinjee.masterdata.dao.email.EmailRecordDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.model.entity.EmailConfig;
import com.qinjee.masterdata.model.entity.EmailRecord;
import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.email.EmailConfigService;
import com.qinjee.masterdata.service.email.EmailRecordService;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.model.request.UserSession;
import com.qinjee.utils.SendManyMailsUtil;
import com.qinjee.utils.ShortEncryptUtils;
import entity.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
public class EmailRecordServiceImpl implements EmailRecordService {

    @Autowired
    private EmailConfigService emailConfigService;

    @Autowired
    private PreEmploymentDao preEmploymentDao;

    @Autowired
    private EmailConfigDao emailConfigDao;

    @Autowired
    private RedisClusterService redisClusterService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private EmailRecordDao emailRecordDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void SendMailForPreRegist(UserSession userSession, List <Integer> list,Integer templateId) throws Exception {
        //获取邮件配置类
        EmailConfig emailConfigByCompanyId = emailConfigService.getEmailConfigByCompanyId ( userSession.getCompanyId () );
        MailConfig mailConfig = emailConfigService.handlerEmailtoMail ( emailConfigByCompanyId );
        //通过类型与companyId找到发送模板
        String template=emailConfigDao.selectTemplate("ENTRY_REGISTRATION");
        //设置前端链接
        String baseShortUrl = sysDictService.searchSysDictByTypeAndCode ( "SHORT_URL", "EMPLOYMENT_REGISTER" ).getDictValue ();
        for (Integer integer : list) {
            //找到预入职人详细信息
            PreEmploymentVo preEmploymentVo = preEmploymentDao.selectPreEmploymentVoById ( integer );
            List < String > objects = new ArrayList <> (1);
            objects.add ( preEmploymentVo.getEmail () );
            String keyValue = "preId=" + preEmploymentVo.getEmploymentId () +"&templateId="+ templateId+"&companyId="+userSession.getCompanyId ();
            String key = ShortEncryptUtils.shortEncrypt ( keyValue );
            Map <String,Integer> stringMap = new HashMap <> (  );
            stringMap.put ( "preId", preEmploymentVo.getEmploymentId ());
            stringMap.put ( "templateId",  templateId);
            stringMap.put ( "companyId",  userSession.getCompanyId ());
            String url = baseShortUrl + key;

            //将短链接作为key，带参数的链接为value存到redis中，有效期为2小时
            redisClusterService.setex(key,2*60*60, JSON.toJSONString ( stringMap));
            //模板设置
            MessageFormat messageFormat = new MessageFormat(template);
            String[] paramArr = {preEmploymentVo.getUserName (),preEmploymentVo.getApplicationPosition (), url};
            //发送邮件
            template = messageFormat.format ( paramArr );
            SendManyMailsUtil.sendMail (mailConfig,objects,null,"预入职登记",template,null);
            //邮件记录
            recordSendEmail ( userSession, emailConfigByCompanyId, template, preEmploymentVo );
        }
    }
    @Override
    public void recordSendEmail(UserSession userSession, EmailConfig emailConfig, String template, PreEmploymentVo preEmploymentVo) {
        EmailRecord emailRecord = new EmailRecord ();
        emailRecord.setEmailTemplateId (1);
        emailRecord.setEmailConfigId (emailConfig.getEmailConfigId () );
        emailRecord.setEmailTitle ( "预入职登记" );
        emailRecord.setEmailContent ( template );
        emailRecord.setBusinessType ( "预入职登记" );
        emailRecord.setFromUser ( userSession.getUserName () );
        emailRecord.setToUser ( preEmploymentVo.getUserName () );
        emailRecord.setOperatorId ( userSession.getArchiveId () );
        emailRecord.setSendStatus ( "已发送" );
        emailRecordDao.insertEmailRecord ( emailRecord );
    }

}
