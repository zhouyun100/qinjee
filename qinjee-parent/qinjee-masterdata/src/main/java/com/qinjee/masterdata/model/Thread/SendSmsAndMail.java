package com.qinjee.masterdata.model.Thread;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.model.vo.staff.PreRegistVo;
import com.qinjee.masterdata.service.email.EmailRecordService;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.masterdata.utils.ThreadAutowired;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import lombok.Data;

import java.util.List;

@Data
public class SendSmsAndMail implements Runnable {

    private SmsRecordService smsRecordService;
    private EmailRecordService emailRecordService;
    private PreRegistVo preRegistVo;
    private UserSession userSession;

    public SendSmsAndMail(PreRegistVo preRegistVo,UserSession userSession){
        this.preRegistVo=preRegistVo;
        this.userSession=userSession;
    }
    @Override
    public void run() {
        this.smsRecordService= ThreadAutowired.getBean ( SmsRecordService.class );
        this.emailRecordService=ThreadAutowired.getBean ( EmailRecordService.class );
        List < Integer > list = preRegistVo.getList();
        for (Integer sendWay : preRegistVo.getSendWay ()) {
            if (sendWay.equals ( 1 )) {
                //短信发送
                try {
                    smsRecordService.sendMessageSms ( list, preRegistVo.getTemplateId (), userSession );
                } catch (Exception e) {
                    ExceptionCast.cast ( CommonCode.FAIL );
                }
            } else if (sendWay.equals ( 2 )) {
                //邮件发送
                try {
                    emailRecordService.SendMailForPreRegist ( userSession, list, preRegistVo.getTemplateId () );
                } catch (Exception e) {
                    ExceptionCast.cast ( CommonCode.FAIL );
                }
            }
        }
    }
}
