package com.qinjee.masterdata.service.staff;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public interface IQrcodeService {
    void dealQrcodeRequest(HttpServletResponse response, Date date, Integer templateId, Integer companyId) throws IOException;
}
