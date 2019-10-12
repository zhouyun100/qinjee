package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.*;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.masterdata.model.entity.CustomArchiveGroup;
import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import com.qinjee.masterdata.model.entity.CustomArchiveTableData;
import com.qinjee.masterdata.model.vo.staff.ForWardPutFile;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.utils.ExcelUtil;
import com.qinjee.utils.UpAndDownUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Administrator
 */
@Service
public class StaffCommonServiceImpl implements IStaffCommonService {
    private static final Logger logger = LoggerFactory.getLogger(StaffCommonServiceImpl.class);
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

    @Override
    public void insertCustomArichiveTable(CustomArchiveTable customArchiveTable) {
        customArchiveTableDao.insertSelective(customArchiveTable);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustomArchiveTable(List<Integer> list) throws Exception {
        Integer max = customArchiveTableDao.selectMaxPrimaryKey();
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i)) {
                throw new Exception("自定义表参数过大");
            }
        }
        customArchiveTableDao.deleteCustomTable(list);
    }

    @Override
    public void updateCustomArchiveTable(CustomArchiveTable customArchiveTable) {

        customArchiveTableDao.updateByPrimaryKey(customArchiveTable);

    }

    @Override
    public PageResult<CustomArchiveTable> selectCustomArchiveTable(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomArchiveTable> customArchiveTables = customArchiveTableDao.selectByPage();
        PageResult pageResult = new PageResult(customArchiveTables);
        return pageResult;
    }

    @Override
    public void insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup) {
        customArchiveGroupDao.insertSelective(customArchiveGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomArchiveGroup(List<Integer> list) throws Exception {
        Integer max = customArchiveGroupDao.selectMaxPrimaryKey();
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i)) {
                throw new Exception("自定义组参数过大");
            }
        }
        customArchiveGroupDao.deleteCustomGroup(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomArchiveGroup(CustomArchiveGroup customArchiveGroup) {
        customArchiveGroupDao.updateByPrimaryKey(customArchiveGroup);
    }

    @Override
    public PageResult<CustomArchiveTable> selectCustomTableFromGroup(Integer currentPage, Integer
            pageSize, Integer customArchiveGroupId) {
        PageHelper.startPage(currentPage, pageSize);
        //获得自定义组中自定义表id的集合
        List<Integer> integerList = customArchiveGroupDao.selectTableId(customArchiveGroupId);

        List<CustomArchiveTable> list = customArchiveTableDao.selectByPrimaryKeyList(integerList);
        PageResult pageResult = new PageResult(list);
        return pageResult;
    }

    @Override
    public void insertCustomArchiveField(CustomArchiveField customArchiveField) {
        customArchiveFieldDao.insertSelective(customArchiveField);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomArchiveField(List<Integer> list) throws Exception {
        Integer max = customArchiveFieldDao.selectMaxPrimaryKey();
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i)) {
                throw new Exception("参数不合理！");
            }
        }
        customArchiveFieldDao.deleteCustomField(list);
    }

    @Override
    public void updateCustomArchiveField(CustomArchiveField customArchiveField) {
        customArchiveFieldDao.updateByPrimaryKey(customArchiveField);
    }

    @Override
    public PageResult<CustomArchiveField> selectCustomArchiveField(Integer currentPage, Integer pageSize,
                                                                   Integer customArchiveTableId) {

        PageHelper.startPage(currentPage, pageSize);
        //根据自定义表找自定义字段id
        List<Integer> integerList = customArchiveFieldDao.selectFieldId(customArchiveTableId);
        List<CustomArchiveField> list = customArchiveFieldDao.selectByPrimaryKeyList(integerList);
        return new PageResult<>(list);
    }

    @Override
    public CustomArchiveField selectCustomArchiveFieldById(Integer customArchiveFieldId) {
        return customArchiveFieldDao.selectByPrimaryKey(customArchiveFieldId);
    }


    @Override
    public List<Integer> getCompanyId(UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        List<Integer> list = organizationDao.getCompanyIdByArchiveId(archiveId);
        return list;

    }

    @Override
    public List<Integer> getOrgIdByCompanyId(Integer orgId) {

        List<Integer> list = organizationDao.getOrgIdByCompanyId(orgId);
        return list;
    }

    @Override
    public List<Integer> getPostByOrgId(Integer orgId) {

        List<Integer> list = postDao.getPostByOrgId(orgId);
        return list;
    }

    @Override
    public List<String> selectTableFromOrg(UserSession userSession) {
        //根据部门id筛选自己的表id
        List<Integer> list1 = customArchiveTableDao.selectIdByComId(userSession.getCompanyId());
        List<String> list = customArchiveTableDao.selectNameById(list1);
        return list;
    }

    @Override
    public List<String> staffCommonService(Integer customArchiveFieldId) {
        //找到企业代码id
        Integer id = customArchiveFieldDao.selectCodeId(customArchiveFieldId);
        //找到自定义字段的值
        List<String> list = companyCodeDao.selectValue(id);
        return list;
    }

    @Override
    public void insertCustomArchiveTableData(CustomArchiveTableData customArchiveTableData, UserSession userSession) {
        //TODO 需要弄清楚数组的形式
        Integer archiveId = userSession.getArchiveId();
        //将前端传过来的大字段进行解析
        JSONArray json = (JSONArray) JSONArray.toJSON(customArchiveTableData.getBigData());
        JSONObject jsono = JSONObject.parseObject(json.toString());
        for (String s : jsono.keySet()) {
            s.replace(s, "@" + s + "@");
        }
        customArchiveTableData.setOperatorId(archiveId);
        customArchiveTableDataDao.insert(customArchiveTableData);
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
        return customArchiveFieldCheckDao.selectCheckName(fieldId);
    }

    @Override
    public void importFile(String path,UserSession userSession) throws IOException {
        MultipartFile multipartFile = ExcelUtil.getMultipartFile(new File(path));
        Integer tableId=null;
        //key是字段名称，value是值
        Map<String, List<String>> stringListMap = ExcelUtil.readExcel(multipartFile);
        Map<Integer,List<String>>  fieldMap=new HashMap<>();
        //需要进行入库操作
        //根据companyId找到自定义表的集合
        List<Integer> list = customArchiveTableDao.selectIdByComId(userSession.getCompanyId());
        //根据自定义表找到自定义字段名存进map中
        for (Integer integer : list) {
            List<String> list1 = customArchiveFieldDao.selectFieldNameListByTableId(integer);
            fieldMap.put(integer,list1);
        }
        //通过匹配找到需要设值的表id
        for (Map.Entry<Integer, List<String>> integerListEntry : fieldMap.entrySet()) {
            List<String> strings = new ArrayList<>(stringListMap.keySet());
            if(strings.containsAll(integerListEntry.getValue())){
                tableId= integerListEntry.getKey();
            }
        }
        // TODO 若表为内置表
           //将字段设置给自定义表
           //将值设置给物理表  需要利用反射设置值
        //TODO 若表为自定义表
            //将字段设置给自定义表
            //将值设置给数据表 直接拼接
    }

    @Override
    public void exportFile(String path, String title, Integer customArchiveTableId) {
        //得到response对象
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        List<String> heads = new ArrayList<>();
        Map<String, String> fieldNameTypemap = new HashMap<>();
        List<Map<String, String>> mapList = new ArrayList<>();
        //获取heads，通过customTableId找到字段名存入heads
        try {
            List<Integer> integerList = customArchiveFieldDao.selectFieldId(customArchiveTableId);
            List<CustomArchiveField> list = customArchiveFieldDao.selectByList(integerList);
            for (int i = 0; i < list.size(); i++) {
                heads.add(list.get(i).getFieldName());
            }
        } catch (Exception e) {
            logger.error("获取heads失败");
        }
        //通过字段表获取字段类型
        try {
            List<String> fieldTypeList = customArchiveFieldDao.selectFieldType(customArchiveTableId);
            //通过类型获取到code
            for (int i = 0; i < fieldTypeList.size(); i++) {
                fieldNameTypemap.put(heads.get(i), fieldTypeList.get(i));
            }
        } catch (Exception e) {
            logger.error("获取字段类型失败");
        }
        //通过表id判断是否是内置表
        Integer integer = customArchiveTableDao.selectInside(customArchiveTableId);
        if (integer > 0) {
            //TODO 是内置表，通过物理表名获得实体对象
            //找到物理字段，反射判断属性值然后设值

        } else {
            //通过自定义表id获得数据表里的大字段，解析出来值存进dates
            try {
                List<Integer> integerList = customArchiveTableDataDao.selectCustomArchiveTableId(customArchiveTableId);
                for (int i = 0; i < integerList.size(); i++) {
                    Map<String, String> map = new HashMap<>();
                    CustomArchiveTableData customArchiveTableData = customArchiveTableDataDao.selectByPrimaryKey(integerList.get(i));
                    JSONArray json = (JSONArray) JSONArray.toJSON(customArchiveTableData.getBigData());
                    for (int j = 0; j < json.size(); j++) {
                        JSONObject jsono = JSONObject.parseObject(json.get(i).toString());
                        for (String s : jsono.keySet()) {
                            map.put(s.replace("@", "").trim(), jsono.getString(s));
                        }
                        mapList.add(map);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("获取字段值失败");
            }
        }

        ExcelUtil.download(path, response, title, heads, mapList, fieldNameTypemap);

    }

    @Override
    public void putFile(String path) {
        //TODO 文件对象键的定义
        String key = "";
        UpAndDownUtil.putFile(path, key);
    }

    @Override
    public ForWardPutFile UploadFileByForWard() {
        String s = UpAndDownUtil.TransToForward();
        ForWardPutFile forWardPutFile = new ForWardPutFile();
        forWardPutFile.setString(s);
        //TODO 对象键的定义需要制定规则，利用规则生成
        forWardPutFile.setKey("");
        return forWardPutFile;
    }
}





