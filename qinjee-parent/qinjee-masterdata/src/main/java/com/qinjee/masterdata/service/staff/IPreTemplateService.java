package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.staff.AboutMobileMessage;
import com.qinjee.masterdata.model.vo.staff.PreRegistVo;
import com.qinjee.masterdata.model.vo.staff.entryregistration.EntryTableListWithValueVo;
import com.qinjee.model.request.UserSession;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IPreTemplateService {
    AboutMobileMessage handlerCustomTableGroupFieldList(Integer companyId, Integer preId, Integer templateId) throws IllegalAccessException;

    String createBackGroundPhoto(MultipartFile file, UserSession userSession) throws Exception;

    void sendRegisterMessage(PreRegistVo preRegistVo, UserSession userSession) throws Exception;

    void createPreRegistQrcode(Integer templateId, HttpServletResponse response, UserSession userSession) throws Exception;

    List<Integer> selectPreIdByPhone(String phone, UserSession userSession);

    String toCompleteMessage(String phone, String s, String code) throws Exception;

    void commitHr(Integer preId, Integer templateId, Integer companyId);
}
