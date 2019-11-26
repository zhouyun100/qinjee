package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.model.request.UserSession;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public interface IStaffImportAndExportService {
    /**
     * 模板导入
     * @param multipartFile
     * @param userSession
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    void importArcFile(MultipartFile multipartFile, UserSession userSession ) throws Exception;

    /**
     * 模板导出档案
     * @return
     */
    void exportArcFile(ExportFile exportFile, HttpServletResponse response) ;
    /**
     */
    void exportPreFile(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 导出业务类
     * @param exportRequest
     * @param userSession
     */
    void exportBusiness(ExportRequest exportRequest,HttpServletResponse response, UserSession userSession) throws Exception;


    void importPreFile(MultipartFile multipartFile, UserSession userSession) throws Exception;

    void exportBlackFile(ExportRequest exportRequest,HttpServletResponse response,UserSession userSession);

    void exportContractList(ExportRequest exportRequest,HttpServletResponse response,UserSession userSession);
}
