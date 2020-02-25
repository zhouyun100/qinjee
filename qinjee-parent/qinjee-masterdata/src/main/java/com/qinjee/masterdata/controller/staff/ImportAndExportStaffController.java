package com.qinjee.masterdata.controller.staff;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.QuerySchemeDao;
import com.qinjee.masterdata.model.entity.QueryScheme;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.masterdata.service.staff.IStaffImportAndExportService;
import com.qinjee.masterdata.service.staff.impl.StaffContractServiceImpl;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/port")
@Api(tags = "【导入导出】文件导入导出接口")
public class ImportAndExportStaffController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ImportAndExportStaffController.class);
    @Autowired
    private IStaffImportAndExportService staffImportAndExportService;
    @Autowired
    private StaffContractServiceImpl staffContractService;
    @Autowired
    private QuerySchemeDao querySchemeDao;
    @Autowired
    private IStaffArchiveService staffArchiveService;


    /**
     * 自定义文件类型校验以及生成list
     */
    @RequestMapping(value = "/importFileAndCheckFilePre", method = RequestMethod.POST)
    @ApiOperation(value = "预入职自定义文件类型校验以及生成list", notes = "hkt")
    public ResponseResult< CheckImportVo > importFileAndCheckFilePre(@RequestParam("file") MultipartFile multipartFile,Integer systemDefine)  {
        Boolean b = checkParam(multipartFile,getUserSession (),systemDefine);
        if(b) {
            CheckImportVo checkImportVo = null;
            try {
                checkImportVo = staffImportAndExportService.importFileAndCheckFile ( multipartFile,"PRE" , getUserSession (),systemDefine );
                return new ResponseResult<>  (checkImportVo, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> ( null,CommonCode.FAIL );
            }

        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }
    /**
     * 自定义文件类型校验以及生成list
     */
    @RequestMapping(value = "/importFileAndCheckFileArc", method = RequestMethod.POST)
    @ApiOperation(value = "档案自定义文件类型校验以及生成list", notes = "hkt")
    public ResponseResult< CheckImportVo > importFileAndCheckFileArc(@RequestParam("file") MultipartFile multipartFile,Integer systemDefine)   {
        Boolean b = checkParam(multipartFile,getUserSession (),systemDefine);
        if(b) {
            CheckImportVo checkImportVo = null;
            try {
                checkImportVo = staffImportAndExportService.importFileAndCheckFile ( multipartFile,"ARC",  getUserSession (),systemDefine );
                return new ResponseResult<>  (checkImportVo, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult<>  (checkImportVo, CommonCode.FAIL);
            }

        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }
    /**
     * 黑名单文件类型校验以及生成list
     */
    @RequestMapping(value = "/importFileAndCheckFileBlackList", method = RequestMethod.POST)
    @ApiOperation(value = "黑名单文件类型校验以及生成list", notes = "hkt")
    public ResponseResult< CheckImportVo > importFileAndCheckFileBlackList(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        Boolean b = checkParam(multipartFile,getUserSession ());
        if(b) {
                CheckImportVo checkImportVo = staffImportAndExportService.importFileAndCheckFileBlackList ( multipartFile, getUserSession () );
                return new ResponseResult<>  (checkImportVo, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }
    /**
     * 合同文件类型校验以及生成list
     */
    @RequestMapping(value = "/importFileAndCheckFileContract", method = RequestMethod.POST)
    @ApiOperation(value = "合同文件类型校验以及生成list", notes = "hkt")
    public ResponseResult< CheckImportVo > importFileAndCheckFileContract(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        Boolean b = checkParam(multipartFile,getUserSession ());
        if(b) {
                CheckImportVo checkImportVo = staffImportAndExportService.importFileAndCheckFileContract ( multipartFile, getUserSession () );
                return new ResponseResult<>  (checkImportVo, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }
    /**
     * 显示校验信息
     */
    @RequestMapping(value = "/exportCheckFile", method = RequestMethod.GET)
    @ApiOperation(value = "显示校验信息", notes = "hkt")
    public ResponseResult<String> exportCheckFileTxt(String funcCode) {
        Boolean b = checkParam(getUserSession (),funcCode.toUpperCase ());
        if(b) {
                String s = staffImportAndExportService.exportCheckFile ( getUserSession (), funcCode );
                return new ResponseResult<>  (s, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }

    /**
     * 导出校验信息
     */
    @RequestMapping(value = "/exportCheckFileTxt", method = RequestMethod.POST)
    @ApiOperation(value = "导出校验信息", notes = "hkt")
    public ResponseResult exportCheckFileTxt(String funcCode,HttpServletResponse response) throws IOException {
        Boolean b = checkParam(funcCode,response,getUserSession ());
        if(b) {
                staffImportAndExportService.exportCheckFileTxt (funcCode,response,getUserSession () );
                return new ResponseResult<>  (null, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }

    /**
     * 取消导入文件
     */
    @RequestMapping(value = "/cancelForImport", method = RequestMethod.POST)
    @ApiOperation(value = "取消导入文件", notes = "hkt")

    public ResponseResult readyForImport(String funcCode) throws Exception {
        Boolean b = checkParam(funcCode,getUserSession ());
        if(b) {
                staffImportAndExportService.cancelForImport (funcCode.toUpperCase (),getUserSession () );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 导入文件
     */
    @RequestMapping(value = "/importFile", method = RequestMethod.GET)
    @ApiOperation(value = "导入文件", notes = "hkt")

    public ResponseResult importFile(  String funcCode) throws Exception {
        Boolean b = checkParam(getUserSession (),funcCode.toUpperCase ());
        if(b) {
                staffImportAndExportService.importFile (getUserSession (),funcCode.toUpperCase () );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
        }
            return new ResponseResult <> (null, CommonCode.INVALID_PARAM);

    }

    /**
     * 模板导入黑名单
     */
    @RequestMapping(value = "/importBlaFile", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入黑名单", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)

    public ResponseResult importBlaFile() throws Exception {
        Boolean b = checkParam(getUserSession());
        if(b){
                staffImportAndExportService.importBlaFile(getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("文件内容错误");
    }
    /**
     * 模板导入合同
     */
    @RequestMapping(value = "/importConFile", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入合同", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)

    public ResponseResult importConFile() throws Exception {
        Boolean b = checkParam(getUserSession());
        if(b){
                staffImportAndExportService.importConFile(getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("文件内容错误");
    }


    /**
     * 模板导出档案
     */
    @RequestMapping(value = "/exportArcFile", method = RequestMethod.POST)
    @ApiOperation(value = "导出档案模板", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "QuerySchemeId", value = "查询方案id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "人员id集合", paramType = "query", required = true),
//    })
    //导出的文件应该是以.xls结尾
    public ResponseResult exportArcFile(@RequestBody ExportArcParamVo exportArcParamVo,HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        Boolean b = checkParam(getUserSession (),exportArcParamVo,response);
        if(b){
                if(!CollectionUtils.isEmpty ( exportArcParamVo.getList () )){
                    staffImportAndExportService.exportArcFile(exportArcParamVo.getList (),response,getUserSession (),exportArcParamVo.getQuerySchemaId ());
                }else{
                    List < UserArchiveVo > list1 = staffArchiveService.selectByOrgList ( exportArcParamVo.getOrgIdList (), getUserSession () );
                    List < Integer > integers = new ArrayList <> ();
                    for (UserArchiveVo userArchiveVo : list1) {
                        integers.add ( userArchiveVo.getArchiveId () );
                    }
                    staffImportAndExportService.exportArcFile(integers,response,getUserSession (),exportArcParamVo.getQuerySchemaId ());
                }
                return null;
        }
        return  failResponseResult("参数错误");
    }
    /**
     * 导出已签合同人员
     */
    @RequestMapping(value = "/exportArcFileCon", method = RequestMethod.POST)
    @ApiOperation(value = "导出已签合同人员", notes ="hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "QuerySchemeId", value = "查询方案id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "人员id集合", paramType = "query", required = true),
//    })
    //导出的文件应该是以.xls结尾
    public ResponseResult exportArcFileCon( HttpServletResponse response,
                                            @RequestBody ExportReadyConVo exportReadyConVo) throws IOException, IllegalAccessException {
        Boolean b = checkParam(response,getUserSession ());
        if(b){
                List < ContractWithArchiveVo > list = exportReadyConVo.getList ();
                if(CollectionUtils.isEmpty ( list)){
                    Boolean aBoolean = checkParam ( exportReadyConVo.getOrgIdList (), exportReadyConVo.getStatus ());
                    if(aBoolean) {
                        list= staffContractService.selectLaborContractserUserAll ( exportReadyConVo.getOrgIdList (),
                                exportReadyConVo.getStatus (),userSession.getCompanyId() );
                    }else{
                        return  failResponseResult("参数错误");
                    }
                }
                //导出文件
                staffImportAndExportService.exportContractWithArc( list,response,getUserSession ());
                return null;
        }
        return  failResponseResult("参数错误");
    }
    /**
     * 导出未签合同人员
     */
    @RequestMapping(value = "/exportArcFileNoCon", method = RequestMethod.POST)
    @ApiOperation(value = "导出未签合同人员", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "QuerySchemeId", value = "查询方案id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "人员id集合", paramType = "query", required = true),
//    })
    //导出的文件应该是以.xls结尾
    public ResponseResult exportArcFileNoCon(@RequestBody ExportArcParamVo exportArcParamVo, HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        Boolean b = checkParam(response,getUserSession (),exportArcParamVo);
        if(b){
                List < Integer > list = exportArcParamVo.getList ();
//                Integer querySchemaId=0;
//                List < QueryScheme > list2 = querySchemeDao.selectQueryByArchiveId ( getUserSession ().getArchiveId () );
//                for (QueryScheme queryScheme : list2) {
//                    if(1==queryScheme.getIsDefault ()){
//                       querySchemaId=queryScheme.getQuerySchemeId ();
//                       break;
//                    }
//                }
                List < Integer > orgIdList = exportArcParamVo.getOrgIdList ();
                if(!CollectionUtils.isEmpty ( list )){
                    staffImportAndExportService.exportNoConArc( list,response,getUserSession ());
                }else{
                    Boolean aBoolean = checkParam ( orgIdList );
                    if(aBoolean) {
                        List < Integer > list1 = staffContractService.selectNoLaborContractAll ( orgIdList,userSession.getCompanyId() );
                        staffImportAndExportService.exportNoConArc ( list1, response, getUserSession ());
                        return null;
                    }else {
                        return  failResponseResult("参数错误");
                    }
                }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 模板导出预入职
     */
    @RequestMapping(value = "/exportPreFile", method = RequestMethod.POST)
    @ApiOperation(value = "导出预入职模板", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "预入职id集合", paramType = "query", required = true),
//    })

    public ResponseResult exportPreFile(@RequestBody ExportRequest exportRequest,HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
        Boolean b = checkParam(exportRequest,response,getUserSession ());
        if(b) {
            staffImportAndExportService.exportPreFile(exportRequest, response, getUserSession());
            return null;
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 模板导出黑名单
     */
    @RequestMapping(value = "/exportBlackFile", method = RequestMethod.POST)
    @ApiOperation(value = "导出黑名单模板", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "预入职id集合", paramType = "query", required = true),
//    })

    public ResponseResult exportBlackFile(@RequestBody ExportRequest exportRequest,HttpServletResponse response) throws IOException {
        Boolean b = checkParam(exportRequest,response,getUserSession ());
        if(b){
                staffImportAndExportService.exportBlackFile(exportRequest,response,getUserSession());
                return null;
        }
        return  failResponseResult("参数错误");
    }
    /**
     * 模板导出合同表
     */
    @RequestMapping(value = "/exportContractList", method = RequestMethod.POST)
    @ApiOperation(value = "模板导出合同表", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "预入职id集合", paramType = "query", required = true),
//    })

    public ResponseResult exportContractList(@RequestBody ExportRequest exportRequest, HttpServletResponse response) throws IOException {
        Boolean b = checkParam(exportRequest,response,getUserSession ());
        if(b){
                staffImportAndExportService.exportContractList(exportRequest,response,getUserSession());
                return null;
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 模板导出业务类
     */
    @RequestMapping(value = "/exportBusiness", method = RequestMethod.POST)
    @ApiOperation(value = "模板导出业务类", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "预入职id集合", paramType = "query", required = true),
//    })

    public ResponseResult exportBusiness(@RequestBody ExportRequest exportRequest,HttpServletResponse response,String funcCode) throws Exception {
        Boolean b = checkParam(exportRequest,response,getUserSession());
        if(b){
                staffImportAndExportService.exportBusiness(exportRequest,response,getUserSession(),funcCode.toUpperCase ());
                return null;
        }
        return  failResponseResult("参数错误");
    }

    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if(param instanceof UserSession){
                if(null == param|| "".equals(param)){
                    ExceptionCast.cast ( CommonCode.INVALID_SESSION );
                    return false;
                }
            }
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    private ResponseResult failResponseResult(String message){
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        logger.error(message);
        return fail;
    }
}
