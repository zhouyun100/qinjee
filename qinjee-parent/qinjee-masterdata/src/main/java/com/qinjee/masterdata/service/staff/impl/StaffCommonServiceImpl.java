package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.CompanyCodeDao;
import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.organation.PostDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveGroupDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDataDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.custom.CheckCustomFieldVO;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.service.employeenumberrule.IEmployeeNumberRuleService;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.masterdata.service.userinfo.UserLoginService;
import com.qinjee.masterdata.utils.FieldToProperty;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
public class StaffCommonServiceImpl implements IStaffCommonService {
    private static final Logger logger = LoggerFactory.getLogger ( StaffCommonServiceImpl.class );
    private static final String ARCHIVE = "档案";
    private static final String PREEMP = "预入职";
    private static final String IDTYPE = "证件类型";
    private static final String IDNUMBER = "证件号码";
    private static final String PHONE = "手机";
    private static final Integer MAXISM = 5242880;
    private static final String FILE = "FILE";
    private static final String PHOTO = "PHOTO";
    @Autowired
    private CustomArchiveTableDao customArchiveTableDao;
    @Autowired
    private CustomArchiveGroupDao customArchiveGroupDao;
    @Autowired
    private CustomTableFieldDao customTableFieldDao;
    @Autowired
    private CustomArchiveTableDataDao customArchiveTableDataDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private CompanyCodeDao companyCodeDao;
    @Autowired
    private PreEmploymentDao preEmploymentDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private PostDao postDao;
    @Autowired
    private IEmployeeNumberRuleService employeeNumberRuleService;

    public List<Map<String, String>> getCustomDataByTableIdAndEmploymentId(Integer employmentId, Integer tableId) throws IllegalAccessException {
        //存放结果
        List<Map<String, String>> resMapList = new ArrayList<>();
        List<Map<Integer, String>> customDataList = selectValue(tableId, employmentId);
        //根据表id查到  <fieldId,fieldName>
        List<CustomArchiveField> fieldList = customTableFieldDao.selectFieldByTableId(tableId);
        //遍历自定义表的大数据字段集合
        for (Map<Integer, String> customDataMap : customDataList) {

            Map<String, String> resMap = new HashMap<>();
            customDataMap.forEach((key, value) -> {
                //比较
                for (CustomArchiveField customArchiveField : fieldList) {
                    if (customArchiveField.getFieldId().equals(key)) {
                        //拼接fieldCode+textType
                        resMap.put(customArchiveField.getFieldCode()+"@@"+customArchiveField.getTextType(), value);
                    }
                }
            });
            resMapList.add(resMap);
        }
        return resMapList;
    }

