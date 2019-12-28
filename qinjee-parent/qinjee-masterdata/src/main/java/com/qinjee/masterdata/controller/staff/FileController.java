package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.AttchmentRecordVo;
import com.qinjee.masterdata.service.file.IFileOperateService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/file")
@Api(tags = "【文件操作】文件上传下载接口")
public class FileController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private IFileOperateService fileOperateService;

    /**
     * 上传文件
     * @param files 文件数组
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件", notes = "hkt")
    public ResponseResult importFile( @RequestParam MultipartFile[] files) throws Exception {
        Boolean b = checkParam(getUserSession (),files);
        if(b) {
                fileOperateService.putFile (files,getUserSession () );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }

    /**
     * 下载档案文件
     * @param response 响应
     */
    @RequestMapping(value = "/downLoadFile", method = RequestMethod.POST)
    @ApiOperation(value = "下载文件", notes = "hkt")
    public ResponseResult downLoadFile(HttpServletResponse response, @RequestBody List<Integer> list) throws Exception {
        Boolean b = checkParam(response,list);
        if(b) {
                //attachment_id
                fileOperateService.downLoadFile (response,list );
                return null;
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 展示文件
     */
    @RequestMapping(value = "/showFile", method = RequestMethod.GET)
    @ApiOperation(value = "展示文件", notes = "hkt")
    public ResponseResult< PageResult <AttchmentRecordVo>> showFile(@RequestParam List<Integer> orgIdList, Integer pageSize, Integer currengPage) {
        Boolean b = checkParam(orgIdList,getUserSession (),pageSize,currengPage);
        if(b) {
                PageResult < AttchmentRecordVo > attchmentRecordVoPageResult = fileOperateService.selectAttach ( orgIdList, getUserSession (), pageSize, currengPage );
                    return new ResponseResult <> (attchmentRecordVoPageResult, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 展示文件路径
     */
    @RequestMapping(value = "/showFilePath", method = RequestMethod.POST)
    @ApiOperation(value = "展示文件路径", notes = "hkt")
    public ResponseResult<List < URL >> showFilePath(String groupName,Integer id) {
        Boolean b = checkParam(groupName,getUserSession (),id);
        if(b) {
                List < URL > filePath = fileOperateService.getFilePath ( getUserSession (), groupName,id );
                    return new ResponseResult <> (filePath, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 删除文件
     */
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    @ApiOperation(value = "删除文件,预入职也调用此接口", notes = "hkt")
    public ResponseResult deleteFile(@RequestBody List<Integer> id,Integer companyId) {
        Boolean b = checkParam(id);
        if(b) {
                if(checkParam ( companyId )){
                    fileOperateService.deleteFile (id,companyId );
                }else{
                    fileOperateService.deleteFile ( id,getUserSession ().getCompanyId () );
                }
                return new ResponseResult <> (null, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }

    /**
     * 验证文件名称是否合法
     * @param fileName 文件名集合
     */
    @RequestMapping(value = "/checkFielName", method = RequestMethod.POST)
    @ApiOperation(value = "验证文件名称是否合法", notes = "hkt")
    public ResponseResult checkFielName(@RequestBody List<String> fileName) {
        Boolean b = checkParam(fileName,getUserSession ());
        if(b) {
                String s = fileOperateService.checkFielName ( fileName, getUserSession () );
                    return new ResponseResult <> (s, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 验证图片比例关系
     * @param files 图片文件数组
     */
    @RequestMapping(value = "/checkImg", method = RequestMethod.POST)
    @ApiOperation(value = "验证图片比例关系", notes = "hkt")
    public ResponseResult checkImg(@RequestParam MultipartFile[] files) throws Exception {
        Boolean b = checkParam( files );
        if(b) {
                String s = fileOperateService.checkImg ( files, getUserSession () );
                    return new ResponseResult <> (s, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 导出校验文件
     */
    @RequestMapping(value = "/exportCheckFile", method = RequestMethod.POST)
    @ApiOperation(value = "导出校验文件", notes = "hkt")
    public ResponseResult exportCheckFile(HttpServletResponse response) throws IOException {
        Boolean b = checkParam(getUserSession (),response);
        if(b) {
                 fileOperateService.exportCheckFile ( getUserSession (),response);
                 return null;
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }

    /**
     * 预入职模板上传文件
     * @param file 文件
     */
    @RequestMapping(value = "/uploadPreFile", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件", notes = "hkt")
    public ResponseResult uploadPreFile( @RequestParam MultipartFile file,Integer preId,String groupName,Integer companyId,HttpServletResponse response) throws Exception {
        Boolean b = checkParam(file,preId,groupName,companyId,response);
        if(b) {
                response.setHeader ( "Access-Control-Allow-Origin","*" );
                fileOperateService.putPreFile (file,preId,groupName,companyId );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }

    /**
     *展示预入职文件
     * @param companyId 机构id
     * @param preId 预入职id
     */
    @RequestMapping(value = "/showPreFile", method = RequestMethod.GET)
    @ApiOperation(value = "展示预入职的文件", notes = "hkt")
    public ResponseResult< List<AttchmentRecordVo>> showPreFile(@RequestParam Integer companyId,Integer preId,HttpServletResponse response) {
        Boolean b = checkParam(companyId,preId);
        if(b) {

                response.setHeader ( "Access-Control-Allow-Origin","*" );
                List < AttchmentRecordVo> list  = fileOperateService.selectPreAttach (companyId,preId );
                    return new ResponseResult<>  (list, CommonCode.SUCCESS);
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
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
