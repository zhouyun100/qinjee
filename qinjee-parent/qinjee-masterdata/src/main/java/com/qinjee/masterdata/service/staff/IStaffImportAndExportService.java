package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.model.vo.sys.CheckCustomFieldVO;
import com.qinjee.model.request.UserSession;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

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
     * 导入预入职
     * @param multipartFile
     * @param userSession
     * @throws Exception
     */
    void importPreFile(MultipartFile multipartFile, UserSession userSession) throws Exception;

    /**
     * 模板导出档案
     * @return
     */
    void exportArcFile(ExportFile exportFile, HttpServletResponse response) ;

    /**
     * 导出预入职
     * @param exportRequest
     * @param response
     * @param userSession
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    void exportPreFile(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 导出业务类
     * @param exportRequest
     * @param userSession
     */
    void exportBusiness(ExportRequest exportRequest,HttpServletResponse response, UserSession userSession) throws Exception;



    /**
     * 导出黑名单
     * @param exportRequest
     * @param response
     * @param userSession
     */
    void exportBlackFile(ExportRequest exportRequest,HttpServletResponse response,UserSession userSession);

    /**
     * 导出合同表
     * @param exportRequest
     * @param response
     * @param userSession
     */
    void exportContractList(ExportRequest exportRequest,HttpServletResponse response,UserSession userSession);

    /**文件检验
     * @param multipartFile
     */
    List <Map< String,String>> importFileAndCheckFile(MultipartFile multipartFile) throws Exception;

    List< CheckCustomFieldVO > checkFile(List< Map< String, String>> list, UserSession userSession);
}