    @Override
    public void insertCustomArichiveTable(CustomArchiveTable customArchiveTable, UserSession userSession) {
        customArchiveTable.setCompanyId ( userSession.getCompanyId () );
        customArchiveTable.setCreatorId ( userSession.getArchiveId () );
        customArchiveTable.setIsDelete ( ( short ) 0 );
        customArchiveTableDao.insertSelective ( customArchiveTable );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustomArchiveTable(List < Integer > list) {
        customArchiveTableDao.deleteCustomTable ( list );
    }

    @Override
    public void updateCustomArchiveTable(CustomArchiveTable customArchiveTable) {
        customArchiveTableDao.updateByPrimaryKeySelective ( customArchiveTable );
    }

    @Override
    public PageResult < CustomArchiveTable > selectCustomArchiveTable(Integer currentPage, Integer pageSize, UserSession userSession) {
        PageHelper.startPage ( currentPage, pageSize );
        List < CustomArchiveTable > customArchiveTables = customArchiveTableDao.selectByPage ( userSession.getCompanyId () );
        for (CustomArchiveTable customArchiveTable : customArchiveTables) {
            logger.info ( "展示自定义表名{}", customArchiveTable.getTableName () );
        }
        return new PageResult <> ( customArchiveTables );
    }

    @Override
    public void insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup, UserSession userSession) {
        customArchiveGroup.setCreatorId ( userSession.getArchiveId () );
        customArchiveGroup.setIsDelete ( ( short ) 0 );
        customArchiveGroupDao.insertSelective ( customArchiveGroup );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomArchiveGroup(List < Integer > list) {
        customArchiveGroupDao.deleteCustomGroup ( list );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomArchiveGroup(CustomArchiveGroup customArchiveGroup) {
        customArchiveGroupDao.updateByPrimaryKeySelective ( customArchiveGroup );
    }

    @Override
    public PageResult < CustomArchiveField > selectArchiveFieldFromGroup(Integer currentPage, Integer pageSize, Integer customArchiveGroupId) {
        PageHelper.startPage ( currentPage, pageSize );
        //获得自定义组中自定义表id的集合
        List < CustomArchiveField > list = customTableFieldDao.selectCustomArchiveField ( customArchiveGroupId );
        return new PageResult <> ( list );
    }

    @Override
    public void insertCustomArchiveField(CustomArchiveField customArchiveField, UserSession userSession) {
        customArchiveField.setCreatorId ( userSession.getArchiveId () );
        customArchiveField.setIsDelete ( ( short ) 0 );
        //这个dao层方法需要重新编写
        customTableFieldDao.insertSelective ( customArchiveField );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomArchiveField(List < Integer > list) {

        customTableFieldDao.deleteCustomField ( list );
    }

    @Override
    public void updateCustomArchiveField(CustomArchiveField customArchiveField) {
        customTableFieldDao.updateByPrimaryKeySelective ( customArchiveField );
    }

    @Override
    public PageResult < CustomArchiveField > selectCustomArchiveField(Integer currentPage, Integer pageSize,
                                                                      Integer customArchiveTableId) {
        PageHelper.startPage ( currentPage, pageSize );
        //根据自定义表找自定义字段id
        List < CustomArchiveField > list = customTableFieldDao.selectFieldByTableId ( customArchiveTableId );
        return new PageResult <> ( list );
    }

    @Override
    public CustomArchiveField selectCustomArchiveFieldById(Integer customArchiveFieldId) {
        return customTableFieldDao.selectByPrimaryKey ( customArchiveFieldId );
    }

    @Override
    public Integer getCompanyId(UserSession userSession) {
        return userSession.getCompanyId ();
    }

    @Override
    public List<OrganzitionVo> getOrgIdByCompanyId( UserSession userSession) {
        return getOrganTree ( userSession.getCompanyId (), userSession.getArchiveId () );
    }

    private List<OrganzitionVo> getOrganTree(Integer companyId, Integer archiveId) {
        //获取该人员下的所有权限机构
        List < OrganzitionVo > list = organizationDao.getOrganizationBycomanyIdAndUserAuth ( companyId, archiveId );
        //取一级子机构
        List < OrganzitionVo > organzitionVoList = list.stream ().filter ( organzitionVo1 -> {
            if (organzitionVo1.getOrg_parent_id ().equals ( 0 )) {
                return true;
            } else {
                return false;
            }
        } ).collect ( Collectors.toList () );
        list.removeAll ( organzitionVoList );
        handlerAllChildOrganizationTree ( organzitionVoList, list );
        return organzitionVoList;
    }

    private void handlerAllChildOrganizationTree(List < OrganzitionVo > organzitionVoList, List < OrganzitionVo > orgList) {

        for (OrganzitionVo organzitionVo : organzitionVoList) {
            List < OrganzitionVo > childOrgList = orgList.stream ().filter ( org -> {
                if (organzitionVo.getOrg_id ().equals ( org.getOrg_parent_id () )) {
                    return true;
                } else {
                    return false;
                }
            } ).collect ( Collectors.toList () );

            if (!CollectionUtils.isEmpty ( childOrgList )) {
                organzitionVo.setList ( childOrgList );
                orgList.removeAll ( childOrgList );
                handlerAllChildOrganizationTree ( childOrgList, orgList );
            }
        }
    }

    @Override
    public String getPostByOrgId(Integer orgId, UserSession userSession) {
        if (orgId == null || orgId == 0) {
            JSON.toJSONString ( postDao.listPostsByCompanyId( userSession.getCompanyId () ) );
        } else {
            return JSON.toJSONString ( postDao.listPostsByOrgId( orgId ,userSession.getCompanyId ()) );
        }
        return null;
    }

    @Override
    public List < String > selectFieldValueById(Integer customArchiveFieldId) {
        //找到企业代码id
        Integer id = customTableFieldDao.selectCodeId ( customArchiveFieldId );
        //找到自定义字段的值
        return companyCodeDao.selectValue ( id );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> saveFieldAndValue(UserSession userSession, InsertDataVo insertDataVo) throws IllegalAccessException, ParseException {
        List<Integer> list= new ArrayList<>();

            List < Integer > idList = new ArrayList <> ( insertDataVo.getList ().get ( 0 ).keySet () );
            Set < Integer > isSystemDefineSet = new HashSet <> ();
            List < Integer > isSystemDefineList = new ArrayList <> ();
            Set < Integer > notSystemDefineSet = new HashSet <> ();
            List < Integer > notSystemDefineList = new ArrayList <> ();
            Map < Integer, Map < String, Integer > > mapMap = customTableFieldDao.selectNameAndIdAndIsSystemDefine ( idList,userSession.getCompanyId () );
            if(mapMap!=null) {
                for (Map.Entry < Integer, Map < String, Integer > > integerMapEntry : mapMap.entrySet ()) {
                    Integer isSystemDefine = integerMapEntry.getValue ().get ( "is_system_define" );
                    //是内置字段
                    if (isSystemDefine == 1) {
                        //记录tableId
                        isSystemDefineSet.add ( integerMapEntry.getValue ().get ( "table_id" ) );
                        //记录fieldId
                        isSystemDefineList.add ( integerMapEntry.getKey () );
                    }
                    //非内置字段
                    else {
                        //记录非内置tableId
                        notSystemDefineSet.add ( integerMapEntry.getValue ().get ( "table_id" ) );
                        //记录fieldId
                        notSystemDefineList.add ( integerMapEntry.getKey () );
                    }
                }
            }else{
                ExceptionCast.cast ( CommonCode.FAIL );
            }
            List<PreEmployment> list3=new ArrayList<>();
            List<PreEmployment> list4=new ArrayList<>();
            List<CustomArchiveTableData> list5=new ArrayList<>();
            Map < Integer, List < Integer > > map = searchFieldIdByTableId ( isSystemDefineSet, isSystemDefineList );
            Map < Integer, List < Integer > > notMap = searchFieldIdByTableId ( notSystemDefineSet, notSystemDefineList );
            if ("ARC".equalsIgnoreCase ( insertDataVo.getFuncCode () )) {
                //找到确认唯一性的字段id，进行判断新增或是更新操作
                //进行对象组装
                for (Map < Integer, String > integerStringMap : insertDataVo.getList ()) {
                    UserArchive userArchive = new UserArchive ();
                    Integer archiveId = getArchiveId ( integerStringMap, isSystemDefineList,userSession );
                    if (checkMap ( map )) {
                        for (Map.Entry < Integer, List < Integer > > integerListEntry : map.entrySet ()) {
                            for (Integer integer : integerListEntry.getValue ()) {
                                Map < String, String > map1 = customTableFieldDao.selectCodeAndTypeById ( integer );
                                Field[] declaredFields = userArchive.getClass ().getDeclaredFields ();
                                for (Field declaredField : declaredFields) {
                                    declaredField.setAccessible ( true );
                                    String s1 = selectValueById ( integerStringMap, integer );
                                    if (!StringUtils.isEmpty ( s1 )) {
                                        if (declaredField.getName ().equals ( FieldToProperty.fieldToProperty ( map1.get ( "field_code" ) ) )) {
                                            if ("text".equals (map1.get ( "text_type" ) )) {
                                                declaredField.set ( userArchive, String.valueOf (s1) );
                                            } else if ("code".equals (map1.get ( "text_type" ) )) {
                                                String s = declaredField.getType ().toString ();
                                                if (s.contains ( "Integer" )) {
                                                    declaredField.set ( userArchive, Integer.parseInt (s1) );
                                                } else if (s.contains ( "String" )) {
                                                    declaredField.set ( userArchive, s1 );
                                                }
                                            } else if ("number".equals (map1.get ( "text_type" ) )) {
                                                declaredField.set ( userArchive, Integer.parseInt ( s1 ) );
                                            } else if ("date".equals (map1.get ( "text_type" ) )) {
                                                SimpleDateFormat sim = new SimpleDateFormat ( "yyyy-MM-dd" );
                                                Date parse = sim.parse ( s1 );
                                                declaredField.set ( userArchive, parse );
                                            }
                                        }
                                    }
                                }
                            }
                            if (userArchive.getPhone () != null) {
                                    userArchive.setUserId ( userLoginService.getUserIdByPhone ( userArchive.getPhone (), userSession.getCompanyId () ) );
                            }
                            if(StringUtils.isEmpty(userArchive.getEmployeeNumber())){
                                userArchive.setEmployeeNumber(employeeNumberRuleService.createEmpNumber(userSession.getCompanyId()));
                            }
                            if (null!=archiveId && 0!=archiveId) {
                                userArchive.setOperatorId ( userSession.getArchiveId () );
                                userArchive.setCompanyId ( userSession.getCompanyId () );
                                userArchive.setArchiveId ( archiveId );
                               userArchiveDao.updateByPrimaryKeySelective(userArchive);
                            } else {
                                userArchive.setOperatorId ( userSession.getArchiveId () );
                                userArchive.setCompanyId ( userSession.getCompanyId () );
                                userArchive.setArchiveStatus ( "SERVICE" );
                               userArchiveDao.insertSelective(userArchive);
                            }
                                list.add ( userArchive.getArchiveId () );
                        }
                    }else {
                        if (archiveId != null && archiveId != 0) {
                            if (checkMap ( notMap )) {
                                for (Map.Entry < Integer, List < Integer > > integerListEntry : notMap.entrySet ()) {
                                    CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData ();
                                    customArchiveTableData.setTableId ( integerListEntry.getKey () );
                                    customArchiveTableData.setOperatorId ( userSession.getArchiveId () );
                                    customArchiveTableData.setBusinessId ( archiveId );
                                    StringBuilder stringBuilder = new StringBuilder ();
                                    for (Integer integer : integerListEntry.getValue ()) {
                                        String s = selectValueById ( integerStringMap, integer );
                                        if (!StringUtils.isEmpty ( s )) {
                                            Map < String, String > map3 = customTableFieldDao.selectCodeAndTypeById ( integer );
                                            stringBuilder.append ( "@@" ).append ( String.valueOf ( map3.get ( "field_id" ) ) ).append ( "@@:" ).append ( s );
                                            customArchiveTableData.setBigData ( stringBuilder.toString () );
                                        }
                                    }
                                    list5.add(customArchiveTableData);
                                    list.add ( customArchiveTableData.getBusinessId () );
                                }
                            }
                        }
                    }
                }

            } else if ("PRE".equalsIgnoreCase ( insertDataVo.getFuncCode () )) {
                for (Map < Integer, String > integerStringMap : insertDataVo.getList ()) {
                    PreEmployment preEmployment = new PreEmployment ();
                    Integer preemploymentId = getPreemploymentId ( integerStringMap, isSystemDefineList );
                    if (checkMap ( map )) {
                    for (Map.Entry < Integer, List < Integer > > integerListEntry : map.entrySet ()) {
                            for (Integer integer : integerListEntry.getValue ()) {
                                Map < String, String > map1 = customTableFieldDao.selectCodeAndTypeById ( integer );
                                preEmployment.setEmploymentState ( "未入职" );
                                preEmployment.setEmploymentRegister ( "未发送" );
                                preEmployment.setDataSource ( "文件导入" );
                                preEmployment.setCompanyId ( userSession.getCompanyId () );
                                preEmployment.setOperatorId ( userSession.getArchiveId () );
                                Field[] declaredFields = preEmployment.getClass ().getDeclaredFields ();
                                for (Field declaredField : declaredFields) {
                                    declaredField.setAccessible ( true );
                                    String s1 = selectValueById ( integerStringMap, integer );
                                    if(!StringUtils.isEmpty ( s1 )) {
                                    if (declaredField.getName ().equals ( FieldToProperty.fieldToProperty ( map1.get ( "field_code" ) ) )) {
                                            if ("text".equals ( map1.get ( "text_type" ) )) {
                                                declaredField.set ( preEmployment, s1 );
                                            } else if ("code".equals ( map1.get ( "text_type" ) )) {
                                                String s = declaredField.getType ().toString ();
                                                if (s.contains ( "Integer" )) {
                                                    declaredField.set ( preEmployment, Integer.parseInt (s1) );
                                                } else if (s.contains ( "String" )) {
                                                    declaredField.set ( preEmployment, s1 );
                                                }
                                            } else if ("number".equals ( map1.get ( "text_type" ) )) {
                                                declaredField.set ( preEmployment, Integer.parseInt ( s1 ) );
                                            } else if ("date".equals (map1.get ( "text_type" ))) {
                                                SimpleDateFormat sim = new SimpleDateFormat ( "yyyy-MM-dd" );
                                                Date parse = sim.parse ( s1 );
                                                declaredField.set ( preEmployment, parse );
                                            }
                                        }
                                    }
                                }
                            }
                            if (preemploymentId != null && preemploymentId != 0) {
                                preEmployment.setEmploymentId ( preemploymentId );
                                list3.add(preEmployment);
                            } else {
                                list4.add(preEmployment);
                                preemploymentId=preEmployment.getEmploymentId (  );
                            }
                            list.add ( preemploymentId );
                        }
                    }else {
                        if (preemploymentId != null && preemploymentId != 0) {
                            if (checkMap ( map )) {
                                for (Map.Entry < Integer, List < Integer > > integerListEntry : map.entrySet ()) {
                                    CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData ();
                                    customArchiveTableData.setTableId ( integerListEntry.getKey () );
                                    customArchiveTableData.setOperatorId ( userSession.getArchiveId () );
                                    customArchiveTableData.setBusinessId ( preemploymentId );
                                    StringBuilder stringBuilder = new StringBuilder ();
                                    for (Integer integer : integerListEntry.getValue ()) {
                                        String s = selectValueById ( integerStringMap, integer );
                                        if(!StringUtils.isEmpty ( s )) {
                                            Map < String, String > map3 = customTableFieldDao.selectCodeAndTypeById ( integer );
                                            stringBuilder.append ( "@@" ).append ( String.valueOf ( map3.get ( "field_id" ) ) ).append ( "@@:" ).append (s);
                                            customArchiveTableData.setBigData ( stringBuilder.toString () );
                                        }
                                    }
                                    list5.add(customArchiveTableData);
                                    list.add ( customArchiveTableData.getBusinessId () );
                                }
                            }
                        }
                    }
                }
            }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(list3)){
        preEmploymentDao.updateBatch(list3);
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(list4)){
        preEmploymentDao.insertBatch(list4);
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(list5)){
        customArchiveTableDataDao.insertBatch(list5);
        }
        return list;
        }

    private Boolean checkMap(Map < Integer, List < Integer > > map) {
        Integer size = 0;
        ArrayList < Integer > integers = new ArrayList <> ( map.keySet () );
        for (Map.Entry < Integer, List < Integer > > integerListEntry : map.entrySet ()) {
            size = integerListEntry.getValue ().size ();
        }
        if (integers.size () <= 1 && size <= 2) {
            return false;
        } else {
            return true;
        }
    }

    private Integer getPreemploymentId(Map < Integer, String > map, List < Integer > systemDefineList) {
        Integer integer1 = customTableFieldDao.selectSymbolForPhone ( systemDefineList );
        Integer integer = customTableFieldDao.selectSymbolForPreIdNumber ( systemDefineList );
        String s3 = selectValueById ( map, integer1 );
        String s4=selectValueById(map,integer);
        List<Integer> list = preEmploymentDao.selectPreByPhone(s3, s4);
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }else{
            return null;
        }
    }

    private Integer getArchiveId(Map < Integer, String > map, List < Integer > isSystemDefineList,UserSession userSession) {
        Integer integer = customTableFieldDao.selectSymbolForArcIdNumber ( isSystemDefineList );
        Integer integer1 = customTableFieldDao.selectSymbolForArcEmploymentNumber ( isSystemDefineList );
        String s2 = selectValueById ( map, integer );
        String s3 = selectValueById ( map, integer1 );
        List<Integer> list = userArchiveDao.selectIdByNumberAndEmploy(s2, s3,userSession.getCompanyId());
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }else{
            return null;
        }
    }



    private String selectValueById(Map < Integer, String > map, Integer id) {
        for (Map.Entry < Integer, String > integerStringEntry : map.entrySet ()) {
            if (integerStringEntry.getKey ().equals ( id )) {
                return integerStringEntry.getValue ();
            }
        }
        return null;
    }

    //查找表对应下的字段
    private Map < Integer, List < Integer > > searchFieldIdByTableId(Set < Integer > set, List < Integer > list) {
        Map < Integer, List < Integer > > map = new HashMap <> ();
        for (Integer integer : set) {
            List < Integer > list1 = new ArrayList <> ();
            for (Integer integer1 : list) {
                if (customTableFieldDao.selectTableIdByFieldId ( integer1 ).equals ( integer )) {
                    list1.add ( integer1 );
                }
            }
            map.put ( integer, list1 );
        }
        return map;

    }


    @Override
    public void insertCustomArchiveTableData(BigDataVo bigDataVo, UserSession userSession) {
        //将前端传过来的json串进行解析
        StringBuilder bigData = new StringBuilder ();
        JSONObject jsonObject = JSONObject.parseObject ( bigDataVo.getJsonString () );
        List < String > strings = new ArrayList <> ( jsonObject.keySet () );
        for (String string : strings) {
            bigData.append ( "@@" ).append ( string ).append ( "@@:" ).append ( jsonObject.get ( string ) );
        }
        CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData ();
        customArchiveTableData.setBigData ( bigData.toString () );
        customArchiveTableData.setBusinessId ( bigDataVo.getBusinessId () );
        customArchiveTableData.setTableId
                ( customArchiveTableDao.selectTableIdByNameAndCompanyId ( bigDataVo.getTitle (), userSession.getCompanyId () ) );
        customArchiveTableData.setIsDelete ( 0 );
        customArchiveTableData.setOperatorId ( userSession.getArchiveId () );
        customArchiveTableDataDao.insertSelective ( customArchiveTableData );
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomArchiveTableData(CustomArchiveTableDataVo customArchiveTableDataVo) {
        CustomArchiveTableData customArchiveTableData = getCustomArchiveTableData(customArchiveTableDataVo);
        if(null ==customArchiveTableData.getId()) {
                customArchiveTableData.setCreateTime ( new Date () );
                customArchiveTableData.setOperatorId(1);
                customArchiveTableDataDao.insertSelective ( customArchiveTableData );
        } else {
                customArchiveTableData.setUpdateTime ( new Date (  ) );
                customArchiveTableDataDao.updateByPrimaryKeySelective(customArchiveTableData);
        }
    }

    @Override
    public PageResult < CustomArchiveTableData > selectCustomArchiveTableData(Integer currentPage, Integer pageSize, Integer customArchiveTableId) {
        PageHelper.startPage ( currentPage, pageSize );
        //通过自定义表id找到数据id集合
        List < Integer > integerList = customArchiveTableDataDao.selectCustomArchiveTableId ( customArchiveTableId );
        List < CustomArchiveTableData > list = customArchiveTableDataDao.selectByPrimaryKeyList ( integerList );
        return new PageResult <> ( list );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Map < Integer, String >> selectValue(Integer tableId, Integer businessId) throws IllegalAccessException {
        List<Map<Integer,String>> list=new ArrayList <> (  );
        //通过tableId确认是自定义表还是内置表，是pre或者arc
        Map < String, String > stringStringMap = customArchiveTableDao.selectIsSysAndFuncCode ( tableId );
        List < Map < String, String > > mapList = customTableFieldDao.selectCodAndIdByTableId ( tableId );
        if (String.valueOf ( stringStringMap.get ( "is_system_define" ) ).equals ( "1" ) && stringStringMap.get ( "func_code" ).equals ( "ARC" )) {
            Map < Integer, String > map = new HashMap <> ();
            UserArchiveVo userArchive = userArchiveDao.selectByPrimaryKey ( businessId );
            if(userArchive!=null) {
                for (Field declaredField : userArchive.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    for (Map<String, String> stringMap : mapList) {
                        if (FieldToProperty.fieldToProperty(stringMap.get("field_code")).equals(declaredField.getName())) {
                            map.put(Integer.parseInt(String.valueOf(stringMap.get("field_id"))), String.valueOf(declaredField.get(userArchive)));
                        }
                    }
                }
                list.add(map);
            }
        }
        if (String.valueOf ( stringStringMap.get ( "is_system_define" ) ).equals ( "0" )) {
            List < CustomArchiveTableData > list1 = customArchiveTableDataDao.selectBigDataBybusinessIdAndTableId ( businessId, tableId );
            if (!CollectionUtils.isEmpty ( list1)) {
                for (int j = 0; j < list1.size (); j++) {
                    if (Strings.isNotBlank ( list1.get(j).getBigData () )) {
                        Map < Integer, String > map = new LinkedHashMap<>();
                        String[] split = list1.get(j).getBigData ().split ( "@@" );
                        for (int i = 1; i < split.length; i = i + 2) {
                            map.put ( Integer.parseInt ( split[i] ), split[i + 1].split ( ":" )[1] );
                        }
                        map.put (-1,String.valueOf (list1.get(j).getId ()) );
                        list.add ( map );
                    }
                }
            }
        }
        if (String.valueOf ( stringStringMap.get ( "is_system_define" ) ).equals ( "1" ) && stringStringMap.get ( "func_code" ).equals ( "PRE" )) {
            PreEmployment preEmployment = preEmploymentDao.selectByPrimaryKey ( businessId );
            Map < Integer, String > map = new HashMap <> ();
            if(preEmployment!=null) {
                for (Field declaredField : preEmployment.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    for (Map<String, String> stringMap : mapList) {
                        if (FieldToProperty.fieldToProperty(stringMap.get("field_code")).equals(declaredField.getName())) {
                            map.put(Integer.parseInt(String.valueOf(stringMap.get("field_id"))), String.valueOf(declaredField.get(preEmployment)));
                        }
                    }
                }
                list.add(map);
            }
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePreValue(Integer id, UserSession userSession) {
        preEmploymentDao.deletePreEmployment ( id );
        customArchiveTableDataDao.deleteByBusinessIdAndFuncode ( userSession.getCompanyId (), id, "PRE" );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArcValue(Integer businessId, UserSession userSession) {
        userArchiveDao.deleteArchiveById ( businessId );
        customArchiveTableDataDao.deleteByBusinessIdAndFuncode ( userSession.getCompanyId (), businessId, "ARC" );
    }

    @Override
    public Map < String, String > getNameForOrganzition(Integer orgId, UserSession userSession, Integer postId) {
        return organizationDao.getNameForOrganzition ( userSession.getCompanyId (), orgId, postId );
    }

    @Override
    public void deleteCustomArchiveTableData(List < Integer > list) {
        customArchiveTableDataDao.deleteByPrimaryKeyList (list  );
    }

    @Override
    public OrganizationVO getOrgById(Integer orgId, UserSession userSession) {
        return organizationDao.getOrgById(orgId,userSession.getCompanyId ());
    }

    @Override
    public Post getPostById(Integer postId,UserSession userSession) {
        return postDao.getPostById( postId );
    }

    @Override
    public void deleteCustomTableData(Integer id) {
        customArchiveTableDataDao.deleteById ( id );
    }

    @Override
    public List < Post > getPostListByOrgId(Integer orgId,Integer companyId) {
       return postDao.listPostsByOrgId(orgId,companyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomArchiveTableDatas(MoblieCustom moblieCustom) {
        //先逻辑删除所有相关的表记录
        customArchiveTableDataDao.deleteByBussinessIdAndTableId(moblieCustom.getBusinessId(),moblieCustom.getTableIdList());
        //更新的时候将is_delete更新过来
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(moblieCustom.getList())) {
            for (CustomArchiveTableDataVo customArchiveTableDataVo : moblieCustom.getList()) {
                CustomArchiveTableData customArchiveTableData = getCustomArchiveTableData(customArchiveTableDataVo);
                if (null == customArchiveTableData.getId()) {
                    customArchiveTableData.setCreateTime(new Date());
                    customArchiveTableData.setOperatorId(1);
                    customArchiveTableDataDao.insertSelective(customArchiveTableData);
                } else {
                    customArchiveTableData.setUpdateTime(new Date());
                    customArchiveTableData.setIsDelete(0);
                    customArchiveTableDataDao.updateByPrimaryKeySelective(customArchiveTableData);
                }
            }
        }
    }

    private CustomArchiveTableData getCustomArchiveTableData(CustomArchiveTableDataVo customArchiveTableDataVo) {
        StringBuilder stringBuilder = new StringBuilder();
        CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData();
        BeanUtils.copyProperties(customArchiveTableDataVo, customArchiveTableData);
        List<CheckCustomFieldVO> customFieldVOList = customArchiveTableDataVo.getCustomFieldVOList();
        for (CheckCustomFieldVO checkCustomFieldVO : customFieldVOList) {
            stringBuilder.append("@@").append(checkCustomFieldVO.getFieldId()).append("@@:").append(checkCustomFieldVO.getFieldValue());
        }
        customArchiveTableData.setBigData(stringBuilder.toString());
        return customArchiveTableData;
    }

}
