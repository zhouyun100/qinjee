package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.CheckTypeDao;
import com.qinjee.masterdata.dao.CompanyCodeDao;
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.*;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.ExportList;
import com.qinjee.masterdata.model.vo.staff.export.ExportArcVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportBusiness;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.masterdata.utils.export.HeadListUtil;
import com.qinjee.masterdata.utils.export.HeadMapUtil;
import com.qinjee.masterdata.utils.export.HeadTypeUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.utils.ExcelUtil;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class StaffCommonServiceImpl implements IStaffCommonService {
    private static final Logger logger = LoggerFactory.getLogger(StaffCommonServiceImpl.class);
    private static final String ARCHIVE = "档案";
    private static final String PREEMP = "预入职";
    private static final String IDTYPE = "证件类型";
    private static final String IDNUMBER = "证件号码";
    private static final String PHONE = "手机";
    private static final Integer MAXISM= 5242880;
    private static final String FILE="FILE";
    private static final  String PHOTO="PHOTO";


    @Autowired
    private CustomArchiveTableDao customArchiveTableDao;
    @Autowired
    private CustomArchiveGroupDao customArchiveGroupDao;
    @Autowired
    private CustomArchiveFieldDao customArchiveFieldDao;
    @Autowired
    private CustomArchiveTableDataDao customArchiveTableDataDao;
    @Autowired
    private CustomArchiveFieldCheckDao customArchiveFieldCheckDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private CompanyCodeDao companyCodeDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private PreEmploymentDao preEmploymentDao;
    @Autowired
    private CheckTypeDao checkTypeDao;
    @Override
    public void insertCustomArichiveTable(CustomArchiveTable customArchiveTable, UserSession userSession) {
        customArchiveTable.setCompanyId(userSession.getCompanyId());
        customArchiveTable.setCreatorId(userSession.getArchiveId());
        customArchiveTable.setIsDelete((short) 0);
        customArchiveTableDao.insertSelective(customArchiveTable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustomArchiveTable(List<Integer> list) throws Exception {
        Integer max = customArchiveTableDao.selectMaxPrimaryKey();
        for (Integer integer : list) {
            if (max < integer) {
                throw new Exception("id有误");
            }
        }
        customArchiveTableDao.deleteCustomTable(list);
    }

    @Override
    public void updateCustomArchiveTable(CustomArchiveTable customArchiveTable) {

        customArchiveTableDao.updateByPrimaryKeySelective(customArchiveTable);
    }

    @Override
    public PageResult<CustomArchiveTable> selectCustomArchiveTable(Integer currentPage, Integer pageSize, UserSession userSession) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomArchiveTable> customArchiveTables = customArchiveTableDao.selectByPage(userSession.getCompanyId());
        for (CustomArchiveTable customArchiveTable : customArchiveTables) {
            logger.info("展示自定义表名{}", customArchiveTable.getTableName());
        }
        return new PageResult<>(customArchiveTables);
    }

    @Override
    public void insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup, UserSession userSession) {
        customArchiveGroup.setCreatorId(userSession.getArchiveId());
        customArchiveGroup.setIsDelete((short) 0);

        customArchiveGroupDao.insertSelective(customArchiveGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomArchiveGroup(List<Integer> list) throws Exception {
        Integer max = customArchiveGroupDao.selectMaxPrimaryKey();
        for (Integer integer : list) {
            if (max < integer) {
                throw new Exception("id有误");
            }
        }
        customArchiveGroupDao.deleteCustomGroup(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomArchiveGroup(CustomArchiveGroup customArchiveGroup) {
        customArchiveGroupDao.updateByPrimaryKeySelective(customArchiveGroup);
    }

    @Override
    public PageResult<CustomArchiveField> selectArchiveFieldFromGroup(Integer currentPage, Integer pageSize, Integer customArchiveGroupId) {
          PageHelper.startPage(currentPage, pageSize);
        //获得自定义组中自定义表id的集合
        List<CustomArchiveField> list=customArchiveFieldDao.selectCustomArchiveField(customArchiveGroupId);
        return new PageResult<>(list);
    }

    @Override
    public void insertCustomArchiveField(CustomArchiveField customArchiveField, UserSession userSession) {
        customArchiveField.setCreatorId(userSession.getArchiveId());
        customArchiveField.setIsDelete((short) 0);
        customArchiveFieldDao.insertSelective(customArchiveField);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomArchiveField(List<Integer> list) throws Exception {
        Integer max = customArchiveFieldDao.selectMaxPrimaryKey();
        for (Integer integer : list) {
            if (max < integer) {
                throw new Exception("id有误");
            }
        }
        customArchiveFieldDao.deleteCustomField(list);
    }

    @Override
    public void updateCustomArchiveField(CustomArchiveField customArchiveField) {
        customArchiveFieldDao.updateByPrimaryKeySelective(customArchiveField);
    }

    @Override
    public PageResult<CustomArchiveField> selectCustomArchiveField(Integer currentPage, Integer pageSize,
                                                                   Integer customArchiveTableId) {

        PageHelper.startPage(currentPage, pageSize);
        //根据自定义表找自定义字段id
        List<CustomArchiveField> list = customArchiveFieldDao.selectFieldByTableId(customArchiveTableId);
        return new PageResult<>(list);
    }

    @Override
    public CustomArchiveField selectCustomArchiveFieldById(Integer customArchiveFieldId) {
        return customArchiveFieldDao.selectByPrimaryKey(customArchiveFieldId);
    }


    @Override
    public List<Integer> getCompanyId(UserSession userSession) {
        List<Integer> integerList = organizationDao.getCompanyIdByAuth(userSession.getArchiveId());
        List<Integer> companyIdByArchiveId = organizationDao.getCompanyIdByArchiveId(userSession.getArchiveId());
        integerList.removeAll(companyIdByArchiveId);
        integerList.addAll(companyIdByArchiveId);
        return integerList;
    }

    @Override
    public List<Integer> getOrgIdByCompanyId(Integer orgId) {

        return organizationDao.getOrgIdByCompanyId(orgId);
    }

    @Override
    public List<Post> getPostByOrgId(Integer orgId) {

        return postDao.getPostByOrgId(orgId);
    }

    @Override
    public List<String> selectTableFromOrg(UserSession userSession) {
        return customArchiveTableDao.selectNameBycomId(userSession.getCompanyId());
    }

    @Override
    public List<String> selectFieldValueById(Integer customArchiveFieldId) {
        //找到企业代码id
        Integer id = customArchiveFieldDao.selectCodeId(customArchiveFieldId);
        //找到自定义字段的值
        return companyCodeDao.selectValue(id);
    }

    @Override
    public void insertCustomArchiveTableData(CustomArchiveTableData customArchiveTableData, UserSession userSession) {
        //将前端传过来的大字段进行解析
        StringBuilder bigData = new StringBuilder();
        JSONObject jsono = JSONObject.parseObject(customArchiveTableData.getBigData());
        List<String> strings = new ArrayList<>(jsono.keySet());
        for (String string : strings) {
            bigData.append("@@").append(string).append("@@:").append(jsono.get(string));
        }
        customArchiveTableData.setIsDelete(0);
        customArchiveTableData.setOperatorId(userSession.getArchiveId());
        customArchiveTableData.setBigData(bigData.toString());
        customArchiveTableDataDao.insertSelective(customArchiveTableData);
    }

    @Override
    public void updateCustomArchiveTableData(CustomArchiveTableData customArchiveTableData) {
        customArchiveTableDataDao.updateByPrimaryKey(customArchiveTableData);
    }

    @Override
    public PageResult<CustomArchiveTableData> selectCustomArchiveTableData(Integer currentPage, Integer pageSize, Integer customArchiveTableId) {
        PageHelper.startPage(currentPage, pageSize);
        //通过自定义表id找到数据id集合
        List<Integer> integerList = customArchiveTableDataDao.selectCustomArchiveTableId(customArchiveTableId);
        List<CustomArchiveTableData> list = customArchiveTableDataDao.selectByPrimaryKeyList(integerList);
        return new PageResult<>(list);
    }


    @Override
    public List<String> checkField(Integer fieldId) {
        //通过字段名找到验证code
        List<String> stringList = customArchiveFieldCheckDao.selectCheckName(fieldId);
        //根据验证code找到验证名称
        return checkTypeDao.selectCheckNameList(stringList);
    }

    @Override
    public void importArcFile(MultipartFile multipartFile, UserSession userSession) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchFieldException {
       //excel方法获得值
        //根据导入文档建立Vo类，然后利用反射设值，此处先用Export代替
        List<Map<String, String>> mapList = ExcelUtil.readExcel(multipartFile);
        List<ExportArcVo> list=new ArrayList<>();
        //获得反射对象
        Class<ExportArcVo> exportArcVoClass = ExportArcVo.class;
        ExportArcVo exportArcVo =exportArcVoClass.newInstance();
        //循环设值
        for (Map<String, String> map : mapList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Field declaredField =exportArcVoClass.getDeclaredField(entry.getKey());
                declaredField.setAccessible(true);
                declaredField.set(exportArcVoClass,entry.getValue());
            }
            list.add(exportArcVo);
        }
        //入库操作
        HeadListUtil.getObjectList(mapList, ExportArcVo.class)
    }



    private String getBigData(Map<String, List<String>> stringListMap, int i) {
        StringBuilder bigData = new StringBuilder();
        Set<Map.Entry<String, List<String>>> entries = stringListMap.entrySet();
        List<Map.Entry<String, List<String>>> entries1 = new ArrayList<>(entries);
        for (Map.Entry<String, List<String>> stringListEntry : entries1) {
            String key = stringListEntry.getKey();
            String s = stringListEntry.getValue().get(i);
            bigData.append("@@").append(key).append("@@:").append(s).append(";");
        }
        return bigData.toString();
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
    public void exportPreFile(String title,List<Integer> list, HttpServletResponse response){
        Map<Integer,Map<String,Object>> map=preEmploymentDao.selectExportPreList(list);
        ExportFile exportFile=new ExportFile();
        exportFile.setTittle(title);
        ExportList exportList=new ExportList();
        exportList.setMap(map);
        exportFile.setExportList(exportList);
        ExcelUtil.download(response,exportFile.getTittle(),
                HeadMapUtil.getHeadsForPre(),
                getDates(exportFile,HeadMapUtil.getHeadsForPre()),
                getTypeMap(HeadMapUtil.getHeadsForPre()));
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
                Map<String, String> stringMap = new LinkedHashMap<>();
                for (String head : heads) {
                    stringMap.put(head,String.valueOf(stringObjectMap.get(customArchiveFieldDao.selectFieldCodeByName(head))));
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

    /**
     * 档案存储字段类型的集合
   **/
    private Map<String, String> getTypeMapForArc(ExportFile exportFile, List<String> heads) {
        Map<String, String> map = new HashMap<>();
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
     * 存储字段类型的集合
     **/
    private Map<String, String> getTypeMap(List<String> heads) {
        Map<String, String> map = new HashMap<>();
        for (String head : heads) {
            map.put(head, HeadTypeUtil.getTypeForPre().get(head));
        }
        return map;
    }
    @Override
    public void exportBusiness(ExportBusiness exportBusiness, HttpServletResponse response, UserSession userSession) {

    }


}






