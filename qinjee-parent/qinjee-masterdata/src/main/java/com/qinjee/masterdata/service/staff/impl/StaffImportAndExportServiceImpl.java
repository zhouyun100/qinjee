package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDataDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.ExportList;
import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.masterdata.model.vo.staff.export.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.export.ContractVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.model.vo.staff.export.ExportPreVo;
import com.qinjee.masterdata.model.vo.staff.export.ImportArcVo;
import com.qinjee.masterdata.model.vo.sys.CheckCustomTableVO;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.staff.IStaffImportAndExportService;
import com.qinjee.masterdata.service.sys.CheckCustomFieldService;
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
    private CheckCustomFieldService checkCustomFieldService;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private LaborContractDao laborContractDao;
    @Autowired
    private BlacklistDao blacklistDao;
    @Autowired
    private RedisClusterService redisClusterService;
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserArchiveDao userArchiveDao;

    @Override
    public List < Map < String, String > > importFileAndCheckFile(MultipartFile multipartFile) throws Exception {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename ();
        assert fileName != null;
        if (!fileName.endsWith ( xls ) && !fileName.endsWith ( xlsx )) {
            throw new IOException ( fileName + "不是excel文件" );
        }
        return getMaps ( multipartFile );
    }

    @Override
    public List < CheckCustomTableVO > checkFile(List < Map < String, String > > list, UserSession userSession) {
        List < Map < Integer, Object > > mapList = new ArrayList <> ();
        Map < Integer, Object > objectMap = new LinkedHashMap <> ();
        //得到fieldName集合
        List < String > fieldNames = null;
        for (Map < String, String > map : list) {
            fieldNames = new ArrayList <> ( map.keySet () );
        }
        //根据filedName与companyId得到fieldId
        List < Integer > idList = customArchiveFieldDao.selectFieldIdByFieldNameAndCompanyId ( fieldNames, userSession.getCompanyId () );
        for (Map < String, String > map : list) {
            List < String > strings = new ArrayList <> ( map.values () );
            for (int i = 0; i < strings.size (); i++) {
                objectMap.put ( idList.get ( i ), strings.get ( i ) );
            }
            mapList.add ( objectMap );
        }
        //请求接口获得返回前端的结果
        return checkCustomFieldService.checkCustomFieldValue ( idList, mapList );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readyForImport(List < Map < String, String > > list, UserSession userSession, String title) {
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
    public void importFile(String title, UserSession userSession) throws Exception {
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
        List < Map < String, String > > list = ( List < Map < String, String > > ) JSONArray.parse ( s );
        //定义fieldname的list，并赋值
        List < String > strings = null;
        for (Map < String, String > map : list) {
            strings = new ArrayList <> ( map.keySet () );
        }
        //通过字段名找到 是否系统定义， tableId，tableName
        Map < String, Map < String, String > > stringMapMap = customArchiveFieldDao.seleleIsSysAndTableIdAndTableName ( strings );
    }


    /**
     * 导入档案
     *
     * @param multipartFile
     * @param userSession
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importArcFile(MultipartFile multipartFile, UserSession userSession) throws Exception {
        //excel方法获得值
        List < Map < String, String > > list = getMaps ( multipartFile );
        List < UserArchive > userArchives = new ArrayList <> ();
        @SuppressWarnings("unchecked")
        List < ImportArcVo > objectList = HeadListUtil.getObjectList ( list, ImportArcVo.class );
        for (ImportArcVo importArcVo : objectList) {
            UserArchive userArchive = new UserArchive ();
            setValue ( importArcVo, userArchive );
            Map < String, Integer > map = postDao.selectPostIdAndOrgIdAndsupiorId ( importArcVo.getOrg_code (), importArcVo.getPost_code (),
                    importArcVo.getSupervisor_code () );
            userArchive.setOrgId ( map.get ( "org_id" ) );
            userArchive.setPostId ( map.get ( "post_id" ) );
            userArchive.setSupervisorId ( map.get ( "archive_id" ) );
            userArchive.setOperatorId ( userSession.getArchiveId () );
            userArchive.setCompanyId ( userSession.getCompanyId () );
            userArchives.add ( userArchive );
        }
        userArchiveDao.insertBatch ( userArchives );
        //批量添加
    }

    /**
     * 导入预入职
     */
    @Override
    public void importPreFile(MultipartFile multipartFile, UserSession userSession) throws Exception {
        List < Map < String, String > > list = getMaps ( multipartFile );
        List < PreEmployment > preEmploymentList = new ArrayList <> ();
        @SuppressWarnings("unchecked")
        List < ExportPreVo > objectList = HeadListUtil.getObjectList ( list, ExportPreVo.class );
        for (ExportPreVo exportPreVo : objectList) {
            PreEmployment preEmployment = new PreEmployment ();
            setValue ( exportPreVo, preEmployment );
            Map < String, Integer > map = postDao.selectPostIdAndOrgId ( exportPreVo.getOrg_code (), exportPreVo.getPost_code () );
            preEmployment.setOrgId ( map.get ( "org_id" ) );
            preEmployment.setPostId ( map.get ( "post_id" ) );
            preEmployment.setOperatorId ( userSession.getArchiveId () );
            preEmployment.setCompanyId ( userSession.getCompanyId () );
            preEmploymentList.add ( preEmployment );
        }
        preEmploymentDao.insertBatch ( preEmploymentList );
    }

    /**
     * 导入黑名单
     */
    @Override
    public void importBlaFile(MultipartFile multipartFile, UserSession userSession) throws Exception {
        //excel方法获得值
        List < Map < String, String > > list = getMaps ( multipartFile );
        List < Blacklist > blacklistList = new ArrayList <> ();
        @SuppressWarnings("unchecked")
        List < BlackListVo > objectList = HeadListUtil.getObjectList ( list, BlackListVo.class );
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
    public void importConFile(MultipartFile multipartFile, UserSession userSession) throws Exception {
        //excel方法获得值
        List < Map < String, String > > list = getMaps ( multipartFile );
        List < LaborContract > laborContractList = new ArrayList <> ();
        @SuppressWarnings("unchecked")
        List < ContractVo > objectList = HeadListUtil.getObjectList ( list, ContractVo.class );
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

    @Override
    public void importBusinessFile(MultipartFile multipartFile, String title, UserSession userSession) throws Exception {
        //excel方法获得值
        List < Map < String, String > > list = getMaps ( multipartFile );
        //获取对象集合
        List objectList = HeadListUtil.getObjectList ( list, Object.class );
        for (Object o : objectList) {
            StringBuilder builder = new StringBuilder (  );
            Field idNumber = o.getClass ().getDeclaredField ( "id_number" );
            Field employmentNumber = o.getClass ().getDeclaredField ( "employment_number" );
            String number = ( String ) idNumber.get ( o );
            String employ = ( String ) employmentNumber.get ( o );
            Integer businessId = userArchiveDao.selectIdByNumberAndEmploy ( number, employ );
            Integer tableId = customArchiveFieldDao.selectTableIdByNameAndCompanyId ( title, userSession.getCompanyId () );
            Field[] declaredFields = o.getClass ().getDeclaredFields ();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible ( true );
                //拼接bigdata
                builder.append ( "@@" ).append ( customArchiveFieldDao.selectFieldCodeByName ( fieldToProperty
                        ( declaredField.getName () ) ) ).append ( "@@:" ).append ( declaredField.get ( o ) );
            }
            CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData ();
            //设值进行入库操作
            customArchiveTableData.setBigData ( builder.toString () );
            customArchiveTableData.setTableId ( tableId );
            customArchiveTableData.setBusinessId ( businessId );
            customArchiveTableData.setIsDelete ( 0 );
            customArchiveTableData.setOperatorId ( userSession.getArchiveId () );
            customArchiveTableDataDao.insertSelective ( customArchiveTableData );
        }
    }

    private void setValue(Object voObject, Object object) throws IllegalAccessException {
        Field[] declaredFields = object.getClass ().getDeclaredFields ();
        Field[] fields = object.getClass ().getDeclaredFields ();
        for (Field field : fields) {
            field.setAccessible ( true );
            String fieldName = field.getName ();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible ( true );
                String name = declaredField.getName ();
                if (name.equals ( fieldToProperty ( fieldName ) )) {
                    declaredField.set ( object, field.get ( voObject ) );
                }
            }
        }
    }

    /**
     * 导出档案
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportArcFile(ExportFile exportFile, HttpServletResponse response) throws IOException {
        ExcelUtil.download ( response, exportFile.getTittle (),
                getHeadsByArc ( exportFile ),
                getDates ( exportFile, getHeadsByArc ( exportFile ) ),
                getTypeMapForArc ( exportFile, getHeadsByArc ( exportFile ) ) );
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
                getDates ( exportFile, HeadMapUtil.getHeadsForPre () ),
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
                getDates ( exportFile, HeadMapUtil.getHeadsForBlackList () ),
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
                getDates ( exportFile, HeadMapUtil.getHeadsForCon () ),
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
                    getDates ( exportFile, businessHead ),
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
    private List < Map < String, String > > getMaps(MultipartFile multipartFile) throws Exception {
        List < Map < String, String > > mapList = ExcelUtil.readExcel ( multipartFile );
        List < Map < String, String > > list = new ArrayList <> ();
        for (Map < String, String > map : mapList) {
            Map < String, String > stringMap = new HashMap <> ();
            for (Map.Entry < String, String > entry : map.entrySet ()) {
                stringMap.put ( customArchiveFieldDao.selectFieldCodeByName ( entry.getKey () ), entry.getValue () );
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
    private List < Map < String, String > > getDates(ExportFile exportFile, List < String > heads) {
        List < Map < String, String > > mapList = new ArrayList <> ();
        List < Map < String, Object > > maps = new ArrayList <> ( exportFile.getExportList ().getMap ().values () );
        for (Map < String, Object > stringObjectMap : maps) {
            //linkedHashMap保证有序
            Map < String, String > stringMap = new LinkedHashMap <> ();
            for (String head : heads) {
                stringMap.put ( head, String.valueOf ( stringObjectMap.get ( customArchiveFieldDao.selectFieldCodeByName ( head ) ) ) );
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
    private List < String > getHeadsByArc(ExportFile exportFile) {
        Integer querySchemaId = exportFile.getExportList ().getQuerySchemaId ();
        List < String > keyList = new ArrayList <> ( 11 );
        if (querySchemaId == null || querySchemaId == 0) {
            return HeadMapUtil.getHeadForArc ( keyList );
        }
        keyList = getKeyList ( exportFile );
        return customArchiveFieldDao.selectFieldNameByCodeList ( keyList );
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
}
