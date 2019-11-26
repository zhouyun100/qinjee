package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.service.staff.IStaffImportAndExportService;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】文件导入导出接口")
public class ImportAndExportStaffController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private IStaffImportAndExportService staffImportAndExportService;

    /**
     * 模板导入档案
     */
    @RequestMapping(value = "/importArcFile", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入档案", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)

    public ResponseResult importArcFile(MultipartFile multipartFile) {
        Boolean b = checkParam(multipartFile);
        if(b){
            try {
                staffImportAndExportService.importArcFile(multipartFile,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导入失败");
            }
        }
        return  failResponseResult("path错误");
    }
    /**
     * 模板导入预入职
     */
    @RequestMapping(value = "/importPreFile", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入预入职", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)

    public ResponseResult importPreFile(MultipartFile multipartFile) {
        Boolean b = checkParam(multipartFile,getUserSession());
        if(b){
            try {
                staffImportAndExportService.importPreFile(multipartFile,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导入失败");
            }
        }
        return  failResponseResult("path错误");
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
    public ResponseResult exportArcFile(@RequestBody @Valid ExportFile exportFile, HttpServletResponse response) {
        Boolean b = checkParam(exportFile,response);
        if(b){
            try {
                staffImportAndExportService.exportArcFile(exportFile,response);
                return ResponseResult.SUCCESS();
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

    public ResponseResult export(@RequestBody ExportRequest exportRequest,HttpServletResponse response) {
        Boolean b = checkParam(exportRequest,response,getUserSession ());
        if(b){
            try {
                staffImportAndExportService.exportPreFile(exportRequest,response,getUserSession () );
                return ResponseResult.SUCCESS();
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

    public ResponseResult exportPreBla(@RequestBody ExportRequest exportRequest,HttpServletResponse response) {
        Boolean b = checkParam(exportRequest,response,getUserSession ());
        if(b){
            try {
                staffImportAndExportService.exportBlackFile(exportRequest,response,getUserSession());
                return ResponseResult.SUCCESS();
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
                return ResponseResult.SUCCESS();
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

    public ResponseResult exportBusiness(@RequestBody ExportRequest exportRequest,HttpServletResponse response) {
        Boolean b = checkParam(exportRequest,response,getUserSession());
        if(b){
            try {
                staffImportAndExportService.exportBusiness(exportRequest,response,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
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
