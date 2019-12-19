package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.service.file.IFileOperateService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件", notes = "hkt")
    public ResponseResult ImportFile(MultipartFile multipartFile) {
        Boolean b = checkParam(getUserSession (),multipartFile);
        if(b) {
            try {
                fileOperateService.putFile (multipartFile,getUserSession () );
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
     * @param path
     * @return
     */
    @RequestMapping(value = "/downLoadFile", method = RequestMethod.POST)
    @ApiOperation(value = "下载文件", notes = "hkt")
    public ResponseResult downLoadFile(HttpServletResponse response, String path) {
        Boolean b = checkParam(response,path);
        if(b) {
            try {
                fileOperateService.downLoadFile (response,path );
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
    @RequestMapping(value = "/showFile", method = RequestMethod.POST)
    @ApiOperation(value = "下载文件", notes = "hkt")
    public ResponseResult< List < AttachmentRecord >> showFile(Integer archiveId) {
        Boolean b = checkParam(archiveId,getUserSession ());
        if(b) {
            try {
                List < AttachmentRecord > list = fileOperateService.selectAttach ( archiveId, getUserSession () );
                if(list.size ()>0){

                    return new ResponseResult <> (list, CommonCode.SUCCESS);
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
    public ResponseResult<List < URL >> showFilePath(String groupName) {
        Boolean b = checkParam(groupName,getUserSession ());
        if(b) {
            try {
                List < URL > filePath = fileOperateService.getFilePath ( getUserSession (), groupName );
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
    public ResponseResult deleteFile(Integer id) {
        Boolean b = checkParam(id,userSession);
        if(b) {
            try {
                fileOperateService.deleteFile (id,userSession );
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
    public ResponseResult checkFielName(String fileName) {
        Boolean b = checkParam(fileName,getUserSession ());
        if(b) {
            try {
                Boolean a = fileOperateService.checkFielName ( fileName, getUserSession () );
                if(a){
                    return new ResponseResult <> (null, CommonCode.SUCCESS);
                }else{
                    return new ResponseResult <> ( null,CommonCode.FAIL );
                }
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
