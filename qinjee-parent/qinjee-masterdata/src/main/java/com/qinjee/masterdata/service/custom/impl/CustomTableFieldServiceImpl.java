/**
 * 文件名：CustomTableFieldServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/28
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.custom.impl;

import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.model.entity.SysDict;
import com.qinjee.masterdata.model.vo.custom.*;
import com.qinjee.masterdata.service.custom.CustomTableFieldService;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.utils.RegexpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 周赟
 * @date 2019/11/28
 */
@Service
public class CustomTableFieldServiceImpl implements CustomTableFieldService {


    public static final String CUSTOM_FIELD_TYPE_DATE = "date";
    public static final String CUSTOM_FIELD_TYPE_TIME = "time";
    public static final String CUSTOM_FIELD_TYPE_DATETIME = "dateTime";

    public static final String CUSTOM_TEXT_TYPE_TEXT = "text";
    public static final String CUSTOM_TEXT_TYPE_NUMBER = "number";
    public static final String CUSTOM_TEXT_TYPE_DATE = "date";

    public static final String CUSTOM_FIELD_CHECK_TYPE_EMAIL = "email";
    public static final String CUSTOM_FIELD_CHECK_TYPE_ID_CARD = "id_card";
    public static final String CUSTOM_FIELD_CHECK_TYPE_PHONE = "phone";


    @Autowired
    private CustomTableFieldDao customTableFieldDao;

    @Autowired
    private SysDictService sysDictService;

