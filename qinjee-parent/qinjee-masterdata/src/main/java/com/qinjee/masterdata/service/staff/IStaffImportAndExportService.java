package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.staff.CheckImportVo;
import com.qinjee.masterdata.model.vo.staff.ContractWithArchiveVo;
import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.model.request.UserSession;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IStaffImportAndExportService {


    /**
     * 模板导出档案
     * @return
     */
    void exportArcFile(List<Integer> list, HttpServletResponse response, UserSession userSession,Integer querySchemaId) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

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
    void exportBusiness(ExportRequest exportRequest,HttpServletResponse response, UserSession userSession,String funcCode) throws Exception;



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
    CheckImportVo importFileAndCheckFile(MultipartFile multipartFile, String funcCode, UserSession userSession,Integer systemDefine) throws Exception;

    /**
     * 取消文件导入
     * @param funcCode
     * @param userSession
     */
    void cancelForImport(String funcCode, UserSession userSession) throws Exception;


    /**
     * 导入黑名单
     * @param userSession
     * @param userSession
     * @throws Exception
     */
    void importBlaFile( UserSession userSession) throws Exception;

    /**
     * 导入合同
     * @param userSession
     * @param userSession
     */
    void importConFile( UserSession userSession) throws Exception;

    /**
     * 导入文件
     * @param userSession
     * @param funcCode
     * @throws Exception
     */
    void importFile( UserSession userSession, String funcCode) throws Exception;

    /**
     * 导出文件txt
     * @param
     * @param response
     * @throws IOException
     */
    void exportCheckFileTxt(String funcCode,HttpServletResponse response,UserSession userSession) throws IOException;

    /**
     * 显示错误信息
     * @param userSession
     * @param funcCode
     */
    String exportCheckFile(UserSession userSession, String funcCode);

    CheckImportVo importFileAndCheckFileBlackList(MultipartFile multipartFile, UserSession userSession) throws Exception;

    CheckImportVo importFileAndCheckFileContract(MultipartFile multipartFile, UserSession userSession) throws Exception;

    void exportContractWithArc(List< ContractWithArchiveVo> list, HttpServletResponse response, UserSession userSession) throws IOException, IllegalAccessException;

    void exportNoConArc(List<Integer> list, HttpServletResponse response, UserSession userSession) throws IOException, IllegalAccessException;
}
