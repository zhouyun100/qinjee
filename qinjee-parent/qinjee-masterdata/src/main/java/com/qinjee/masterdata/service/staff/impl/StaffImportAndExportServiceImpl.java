package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDataDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.QuerySchemeDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.QueryScheme;
import com.qinjee.masterdata.model.vo.custom.CheckCustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CheckCustomTableVO;
import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.model.vo.staff.export.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.export.ContractVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.custom.CustomTableFieldService;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.masterdata.service.staff.IStaffImportAndExportService;
import com.qinjee.masterdata.utils.export.HeadFieldUtil;
import com.qinjee.masterdata.utils.export.HeadMapUtil;
import com.qinjee.masterdata.utils.pexcel.FieldToProperty;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.utils.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 */
@Service
public class StaffImportAndExportServiceImpl implements IStaffImportAndExportService {
    private static final Logger logger = LoggerFactory.getLogger( StaffImportAndExportServiceImpl.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    private final static String ORGCODE = "部门编码";
    private final static String POSTCODE = "岗位编码";
    private final static String SUPORCODE = "直接上级编码";
    private final static String BUSINESSUNITCODE = "机构编码编码";
    @Autowired
    private CustomTableFieldDao customTableFieldDao;
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
    @Autowired
    private QuerySchemeDao querySchemeDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private IStaffCommonService staffCommonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckImportVo importFileAndCheckFile(MultipartFile multipartFile, String funcCode, UserSession userSession) throws Exception {
        // 获取文件名
        checkFileType ( multipartFile );
        //解析集合
        List < Map < Integer, String > > maps = getMaps ( multipartFile, funcCode, userSession );
        //获取检验结果
        List < CheckCustomTableVO > checkCustomTableVOS = checkFile ( maps, funcCode );
        redisClusterService.setex ( userSession.getCompanyId ()+funcCode+userSession.getArchiveId ()+"import",2*60*60,
                JSON.toJSONString ( maps ));
        //将获取的结果存进缓存中，有效期2小时
        redisClusterService.setex ( userSession.getCompanyId ()+funcCode+userSession.getArchiveId ()+"check",2*60*60,
                JSON.toJSONString (checkCustomTableVOS));
        //拼接head
        List< TableHead> list=new ArrayList <> (  );
        for (Map < Integer, String > map : maps) {
            for (Map.Entry < Integer, String > integerStringEntry : map.entrySet ()) {
                TableHead tableHead=new TableHead ();
                tableHead.setIsShow ( 1 );

                if("PRE".equals ( funcCode )) {
                    CustomFieldVO customFieldVO = customTableFieldDao.selectFieldById ( integerStringEntry.getKey (),
                            userSession.getCompanyId (), "PRE" );
                    tableHead.setName ( customFieldVO.getFieldName () );
                    tableHead.setKey ( customFieldVO.getFieldCode () );
                }else{
                    CustomFieldVO customFieldVO = customTableFieldDao.selectFieldById ( integerStringEntry.getKey (),
                            userSession.getCompanyId (), "ARC" );
                    tableHead.setName ( customFieldVO.getFieldName () );
                    tableHead.setKey ( customFieldVO.getFieldCode () );
                }
                list.add ( tableHead );
            }
                break;
        }
        CheckImportVo checkImportVo = new CheckImportVo ();
        checkImportVo.setHeadList ( list );
        checkImportVo.setList ( checkCustomTableVOS );
        return checkImportVo;
    }
    @Override
    public CheckImportVo importFileAndCheckFileBlackList(MultipartFile multipartFile, UserSession userSession) throws Exception {
        checkFileType ( multipartFile );
        List < Map < String, String > > mapList = ExcelUtil.readExcel ( multipartFile );
        InsideCheckAndImport insideCheckAndImport = customTableFieldService.checkInsideFieldValue ( new BlackListVo (), mapList );
        redisClusterService.setex ( userSession.getCompanyId ()+"BLACKLIST"+userSession.getArchiveId ()+"import",2*60*60,
                JSON.toJSONString ( insideCheckAndImport.getObjectList () ));
        //将获取的结果存进缓存中，有效期2小时
        redisClusterService.setex ( userSession.getCompanyId ()+"BLACKLIST"+userSession.getArchiveId ()+"check",2*60*60,
                JSON.toJSONString (insideCheckAndImport.getList ()));
        CheckImportVo checkImportVo = new CheckImportVo ();
        checkImportVo.setList ( insideCheckAndImport.getList () );
        return checkImportVo;
    }

    @Override
    public CheckImportVo importFileAndCheckFileContract(MultipartFile multipartFile, UserSession userSession) throws Exception {
        // 获取文件名
        checkFileType ( multipartFile );
        List < Map < String, String > > mapList = ExcelUtil.readExcel ( multipartFile );
        InsideCheckAndImport insideCheckAndImport = customTableFieldService.checkInsideFieldValue ( new ContractVo (), mapList );
        redisClusterService.setex ( userSession.getCompanyId ()+"CONTRACT"+userSession.getArchiveId ()+"import",2*60*60,
                JSON.toJSONString ( insideCheckAndImport.getObjectList () ));
        //将获取的结果存进缓存中，有效期2小时
        redisClusterService.setex ( userSession.getCompanyId ()+"CONTRACT"+userSession.getArchiveId ()+"check",2*60*60,
                JSON.toJSONString (insideCheckAndImport.getList ()));
        CheckImportVo checkImportVo = new CheckImportVo ();
        checkImportVo.setList ( insideCheckAndImport.getList () );
        return checkImportVo;
    }
    private void checkFileType(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename ();
        assert fileName != null;
        if (!fileName.endsWith ( xls ) && !fileName.endsWith ( xlsx )) {
            ExceptionCast.cast ( CommonCode.FILE_FORMAT_ERROR );
        }
    }
    @Override
    public String exportCheckFile(UserSession userSession, String funcCode) {
        Map < String, String > map = getCheckMap ( funcCode, userSession );
        return JSON.toJSONString ( map );
    }

    @Override
    public void exportCheckFileTxt(String funcCode,HttpServletResponse response,UserSession userSession) throws IOException {
        Map < String, String > map = getCheckMap ( funcCode, userSession );
        //导出txt
        response.setContentType("application/x-msdownload;charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=\"" + URLEncoder.encode(funcCode+".txt", "UTF-8") + "\"");
        response.setHeader("fileName", URLEncoder.encode(funcCode+".txt", "UTF-8"));
        StringBuffer stringBuffer=new StringBuffer (  );
        ServletOutputStream outputStream = response.getOutputStream ();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream ( outputStream );
        stringBuffer.append ("行号").append ( "\t" ).append ( "错误原因" ).append ( "\r\n" );
        for (Map.Entry < String, String > integerStringEntry : map.entrySet ()) {
            stringBuffer.append ( integerStringEntry.getKey () ).append ( "\t" )
                    .append ( integerStringEntry.getValue () ).append ( "\r\n" );
        }
        bufferedOutputStream.write ( stringBuffer.toString ().getBytes ("UTF-8") );
        bufferedOutputStream.flush ();
        bufferedOutputStream.close ();
    }

    private Map < String, String > getCheckMap(String funcCode, UserSession userSession) {
        String key = userSession.getCompanyId () +funcCode+ userSession.getArchiveId () +"check";
        String s = null;
        if (redisClusterService.exists ( key )) {
            s = redisClusterService.get ( key );
        } else {
            logger.error ( "获取缓存失败" );
        }
        Map < String, String > map = new HashMap <> ();
        List < CheckCustomTableVO > list = JSONArray.parseArray ( s, CheckCustomTableVO.class );
        try {
            for (int i = 0; i < list.size (); i++) {
                if (!"".equals (list.get ( 0 ).getCheckResult())&& list.get ( 0 ).getCheckResult () == false) {
                    map.put ( "第" + (i + 1) + "行", list.get ( i ).getResultMsg () );
                }
            }
        } catch (Exception e) {
            ExceptionCast.cast ( CommonCode.TARGET_NOT_EXIST );
        }
        return map;
    }


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
                if (pre != null || achiveId != null) {
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
    public void cancelForImport(String funcCode, UserSession userSession)  {
        //拼接key
        String s1 = userSession.getCompanyId ()+funcCode+userSession.getArchiveId ()+"import" ;
        if (redisClusterService.exists ( s1 )) {
            redisClusterService.del ( s1 );
        } else {
           ExceptionCast.cast ( CommonCode.FAIL );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importFile( UserSession userSession, String funcCode) throws Exception {

        //从redis中取得文件
        //拼接key
        String s1 = userSession.getCompanyId () +funcCode+ userSession.getArchiveId () + "import";
        String s=null;
        //缓存中获取内容
        if (redisClusterService.exists ( s1 )) {
            s = redisClusterService.get ( s1 );
        } else {
            logger.error ( "获取缓存失败" );
        }
        //还原成list
        List < Map < Integer, String > > list = ( List < Map < Integer, String > > ) JSONArray.parse ( s );
        InsertDataVo insertDataVo=new InsertDataVo ();
        insertDataVo.setFuncCode ( funcCode );
        insertDataVo.setList ( list );
        try {
            staffCommonService.saveFieldAndValue ( userSession,insertDataVo );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }



    /**
     * 导入黑名单
     */
    @Override
    public void importBlaFile( UserSession userSession) throws Exception {
//        //excel方法获得值
//        List<BlackListVo> objectList=new ArrayList<>();
//        List<Map<String, String>> mapList = ExcelUtil.readExcel(multipartFile);
        List < Blacklist > blacklistList = new ArrayList <> ();
//        //反射组装对象
//        for (Map < String, String > map : mapList) {
//            BlackListVo blackListVo=new BlackListVo();
//            Class aclass = blackListVo.getClass();
//            for (Map.Entry<String, String> integerStringEntry : map.entrySet()) {
//                for (Field declaredField : aclass.getDeclaredFields()) {
//                    declaredField.setAccessible(true);
//                    if(declaredField.getName().equals(
//                            HeadFieldUtil.getFieldMap().get(integerStringEntry.getKey()))) {
//                        Class typeClass = declaredField.getType();
//                        Constructor con = typeClass.getConstructor(typeClass);
//                        Object field = con.newInstance(integerStringEntry.getValue());
//                        declaredField.set(blackListVo, field);
//                    }
//                }
//            }
//            objectList.add(blackListVo);
//        }
        //从redis中取得文件
        //拼接key
        String s1 = userSession.getCompanyId () +"BLACKLIST"+ userSession.getArchiveId () + "import";
        String s=null;
        //缓存中获取内容
        if (redisClusterService.exists ( s1 )) {
            s = redisClusterService.get ( s1 );
        } else {
            logger.error ( "获取缓存失败" );
        }
        //还原成list
        List<BlackListVo> objectList = ( List < BlackListVo > ) JSONArray.parse ( s );
        for (BlackListVo blackListVo : objectList) {
            Blacklist blacklist = new Blacklist ();
            setValue ( blackListVo, blacklist );
            blacklist.setOperatorId ( userSession.getArchiveId () );
            blacklist.setCompanyId ( userSession.getCompanyId () );
            Map<String, Integer> stringIntegerMap = organizationDao.selectOrgIdByNameAndCompanyId(blackListVo.getOrg_name(), userSession.getCompanyId(), blackListVo.getPost_name());
            blacklist.setOrgId(stringIntegerMap.get("org_id"));
            blacklist.setPostId(stringIntegerMap.get("post_id"));
            blacklistList.add (blacklist);

        }
        blacklistDao.insertBatch ( blacklistList );
        //批量添加
    }

    /**
     * 导入合同表
     *
     * @param userSession
     * @throws Exception
     */
    @Override
    public void importConFile(UserSession userSession) throws Exception {
//        //excel方法获得值
//        List<ContractVo> objectList=new ArrayList<>();
//        List<Map<String, String>> mapList = ExcelUtil.readExcel(multipartFile);
        List < LaborContract > contractVos = new ArrayList <> ();
//        //反射组装对象
//        for (Map < String, String > map : mapList) {
//           ContractVo contractVo=new ContractVo ();
//            Class aclass = contractVo.getClass();
//            for (Map.Entry<String, String> integerStringEntry :map.entrySet()) {
//                for (Field declaredField : aclass.getDeclaredFields()) {
//                    declaredField.setAccessible(true);
//                    if(declaredField.getName().equals(
//                            HeadFieldUtil.getFieldMap().get(integerStringEntry.getKey()))) {
//                        Class typeClass = declaredField.getType();
//                        int i = typeClass.getName ().lastIndexOf ( "." );
//                        String type=typeClass.getTypeName ().substring ( i+1 );
//                        Object field;
//                        if("Date".equals ( type )){
//                            SimpleDateFormat sdf=new SimpleDateFormat ( "yyyy-MM-dd" );
//                             field=sdf.parse ( integerStringEntry.getValue () );
//                            declaredField.set(contractVo, field);
//                        }
//                        if("Integer".equals ( type )){
//                             field=Integer.parseInt ( integerStringEntry.getValue () );
//                            declaredField.set(contractVo, field);
//                        }
//                        if("String".equals ( type )){
//                             field=integerStringEntry.getValue ();
//                            declaredField.set(contractVo, field);
//                        }
//                    }
//                }
//            }
//            objectList.add(contractVo);
//        }
        //从redis中取得文件
        //拼接key
        String s1 = userSession.getCompanyId () +"CONTRACT"+ userSession.getArchiveId () + "import";
        String s=null;
        //缓存中获取内容
        if (redisClusterService.exists ( s1 )) {
            s = redisClusterService.get ( s1 );
        } else {
            logger.error ( "获取缓存失败" );
        }
        //还原成list
        List<ContractVo> objectList = ( List < ContractVo > ) JSONArray.parse ( s );
        for (ContractVo contractVo : objectList) {
            LaborContract laborContract = new LaborContract ();
            setValue ( contractVo, laborContract );
            //根据证件号与工号找到人员id
            Integer businessId = userArchiveDao.selectIdByNumberAndEmploy ( contractVo.getId_number (), contractVo.getEmployee_number () );
            if(businessId==null || businessId==0){
               ExceptionCast.cast ( CommonCode.TARGET_NOT_EXIST);
            }
            laborContract.setArchiveId ( businessId );
            laborContract.setOperatorId ( userSession.getArchiveId () );
            contractVos.add ( laborContract );
        }
        //批量添加
        laborContractDao.insertBatch ( contractVos );
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
                if (fieldName.equals ( FieldToProperty.fieldToProperty ( name ) )) {
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
        List<String> headsByArc=new LinkedList <> (  );
        List < QueryScheme > list = querySchemeDao.selectQueryByArchiveId ( userSession.getArchiveId () );
        Integer j=0;
        for (QueryScheme queryScheme :list ) {
            if(queryScheme.getIsDefault ()==1){
                j++;
            }
        }
        if(j>0){
            headsByArc = getHeadsByArc ( exportFile, userSession.getCompanyId () );
        }else{
            headsByArc=HeadMapUtil.getHeadForArc (headsByArc);
        }
        ExcelUtil.download ( response, exportFile.getTittle (),
                headsByArc,
                getDates ( exportFile, headsByArc, "ARC", userSession.getCompanyId () ),
                getTypeMap ( headsByArc ) );
    }
        /**
     * 分情况获得文件头
     *
     * @param exportFile
     * @return
     */
    private List < String > getHeadsByArc(ExportFile exportFile,Integer companyId) {
        List<String> keyList=new LinkedList <> (  ) ;
        List < Map < String, Object > > maps = new ArrayList <> ( exportFile.getMap ().values () );
        for (Map < String, Object > stringObjectMap : maps) {
            for (String s : stringObjectMap.keySet ()) {
                if(HeadFieldUtil.getFieldCode ().get(s)!=null){
                    keyList.add ( HeadFieldUtil.getFieldCode ().get ( s ) );
                }else{
                    String arc = customTableFieldDao.selectFieldNameByCode ( s, companyId, "ARC" );
                    if(arc!=null){
                        keyList.add ( arc );
                    }
                }
                if("employment_type".equals ( s )){
                    keyList.add ( "任职类型" );
                }
            }
        }
       return keyList;
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
        Map < Integer, Map < String, Object > > map;
        if(CollectionUtils.isEmpty ( exportRequest.getList () )){
            List < Integer > list = preEmploymentDao.selectIdByComId ( userSession.getCompanyId () );
             map = preEmploymentDao.selectExportPreList ( list, userSession.getCompanyId () );
        }else{
             map = preEmploymentDao.selectExportPreList ( exportRequest.getList (), userSession.getCompanyId () );
        }
        ExportFile exportFile = new ExportFile ();
        exportFile.setTittle ( exportRequest.getTitle () );
        exportFile.setMap ( map );
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
        exportFile.setMap ( map );
        ExcelUtil.download ( response, exportFile.getTittle (),
                HeadMapUtil.getHeadsForBlackList (),
                getDates ( exportFile, HeadMapUtil.getHeadsForBlackList (),"ARC", userSession.getCompanyId () ),
                getTypeMap ( HeadMapUtil.getHeadsForBlackList () ) );
    }

    /**
     * 导出合同表
     * @param exportRequest
     * @param response
     * @param userSession
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportContractList(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession) throws IOException {
        Map < Integer, Map < String, Object > > map = laborContractDao.selectExportConList ( exportRequest.getList (), userSession.getCompanyId () );
        ExportFile exportFile = new ExportFile ();
        exportFile.setTittle ( exportRequest.getTitle () );
        exportFile.setMap ( map );
        ExcelUtil.download ( response, exportFile.getTittle (),
                HeadMapUtil.getHeadsForCon (),
                getDates ( exportFile, HeadMapUtil.getHeadsForCon (), "ARC",userSession.getCompanyId () ),
                getTypeMap ( HeadMapUtil.getHeadsForCon () ) );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportBusiness(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession,String funcCode) throws Exception {
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
                map2.put ( customTableFieldDao.selectFieldById (Integer.parseInt(split[i].split ( ";" )[0]), userSession.getCompanyId(),funcCode).getCode (), split[i + 1].split ( ":" )[1] );
            }
            map.put ( integerMapEntry.getKey (), map2 );
        }
        ExportFile exportFile = new ExportFile ();
        exportFile.setTittle ( exportRequest.getTitle () );
        exportFile.setMap ( map );
        List < String > businessHead = HeadMapUtil.getBusinessHead ( exportFile.getTittle () );
        if (businessHead != null) {
            ExcelUtil.download ( response, exportFile.getTittle (),
                    businessHead,
                    getDates ( exportFile, businessHead, funcCode, userSession.getCompanyId () ),
                    getTypeMap ( businessHead ) );
        } else {
           ExceptionCast.cast ( CommonCode.FILE_PARSING_EXCEPTION );
        }
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
     * 将excel文件解析成list集合
     */
    private List < Map < Integer, String > > getMaps(MultipartFile multipartFile, String funcCode, UserSession userSession) throws Exception {
        List < Map < String, String > > mapList = ExcelUtil.readExcel ( multipartFile );
        List < Map < Integer, String > > list = new ArrayList <> ();
        for (Map < String, String > map : mapList) {
            Map < Integer, String > stringMap = new HashMap <> ();
            for (Map.Entry < String, String > entry : map.entrySet ()) {
                Map < Integer, String > map1 = transField ( funcCode, userSession.getCompanyId (), entry.getValue (), entry.getKey () );
                for (Map.Entry < Integer, String > integerStringEntry : map1.entrySet ()) {
                    stringMap.put ( integerStringEntry.getKey (),integerStringEntry.getValue () );
                }
            }
            list.add ( stringMap );
        }
        return list;
    }

    /**
 * 根据fieldName与funcode找到对应的fieldId
 */
private Map<Integer,String> transField(String funcCode,Integer companyId,String value,String fieldName){
    if(ORGCODE.equals ( fieldName ) || BUSINESSUNITCODE.equals ( fieldName )){
        return customTableFieldDao.transOrgId(funcCode,companyId,value);
    }
    if(SUPORCODE.equals ( fieldName ) ){
        return customTableFieldDao.transSupiorId(funcCode,companyId,value);
    }
    if(POSTCODE.equals ( fieldName ) ){
        return customTableFieldDao.transPostId(funcCode,companyId,value);
    }
    else {
        Map<Integer, String> map = new HashMap<>();
        Integer integer = customTableFieldDao.selectFieldIdByFieldNameAndCompanyIdAndFuncCode(fieldName, companyId, funcCode);
        if(integer!=null && integer!=0) {
            map.put(integer, value);
        }
        return map;
    }

}

    /**
     * 从传过来map中解析数据并转化封装到Dates中
     *
     * @param exportFile
     * @return
     */
    private List < Map < String, String > > getDates(ExportFile exportFile, List < String > heads,String funcCode, Integer companyId) {
        List < Map < String, String > > mapList = new ArrayList <> ();
        String s;
        List < Map < String, Object > > maps = new ArrayList <> ( exportFile.getMap ().values () );
        for (Map < String, Object > stringObjectMap : maps) {
            //linkedHashMap保证有序
            Map < String, String > stringMap = new LinkedHashMap <> ();
            for (String head : heads) {
                s = TransValue ( head );
                if(s==null) {
                    s = customTableFieldDao.selectFieldCodeByName ( head, companyId,funcCode );
                }
                if (head.equals ( "任职类型" )) {
                    stringMap.put ( head, "在职" );
                }
                Object o = stringObjectMap.get ( s );
                String date = isDate ( String.valueOf ( o ) );
                if (date != null) {
                    o = date;
                }
                stringMap.put ( head, String.valueOf ( o ) );
            }
            mapList.add ( stringMap );
        }
        return mapList;
    }



    private String TransValue( String head) {
        for (Map.Entry < String, String > entry : HeadFieldUtil.getFieldMap ().entrySet ()) {
            if(head.equals ( entry.getKey () )){
                 return entry.getValue ();
            }
        }
        return null;
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

    private String isDate(String date){
        SimpleDateFormat sdf=new SimpleDateFormat ( "yy-MM-dd" );
        try {
            return sdf.format ( new Date ( date ) );
        } catch (Exception e) {
            return null;
        }
    }


}
