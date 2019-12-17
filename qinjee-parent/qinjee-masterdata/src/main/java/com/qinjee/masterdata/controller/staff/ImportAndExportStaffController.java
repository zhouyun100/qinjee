package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.vo.staff.CheckImportVo;
import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportNoConArcVo;
import com.qinjee.masterdata.service.staff.IStaffImportAndExportService;
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
import javax.validation.Valid;
import java.io.IOException;
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
    private UserArchiveDao userArchiveDao;
    /**
     * 自定义文件类型校验以及生成list
     */
    @RequestMapping(value = "/importFileAndCheckFilePre", method = RequestMethod.POST)
    @ApiOperation(value = "预入职自定义文件类型校验以及生成list", notes = "hkt")
    public ResponseResult< CheckImportVo > importFileAndCheckFilePre(@RequestParam("file") MultipartFile multipartFile) {
        Boolean b = checkParam(multipartFile,getUserSession ());
        if(b) {
            try {
                CheckImportVo checkImportVo = staffImportAndExportService.importFileAndCheckFile ( multipartFile,"PRE" , getUserSession () );
                return new ResponseResult  (checkImportVo, CommonCode.SUCCESS);
            } catch (IOException e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FILE_PARSING_EXCEPTION);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }
    /**
     * 自定义文件类型校验以及生成list
     */
    @RequestMapping(value = "/importFileAndCheckFileArc", method = RequestMethod.POST)
    @ApiOperation(value = "档案自定义文件类型校验以及生成list", notes = "hkt")
    public ResponseResult< CheckImportVo > importFileAndCheckFileARC(@RequestParam("file") MultipartFile multipartFile) {
        Boolean b = checkParam(multipartFile,getUserSession ());
        if(b) {
            try {
                CheckImportVo checkImportVo = staffImportAndExportService.importFileAndCheckFile ( multipartFile,"ARC",  getUserSession () );
                return new ResponseResult  (checkImportVo, CommonCode.SUCCESS);
            } catch (IOException e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FILE_PARSING_EXCEPTION);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }
    /**
     * 黑名单文件类型校验以及生成list
     */
    @RequestMapping(value = "/importFileAndCheckFileBlackList", method = RequestMethod.POST)
    @ApiOperation(value = "黑名单文件类型校验以及生成list", notes = "hkt")
    public ResponseResult< CheckImportVo > importFileAndCheckFileBlackList(@RequestParam("file") MultipartFile multipartFile) {
        Boolean b = checkParam(multipartFile,getUserSession ());
        if(b) {
            try {
                CheckImportVo checkImportVo = staffImportAndExportService.importFileAndCheckFileBlackList ( multipartFile, getUserSession () );
                return new ResponseResult  (checkImportVo, CommonCode.SUCCESS);
            }catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }
    /**
     * 合同文件类型校验以及生成list
     */
    @RequestMapping(value = "/importFileAndCheckFileContract", method = RequestMethod.POST)
    @ApiOperation(value = "合同文件类型校验以及生成list", notes = "hkt")
    public ResponseResult< CheckImportVo > importFileAndCheckFileContract(@RequestParam("file") MultipartFile multipartFile) {
        Boolean b = checkParam(multipartFile,getUserSession ());
        if(b) {
            try {
                CheckImportVo checkImportVo = staffImportAndExportService.importFileAndCheckFileContract ( multipartFile, getUserSession () );
                return new ResponseResult  (checkImportVo, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode.BUSINESS_EXCEPTION);
            }
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
            try {
                String s = staffImportAndExportService.exportCheckFile ( getUserSession (), funcCode );
                return new ResponseResult  (s, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }

    /**
     * 导出校验信息
     */
    @RequestMapping(value = "/exportCheckFileTxt", method = RequestMethod.POST)
    @ApiOperation(value = "导出校验信息", notes = "hkt")
    public ResponseResult exportCheckFileTxt(String funcCode,HttpServletResponse response) {
        Boolean b = checkParam(funcCode,response,getUserSession ());
        if(b) {
            try {
                staffImportAndExportService.exportCheckFileTxt (funcCode,response,getUserSession () );
                return new ResponseResult  (null, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.FILE_EMPTY);
    }

    /**
     * 取消导入文件
     */
    @RequestMapping(value = "/cancelForImport", method = RequestMethod.POST)
    @ApiOperation(value = "取消导入文件", notes = "hkt")

    public ResponseResult readyForImport(String funcCode) {
        Boolean b = checkParam(funcCode,getUserSession ());
        if(b) {
            try {
                staffImportAndExportService.cancelForImport (funcCode.toUpperCase (),getUserSession () );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult <> (null, CommonCode. REDIS_KEY_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 导入文件
     */
    @RequestMapping(value = "/importFile", method = RequestMethod.GET)
    @ApiOperation(value = "导入文件", notes = "hkt")

    public ResponseResult ImportFile(  String funcCode) {
        Boolean b = checkParam(getUserSession (),funcCode.toUpperCase ());
        if(b) {
            try {
                staffImportAndExportService.importFile (getUserSession (),funcCode.toUpperCase () );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. REDIS_KEY_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }

    /**
     * 模板导入黑名单
     */
    @RequestMapping(value = "/importBlaFile", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入黑名单", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)

    public ResponseResult importBlaFile() {
        Boolean b = checkParam(getUserSession());
        if(b){
            try {
                staffImportAndExportService.importBlaFile(getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导入失败");
            }
        }
        return  failResponseResult("文件内容错误");
    }
    /**
     * 模板导入合同
     */
    @RequestMapping(value = "/importConFile", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入合同", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)

    public ResponseResult importConFile() {
        Boolean b = checkParam(getUserSession());
        if(b){
            try {
                staffImportAndExportService.importConFile(getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导入失败");
            }
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
    public ResponseResult exportArcFile(@RequestParam List<Integer> list, HttpServletResponse response) {
        Boolean b = checkParam(list,response,getUserSession ());
        if(b){
            try {
                staffImportAndExportService.exportArcFile(list,response,getUserSession ());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导出失败");
            }
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
    public ResponseResult exportArcFileNoCon(@RequestBody @Valid ExportNoConArcVo exportNoConArcVo, HttpServletResponse response) {
        Boolean b = checkParam(response,getUserSession (),exportNoConArcVo);
        if(b){
            try {
                if(CollectionUtils.isEmpty ( exportNoConArcVo.getList () )){
                    List< UserArchiveVo > arcList=userArchiveDao.selectArcByNotCon(exportNoConArcVo.getOrgId ());
                    List < Integer > objects = new ArrayList <> ();
                    for (UserArchiveVo userArchiveVo : arcList) {
                        objects.add ( userArchiveVo.getArchiveId () );
                    }
                   exportNoConArcVo.setList ( objects );
                }
                staffImportAndExportService.exportArcFile(exportNoConArcVo.getList (),response,getUserSession ());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导出失败");
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

    public ResponseResult exportPreFile(@RequestBody ExportRequest exportRequest,HttpServletResponse response) {
        Boolean b = checkParam(exportRequest,response,getUserSession ());
        if(b){
            try {
                staffImportAndExportService.exportPreFile(exportRequest,response,getUserSession () );
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导出失败");
            }
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

    public ResponseResult exportBlackFile(@RequestBody ExportRequest exportRequest,HttpServletResponse response) {
        Boolean b = checkParam(exportRequest,response,getUserSession ());
        if(b){
            try {
                staffImportAndExportService.exportBlackFile(exportRequest,response,getUserSession());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导出失败");
            }
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

    public ResponseResult exportContractList(@RequestBody ExportRequest exportRequest, HttpServletResponse response) {
        Boolean b = checkParam(exportRequest,response,getUserSession ());
        if(b){
            try {
                staffImportAndExportService.exportContractList(exportRequest,response,getUserSession());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导出失败");
            }
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

    public ResponseResult exportBusiness(@RequestBody ExportRequest exportRequest,HttpServletResponse response,String funcCode) {
        Boolean b = checkParam(exportRequest,response,getUserSession());
        if(b){
            try {
                staffImportAndExportService.exportBusiness(exportRequest,response,getUserSession(),funcCode.toUpperCase ());
                return null;
            } catch (Exception e) {
                e.printStackTrace ();
                return failResponseResult("导出失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    private Boolean checkParam(Object... params) {
        for (Object param : params) {
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
