/**
 * 文件名：CheckCustomFieldServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/26
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sys.impl;

import com.qinjee.masterdata.dao.sys.CheckCustomFieldDao;
import com.qinjee.masterdata.model.vo.sys.CheckCustomFieldVO;
import com.qinjee.masterdata.model.vo.sys.CheckCustomTableVO;
import com.qinjee.masterdata.service.sys.CheckCustomFieldService;
import com.qinjee.utils.RegexpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


/**
 * @author 周赟
 * @date 2019/11/22
 */
@Service
public class CheckCustomFieldServiceImpl implements CheckCustomFieldService {

    public static final String CUSTOM_FIELD_TYPE_VARCHAR = "VARCHAR";
    public static final String CUSTOM_FIELD_TYPE_INTEGER = "INTEGER";
    public static final String CUSTOM_FIELD_TYPE_DECIMAL = "DECIMAL";
    public static final String CUSTOM_FIELD_TYPE_DATE = "DATE";
    public static final String CUSTOM_FIELD_TYPE_DATETIME = "DATETIME";
    public static final String CUSTOM_FIELD_TYPE_TEXT = "TEXT";
    public static final String CUSTOM_FIELD_TYPE_BOOLEAN = "BOOLEAN";
    public static final String CUSTOM_FIELD_TYPE_CODE = "CODE";
    public static final String CUSTOM_FIELD_TYPE_MULCODE = "MULCODE";
    public static final String CUSTOM_FIELD_TYPE_RELATE = "RELATE";

    public static final String CUSTOM_FIELD_CHECK_TYPE_NOTNULL = "NOTNULL";
    public static final String CUSTOM_FIELD_CHECK_TYPE_EMAIL = "EMAIL";
    public static final String CUSTOM_FIELD_CHECK_TYPE_ID_CARD = "ID_CARD";
    public static final String CUSTOM_FIELD_CHECK_TYPE_DATE = "DATE";
    public static final String CUSTOM_FIELD_CHECK_TYPE_INTEGER = "INTEGER";

    public static final String CUSTOM_FIELD_TYPE_BOOLEAN_TRUE = "true";
    public static final String CUSTOM_FIELD_TYPE_BOOLEAN_FALSE = "false";

    @Autowired
    private CheckCustomFieldDao checkCustomFieldDao;

    @Override
    public List<CheckCustomTableVO> checkCustomFieldValue(List<Integer> fileIdList, List<Map<Integer, Object>> mapList) {

        List<CheckCustomTableVO> checkCustomTableVOList = new ArrayList<>();
        List<CheckCustomFieldVO> customFieldValueList;
        CheckCustomTableVO customTableVO;
        CheckCustomFieldVO customFieldVO;
        StringBuffer resultMsg;
        Boolean checkResult;

        //查询自定义字段详细信息，并转化为Map，方便通过fieldId获取对象
        List<CheckCustomFieldVO> customFieldDetailList = checkCustomFieldDao.searchCustomFieldList(fileIdList);
        Map<Integer,CheckCustomFieldVO> customFieldMap = new HashMap<>(customFieldDetailList.size());
        for(CheckCustomFieldVO customFieldDetail : customFieldDetailList){
            customFieldMap.put(customFieldDetail.getFieldId(),customFieldDetail);
        }

        //循环大表数据
        for(Map<Integer,Object> map : mapList){
            customFieldValueList = new ArrayList<>();
            resultMsg = new StringBuffer();
            customTableVO = new CheckCustomTableVO();
            checkResult = true;

            //循环每条记录的每个字段对应的值
            for(Map.Entry<Integer,Object> entry : map.entrySet()){
                //获取字段的配置信息
                customFieldVO = customFieldMap.get(entry.getKey());
                if(customFieldVO == null){
                    continue;
                }

                //设置字段录入的值
                customFieldVO.setFieldValue(String.valueOf(entry.getValue()));

                //字段验证类型校验
                validCustomFieldCheckType(customFieldVO);
                //字段类型校验
                validCustomFieldType(customFieldVO);

                //每条记录中但凡有一个字段校验不通过，则视为整行数据均不予通过
                //无校验失败则默认校验通过
                if(customFieldVO.getCheckResult() == null){
                    customFieldVO.setCheckResult(true);
                }else if(!customFieldVO.getCheckResult()){
                    checkResult = false;

                    //错误信息追加
                    resultMsg.append(customFieldVO.getResultMsg());
                }

                customFieldValueList.add(customFieldVO);
            }

            customTableVO.setCustomFieldVOList(customFieldValueList);
            customTableVO.setCheckResult(checkResult);
            customTableVO.setResultMsg(resultMsg.toString());
            checkCustomTableVOList.add(customTableVO);
        }
        return checkCustomTableVOList;
    }