    @Override
    public List<CheckCustomTableVO> checkCustomFieldValue(List<Integer> fileIdList, List<Map<Integer, Object>> mapList) {

        List<CheckCustomTableVO> checkCustomTableVOList = new ArrayList<>();
        List<CheckCustomFieldVO> customFieldValueList;
        CheckCustomTableVO customTableVO;
        CheckCustomFieldVO customFieldVO;
        StringBuffer resultMsg;
        Boolean checkResult;

        //查询自定义字段详细信息，并转化为Map，方便通过fieldId获取对象
        List<CustomFieldVO> customFieldDetailList = customTableFieldDao.searchCustomFieldListByFieldIdList(fileIdList);
        Map<Integer,CheckCustomFieldVO> customFieldMap = new HashMap<>(customFieldDetailList.size());
        for(CustomFieldVO customFieldDetail : customFieldDetailList){
            CheckCustomFieldVO checkCustomFieldVO = new CheckCustomFieldVO();
            BeanUtils.copyProperties(customFieldDetail,checkCustomFieldVO);
            customFieldMap.put(customFieldDetail.getFieldId(),checkCustomFieldVO);
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

                //字段值规则校验
                validCustomFieldValue(customFieldVO);

                //每条记录中但凡有一个字段校验不通过，则视为整行数据均不予通过
                if(!customFieldVO.getCheckResult()){
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


    /**
     * 校验自定义字段值（单个校验）
     * @param customFieldVO
     */
    @Override
    public void validCustomFieldValue(CheckCustomFieldVO customFieldVO) {

        //验证不为空
        if(customFieldVO.getIsMust() != null && customFieldVO.getIsMust() == 1){
            if(StringUtils.isBlank(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"数据不能为空！");
                return;
            }
        }

        //值类型校验
        if(customFieldVO.getTextType().equals(CUSTOM_TEXT_TYPE_DATE)){

            //日期验证
            validDate(customFieldVO);

        }else if(customFieldVO.getTextType().equals(CUSTOM_TEXT_TYPE_TEXT)){

            //文本验证
            validText(customFieldVO);

        }else if(customFieldVO.getTextType().equals(CUSTOM_TEXT_TYPE_NUMBER)){

            //数据验证
            validNumber(customFieldVO);

        }

        //规则校验
        if(StringUtils.isNoneBlank(customFieldVO.getRule())){
            String [] ruleArr = customFieldVO.getRule().split(",");
            for(String rule : ruleArr){
                //验证身份证
                if(rule.equals(CUSTOM_FIELD_CHECK_TYPE_ID_CARD)){
                    if(!RegexpUtils.checkIdCard(customFieldVO.getFieldValue())){
                        handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
                    }
                }

                //验证邮箱
                if(rule.equals(CUSTOM_FIELD_CHECK_TYPE_EMAIL)){
                    if(!RegexpUtils.checkEmail(customFieldVO.getFieldValue())){
                        handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
                    }
                }

                //验证手机号
                if(!rule.equals(CUSTOM_FIELD_CHECK_TYPE_PHONE)){
                    if(!RegexpUtils.checkPhone(customFieldVO.getFieldValue())){
                        handlerCheckCustomFieldVO(customFieldVO, false,"格式不正确！");
                    }
                }
            }
        }

        //无校验失败则默认校验通过
        if(customFieldVO.getCheckResult() == null){
            customFieldVO.setCheckResult(true);
        }

    }

    /**
     * 字段类型
     * @param customFieldVO
     */
    public void validDate(CheckCustomFieldVO customFieldVO) {
        if(StringUtils.isNoneBlank(customFieldVO.getFieldValue()) && StringUtils.isNoneBlank(customFieldVO.getFieldType())){

            //日期格式根据字段类型fieldType分别判断date、time、datetime
            if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_DATE) && !RegexpUtils.checkDate(customFieldVO.getFieldValue())){

                handlerCheckCustomFieldVO(customFieldVO, false,"日期格式不正确!");

            }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_TIME) && !RegexpUtils.checkTime(customFieldVO.getFieldValue())){

                handlerCheckCustomFieldVO(customFieldVO, false,"时间格式不正确!");

            }else if(customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_DATETIME) && !RegexpUtils.checkDateTime(customFieldVO.getFieldValue())){

                handlerCheckCustomFieldVO(customFieldVO, false,"日期时间格式不正确!");

            }

        }

    }

    /**
     * 文本校验
     * @param customFieldVO
     */
    private void validText(CheckCustomFieldVO customFieldVO){
        if(StringUtils.isNoneBlank(customFieldVO.getFieldValue())){
            if(customFieldVO.getMaxLength() != null){
                if(customFieldVO.getFieldValue().length() > customFieldVO.getMaxLength()){
                    handlerCheckCustomFieldVO(customFieldVO, false,"内容太多，字符数超过：" + customFieldVO.getMaxLength());
                }
            }
            if(customFieldVO.getMinLength() != null){
                if(customFieldVO.getFieldValue().length() < customFieldVO.getMinLength()){
                    handlerCheckCustomFieldVO(customFieldVO, false,"内容太少，字符数超过：" + customFieldVO.getMinLength());
                }
            }
        }
    }

    /**
     * 校验精度
     * @param customFieldVO
     */
    private void validNumber(CheckCustomFieldVO customFieldVO){
        if(StringUtils.isNoneBlank(customFieldVO.getFieldValue())){
            if(!RegexpUtils.checkDecimal(customFieldVO.getFieldValue())){
                handlerCheckCustomFieldVO(customFieldVO, false,"不是数值类型！");
            }else{
                if(customFieldVO.getMaxNumber() != null){
                    if(Double.valueOf(customFieldVO.getFieldValue()) >= customFieldVO.getMaxNumber()){
                        handlerCheckCustomFieldVO(customFieldVO, false,"数值不能大于" + customFieldVO.getMaxNumber());
                    }
                }

                if(customFieldVO.getMinNumber() != null){
                    if(Double.valueOf(customFieldVO.getFieldValue()) <= customFieldVO.getMinNumber()){
                        handlerCheckCustomFieldVO(customFieldVO, false,"数值不能小于" + customFieldVO.getMinNumber());
                    }
                }

                if(customFieldVO.getFloatLength() != null){
                    String[] strArr = customFieldVO.getFieldValue().split("\\.");
                    int decimalDigit = strArr.length > 1 ? strArr[1].length() : 0;
                    if(decimalDigit > customFieldVO.getFloatLength()){
                        handlerCheckCustomFieldVO(customFieldVO, false,"小数位超过" + customFieldVO.getFloatLength());
                    }
                }
            }
        }
    }

    private void handlerCheckCustomFieldVO(CheckCustomFieldVO customFieldVO,Boolean checkResult,String resultMsg){
        customFieldVO.setCheckResult(checkResult);
        customFieldVO.setResultMsg(customFieldVO.getFieldValue() + resultMsg);
    }

    @Override
    public List<CustomTableVO> searchCustomTableListByCompanyIdAndFuncCode(CustomTableVO customTableVO) {
            customTableVO.setIsEnable(Short.valueOf("1"));
        List<CustomTableVO> customTableList = customTableFieldDao.searchCustomTableListByCompanyIdAndFuncCode(customTableVO);
        return customTableList;
    }

    @Override
    public CustomTableVO searchCustomTableGroupFieldListByTableCode(Integer companyId, String tableCode) {

        List<CustomGroupVO> customGroupList = customTableFieldDao.searchCustomGroupList(companyId,tableCode,null);

        List<CustomFieldVO> customFieldList = customTableFieldDao.searchCustomFieldList(companyId,tableCode,null);

        CustomTableVO customTable = handlerGroupFieldToTable(customGroupList,customFieldList);

        return customTable;
    }

    @Override
    public CustomTableVO searchCustomTableGroupFieldListByTableId(Integer tableId) {
        List<CustomGroupVO> customGroupList = customTableFieldDao.searchCustomGroupList(null,null, tableId);

        List<CustomFieldVO> customFieldList = customTableFieldDao.searchCustomFieldList(null,null,tableId);

        CustomTableVO customTable = handlerGroupFieldToTable(customGroupList,customFieldList);

        return customTable;
    }

    private CustomTableVO handlerGroupFieldToTable(List<CustomGroupVO> customGroupList, List<CustomFieldVO> customFieldList){
        CustomTableVO customTable = new CustomTableVO();

        //如果没有自定义组，则设置一个groupId为0的空组
        if(CollectionUtils.isEmpty(customGroupList) || customGroupList.size() == 0){
            customGroupList = new ArrayList<>();
            CustomGroupVO groupVO = new CustomGroupVO();
            groupVO.setCustomFieldVOList(customFieldList);
            customGroupList.add(groupVO);
            customTable.setCustomGroupVOList(customGroupList);
        }else{
            for(CustomGroupVO groupVO : customGroupList){

                List<CustomFieldVO> fieldList = customFieldList.stream().filter(customFieldVO -> {
                    if(customFieldVO.getGroupId().equals(groupVO.getGroupId())){
                        String textType = customFieldVO.getTextType();
                        String code = customFieldVO.getCode();
                        if(StringUtils.isNoneBlank(textType) && textType.equals("code") && StringUtils.isNoneBlank(code)){

                            List<SysDict> dictList = sysDictService.searchSysDictListByDictType(code);
                            customFieldVO.setDictList(dictList);
                        }
                        return true;
                    }else{
                        return false;
                    }
                }).collect(Collectors.toList());

                customFieldList.removeAll(fieldList);
                groupVO.setCustomFieldVOList(fieldList);

            }
            customTable.setCustomGroupVOList(customGroupList);
        }

        return customTable;
    }

    @Override
    public List<CustomFieldVO> searchCustomFieldListByCompanyIdAndFuncCode(Integer companyId, String funcCode) {
        List<CustomFieldVO> customFieldList = customTableFieldDao.searchCustomFieldListByCompanyIdAndFuncCode(companyId, funcCode);
        return customFieldList;
    }

    @Override
    public CustomTableVO handlerCustomTableGroupFieldList(CustomTableVO customTable, Map<Integer, String> mapValue) {

        if(customTable != null && !CollectionUtils.isEmpty(mapValue)){
            List<CustomGroupVO> groupList = customTable.getCustomGroupVOList();

            SysDict sysDict = null;
            if(!CollectionUtils.isEmpty(groupList)){
                for(CustomGroupVO groupVO : groupList){
                    List<CustomFieldVO> fieldList = groupVO.getCustomFieldVOList();

                    if(!CollectionUtils.isEmpty(fieldList)){
                        for(CustomFieldVO fieldVO : fieldList){
                            if(fieldVO.getFieldId() != null){
                                if(StringUtils.isNoneBlank(fieldVO.getTextType()) && fieldVO.getTextType().equals("code") && StringUtils.isNoneBlank(fieldVO.getCode())){

                                    sysDict = sysDictService.searchSysDictByTypeAndCode(fieldVO.getCode(),mapValue.get(fieldVO.getFieldId()));
                                    if(sysDict != null){
                                        fieldVO.setDefaultValue(sysDict.getDictValue());
                                    }
                                }else{
                                    fieldVO.setDefaultValue(mapValue.get(fieldVO.getFieldId()));
                                }
                            }
                        }
                    }
                }
            }

        }
        return null;
    }
}
