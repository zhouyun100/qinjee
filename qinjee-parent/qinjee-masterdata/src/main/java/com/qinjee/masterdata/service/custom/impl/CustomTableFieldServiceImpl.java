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

import com.alibaba.fastjson.JSON;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.organation.PostDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.SysDict;
import com.qinjee.masterdata.model.vo.custom.*;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.staff.InsideCheckAndImport;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.model.vo.staff.export.ContractVo;
import com.qinjee.masterdata.service.custom.CustomTableFieldService;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.masterdata.utils.export.HeadFieldUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.utils.RegexpUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private OrganizationDao organizationDao;
    @Autowired
    private PostDao postDao;

    @Autowired
    private UserArchiveDao userArchiveDao;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private BlacklistDao blacklistDao;


    @Override
    public List<CheckCustomTableVO> checkCustomFieldValue(List<Integer> fileIdList, List<Map<Integer, Object>> mapList) {

        List<CheckCustomTableVO> checkCustomTableVOList = new ArrayList<>();
//        List<CheckCustomFieldVO> customFieldValueList;
        StringBuffer resultMsg;
        Boolean checkResult;

        //查询自定义字段详细信息，并转化为Map，方便通过fieldId获取对象
        List<CustomFieldVO> customFieldDetailList = customTableFieldDao.searchCustomFieldListByFieldIdList(fileIdList);
        Map<Integer, CheckCustomFieldVO> customFieldMap = new HashMap<>(customFieldDetailList.size());
        for (CustomFieldVO customFieldDetail : customFieldDetailList) {
            CheckCustomFieldVO checkCustomFieldVO = new CheckCustomFieldVO();
            BeanUtils.copyProperties(customFieldDetail, checkCustomFieldVO);
            customFieldMap.put(customFieldDetail.getFieldId(), checkCustomFieldVO);
        }

        //循环大表数据
        for (Map<Integer, Object> map : mapList) {
            List<CheckCustomFieldVO> customFieldValueList = new ArrayList<>();
            CheckCustomTableVO customTableVO1 = new CheckCustomTableVO();
            resultMsg=new StringBuffer();
            checkResult = true;
            //循环每条记录的每个字段对应的值
            for (Map.Entry<Integer, Object> entry : map.entrySet()) {
                CheckCustomFieldVO customFieldVOTemp = new CheckCustomFieldVO();
                //获取字段的配置信息
                if ( customFieldMap.get(entry.getKey()) == null) {
                    continue;
                }
                //设置字段录入的值
                customFieldMap.get(entry.getKey()).setFieldId(entry.getKey());
                customFieldMap.get(entry.getKey()).setFieldValue(String.valueOf(entry.getValue()));
                customFieldMap.get(entry.getKey()).setCheckResult(true);
                customFieldMap.get(entry.getKey()).setResultMsg(null);
                //字段值规则校验
                validCustomFieldValue( customFieldMap.get(entry.getKey()));

                //每条记录中但凡有一个字段校验不通过，则视为整行数据均不予通过
                if (! customFieldMap.get(entry.getKey()).getCheckResult()) {
                    checkResult = false;
                    //错误信息追加
                    resultMsg.append( customFieldMap.get(entry.getKey()).getResultMsg());
                }
                customFieldVOTemp = customFieldMap.get(entry.getKey()).clone();
                customFieldValueList.add(customFieldVOTemp);
            }
            customTableVO1.setCustomFieldVOList(customFieldValueList);
            customTableVO1.setCheckResult(checkResult);
            customTableVO1.setResultMsg(resultMsg.toString());
            checkCustomTableVOList.add(customTableVO1);
        }
        return checkCustomTableVOList;
    }

    @Override
    public InsideCheckAndImport checkInsideFieldValue(Object object, List<Map<String, String>> lists,UserSession userSession) throws ParseException, IllegalAccessException {
        //        checkExcelHead(object, lists);
        List<Object> objectList=new ArrayList<>();
        Boolean checkResult=true;
        List<CheckCustomTableVO> checkCustomTableVOS=new ArrayList<>();
        InsideCheckAndImport insideCheckAndImport=new InsideCheckAndImport();
        checkEntity(object, lists, objectList, checkResult, checkCustomTableVOS);
        checkBlackList(checkCustomTableVOS,userSession);
        insideCheckAndImport.setObjectList(objectList);
        insideCheckAndImport.setList(checkCustomTableVOS);
        return insideCheckAndImport;
    }
    private Object deepCopyByJson(Object obj) {
        String json = JSON.toJSONString(obj);
        return JSON.parseObject(json, Object.class);
    }
    private void checkBlackList(List<CheckCustomTableVO> checkCustomTableVOS,UserSession userSession){
        String idnumber = null;
        String phone = null;
        for (CheckCustomTableVO checkCustomTableVO : checkCustomTableVOS) {
            for (CheckCustomFieldVO checkCustomFieldVO : checkCustomTableVO.getCustomFieldVOList()) {
                if ("证件号码".equals(checkCustomFieldVO.getCode())) {
                    idnumber = checkCustomFieldVO.getFieldValue();
                }
                if ("联系电话".equals(checkCustomFieldVO.getCode())) {
                    phone=checkCustomFieldVO.getFieldValue();
                }
            }
            if (StringUtils.isEmpty(idnumber)) {
                checkCustomTableVO.setCheckResult(false);
                checkCustomTableVO.setResultMsg(checkCustomTableVO.getResultMsg() + "证件号码不能为空");
            }
            if (StringUtils.isEmpty(phone)) {
                checkCustomTableVO.setCheckResult(false);
                checkCustomTableVO.setResultMsg(checkCustomTableVO.getResultMsg() + "联系电话不能为空");
            }

            if (org.apache.commons.lang.StringUtils.isNotBlank(idnumber) || org.apache.commons.lang.StringUtils.isNotBlank(phone)) {
                List<Blacklist> blacklistList = blacklistDao.selectByIdNumberAndPhone(idnumber, phone, userSession.getCompanyId());
                if (!org.springframework.util.CollectionUtils.isEmpty(blacklistList)) {
                    checkCustomTableVO.setCheckResult(false);
                    checkCustomTableVO.setResultMsg(checkCustomTableVO.getResultMsg() + "此人员已经存在于黑名单");
                }
            }
        }
    }
    private void checkExcelHead(Object object, List<Map<String, String>> lists) {
        StringBuffer stringBuffer=new StringBuffer();
        Integer check=0;
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field declaredField :declaredFields) {
           stringBuffer.append(declaredField.getName());
        }
        for (int i = 0; i < lists.size(); i++) {
            for (Map.Entry<String, String> stringStringEntry : lists.get(i).entrySet()) {
                   String s= HeadFieldUtil.getFieldMap().get(stringStringEntry.getKey());
                   check++;
                   if(!stringBuffer.toString().contains(s)){
                       ExceptionCast.cast(CommonCode.EXCEL_HEAD_WRONG);
                   }
            }
        }
        if(check!=declaredFields.length){
            ExceptionCast.cast(CommonCode.EXCEL_HEAD_WRONG);
        }
    }


    /**
     * 校验自定义字段值（单个校验）
     *
     * @param customFieldVO
     */
    @Override
    public void validCustomFieldValue(CheckCustomFieldVO customFieldVO) {

        //验证不为空
        if (customFieldVO.getIsMust() != null && customFieldVO.getIsMust() == 1) {
            if (StringUtils.isBlank(customFieldVO.getFieldValue())) {
                handlerCheckCustomFieldVO(customFieldVO, false, "数据不能为空！");
                return;
            }
        }

        //值类型校验
        if (customFieldVO.getTextType().equals(CUSTOM_TEXT_TYPE_DATE)) {

            //日期验证
            validDate(customFieldVO);

        } else if (customFieldVO.getTextType().equals(CUSTOM_TEXT_TYPE_TEXT)) {

            //文本验证
            validText(customFieldVO);

        } else if (customFieldVO.getTextType().equals(CUSTOM_TEXT_TYPE_NUMBER)) {

            //数据验证
            validNumber(customFieldVO);

        }

        //规则校验
        if (StringUtils.isNoneBlank(customFieldVO.getRule())) {
            String[] ruleArr = customFieldVO.getRule().split(",");
            for (String rule : ruleArr) {
                //验证身份证
                if (rule.equals(CUSTOM_FIELD_CHECK_TYPE_ID_CARD)) {
                    if (!RegexpUtils.checkIdCard(customFieldVO.getFieldValue())) {
                        handlerCheckCustomFieldVO(customFieldVO, false, "身份证号码格式不正确！");
                    }
                }

                //验证邮箱
                if (rule.equals(CUSTOM_FIELD_CHECK_TYPE_EMAIL)) {
                    if (!RegexpUtils.checkEmail(customFieldVO.getFieldValue())) {
                        handlerCheckCustomFieldVO(customFieldVO, false, "邮箱格式不正确！");
                    }
                }

                //验证手机号
                if (rule.equals(CUSTOM_FIELD_CHECK_TYPE_PHONE)) {
                    if (!RegexpUtils.checkPhone(customFieldVO.getFieldValue())) {
                        handlerCheckCustomFieldVO(customFieldVO, false, "手机号格式不正确！");
                    }
                }
            }
        }

        //无校验失败则默认校验通过
        if (customFieldVO.getCheckResult() == null) {
            customFieldVO.setCheckResult(true);
        }

    }

    /**
     * 字段类型
     *
     * @param customFieldVO
     */
    public void validDate(CheckCustomFieldVO customFieldVO) {
        if (StringUtils.isNoneBlank(customFieldVO.getFieldValue()) && StringUtils.isNoneBlank(customFieldVO.getFieldType())) {

            //日期格式根据字段类型fieldType分别判断date、time、datetime
            if (customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_DATE) && !RegexpUtils.checkDate(customFieldVO.getFieldValue())) {

                handlerCheckCustomFieldVO(customFieldVO, false, "日期格式不正确!");

            } else if (customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_TIME) && !RegexpUtils.checkTime(customFieldVO.getFieldValue())) {

                handlerCheckCustomFieldVO(customFieldVO, false, "时间格式不正确!");

            } else if (customFieldVO.getFieldType().equals(CUSTOM_FIELD_TYPE_DATETIME) && !RegexpUtils.checkDateTime(customFieldVO.getFieldValue())) {

                handlerCheckCustomFieldVO(customFieldVO, false, "日期时间格式不正确!");

            }
        }

    }

    /**
     * 文本校验
     *
     * @param customFieldVO
     */
    private void validText(CheckCustomFieldVO customFieldVO) {
        if (StringUtils.isNoneBlank(customFieldVO.getFieldValue())) {
            if (customFieldVO.getMaxLength() != null) {
                if (customFieldVO.getFieldValue().length() > customFieldVO.getMaxLength()) {
                    handlerCheckCustomFieldVO(customFieldVO, false, "内容太多，字符数超过：" + customFieldVO.getMaxLength());
                }
            }
            if (customFieldVO.getMinLength() != null) {
                if (customFieldVO.getFieldValue().length() < customFieldVO.getMinLength()) {
                    handlerCheckCustomFieldVO(customFieldVO, false, "内容太少，字符数超过：" + customFieldVO.getMinLength());
                }
            }
        }
    }

    /**
     * 校验精度
     *
     * @param customFieldVO
     */
    private void validNumber(CheckCustomFieldVO customFieldVO) {
        if (StringUtils.isNoneBlank(customFieldVO.getFieldValue())) {
            if (!RegexpUtils.checkDecimal(customFieldVO.getFieldValue())) {
                handlerCheckCustomFieldVO(customFieldVO, false, "不是数值类型！");
            } else {
                if (customFieldVO.getMaxNumber() != null) {
                    if (Double.valueOf(customFieldVO.getFieldValue()) >= customFieldVO.getMaxNumber()) {
                        handlerCheckCustomFieldVO(customFieldVO, false, "数值不能大于" + customFieldVO.getMaxNumber());
                    }
                }

                if (customFieldVO.getMinNumber() != null) {
                    if (Double.valueOf(customFieldVO.getFieldValue()) <= customFieldVO.getMinNumber()) {
                        handlerCheckCustomFieldVO(customFieldVO, false, "数值不能小于" + customFieldVO.getMinNumber());
                    }
                }

                if (customFieldVO.getFloatLength() != null) {
                    String[] strArr = customFieldVO.getFieldValue().split("\\.");
                    int decimalDigit = strArr.length > 1 ? strArr[1].length() : 0;
                    if (decimalDigit > customFieldVO.getFloatLength()) {
                        handlerCheckCustomFieldVO(customFieldVO, false, "小数位超过" + customFieldVO.getFloatLength());
                    }
                }
            }
        }
    }

    private void handlerCheckCustomFieldVO(CheckCustomFieldVO customFieldVO, Boolean checkResult, String resultMsg) {
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
    public CustomTableVO searchCustomTableGroupFieldListByTableCode(UserSession userSession, String tableCode) {

        List<CustomGroupVO> customGroupList = customTableFieldDao.searchCustomGroupList(userSession.getCompanyId(), tableCode, null);

        List<CustomFieldVO> customFieldList = customTableFieldDao.searchCustomFieldList(userSession.getCompanyId(), userSession.getArchiveId(), tableCode, null);

        CustomTableVO customTable = handlerGroupFieldToTable(customGroupList, customFieldList);

        return customTable;
    }

    @Override
    public CustomTableVO searchCustomTableGroupFieldListByTableId(Integer tableId, UserSession userSession) {
        List<CustomGroupVO> customGroupList = customTableFieldDao.searchCustomGroupList(null, null, tableId);

        List<CustomFieldVO> customFieldList = customTableFieldDao.searchCustomFieldList(userSession.getCompanyId(), userSession.getArchiveId(), null, tableId);

        CustomTableVO customTable = handlerGroupFieldToTable(customGroupList, customFieldList);

        return customTable;
    }

    private CustomTableVO handlerGroupFieldToTable(List<CustomGroupVO> customGroupList, List<CustomFieldVO> customFieldList) {
        CustomTableVO customTable = new CustomTableVO();

        if (CollectionUtils.isNotEmpty(customFieldList)) {
            for (CustomFieldVO customFieldVO : customFieldList) {
                handlerCustomFieldDict(customFieldVO);
            }
        }

        //如果没有自定义组，则设置一个groupId为0的空组
        if (CollectionUtils.isEmpty(customGroupList)) {
            customGroupList = new ArrayList<>();
            CustomGroupVO groupVO = new CustomGroupVO();
            groupVO.setCustomFieldVOList(customFieldList);
            customGroupList.add(groupVO);
            customTable.setCustomGroupVOList(customGroupList);
        } else {
            for (CustomGroupVO groupVO : customGroupList) {

                List<CustomFieldVO> fieldList = customFieldList.stream().filter(customFieldVO -> {
                    if (customFieldVO.getGroupId().equals(groupVO.getGroupId())) {
                        return true;
                    } else {
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

    /**
     * 自定义表字段设置code下拉数据
     *
     * @param customFieldVO
     */
    private void handlerCustomFieldDict(CustomFieldVO customFieldVO) {
        String textType = customFieldVO.getTextType();
        String code = customFieldVO.getCode();
        if (StringUtils.isNoneBlank(textType) && textType.equals("code") && StringUtils.isNoneBlank(code)) {
            List<SysDict> dictList = sysDictService.searchSysDictListByDictType(code);
            customFieldVO.setDictList(dictList);
        }
    }

    @Override
    public List<CustomFieldVO> searchCustomFieldListByCompanyIdAndFuncCode(Integer companyId, String funcCode) {
        List<CustomFieldVO> customFieldList = customTableFieldDao.searchCustomFieldListByCompanyIdAndFuncCode(companyId, funcCode);
        return customFieldList;
    }

    @Override
    public List<CustomFieldVO> selectFieldListByTableId(Integer tableId) {
        return customTableFieldDao.selectFieldListByTableId(tableId);

    }

    @Override
    public InsideCheckAndImport checkInsideFieldValueContract(Object object, List<Map<String, String>> lists, UserSession userSession) throws IllegalAccessException, ParseException {
        //        checkExcelHead(object, lists);
        List<Object> objectList=new ArrayList<>();
        Boolean checkResult=true;
        List<CheckCustomTableVO> checkCustomTableVOS=new ArrayList<>();
        InsideCheckAndImport insideCheckAndImport=new InsideCheckAndImport();
        checkEntity(object, lists, objectList, checkResult, checkCustomTableVOS);
        checkContract(checkCustomTableVOS,userSession);
        insideCheckAndImport.setObjectList(objectList);
        insideCheckAndImport.setList(checkCustomTableVOS);
        return insideCheckAndImport;
    }

    private void checkEntity(Object object, List<Map<String, String>> lists, List<Object> objectList, Boolean checkResult, List<CheckCustomTableVO> checkCustomTableVOS) throws ParseException, IllegalAccessException {
        for (Map<String, String> list : lists) {
            CheckCustomTableVO checkCustomTableVO = new CheckCustomTableVO();
            Object o = new Object();
            List<CheckCustomFieldVO> checkCustomFieldVOS = new ArrayList<>();
            StringBuffer resultMsg = new StringBuffer();
            for (Map.Entry<String, String> integerStringEntry : list.entrySet()) {
                CheckCustomFieldVO checkCustomFieldVO = new CheckCustomFieldVO();
                for (Field declaredField : object.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    //获得code
                    String s = HeadFieldUtil.getFieldMap().get(integerStringEntry.getKey());
                    if (declaredField.getName().equals(s)) {
                        CheckCustomFieldVO checkCustomFieldVOTemp = new CheckCustomFieldVO();
                        checkCustomFieldVOTemp.setCode(integerStringEntry.getKey());
                        checkCustomFieldVO = checkCustomFieldVOTemp.clone();
                        Class typeClass = declaredField.getType();
                        int i = typeClass.getName().lastIndexOf(".");
                        String type = typeClass.getTypeName().substring(i + 1);
                        //拼接单个字段
                        if ("Date".equals(type)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date parse = sdf.parse(integerStringEntry.getValue());
                            declaredField.set(object, parse);
                            checkCustomFieldVO.setTextType("date");
                        }
                        if ("Integer".equals(type)) {
                            int i1 = Integer.parseInt(integerStringEntry.getValue());
                            declaredField.set(object, i1);
                            checkCustomFieldVO.setTextType("integer");
                        }
                        if ("String".equals(type)) {
                            String value = integerStringEntry.getValue();
                            declaredField.set(object, value);
                            checkCustomFieldVO.setTextType("text");
                        }
                        o = deepCopyByJson(object);
                        if(StringUtils.isNotBlank(integerStringEntry.getValue())) {
                            checkCustomFieldVO.setFieldValue(integerStringEntry.getValue());
                        }
                        validCustomFieldValue(checkCustomFieldVO);

                        if (StringUtils.isNotBlank(checkCustomFieldVO.getResultMsg())) {
                            resultMsg.append(checkCustomFieldVO.getResultMsg());
                        }
                        checkResult = checkCustomFieldVO.getCheckResult();
                        checkCustomFieldVOS.add(checkCustomFieldVO);
                    }
                    checkCustomTableVO.setResultMsg(resultMsg.toString());
                    checkCustomTableVO.setCheckResult(checkResult);
                    checkCustomTableVO.setCustomFieldVOList(checkCustomFieldVOS);
                }
            }
            objectList.add(o);
            checkCustomTableVOS.add(checkCustomTableVO);
        }
    }

    private void checkContract(List<CheckCustomTableVO> checkCustomTableVOS,UserSession userSession){
        String idnumber = null;
        for (CheckCustomTableVO checkCustomTableVO : checkCustomTableVOS) {
            for (CheckCustomFieldVO checkCustomFieldVO : checkCustomTableVO.getCustomFieldVOList()) {
                if ("证件号码".equals(checkCustomFieldVO.getCode())) {
                    idnumber = checkCustomFieldVO.getFieldValue();
                }
            }
            if(StringUtils.isNotBlank(idnumber)) {
                Integer integer = userArchiveDao.selectIdByNumber(idnumber, userSession.getCompanyId());
                if (integer == null || integer == 0) {
                    checkCustomTableVO.setCheckResult(false);
                    checkCustomTableVO.setResultMsg(checkCustomTableVO.getResultMsg() + "证件号码不能为空");
                }
            }
        }
    }

    @Override
    public CustomTableVO handlerCustomTableGroupFieldList( CustomTableVO customTableVO1, Map<Integer, String> mapValue,Integer index) {

        CustomTableVO customTableVO = SerializationUtils.clone(customTableVO1);
        if (customTableVO != null && mapValue != null) {
            List<CustomGroupVO> groupList = customTableVO.getCustomGroupVOList();
            if (CollectionUtils.isNotEmpty(groupList)) {
                for (CustomGroupVO groupVO : groupList) {
                    List<CustomFieldVO> fieldList = groupVO.getCustomFieldVOList();
                    //处理自定义组字段数据回填
                    handlerCustomTableGroupFieldList(fieldList, mapValue);
                }
            }
            if (StringUtils.isNotBlank(mapValue.get(-index-1))) {

                customTableVO.setBigDataId(Integer.parseInt(mapValue.get(-index-1)));
            }
        }
        return customTableVO;
    }

    @Override
    public void handlerCustomTableGroupFieldList(List<CustomFieldVO> customFieldList, Map<Integer, String> mapValue) {
        if (CollectionUtils.isNotEmpty(customFieldList)) {
            for (CustomFieldVO fieldVO : customFieldList) {
                //设置默认值
                fieldVO.setDefaultValue(mapValue.get(fieldVO.getFieldId()));
                if (StringUtils.isNotBlank(fieldVO.getTextType()) && fieldVO.getTextType().equals("code") && StringUtils.isNotBlank(fieldVO.getCode())&& !"null".equalsIgnoreCase ( fieldVO.getDefaultValue() )) {
                    List<SysDict> dictList = sysDictService.searchSysDictListByDictType(fieldVO.getCode());
                    for (SysDict dict : dictList) {
                        if (dict.getDictCode().equals(mapValue.get(fieldVO.getFieldId()))) {
                            //设置中文值
                            fieldVO.setChDefaultValue(dict.getDictValue());
                        }
                    }
                    //设置字典类
                    fieldVO.setDictList(dictList);
                } else {
                    //如果是部门 单位 岗位 直接上级  也同样设置中文值
                    if (("business_unit_id".equals(fieldVO.getFieldCode()) || "org_id".equals(fieldVO.getFieldCode()))&&Objects.nonNull(fieldVO.getDefaultValue())&& !"null".equalsIgnoreCase ( fieldVO.getDefaultValue() )) {
                        OrganizationVO org = organizationDao.getOrganizationById(Integer.parseInt(fieldVO.getDefaultValue()));
                        if(org!=null) {
                            fieldVO.setChDefaultValue ( org.getOrgName () );
                        }
                    } else if ("post_id".equals(fieldVO.getFieldCode())&&Objects.nonNull(fieldVO.getDefaultValue())&&!"null".equalsIgnoreCase ( fieldVO.getDefaultValue() )) {
                        Post post = postDao.getPostById(fieldVO.getDefaultValue());
                        if(post!=null) {
                            fieldVO.setChDefaultValue ( post.getPostName () );
                        }
                    } else if ("supervisor_id".equals(fieldVO.getFieldCode())&&Objects.nonNull(fieldVO.getDefaultValue())&& !"null".equalsIgnoreCase ( fieldVO.getDefaultValue() )) {
                        UserArchiveVo archiveVo = userArchiveDao.selectByPrimaryKey(Integer.parseInt(fieldVO.getDefaultValue()));
                        if(archiveVo!=null) {
                            fieldVO.setChDefaultValue ( archiveVo.getUserName () );
                        }
                    } else {
                        fieldVO.setChDefaultValue(mapValue.get(fieldVO.getFieldId()));
                    }
                }
            }
        }
    }
}