    @Override
    public void validCustomFieldValue(CheckCustomFieldVO customFieldVO) {

        //字段验证类型校验
        validCustomFieldCheckType(customFieldVO);

        //字段类型校验
        validCustomFieldType(customFieldVO);

        //无校验失败则默认校验通过
        if(customFieldVO.getCheckResult() == null){
            customFieldVO.setCheckResult(true);
        }
    }

    /**
     * 字段验证类型
     * @param customFieldVO
     */
    public void validCustomFieldCheckType(CheckCustomFieldVO customFieldVO) {

        //验证不为空
        if(StringUtils.isBlank(customFieldVO.getFieldValue())){
            if(StringUtils.isNoneBlank(customFieldVO.getIsNotNull()) && customFieldVO.getIsNotNull().equals(CUSTOM_FIELD_CHECK_TYPE_NOTNULL)){
                handlerCheckCustomFieldVO(customFieldVO, false,"数据为空！");
            }
            return;
        }

        //验证日期格式
        if(StringUtils.isNoneBlank(customFieldVO.getIsDate()) && customFieldVO.getIsDate().equals(CUSTOM_FIELD_CHECK_TYPE_DATE)){
            if(!RegexpUtils.checkDateTime(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }
        }

        //验证整数类型
        if(StringUtils.isNoneBlank(customFieldVO.getIsInteger()) && customFieldVO.getIsInteger().equals(CUSTOM_FIELD_CHECK_TYPE_INTEGER)){
            if(!RegexpUtils.checkInteger(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }
        }

        //验证身份证
        if(StringUtils.isNoneBlank(customFieldVO.getIsIdCard()) && customFieldVO.getIsIdCard().equals(CUSTOM_FIELD_CHECK_TYPE_ID_CARD)){
            if(!RegexpUtils.checkIdCard(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }
        }

        //验证邮箱
        if(StringUtils.isNoneBlank(customFieldVO.getIsEmail()) && customFieldVO.getIsEmail().equals(CUSTOM_FIELD_CHECK_TYPE_EMAIL)){
            if(!RegexpUtils.checkEmail(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }
        }
    }

    /**
     * 字段类型
     * @param customFieldVO
     */
    public void validCustomFieldType(CheckCustomFieldVO customFieldVO) {
        if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_BOOLEAN)){

            //验证是否型数据
            if(!customFieldVO.getFieldValue().equals(CUSTOM_FIELD_TYPE_BOOLEAN_TRUE) && !customFieldVO.getFieldValue().equals(CUSTOM_FIELD_TYPE_BOOLEAN_FALSE)){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }

        }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_DATE)){

            //验证日期格式
            if(!RegexpUtils.checkDate(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }

        }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_DATETIME)){

            //验证日期时间格式
            if(!RegexpUtils.checkTime(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }

        }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_INTEGER)){

            //整数型
            if(!RegexpUtils.checkInteger(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }else{
                //整数长度验证
                validValLength(customFieldVO);
            }

        }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_VARCHAR) ||
                customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_TEXT)){

            //字符型、备注型，文本长度验证
            validValLength(customFieldVO);

        }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_DECIMAL)){

            //数值型
            if(!RegexpUtils.checkDecimal(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
            }else{
                //数据精度验证
                validValPrecision(customFieldVO);
            }

        }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_CODE)){

        }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_MULCODE)){

        }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_RELATE)){

        }
    }

    /**
     * 校验长度
     * @param customFieldVO
     */
    private void validValLength(CheckCustomFieldVO customFieldVO){
        if(customFieldVO.getValLength() != null && customFieldVO.getFieldValue().length() > customFieldVO.getValLength()){
            handlerCheckCustomFieldVO(customFieldVO, false,"长度超过上限！");
        }
    }

    /**
     * 校验精度
     * @param customFieldVO
     */
    private void validValPrecision(CheckCustomFieldVO customFieldVO){
        if(customFieldVO.getValLength() != null && customFieldVO.getValPrecision() != null){
            String[] strArr = customFieldVO.getFieldValue().split("\\.");
            int decimalDigit = strArr.length > 1 ? strArr[1].length() : 0;
            if(strArr[0].length() > customFieldVO.getValLength() ||
                    (decimalDigit > customFieldVO.getValPrecision())){
                handlerCheckCustomFieldVO(customFieldVO, false,"数值精度不正确！");
            }
        }
    }

    private void handlerCheckCustomFieldVO(CheckCustomFieldVO customFieldVO,Boolean checkResult,String resultMsg){
        customFieldVO.setCheckResult(checkResult);
        customFieldVO.setResultMsg(customFieldVO.getFieldValue() + resultMsg);
    }

}
