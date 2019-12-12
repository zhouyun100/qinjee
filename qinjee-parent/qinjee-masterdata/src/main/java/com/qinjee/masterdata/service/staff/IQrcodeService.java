package com.qinjee.masterdata.service.staff;

import com.qinjee.model.request.UserSession;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public interface IQrcodeService {


    void sendPreCheckCode(String phone);

    void checkPreCodeAndRedirect(String phone, String code, HttpServletResponse response, Integer templateId,Integer companyId) throws Exception;

    void dealQrcodeRequest(Date date, Integer templateId, Integer companyId);
}
