package com.qinjee.masterdata.service.organation;

import com.alibaba.fastjson.JSON;
import com.github.liaochong.myexcel.core.SaxExcelReader;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.MyCollectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @program: eTalent
 * @description: 组织相关接口支持类。
 * @author: penghs
 * @create: 2020-01-08 14:52
 **/
@Component
public abstract class AbstractOrganizationHelper<T> {
    private static Logger logger = LogManager.getLogger(AbstractOrganizationHelper.class);

    @Autowired
    private RedisClusterService redisService;

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    private final static String XLS = "XLS";
    private final static String XLSX = "XLSX";
    @Autowired
    private OrganizationDao organizationDao;


    protected ResponseResult doUploadAndCheck(MultipartFile multfile, Class clazz, UserSession userSession, HttpServletResponse response) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        HashMap<Object, Object> resultMap = new HashMap<>();
        //excel文件基本校验
        basicCheck(multfile);
        //excel文件导入内存
        List<T> excelList = importFromExcel(multfile, clazz);
        List<T> userArchiveList = new ArrayList<>(excelList.size());
        //进行一些前置处理，并把excel原表信息与处理后的信息存入redis
        List<T> dataList = preHandle(excelList, userArchiveList);
        String key = "userDataKey" + multfile.getOriginalFilename().hashCode() + "-" + RandomStringUtils.random( 6, true, true);
        //将记录行号后的集合存入redis
        putIntoRedis(key, dataList, resultMap);
        //将上一步处理后的信息进行规则校
        List<T> checkList = checkExcel(dataList, userSession);
        //处理校验结果
        String errorKey = "errorInfoKey" + multfile.getOriginalFilename().hashCode() + "-" + RandomStringUtils.random( 6, true, true);
        handleErrorInfo(errorKey, checkList, resultMap, responseResult);
        responseResult.setResult(resultMap);
        return responseResult;
    }

    //导入xls/xlsx文件基本校验
    private boolean basicCheck(MultipartFile multfile) {
        //判断文件名
        String filename = multfile.getOriginalFilename();
        if (!(filename.endsWith(xls) || filename.endsWith(xlsx) || filename.endsWith(XLS) || filename.endsWith(XLSX))) {
            //文件格式不对
            ExceptionCast.cast(CommonCode.FILE_FORMAT_ERROR);
        }

        return false;
    }

    private List<T> importFromExcel(MultipartFile multfile, Class<T> clazz) throws IOException {
        String filename = multfile.getOriginalFilename();
        logger.info("上传文件名：" + filename);
        File tempFile = null;
        if (filename.endsWith("xls") || filename.endsWith("XLS")) {
            tempFile = File.createTempFile("temp", ".xls");//必须要是xlsx
        } else {
            tempFile = File.createTempFile("temp", ".xlsx");
        }


        multfile.transferTo(tempFile);
        List<T> excelDataList = null;
        try {
            excelDataList = SaxExcelReader.of(clazz).sheet(0).rowFilter(row -> row.getRowNum() > 0).read(tempFile);
        } catch (Exception e) {
            ExceptionCast.cast(CommonCode.FILE_IMPORT_FAILED);
        }
        tempFile.delete();
        if (CollectionUtils.isEmpty(excelDataList)) {
            //excel为空
            ExceptionCast.cast(CommonCode.FILE_EMPTY);
        }
        return excelDataList;
    }

    protected abstract List<T> checkExcel(List<T> userArchiveList, UserSession userSession);


    private void putIntoRedis(String key, List<T> dataList, HashMap<Object, Object> resultMap) {
        redisService.del(key);
        String json = JSON.toJSONString(dataList);
        redisService.setex(key, 10 * 60, json);
        //将原表信息及redis key置入返回对象
        resultMap.put("excelList", dataList);
        resultMap.put("redisKey", key);
    }

    private List<T> preHandle(List<T> excelList, List<T> dataList) {
        //记录行号
        Integer number = 1;
        for (T vo : excelList) {
            try {
                Method setLineNumber = vo.getClass().getMethod("setLineNumber", Integer.class);
                setLineNumber.invoke(vo, ++number);
                //排序前记录行号
                dataList.add(vo);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return dataList;
    }

    private void handleErrorInfo(String key, List<T> checkList, HashMap<Object, Object> resultMap, ResponseResult responseResult) throws Exception {

        //如果不为空则校验成功,将错误信息、原表数据存储到redis后抛出异常
        if (!CollectionUtils.isEmpty(checkList)) {
            StringBuilder errorSb = new StringBuilder();
            errorSb.append("行号    |        错误信息\r\n");
            errorSb.append("--------------------------------\r\n");
            for (T error : checkList) {
                Method getLineNumber = error.getClass().getMethod("getLineNumber");
                Method getResultMsg = error.getClass().getMethod("getResultMsg");
                Integer lineNum = (Integer) getLineNumber.invoke(error);
                String msg = (String) getResultMsg.invoke(error);
                errorSb.append(lineNum + " -   " + msg + "\r\n");
            }
            redisService.del(key);
            String errorStr = errorSb.toString();
            redisService.setex(key, 10 * 60, errorStr);
            //将错误信息置入返回对象
            resultMap.put("failCheckList", checkList);
            resultMap.put("errorInfoKey", key);
            //文件解析失败
            responseResult.setResultCode(CommonCode.FILE_PARSING_EXCEPTION);
        } else {
            responseResult.setResultCode(CommonCode.SUCCESS);
            responseResult.setMessage("文件校验成功");
        }
    }

}
