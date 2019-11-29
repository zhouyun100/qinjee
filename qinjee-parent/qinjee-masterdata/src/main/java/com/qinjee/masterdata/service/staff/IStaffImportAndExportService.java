package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.custom.CheckCustomTableVO;
import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.model.request.UserSession;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface IStaffImportAndExportService {
    /**
     * 模板导入档案表
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
    void exportArcFile(ExportFile exportFile, HttpServletResponse response) throws IOException;

    /**
     * 导出预入职
     * @param exportRequest
     * @param response
     * @param userSession
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    void exportPreFile(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException;

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
    void exportBlackFile(ExportRequest exportRequest,HttpServletResponse response,UserSession userSession) throws IOException;

    /**
     * 导出合同表
     * @param exportRequest
     * @param response
     * @param userSession
     */
    void exportContractList(ExportRequest exportRequest,HttpServletResponse response,UserSession userSession) throws IOException;

    /**文件检验
     * @param multipartFile
     */
    List <Map< String,String>> importFileAndCheckFile(MultipartFile multipartFile) throws Exception;

    /**
     * 字段检验
     * @param list
     * @param userSession
     * @return
     */
    List<CheckCustomTableVO> checkFile(List< Map< String, String>> list, UserSession userSession);

    /**
     * 准备上传
     * @param list
     * @param userSession
     */
    void readyForImport(List< Map< String, String>> list, UserSession userSession,String title);

    /**
     * 取消文件导入
     * @param title
     * @param userSession
     */
    void cancelForImport(String title, UserSession userSession) throws Exception;


    /**
     * 导入黑名单
     * @param multipartFile
     * @param userSession
     * @throws Exception
     */
    void importBlaFile(MultipartFile multipartFile, UserSession userSession) throws Exception;

    /**
     * 导入合同
     * @param multipartFile
     * @param userSession
     */
    void importConFile(MultipartFile multipartFile, UserSession userSession) throws Exception;

    /**
     * 导入业务信息
     * @param multipartFile
     * @param userSession
     */
    void importBusinessFile(MultipartFile multipartFile,String title, UserSession userSession) throws Exception;
}
