package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.AttachmentRecordDao;
import com.qinjee.masterdata.dao.CheckTypeDao;
import com.qinjee.masterdata.dao.CompanyCodeDao;
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.*;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.ExportPreVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportArc;
import com.qinjee.masterdata.model.vo.staff.export.ExportBusiness;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.masterdata.utils.export.HeadMapUtil;
import com.qinjee.masterdata.utils.export.HeadTypeUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
    @Autowired
    private AttachmentRecordDao attachmentRecordDao;
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
    public void importFile(String path, UserSession userSession) throws NoSuchFieldException, IllegalAccessException, IOException {
        MultipartFile multipartFile = ExcelUtil.getMultipartFile(new File(path));
        ExcelUtil.readExcel(multipartFile);
        Integer tableId = null;
        //key是字段名称，value是值
        Map<String, List<String>> stringListMap = null;

        Map<Integer, List<String>> fieldMap = new HashMap<>();
        //需要进行入库操作
        //根据companyId，功能code为档案和预入职的表
        List<Integer> list = customArchiveTableDao.selectidbycomidandfunccode(userSession.getCompanyId());
        //根据自定义表找到自定义字段名存进map中
        for (Integer integer : list) {
            List<String> list1 = customArchiveFieldDao.selectFieldNameListByTableId(integer);
            fieldMap.put(integer, list1);
        }
        //匹配找到需要设值的表id
        for (Map.Entry<Integer, List<String>> integerListEntry : fieldMap.entrySet()) {
            List<String> strings = new ArrayList<>(stringListMap.keySet());
            //将需要设置进行业务id寻找的字段剔除，保证能找到tableId
            for (String s : strings) {
                if (PHONE.equals(s) || IDNUMBER.equals(s) || IDTYPE.equals(s)) {
                    strings.remove(s);
                }
            }
            if (integerListEntry.getValue().containsAll(strings)) {
                tableId = integerListEntry.getKey();
            } else {
                logger.error("字段有误");
            }
        }
        Integer integer = customArchiveTableDao.selectInside(tableId);
        if (integer > 0) {
            //  若表为内置表
            //根据表id确认是档案表还是预入职表
            String funcCode = customArchiveTableDao.selectFuncCode(tableId);
            if (ARCHIVE.equals(funcCode)) {
                //将值设入属性,判断是新增还是更新
                List<String> list1 = stringListMap.get(IDTYPE);
                List<String> list2 = stringListMap.get(IDNUMBER);
                for (int i = 0; i < list1.size(); i++) {
                    Integer id = userArchiveDao.selectId(list1.get(i), list2.get(i));
                    if (null == id) {
                        UserArchive userArchive = new UserArchive();
                        setValue(stringListMap, i, userArchive);
                        userArchiveDao.insertSelective(userArchive);
                    } else {
                        UserArchive userArchive = new UserArchive();
                        userArchive.setArchiveId(id);
                        setValue(stringListMap, i, userArchive);
                        userArchiveDao.updateByPrimaryKeySelective(userArchive);
                    }
                }
            }
            if (PREEMP.equals(funcCode)) {
                List<String> list1 = stringListMap.get(PHONE);
                for (int i = 0; i < list1.size(); i++) {
                    Integer id = preEmploymentDao.selectIdByNumber(list1.get(i));
                    if (null == id) {
                        PreEmployment preEmployment = new PreEmployment();
                        setValue(stringListMap, i, preEmployment);
                        preEmploymentDao.insertSelective(preEmployment);
                    } else {
                        //将值设入属性
                        PreEmployment preEmployment = new PreEmployment();
                        preEmployment.setEmploymentId(id);
                        setValue(stringListMap, i, preEmployment);
                        preEmploymentDao.updateByPrimaryKey(preEmployment);
                    }
                }
            }
            logger.error("未找到对应的物理表,导入失败");
           /*
            //将值设入属性
            Set<Map.Entry<String, List<String>>> entries = stringListMap.entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                Field declaredField = object.getClass().getDeclaredField(entry.getKey());
                declaredField.setAccessible(true);
                declaredField.set(object,entry.getValue());
            }
            //将值设置给物理表
            //物理属性名集合
            List<String> strings = customArchiveFieldDao.selectPhysicNameByList(new ArrayList<>(stringListMap.keySet()));
            //拼接sql并插入
            customArchiveTableDataDao.insertCustom(s,getFieldSql(strings),getValueSql(strings));*/
        } else {
            //若此表为自定义表，说明已经存在了基本表。此时需要需要通过传过来的手机号找到业务id确认存进哪张表中

            String funcCode = customArchiveTableDao.selectFuncCode(tableId);
            if (ARCHIVE.equals(funcCode)) {
                //根据证件类型，证件号找到id，作为业务id
                List<String> list1 = stringListMap.get(IDTYPE);
                List<String> list2 = stringListMap.get(IDNUMBER);
                for (int i = 0; i < list1.size(); i++) {
                    //业务id
                    Integer id = userArchiveDao.selectId(list1.get(i), list2.get(i));
                    setCustom(userSession, tableId, stringListMap, i, id);
                }
            }
            if (PREEMP.equals(funcCode)) {
                List<String> list1 = stringListMap.get(PHONE);
                for (int i = 0; i < list1.size(); i++) {
                    Integer id = preEmploymentDao.selectIdByNumber(list1.get(i));
                    setCustom(userSession, tableId, stringListMap, i, id);
                }
            }
            logger.error("未找到对应的自定义表,导入失败");
        }
    }

    private void setCustom(UserSession userSession, Integer tableId, Map<String, List<String>> stringListMap, int i, Integer id) {
        CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData();
        customArchiveTableData.setBusinessId(id);
        customArchiveTableData.setTableId(tableId);
        customArchiveTableData.setOperatorId(userSession.getArchiveId());
        customArchiveTableData.setIsDelete(0);
        String bigData = getBigData(stringListMap, i);
        customArchiveTableData.setBigData(bigData);
        Integer integer1 = customArchiveTableDataDao.selectTableIdByBusinessIdAndTableId(id, tableId);
        if (null == integer1 || 0 == integer1) {
            customArchiveTableDataDao.insertSelective(customArchiveTableData);
        } else {
            customArchiveTableDataDao.updateByPrimaryKey(customArchiveTableData);
        }
    }

    private void setValue(Map<String, List<String>> stringListMap, int i, Object o) throws NoSuchFieldException, IllegalAccessException {
        Set<Map.Entry<String, List<String>>> entries = stringListMap.entrySet();
        List<Map.Entry<String, List<String>>> entries1 = new ArrayList<>(entries);
        for (Map.Entry<String, List<String>> stringListEntry : entries1) {
            Field declaredField = o.getClass().getDeclaredField(stringListEntry.getKey());
            declaredField.setAccessible(true);
            declaredField.set(o, stringListEntry.getValue().get(i));
        }
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
    public void exportPreFile(ExportArc exportArc, HttpServletResponse response, UserSession userSession){
    }


    /**
     * 通过号码匹配得到拉黑原因，设置进VO类
     */
    private void setBlockReason(ExportPreVo exportPreVo, List<Blacklist> list) {
        for (Blacklist blacklist : list) {
            if (blacklist.getPhone().equals(exportPreVo.getPhone())) {
                exportPreVo.setBlockReason(blacklist.getBlockReason());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportArcFile(ExportArc exportArc, HttpServletResponse response, UserSession userSession) {
        ExcelUtil.download(exportArc.getPath(), response, exportArc.getTittle(),
                getHeadsByArc(exportArc),
                getDatesForArc(exportArc),
                getTypeMapForArc(exportArc, getHeadsByArc(exportArc)));
    }

    private List<Map<String, String>> getDatesForArc(ExportArc exportArcVo) {
        List<Map<String, String>> mapList = new ArrayList<>();
        List<String> keyList = getKeyList(exportArcVo);
        if(exportArcVo.getArchiveShowVo().getQuerySchemaId() ==null || exportArcVo.getArchiveShowVo().getQuerySchemaId() ==0){
            List<String> strings = new ArrayList<>();
            for (String s : keyList) {
                strings.add(HeadMapUtil.getHeadForArc().get(s));
            }
            keyList.clear();
            keyList.addAll(strings);
        }
        List<Map<String, Object>> maps = new ArrayList<>(exportArcVo.getArchiveShowVo().getMap().values());
            for (Map<String, Object> stringObjectMap : maps) {
                Map<String, String> stringMap = new HashMap<>();
                List<Object> objects = new ArrayList<>(stringObjectMap.values());
                for (int i = 0; i < objects.size(); i++) {
                    stringMap.put(keyList.get(i), String.valueOf(objects.get(i)));
                }
                mapList.add(stringMap);
            }
            return mapList;
    }
      private List<String> getHeadsByArc(ExportArc exportArcVo) {
          Integer querySchemaId = exportArcVo.getArchiveShowVo().getQuerySchemaId();
          if(querySchemaId ==null || querySchemaId ==0){
              String[] strings={"姓名","单位","部门","联系电话","任职类型","工号","档案id","直接上级","岗位","试用期到期时间","入职日期"};
              return Arrays.asList(strings);
          }
          List<String> keyList = getKeyList(exportArcVo);
          List<String> list = customArchiveFieldDao.selectFieldNameByCodeList(keyList);
          return list;
      }

    private List<String> getKeyList(ExportArc exportArcVo) {
        List<String> keyList=new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>(exportArcVo.getArchiveShowVo().getMap().values());
        for (Map<String, Object> stringObjectMap : maps) {
            keyList = new ArrayList<>((new ArrayList<>(stringObjectMap.keySet())));
        }
        return keyList;
    }

    /**
     * 存储字段类型的集合
   **/
    private Map<String, String> getTypeMapForArc(ExportArc exportArcVo, List<String> heads) {
        Map<String, String> map = new HashMap<>();
        if(exportArcVo.getArchiveShowVo().getQuerySchemaId()==null || exportArcVo.getArchiveShowVo().getQuerySchemaId()==0) {
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
    @Override
    public void exportBusiness(ExportBusiness exportBusiness, HttpServletResponse response, UserSession userSession) {

    }


}






