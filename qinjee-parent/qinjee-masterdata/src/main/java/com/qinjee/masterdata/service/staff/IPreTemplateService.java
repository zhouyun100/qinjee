package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.model.vo.staff.EmailSendVo;
import com.qinjee.masterdata.model.vo.staff.PreRegistVo;
import com.qinjee.model.request.UserSession;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IPreTemplateService {
    List< EntryRegistrationTableVO> handlerCustomTableGroupFieldList(Integer companyId, Integer preId,Integer templateId) throws IllegalAccessException;

    String createBackGroundPhoto(MultipartFile file, UserSession userSession) throws Exception;

    void sendRegisterMessage(PreRegistVo preRegistVo,UserSession userSession) throws Exception;

    void createPreRegistQrcode(Integer templateId, HttpServletResponse response,UserSession userSession) throws IOException;

    void sendManyMail(EmailSendVo emailSendVo, UserSession userSession);
}
