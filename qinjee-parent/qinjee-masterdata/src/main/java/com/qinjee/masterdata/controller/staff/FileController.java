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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
     * @param
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件", notes = "hkt")
    public ResponseResult importFile( @RequestParam MultipartFile[] files) {
        Boolean b = checkParam(getUserSession (),files);
        if(b) {
            try {
                fileOperateService.putFile (files,getUserSession () );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FAIL);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }

    /**
     * 下载文件
     * @param response
     * @return
     */
    @RequestMapping(value = "/downLoadFile", method = RequestMethod.POST)
    @ApiOperation(value = "下载文件", notes = "hkt")
    public ResponseResult downLoadFile(HttpServletResponse response, @RequestBody List<Integer> list) {
        Boolean b = checkParam(response,list);
        if(b) {
            try {
                //attachment_id
                fileOperateService.downLoadFile (response,list );
                return null;
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FAIL);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 展示文件
     * @return
     */
    @RequestMapping(value = "/showFile", method = RequestMethod.GET)
    @ApiOperation(value = "展示文件", notes = "hkt")
    public ResponseResult< PageResult <AttchmentRecordVo>> showFile(@RequestParam List<Integer> orgIdList, Integer pageSize, Integer currengPage) {
        Boolean b = checkParam(orgIdList,getUserSession (),pageSize,currengPage);
        if(b) {
            try {
                PageResult < AttchmentRecordVo > attchmentRecordVoPageResult = fileOperateService.selectAttach ( orgIdList, getUserSession (), pageSize, currengPage );
                if(attchmentRecordVoPageResult!=null){
                    return new ResponseResult <> (attchmentRecordVoPageResult, CommonCode.SUCCESS);
                }else{
                    return new ResponseResult <> ( null,CommonCode.FAIL_VALUE_NULL );
                }
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FAIL);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 展示文件路径
     * @return
     */
    @RequestMapping(value = "/showFilePath", method = RequestMethod.POST)
    @ApiOperation(value = "展示文件路径", notes = "hkt")
    public ResponseResult<List < URL >> showFilePath(String groupName,Integer id) {
        Boolean b = checkParam(groupName,getUserSession (),id);
        if(b) {
            try {
                List < URL > filePath = fileOperateService.getFilePath ( getUserSession (), groupName,id );
                if(filePath.size ()>0){

                    return new ResponseResult <> (filePath, CommonCode.SUCCESS);
                }else{
                    return new ResponseResult <> ( null,CommonCode.FAIL_VALUE_NULL );
                }
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FAIL);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 删除文件
     */
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    @ApiOperation(value = "删除文件", notes = "hkt")
    public ResponseResult deleteFile(@RequestBody List<Integer> id) {
        Boolean b = checkParam(id,getUserSession ());
        if(b) {
            try {
                fileOperateService.deleteFile (id,getUserSession () );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FAIL);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }

    /**
     * 验证文件名称是否合法
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/checkFielName", method = RequestMethod.POST)
    @ApiOperation(value = "验证文件名称是否合法", notes = "hkt")
    public ResponseResult checkFielName(@RequestBody List<String> fileName) {
        Boolean b = checkParam(fileName,getUserSession ());
        if(b) {
            try {
                String s = fileOperateService.checkFielName ( fileName, getUserSession () );
                if(StringUtils.isNotBlank ( s )){
                    return new ResponseResult <> (s, CommonCode.SUCCESS);
                }else{
                    return new ResponseResult <> ( null,CommonCode.CHECK_FALSE );
                }
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FAIL);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 验证图片比例关系
     * @param files
     * @return
     */
    @RequestMapping(value = "/checkImg", method = RequestMethod.POST)
    @ApiOperation(value = "验证图片比例关系", notes = "hkt")
    public ResponseResult checkImg(@RequestParam MultipartFile[] files) {
        Boolean b = checkParam( files );
        if(b) {
            try {
                String s = fileOperateService.checkImg ( files, getUserSession () );
                if(StringUtils.isNotBlank ( s )){
                    return new ResponseResult <> (s, CommonCode.SUCCESS);
                }else{
                    return new ResponseResult <> ( null,CommonCode.CHECK_FALSE );
                }
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FAIL);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 导出校验文件
     * @return
     */
    @RequestMapping(value = "/exportCheckFile", method = RequestMethod.POST)
    @ApiOperation(value = "导出校验文件", notes = "hkt")
    public ResponseResult exportCheckFile(HttpServletResponse response) {
        Boolean b = checkParam(getUserSession (),response);
        if(b) {
            try {
                 fileOperateService.exportCheckFile ( getUserSession (),response);
                 return null;
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. FAIL);
            }
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
