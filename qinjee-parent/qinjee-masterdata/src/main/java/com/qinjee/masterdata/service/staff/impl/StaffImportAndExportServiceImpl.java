package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDataDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.staff.ExportList;
import com.qinjee.masterdata.model.vo.staff.ExportRequest;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.model.vo.staff.export.ExportPreVo;
import com.qinjee.masterdata.model.vo.sys.CheckCustomFieldVO;
import com.qinjee.masterdata.service.staff.IStaffImportAndExportService;
import com.qinjee.masterdata.service.sys.CheckCustomFieldService;
import com.qinjee.masterdata.utils.export.HeadListUtil;
import com.qinjee.masterdata.utils.export.HeadMapUtil;
import com.qinjee.masterdata.utils.export.HeadTypeUtil;
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
    private PostDao postDao;
    @Override
    public  List <Map< String,String>> importFileAndCheckFile(MultipartFile multipartFile) throws Exception {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        assert fileName != null;
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            throw new IOException (fileName + "不是excel文件");
        }
        return getMaps(multipartFile );
    }

    @Override
    public List< CheckCustomFieldVO > checkFile(List < Map < String, String > > list, UserSession userSession) {
        List<Map<Integer,Object>> mapList=new ArrayList <> (  );
        Map<Integer,Object> objectMap=new LinkedHashMap <> (  );
        //得到fieldName集合
        List<String> fieldNames =null;
        for (Map < String, String > map : list) {
            fieldNames = new ArrayList <> ( map.keySet () );
        }
        //根据filedName与companyId得到fieldId
        List<Integer> idList=customArchiveFieldDao.selectFieldIdByFieldNameAndCompanyId(fieldNames,userSession.getCompanyId ());
        for (Map < String, String > map : list) {
            List < String > strings = new ArrayList <> ( map.values () );
            for (int i = 0; i < strings.size (); i++) {
                objectMap.put ( idList.get(i),strings.get(i) );
            }
            mapList.add (objectMap);
        }
              //请求接口获得返回前端的结果
        List < Map < Integer, CheckCustomFieldVO > > maps = checkCustomFieldService.checkCustomFieldValue ( idList, mapList );
        return null;
    }

    @Override
    public void importPreFile(MultipartFile multipartFile, UserSession userSession) throws Exception {
        Integer orgId;
        Integer postId;
        List<Map<String,String>> list=getMaps(multipartFile );
        List< PreEmployment > preEmploymentList=new ArrayList <> ();
        @SuppressWarnings("unchecked")
        List< ExportPreVo > objectList = HeadListUtil.getObjectList ( list, ExportPreVo.class );
        for (ExportPreVo exportPreVo : objectList) {
            PreEmployment preEmployment=new PreEmployment();
            Field[] declaredFields = preEmployment.getClass ().getDeclaredFields ();
            Field[] fields = exportPreVo.getClass ().getDeclaredFields ();
            for (Field field : fields) {
                field.setAccessible (true);
                String fieldName = field.getName ();
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible (true);
                    String name = declaredField.getName ();
                    if(name.equals (fieldToProperty (fieldName))){
                        declaredField.set (preEmployment,field.get(exportPreVo) );
                    }
                }
            }
            try {
                orgId= organizationDao.selectOrgIdByName ( exportPreVo.getOrg_name () );
            } catch (Exception e) {
                orgId=0;
            }
            preEmployment.setOrgId(orgId);
            try {
                postId=postDao.selectPostIdByName(exportPreVo.getPost_name());
            } catch (Exception e) {
                postId=0;
            }
            preEmployment.setPostId (postId);
            preEmployment.setOperatorId ( userSession.getArchiveId () );
            preEmploymentList.add ( preEmployment );
        }
        preEmploymentDao.insertBatch(preEmploymentList);
    }
    @Override
    public void importArcFile(MultipartFile multipartFile,UserSession userSession) throws Exception {
        //excel方法获得值
        List<Map<String,String>> list=getMaps ( multipartFile );

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportArcFile(ExportFile exportFile, HttpServletResponse response) {
        ExcelUtil.download( response, exportFile.getTittle(),
                getHeadsByArc(exportFile),
                getDates(exportFile,getHeadsByArc(exportFile)),
                getTypeMapForArc(exportFile, getHeadsByArc(exportFile)));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportPreFile(ExportRequest exportRequest, HttpServletResponse response, UserSession userSession){
        Map<Integer, Map <String,Object> > map=preEmploymentDao.selectExportPreList(exportRequest.getList (),userSession.getCompanyId ());
        ExportFile exportFile=new ExportFile();
        exportFile.setTittle(exportRequest.getTitle ());
        ExportList exportList=new ExportList();
        exportList.setMap(map);
        exportFile.setExportList(exportList);
        ExcelUtil.download(response,exportFile.getTittle(),
                HeadMapUtil.getHeadsForPre(),
                getDates(exportFile,HeadMapUtil.getHeadsForPre()),
                getTypeMapForPre(HeadMapUtil.getHeadsForPre()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportBlackFile(ExportRequest exportRequest,HttpServletResponse response,UserSession userSession) {
        Map<Integer,Map<String,Object>> map=blacklistDao.selectExportBlackList(exportRequest.getList (),userSession.getCompanyId());
        ExportFile exportFile=new ExportFile();
        exportFile.setTittle(exportRequest.getTitle ());
        ExportList exportList=new ExportList();
        exportList.setMap(map);
        exportFile.setExportList(exportList);
        ExcelUtil.download (response,exportFile.getTittle (),
                HeadMapUtil.getHeadsForBlackList (),
                getDates (exportFile, HeadMapUtil.getHeadsForBlackList ()),
                getTypeMapForBla( HeadMapUtil.getHeadsForBlackList () ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportContractList(ExportRequest exportRequest,HttpServletResponse response,UserSession userSession) {
        Map<Integer,Map<String,Object>> map=laborContractDao.selectExportConList(exportRequest.getList (),userSession.getCompanyId());
        ExportFile exportFile=new ExportFile();
        exportFile.setTittle(exportRequest.getTitle ());
        ExportList exportList=new ExportList();
        exportList.setMap(map);
        exportFile.setExportList(exportList);
        ExcelUtil.download (response,exportFile.getTittle (),
                HeadMapUtil.getHeadsForCon (),
                getDates (exportFile,  HeadMapUtil.getHeadsForCon ()),
                getTypeMapForPre(  HeadMapUtil.getHeadsForCon () ));
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportBusiness(ExportRequest exportRequest, HttpServletResponse response,UserSession userSession) throws Exception {
        //根据title找到表id
        //通过表id与业务id集合找到存储数据的字段
        //将字段进行解析,循环置于List<map>中
        Map<Integer,Map<String,Object>> map=new HashMap <> ();
        //通过表id与业务id集合找到存储数据的字段
        List <Map<Integer,String>> list=
                customArchiveTableDataDao.selectBigDataByBusinessIdAndTitleListAndCompanyId(exportRequest.getList (),
                        exportRequest.getTitle (),userSession.getCompanyId ());
        //将字段进行解析，存进listMap中
        for (Map < Integer, String > integerStringMap : list) {
            for (Map.Entry < Integer, String > integerStringEntry : integerStringMap.entrySet ()) {
                String[] split = integerStringEntry.getValue ().split ( "@@" );
                Map<String,Object> map1=new LinkedHashMap <> ();
                for (int i = 0; i < split.length; i=i+2) {
                    map1.put(split[i],split[i+1]);
                }
                map.put (integerStringEntry.getKey (),map1);
            }
        }
        ExportFile exportFile=new ExportFile();
        exportFile.setTittle(exportRequest.getTitle ());
        ExportList exportList=new ExportList();
        exportList.setMap(map);
        exportFile.setExportList(exportList);
        List<String> businessHead = HeadMapUtil.getBusinessHead(exportFile.getTittle());
        if(businessHead!=null) {
            ExcelUtil.download(response, exportFile.getTittle(),
                    businessHead,
                    getDates(exportFile, businessHead),
                    getTypeMapForPre(businessHead));
        }else {
            throw new Exception("没有获取到对应的自己表头");
        }

    }

    /**
     * 档案存储字段类型的集合
     **/
    private Map<String, String> getTypeMapForArc(ExportFile exportFile, List<String> heads) {
        Map<String, String> map = new HashMap<>();
        //如果没有查询方案
        if(exportFile.getExportList().getQuerySchemaId()==null || exportFile.getExportList().getQuerySchemaId()==0) {
            for (String head : heads) {
                map.put(head, HeadTypeUtil.getTypeForArc().get(head));
            }
            return map;
        }
        List<String> list = customArchiveFieldDao.selectFieldTypeByNameList(heads);
        for (int i = 0; i < list.size(); i++) {
            map.put(heads.get(i),HeadTypeUtil.transTypeCode().get(list.get(i)));
        }
        return map;
    }
    /**
     * 预入职存储字段类型的集合
     **/
    private Map<String, String> getTypeMapForPre(List<String> heads) {
        Map<String, String> map = new HashMap<>();
        for (String head : heads) {
            map.put(head, HeadTypeUtil.getTypeForPre().get(head));
        }
        return map;
    }
    /**
     * 黑名单的存储类型
     */
    private Map<String, String> getTypeMapForBla(List<String> heads) {
        Map<String, String> map = new HashMap<>();
        for (String head : heads) {
            map.put(head, HeadTypeUtil.getTypeForBla().get(head));
        }
        return map;
    }

    /**
     * 表字段名称转化为属性名称
     * @param field
     * @return
     */
    private static String fieldToProperty(String field) {
        if (null == field){
            return "";
        }
        char[] chars = field.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_') {
                int j = i + 1;
                if (j < chars.length) {
                    sb.append( StringUtils.upperCase( CharUtils.toString(chars[j])));
                    i++;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    /**
     *
     将excel文件解析成list集合
     */
    private List < Map < String, String > > getMaps(MultipartFile multipartFile) throws Exception {
        List<Map<String, String>> mapList = ExcelUtil.readExcel(multipartFile);
        List<Map<String, String>> list=new ArrayList <> ();
        for (Map<String, String> map : mapList) {
            Map<String,String> stringMap=new HashMap <> ();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringMap.put(customArchiveFieldDao.selectFieldCodeByName(entry.getKey()),entry.getValue());
            }
            list.add(stringMap);
        }
        return list;
    }


    /**
     * 从传过来map中解析数据并转化封装到Dates中
     * @param exportFile
     * @return
     */
    private List<Map<String, String>> getDates(ExportFile exportFile,List<String> heads ) {
        List<Map<String, String>> mapList = new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>(exportFile.getExportList().getMap().values());
        for (Map<String, Object> stringObjectMap : maps) {
            //linkedHashMap保证有序
            Map<String, String> stringMap = new LinkedHashMap<>();
            for (String head : heads) {
                stringMap.put( head,String.valueOf(stringObjectMap.get(customArchiveFieldDao.selectFieldCodeByName(head))));
            }
            mapList.add(stringMap);
        }
        return mapList;
    }

    /**
     * 分情况获得文件头
     * @param exportFile
     * @return
     */
    private List<String> getHeadsByArc(ExportFile exportFile) {
        Integer querySchemaId = exportFile.getExportList().getQuerySchemaId();
        List<String> keyList=new ArrayList<>(11);
        if(querySchemaId ==null || querySchemaId ==0){
            return HeadMapUtil.getHeadForArc(keyList);
        }
        keyList = getKeyList(exportFile);
        return customArchiveFieldDao.selectFieldNameByCodeList(keyList);
    }


    /**
     * 获取list中的key，通过此来对应表头
     * @param exportFile
     * @return
     */
    private List<String> getKeyList(ExportFile exportFile) {
        List<String> keyList=new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>(exportFile.getExportList().getMap().values());
        for (Map<String, Object> stringObjectMap : maps) {
            keyList = new ArrayList<>((new ArrayList<>(stringObjectMap.keySet())));
        }
        return keyList;
    }
}
