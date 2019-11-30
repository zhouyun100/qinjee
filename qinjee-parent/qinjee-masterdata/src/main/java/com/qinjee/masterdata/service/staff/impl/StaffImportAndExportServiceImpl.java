package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDataDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.custom.CheckCustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CheckCustomTableVO;
import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.staff.ExportList;
import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.masterdata.model.vo.staff.export.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.export.ContractVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.custom.CustomTableFieldService;
import com.qinjee.masterdata.service.staff.IStaffImportAndExportService;
import com.qinjee.masterdata.utils.export.HeadListUtil;
import com.qinjee.masterdata.utils.export.HeadMapUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.utils.ExcelUtil;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 */
@Service
public class StaffImportAndExportServiceImpl implements IStaffImportAndExportService {
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    @Autowired
    private CustomArchiveFieldDao customArchiveFieldDao;
    @Autowired
    private CustomArchiveTableDataDao customArchiveTableDataDao;
    @Autowired
    private PreEmploymentDao preEmploymentDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private CustomTableFieldService customTableFieldService;
    @Autowired
    private LaborContractDao laborContractDao;
    @Autowired
    private BlacklistDao blacklistDao;
    @Autowired
    private RedisClusterService redisClusterService;

    @Override
    public List < Map < Integer, String > > importFileAndCheckFile(MultipartFile multipartFile, String funcCode, UserSession userSession) throws Exception {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename ();
        assert fileName != null;
        if (!fileName.endsWith ( xls ) && !fileName.endsWith ( xlsx )) {
            throw new IOException ( fileName + "不是excel文件" );
        }
        return getMaps ( multipartFile, funcCode, userSession );
    }

