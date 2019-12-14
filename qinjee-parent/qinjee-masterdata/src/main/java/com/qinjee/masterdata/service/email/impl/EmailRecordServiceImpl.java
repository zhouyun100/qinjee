package com.qinjee.masterdata.service.email.impl;

import com.qinjee.consts.AesKeyConsts;
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
import com.qinjee.utils.AesUtils;
import com.qinjee.utils.SendManyMailsUtil;
import entity.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
            //将短链接作为key，带参数的链接为value存到redis中，有效期为2小时
            redisClusterService.setex ( AesUtils.aesEncrypt ( baseShortUrl + "?" + preEmploymentVo.getEmploymentId () +"?"+ templateId, AesKeyConsts.PRE_SMS_AES_KEY ),
                    2*60*60,baseShortUrl + "?" +preEmploymentVo.getEmploymentId () +"?"+ templateId);
            //模板设置
            template.replace ( "{姓名}",preEmploymentVo.getUserName () );
            template.replace ( "{企业名称}", preEmploymentVo.getApplicationPosition ());
            template.replace ( "{短链接}",baseShortUrl + "?" +preEmploymentVo.getEmploymentId () +"?"+ templateId );
            //发送邮件
            SendManyMailsUtil.sendMail (mailConfig,objects,null,"预入职登记",template,null);
            //邮件记录
            EmailRecord emailRecord = new EmailRecord ();
            emailRecord.setEmailTemplateId (1);
            emailRecord.setEmailConfigId (emailConfigByCompanyId.getEmailConfigId () );
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

    @Override
    public void RecordSendEmail(EmailConfig emailConfig,MailConfig mailConfig) {

        EmailRecord emailRecord = new EmailRecord ();

        emailConfig.setEmailConfigId ( emailConfig.getEmailConfigId () );



    }



}
