package com.qinjee.masterdata.service.email;

import com.qinjee.masterdata.model.entity.EmailConfig;
import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import com.qinjee.model.request.UserSession;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmailRecordService {
   /**
    * 发送邮件
    * @param userSession
    * @param list
    * @param templateId
    * @throws Exception
    */
   void SendMailForPreRegist(UserSession userSession, List<Integer> list,Integer templateId ) throws Exception;


   void recordSendEmail(UserSession userSession, EmailConfig emailConfig, String template, PreEmploymentVo preEmploymentVo);
}