    @Override
    public List < CheckCustomTableVO > checkFile(List < Map < Integer, String > > list, String funcCode) {
        List < Integer > idList = null;
        idList = new ArrayList <> ( list.get ( 0 ).keySet () );
        List < Map < Integer, Object > > mapList = new ArrayList <> ();
        for (Map < Integer, String > map : list) {
            Map < Integer, Object > map1 = new HashMap <> ();
            for (Map.Entry < Integer, String > integerStringEntry : map.entrySet ()) {
                map1.put ( integerStringEntry.getKey (), integerStringEntry.getValue () );
            }
            mapList.add ( map1 );
        }
        //请求接口获得返回前端的结果
        List < CheckCustomTableVO > checkCustomTableVOS = customTableFieldService.checkCustomFieldValue ( idList, mapList );
        for (CheckCustomTableVO checkCustomTableVO : checkCustomTableVOS) {
            String idtype = null;
            String idnumber = null;
            String employmentNumber = null;
            for (CheckCustomFieldVO fieldVO : checkCustomTableVO.getCustomFieldVOList ()) {
                if (fieldVO.getFieldCode ().equals ( "id_type" )) {
                    idtype = fieldVO.getFieldValue ();
                } else if (fieldVO.getFieldCode ().equals ( "id_number" )) {
                    idnumber = fieldVO.getFieldValue ();
                } else if (fieldVO.getFieldCode ().equals ( "employment_number" )) {
                    employmentNumber = fieldVO.getFieldValue ();
                }
            }
            boolean result = false;
            if (StringUtils.isNotBlank ( idtype ) && StringUtils.isNotBlank ( idnumber )) {
                Integer pre = null;
                Integer achiveId = null;
                if (funcCode.equals ( "PRE" )) {
                    pre = preEmploymentDao.selectPreByIdtypeAndIdnumber ( idtype, idnumber );
                } else if (funcCode.equals ( "ARC" )) {
                    achiveId = userArchiveDao.selectIdByNumberAndEmploy ( idnumber, employmentNumber );
                }
                if (pre != null && achiveId != null) {
                    result = true;
                }
            }
            if (!result) {
                checkCustomTableVO.setCheckResult ( false );
                checkCustomTableVO.setResultMsg ( "用户不存在" );
            }
        }

        return checkCustomTableVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readyForImport(List < Map < Integer, String > > list, UserSession userSession, String title) {
        //将list存进缓存，并且设置过期时间
        String s = JSON.toJSONString ( list );
        //定义唯一的key
        String s1 = userSession.getCompanyId () + userSession.getArchiveId () + title;
        //过期时间为2小时
        //存储的值
        redisClusterService.setex ( s1, 2 * 60 * 60, s );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelForImport(String title, UserSession userSession) throws Exception {
        //拼接key
        String s1 = userSession.getCompanyId () + userSession.getArchiveId () + title;
        if (redisClusterService.exists ( s1 )) {
            redisClusterService.del ( s1 );
        } else {
            throw new Exception ( "移除缓存失败！" );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void importFile(String title, UserSession userSession, String funcCode) throws Exception {
        //从redis中取得文件
        //拼接key
        String s1 = userSession.getCompanyId () + userSession.getArchiveId () + title;
        String s;
        //缓存中获取内容
        if (redisClusterService.exists ( s1 )) {
            s = redisClusterService.get ( s1 );
        } else {
            throw new Exception ( "获取缓存失败！" );
        }
        //还原成list
        List < Map < Integer, String > > list = ( List < Map < Integer, String > > ) JSONArray.parse ( s );
        List < Integer > idList = new ArrayList <> ( list.get ( 0 ).keySet () );
        Set < Integer > isSystemDefineSet = new HashSet <> ();
        List < Integer > isSystemDefineList = new ArrayList <> ();
        Set < Integer > notSystemDefineSet = new HashSet <> ();
        List < Integer > notSystemDefineList = new ArrayList <> ();
        Map < Integer, Map < String, String > > mapMap = customArchiveFieldDao.selectNameAndIdAndIsSystemDefine ( idList );
        for (Map.Entry < Integer, Map < String, String > > integerMapEntry : mapMap.entrySet ()) {
            Integer is_system_define = Integer.parseInt ( integerMapEntry.getValue ().get ( "is_system_define" ) );
            //是内置字段
            if (is_system_define == 1) {
                //记录tableId
                isSystemDefineSet.add ( Integer.parseInt ( integerMapEntry.getValue ().get ( "table_id" ) ) );
                //记录fieldId
                isSystemDefineList.add ( integerMapEntry.getKey () );
            }
            //非内置字段
            else {
                //记录非内置tableId
                notSystemDefineSet.add ( Integer.parseInt ( integerMapEntry.getValue ().get ( "table_id" ) ) );
                //记录fieldId
                notSystemDefineList.add ( integerMapEntry.getKey () );
            }
        }
        Map < Integer, List < Integer > > map = searchFieldIdByTableId ( isSystemDefineSet, isSystemDefineList );
        Map < Integer, List < Integer > > notMap = searchFieldIdByTableId ( notSystemDefineSet, notSystemDefineList );
        if ("ARC".equals ( funcCode )) {
            //找到确认唯一性的字段id，进行判断新增或是更新操作
            Integer integer2 = getArchiveId ( list, isSystemDefineList );
            checkMap ( map );
            //进行对象组装
            UserArchive userArchive = new UserArchive ();
            for (Map.Entry < Integer, List < Integer > > integerListEntry : map.entrySet ()) {
                for (Integer integer : integerListEntry.getValue ()) {
                    Map < String, String > map1 = customArchiveFieldDao.selectCodeAndTypeById ( integer );
                    Field[] declaredFields = userArchive.getClass ().getDeclaredFields ();
                    for (Field declaredField : declaredFields) {
                        declaredField.setAccessible ( true );
                        if (declaredField.getName ().equals ( map1.get ( "field_code" ) )) {
                            if (map1.get ( "text_type" ).equals ( "text" )) {
                                declaredField.set ( userArchive, selectValueById ( list, integer ) );
                            }
                            if (map1.get ( "text_type" ).equals ( "number" )) {
                                declaredField.setInt ( userArchive, Integer.parseInt ( selectValueById ( list, integer ) ) );
                            }
                            if (map1.get ( "text_type" ).equals ( "date" )) {
                                SimpleDateFormat sim = new SimpleDateFormat ( "yyyy-MM-dd" );
                                Date parse = sim.parse ( selectValueById ( list, integer ) );
                                declaredField.set ( userArchive, parse );
                            }
                        }
                    }
                }
            }
            if (integer2 == null) {
                //新增操作
                userArchiveDao.insertSelective ( userArchive );
            } else {
                //更新操作
                userArchiveDao.updateByPrimaryKeySelective ( userArchive );
            }
            Integer archiveId = getArchiveId ( list, notSystemDefineList );
            //businessId,tableId,字段拼接
            checkMap ( notMap );
            if (archiveId != null) {
                List < CustomArchiveTableData > customArchiveTableDatas = new ArrayList <> ();
                for (Map.Entry < Integer, List < Integer > > integerListEntry : map.entrySet ()) {
                    if (integerListEntry.getValue () != null) {
                        CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData ();
                        customArchiveTableData.setIsDelete ( 0 );
                        customArchiveTableData.setBusinessId ( archiveId );
                        customArchiveTableData.setTableId ( integerListEntry.getKey () );
                        customArchiveTableData.setOperatorId ( userSession.getArchiveId () );
                        for (Integer integer : integerListEntry.getValue ()) {
                            Map < String, String > map1 = customArchiveFieldDao.selectCodeAndTypeById ( integer );
                            StringBuilder stringBuilder = new StringBuilder ();
                            stringBuilder.append ( "@@" ).append ( map1.get ( "field_code" ) ).append ( "@@:" ).append ( selectValueById ( list, integer ) );
                            customArchiveTableData.setBigData ( stringBuilder.toString () );
                        }
                        customArchiveTableDataDao.insertSelective ( customArchiveTableData );
                        //TODO 批量插入
                        customArchiveTableDatas.add ( customArchiveTableData );
                    }
                }
            }else {
                throw new Exception ( "查无此人!" );
            }

        } else if ("PRE".equals ( funcCode )) {
            //找到确认唯一性的字段id，进行判断新增或是更新操作
            Integer preemploymentId = getPreemploymentId ( list, isSystemDefineList );
            //进行对象组装
            checkMap ( notMap );
            //进行对象组装
            PreEmployment preEmployment = new PreEmployment ();
            for (Map.Entry < Integer, List < Integer > > integerListEntry : map.entrySet ()) {
                for (Integer integer : integerListEntry.getValue ()) {
                    Map < String, String > map1 = customArchiveFieldDao.selectCodeAndTypeById ( integer );
                    Field[] declaredFields = preEmployment.getClass ().getDeclaredFields ();
                    for (Field declaredField : declaredFields) {
                        declaredField.setAccessible ( true );
                        if (declaredField.getName ().equals ( map1.get ( "field_code" ) )) {
                            if (map1.get ( "text_type" ).equals ( "text" )) {
                                declaredField.set ( preEmployment, selectValueById ( list, integer ) );
                            }
                            if (map1.get ( "text_type" ).equals ( "number" )) {
                                declaredField.setInt ( preEmployment, Integer.parseInt ( selectValueById ( list, integer ) ) );
                            }
                            if (map1.get ( "text_type" ).equals ( "date" )) {
                                SimpleDateFormat sim = new SimpleDateFormat ( "yyyy-MM-dd" );
                                Date parse = sim.parse ( selectValueById ( list, integer ) );
                                declaredField.set ( preEmployment, parse );
                            }
                        }
                    }
                    //新增操作
                }

            }
            if (preemploymentId == null) {
                preEmploymentDao.insert ( preEmployment );
            } else {
                //更新操作
                preEmploymentDao.updateByPrimaryKey ( preEmployment );
            }
        }

            Integer preemploymentId1 = getPreemploymentId ( list, notSystemDefineList );
            checkMap ( notMap );
            if (preemploymentId1 != null) {
                List < CustomArchiveTableData > customArchiveTableDatas = new ArrayList <> ();
                for (Map.Entry < Integer, List < Integer > > integerListEntry : map.entrySet ()) {
                    if (integerListEntry.getValue () != null) {
                        CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData ();
                        customArchiveTableData.setIsDelete ( 0 );
                        customArchiveTableData.setBusinessId ( preemploymentId1 );
                        customArchiveTableData.setTableId ( integerListEntry.getKey () );
                        customArchiveTableData.setOperatorId ( userSession.getArchiveId () );
                        for (Integer integer : integerListEntry.getValue ()) {
                            Map < String, String > map1 = customArchiveFieldDao.selectCodeAndTypeById ( integer );
                            StringBuilder stringBuilder = new StringBuilder ();
                            stringBuilder.append ( "@@" ).append ( map1.get ( "field_code" ) ).append ( "@@:" ).append ( selectValueById ( list, integer ) );
                            customArchiveTableData.setBigData ( stringBuilder.toString () );
                        }
                        customArchiveTableDataDao.insertSelective ( customArchiveTableData );
                        //TODO 批量插入
                        customArchiveTableDatas.add ( customArchiveTableData );
                    }
                }
        } else {
            throw new Exception ( "funccode有误" );
        }
    }



    private void checkMap(Map < Integer, List < Integer > > map) throws Exception {
        List < List < Integer > > lists = new ArrayList <> ( map.values () );
        if (lists.size () < 2 && lists.get ( 0 ).size () <= 2) {
            throw new Exception ( "你这个数据是空的啊，不允许进行操作！" );
        }
    }

    private Integer getPreemploymentId(List < Map < Integer, String > > list, List < Integer > notSystemDefineList) {
        Integer integer = customArchiveFieldDao.selectSymbolForPreIdNumber ( notSystemDefineList );
        Integer integer1 = customArchiveFieldDao.selectSymbolForPreIdType ( notSystemDefineList );
        String s2 = selectValueById ( list, integer );
        String s3 = selectValueById ( list, integer1 );
        return preEmploymentDao.selectPreByIdtypeAndIdnumber ( s3, s2 );
    }

    private Integer getArchiveId(List < Map < Integer, String > > list, List < Integer > isSystemDefineList) {
        Integer integer = customArchiveFieldDao.selectSymbolForArcIdNumber ( isSystemDefineList );
        Integer integer1 = customArchiveFieldDao.selectSymbolForArcEmploymentNumber ( isSystemDefineList );
        String s2 = selectValueById ( list, integer );
        String s3 = selectValueById ( list, integer1 );
        return userArchiveDao.selectIdByNumberAndEmploy ( s2, s3 );
    }

    private String selectValueById(List < Map < Integer, String > > list, Integer id) {
        for (Map < Integer, String > map : list) {
            for (Map.Entry < Integer, String > integerStringEntry : map.entrySet ()) {
                if (integerStringEntry.getKey ().equals ( id )) {
                    return integerStringEntry.getValue ();
                }

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
                if (customArchiveFieldDao.selectTableIdByFieldId ( integer1 ).equals ( integer )) {
                    list1.add ( integer1 );
                }
            }
            map.put ( integer, list1 );
        }
        return map;
    }

    /**
     * 导入黑名单
     */
    @Override
    public void importBlaFile(MultipartFile multipartFile, UserSession userSession,String funcCode) throws Exception {
        //excel方法获得值
        List<Map<String,String>>  list=new ArrayList <> (  );
        List < Map < Integer, String > > maps = getMaps ( multipartFile, funcCode, userSession );
        List < Blacklist > blacklistList = new ArrayList <> ();
        @SuppressWarnings("unchecked")
        List < BlackListVo > objectList = HeadListUtil.getObjectList (list , BlackListVo.class ,getTypeMaps("BLA",userSession.getCompanyId ()));
        for (BlackListVo blackListVo : objectList) {
            Blacklist blacklist = new Blacklist ();
            setValue ( blackListVo, blacklist );
            blacklist.setOperatorId ( userSession.getArchiveId () );
            blacklist.setCompanyId ( userSession.getCompanyId () );
            blacklistList.add (blacklist);
        }
        blacklistDao.insertBatch ( blacklistList );
        //批量添加
    }

    /**
     * 导入合同表
     *
     * @param multipartFile
     * @param userSession
     * @throws Exception
     */
    @Override
    public void importConFile(MultipartFile multipartFile,String funcCode ,UserSession userSession) throws Exception {
        //excel方法获得值
        List < Map < Integer, String > > maps = getMaps ( multipartFile, funcCode, userSession );
        List < LaborContract > laborContractList = new ArrayList <> ();
        @SuppressWarnings("unchecked")
        List<Map<String,String>>  list=new ArrayList <> (  );
        List < ContractVo > objectList = HeadListUtil.getObjectList ( list, ContractVo.class,getTypeMaps("CON",userSession.getCompanyId ()) );
        for (ContractVo contractVo : objectList) {
            LaborContract laborContract = new LaborContract ();
            setValue ( contractVo, laborContract );
            //根据证件号与工号找到人员id
            Integer businessId = userArchiveDao.selectIdByNumberAndEmploy ( contractVo.getId_number (), contractVo.getEmployment_number () );
            laborContract.setArchiveId ( businessId );
            laborContract.setOperatorId ( userSession.getArchiveId () );
            laborContractList.add ( laborContract );
        }
        //批量添加
        laborContractDao.insertBatch ( laborContractList );
    }
    private void setValue(Object voObject, Object object) throws IllegalAccessException {
        Field[] declaredFields = voObject.getClass ().getDeclaredFields ();
        Field[] fields = object.getClass ().getDeclaredFields ();
        for (Field field : fields) {
            field.setAccessible ( true );
            String fieldName = field.getName ();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible ( true );
                String name = declaredField.getName ();
                if (fieldName.equals ( fieldToProperty ( name ) )) {
                    field.set ( object, declaredField.get ( voObject ) );
                }
            }
        }
    }

    /**
     * 导出档案
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportArcFile(ExportFile exportFile, HttpServletResponse response, UserSession userSession) throws IOException {
        ExcelUtil.download ( response, exportFile.getTittle (),
                getHeadsByArc ( exportFile, userSession.getCompanyId () ),
                getDates ( exportFile, getHeadsByArc ( exportFile, userSession.getCompanyId () ), "ARC", userSession.getCompanyId () ),
                getTypeMapForArc ( exportFile, getHeadsByArc ( exportFile, userSession.getCompanyId () ) ) );
    }

    /**
     * 导出预入职
     *
     * @param exportRequest
     * @param response
     * @param userSession
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportPreFile(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession) throws IOException {
        Map < Integer, Map < String, Object > > map = preEmploymentDao.selectExportPreList ( exportRequest.getList (), userSession.getCompanyId () );
        ExportFile exportFile = new ExportFile ();
        exportFile.setTittle ( exportRequest.getTitle () );
        ExportList exportList = new ExportList ();
        exportList.setMap ( map );
        exportFile.setExportList ( exportList );
        ExcelUtil.download ( response, exportFile.getTittle (),
                HeadMapUtil.getHeadsForPre (),
                getDates ( exportFile, HeadMapUtil.getHeadsForPre (), "PRE", userSession.getCompanyId () ),
                getTypeMap ( HeadMapUtil.getHeadsForPre () ) );
    }

    /**
     * 导出黑名单
     *
     * @param exportRequest
     * @param response
     * @param userSession
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportBlackFile(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession) throws IOException {
        Map < Integer, Map < String, Object > > map = blacklistDao.selectExportBlackList ( exportRequest.getList (), userSession.getCompanyId () );
        ExportFile exportFile = new ExportFile ();
        exportFile.setTittle ( exportRequest.getTitle () );
        ExportList exportList = new ExportList ();
        exportList.setMap ( map );
        exportFile.setExportList ( exportList );
        ExcelUtil.download ( response, exportFile.getTittle (),
                HeadMapUtil.getHeadsForBlackList (),
                getDatesPhysic ( exportFile, HeadMapUtil.getHeadsForBlackList (), userSession.getCompanyId () ),
                getTypeMap ( HeadMapUtil.getHeadsForBlackList () ) );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportContractList(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession) throws IOException {
        Map < Integer, Map < String, Object > > map = laborContractDao.selectExportConList ( exportRequest.getList (), userSession.getCompanyId () );
        ExportFile exportFile = new ExportFile ();
        exportFile.setTittle ( exportRequest.getTitle () );
        ExportList exportList = new ExportList ();
        exportList.setMap ( map );
        exportFile.setExportList ( exportList );
        ExcelUtil.download ( response, exportFile.getTittle (),
                HeadMapUtil.getHeadsForCon (),
                getDatesPhysic ( exportFile, HeadMapUtil.getHeadsForCon (), userSession.getCompanyId () ),
                getTypeMap ( HeadMapUtil.getHeadsForCon () ) );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportBusiness(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession) throws Exception {
        //根据title找到表id
        //通过表id与业务id集合找到存储数据的字段
        //将字段进行解析,循环置于List<map>中
        Map < Integer, Map < String, Object > > map = new HashMap <> ();
        //通过表id与业务id集合找到存储数据的字段
        Map < Integer, Map < String, String > > integerMapMap =
                customArchiveTableDataDao.selectBigDataByBusinessIdAndTitleListAndCompanyId ( exportRequest.getList (),
                        exportRequest.getTitle (), userSession.getCompanyId () );
        //将字段进行解析，存进listMap中
        for (Map.Entry < Integer, Map < String, String > > integerMapEntry : integerMapMap.entrySet ()) {
            Map < String, String > value = integerMapEntry.getValue ();
            String big_data = value.get ( "big_data" );
            Map < String, Object > map2 = new LinkedHashMap <> ();
            String[] split = big_data.split ( "@@" );
            for (int i = 1; i < split.length; i = i + 2) {
                map2.put ( split[i].split ( ";" )[0], split[i + 1].split ( ":" )[1] );
            }
            map.put ( integerMapEntry.getKey (), map2 );
        }
        ExportFile exportFile = new ExportFile ();
        exportFile.setTittle ( exportRequest.getTitle () );
        ExportList exportList = new ExportList ();
        exportList.setMap ( map );
        exportFile.setExportList ( exportList );
        List < String > businessHead = HeadMapUtil.getBusinessHead ( exportFile.getTittle () );
        if (businessHead != null) {
            ExcelUtil.download ( response, exportFile.getTittle (),
                    businessHead,
                    getDates ( exportFile, businessHead, "ARC", userSession.getCompanyId () ),
                    getTypeMap ( businessHead ) );
        } else {
            throw new Exception ( "没有获取到对应的自己表头" );
        }
    }


    /**
     * 档案存储字段类型的集合
     **/
    private Map < String, String > getTypeMapForArc(ExportFile exportFile, List < String > heads) {
        Map < String, String > map = new HashMap <> ();
        //如果没有查询方案
        if (exportFile.getExportList ().getQuerySchemaId () == null || exportFile.getExportList ().getQuerySchemaId () == 0) {
            for (String head : heads) {
                map.put ( head, "String" );
            }

            return map;
        }
        List < String > list = customArchiveFieldDao.selectFieldTypeByNameList ( heads );
        for (int i = 0; i < list.size (); i++) {
            map.put ( heads.get ( i ), "String" );
        }
        return map;
    }

    /**
     * 预入职存储字段类型的集合
     **/
    private Map < String, String > getTypeMap(List < String > heads) {
        Map < String, String > map = new HashMap <> ();
        for (String head : heads) {
            map.put ( head, "String" );
        }
        return map;
    }

    /**
     * 表字段名称转化为属性名称
     *
     * @param field
     * @return
     */
    private static String fieldToProperty(String field) {
        if (null == field) {
            return "";
        }
        char[] chars = field.toCharArray ();
        StringBuffer sb = new StringBuffer ();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_') {
                int j = i + 1;
                if (j < chars.length) {
                    sb.append ( StringUtils.upperCase ( CharUtils.toString ( chars[j] ) ) );
                    i++;
                }
            } else {
                sb.append ( c );
            }
        }
        return sb.toString ();
    }

    /**
     * 将excel文件解析成list集合
     */
    private List < Map < Integer, String > > getMaps(MultipartFile multipartFile, String funcCode, UserSession userSession) throws Exception {
        List < Map < String, String > > mapList = ExcelUtil.readExcel ( multipartFile );
        List < Map < Integer, String > > list = new ArrayList <> ();
        for (Map < String, String > map : mapList) {
            Map < Integer, String > stringMap = new HashMap <> ();
            for (Map.Entry < String, String > entry : map.entrySet ()) {
                stringMap.put ( customArchiveFieldDao.selectFieldIdByFieldNameAndCompanyIdAndFuncCode ( entry.getKey (), userSession.getCompanyId (),
                        funcCode ), entry.getValue () );
            }
            list.add ( stringMap );
        }
        return list;
    }


    /**
     * 从传过来map中解析数据并转化封装到Dates中
     *
     * @param exportFile
     * @return
     */
    private List < Map < String, String > > getDates(ExportFile exportFile, List < String > heads, String funcCode, Integer companyId) {
        List < Map < String, String > > mapList = new ArrayList <> ();
        List < Map < String, Object > > maps = new ArrayList <> ( exportFile.getExportList ().getMap ().values () );
        for (Map < String, Object > stringObjectMap : maps) {
            //linkedHashMap保证有序
            Map < String, String > stringMap = new LinkedHashMap <> ();
            for (String head : heads) {
                stringMap.put ( head, String.valueOf ( stringObjectMap.get ( customArchiveFieldDao.selectFieldCodeByNameAndFuncCodeAndCompanyId ( head, funcCode, companyId ) ) ) );
            }
            mapList.add ( stringMap );
        }
        return mapList;
    }

    /**
     * 从传过来map中解析数据并转化封装到Dates中
     *
     * @param exportFile
     * @return
     */
    private List < Map < String, String > > getDatesPhysic(ExportFile exportFile, List < String > heads, Integer companyId) {
        List < Map < String, String > > mapList = new ArrayList <> ();
        List < Map < String, Object > > maps = new ArrayList <> ( exportFile.getExportList ().getMap ().values () );
        for (Map < String, Object > stringObjectMap : maps) {
            //linkedHashMap保证有序
            Map < String, String > stringMap = new LinkedHashMap <> ();
            for (String head : heads) {
                stringMap.put ( head, String.valueOf ( stringObjectMap.get ( customArchiveFieldDao.selectFieldCodeByName ( head, companyId ) ) ) );
            }

            mapList.add ( stringMap );
        }
        return mapList;
    }

    /**
     * 分情况获得文件头
     *
     * @param exportFile
     * @return
     */
    private List < String > getHeadsByArc(ExportFile exportFile, Integer companyId) {
        Integer querySchemaId = exportFile.getExportList ().getQuerySchemaId ();
        List < String > keyList = new ArrayList <> ( 11 );
        if (querySchemaId == null || querySchemaId == 0) {
            return HeadMapUtil.getHeadForArc ( keyList );
        }
        keyList = getKeyList ( exportFile );
        return customArchiveFieldDao.selectFieldNameByCodeList ( keyList, companyId );
    }


    /**
     * 获取list中的key，通过此来对应表头
     *
     * @param exportFile
     * @return
     */
    private List < String > getKeyList(ExportFile exportFile) {
        List < String > keyList = new ArrayList <> ();
        List < Map < String, Object > > maps = new ArrayList <> ( exportFile.getExportList ().getMap ().values () );
        for (Map < String, Object > stringObjectMap : maps) {
            keyList = new ArrayList <> ( (new ArrayList <> ( stringObjectMap.keySet () )) );
        }
        return keyList;
    }

    private Map < String, String > getTypeMaps(String funcCode, Integer companyId) {
        List < CustomFieldVO > customFieldVOS =
                customTableFieldService.searchCustomFieldListByCompanyIdAndFuncCode ( companyId, funcCode );
        Map < String, String > typeMap = new HashMap <> ();
        for (CustomFieldVO customFieldVO : customFieldVOS) {
            typeMap.put ( customFieldVO.getFieldName (), customFieldVO.getTextType () );
        }
        return typeMap;
    }

}
