package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.*;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.ForWardPutFile;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.ExcelUtil;
import com.qinjee.utils.UpAndDownUtil;
import entity.ExcelEntity;
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
    private PostDao postDao;

    @Override
    public Integer insertCustomArichiveTable(CustomArchiveTable customArchiveTable) {
        int i = customArchiveTableDao.insertSelective(customArchiveTable);
        return i;
    }

    @Transactional(rollbackFor =Exception.class)
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
        PageHelper.startPage(currentPage,pageSize);
        List<CustomArchiveTable> customArchiveTables = customArchiveTableDao.selectByPage();
        PageResult pageResult=new PageResult(customArchiveTables);
        return pageResult;
    }

    @Override
    public void insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup) {
            customArchiveGroupDao.insertSelective(customArchiveGroup);
    }

    @Override
    @Transactional(rollbackFor =Exception.class)
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
        public PageResult<CustomArchiveTable> selectCustomTableFromGroup (Integer currentPage, Integer
        pageSize, Integer customArchiveGroupId){
            PageHelper.startPage(currentPage, pageSize);
            //获得自定义组中自定义表id的集合
            List<Integer> integerList = customArchiveGroupDao.selectTableId(customArchiveGroupId);

            List<CustomArchiveTable> list= customArchiveTableDao.selectByPrimaryKeyList(integerList);
            PageResult pageResult=new PageResult(list);
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
            List<CustomArchiveField> list= customArchiveFieldDao.selectByPrimaryKeyList(integerList);
            return new PageResult<>(list);
    }

    @Override
    public CustomArchiveField selectCustomArchiveFieldById(Integer customArchiveFieldId) {
          return customArchiveFieldDao.selectByPrimaryKey(customArchiveFieldId);
    }




    @Override
        public List<Integer> getCompanyId (UserSession userSession){
        Integer archiveId = userSession.getArchiveId();
        List<Integer> list = organizationDao.getCompanyIdByArchiveId(archiveId);
        return list;

        }

        @Override
        public List<Integer> getOrgIdByCompanyId (Integer orgId){

                List<Integer> list = organizationDao.getOrgIdByCompanyId(orgId);
                return list;
        }

        @Override
        public List<Integer> getPostByOrgId (Integer orgId){

                List<Integer> list = postDao.getPostByOrgId(orgId);
                return list;
        }

    @Override
    public void insertCustomArchiveTableData(CustomArchiveTableData customArchiveTableData, UserSession userSession) {
        //TODO 需要弄清楚数组的形式
        Integer archiveId = userSession.getArchiveId();
        //将前端传过来的大字段进行解析
                 JSONArray json = (JSONArray) JSONArray.toJSON(customArchiveTableData.getBigData());
                for (int i = 0; i < json.size(); i++) {
                    JSONObject jsono = JSONObject.parseObject(json.get(i).toString());
                    for (String s : jsono.keySet()) {
                        s.replace(s, "@" + s + "@");
                    }
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

         List<CustomArchiveTableData> list=customArchiveTableDataDao.selectByPrimaryKeyList(integerList);

         return new PageResult<>(list);
    }


        @Override
        public List<String> checkField (Integer fieldId){

                //通过字段名找到验证code
               return customArchiveFieldCheckDao.selectCheckName(fieldId);
        }

        @Override
        public ResponseResult importFile (String path){
            ExcelEntity excelEntity = null;
            MultipartFile multipartFile = ExcelUtil.getMultipartFile(new File(path));
            try {
                excelEntity = ExcelUtil.readExcel(multipartFile);
                return new ResponseResult(excelEntity, CommonCode.SUCCESS);
            } catch (IOException e) {
                return new ResponseResult(excelEntity, CommonCode.FAIL);
            }

        }

        @Override
        public ResponseResult exportFile (String path, String title, Integer customArchiveTableId){
            //得到response对象
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            String[] heads = null;
            Map<String, String> fieldNameTypemap = new HashMap<>();
            List<Map<String, String>> mapList = new ArrayList<>();
            //获取heads，通过customTableId找到字段名存入heads
            try {
                List<Integer> integerList = customArchiveFieldDao.selectFieldId(customArchiveTableId);
                List<CustomArchiveField> list=customArchiveFieldDao.selectByList(integerList);

                for (int i = 0; i < list.size(); i++) {
                    heads[i] = list.get(i).getFieldName();
                }
            } catch (Exception e) {
                logger.error("获取heads失败");
            }
            //通过字段表获取字段类型
            try {
                List<String> fieldTypeList = customArchiveFieldDao.selectFieldType(customArchiveTableId);
                //通过类型获取到code
                for (int i = 0; i < fieldTypeList.size(); i++) {
                    fieldNameTypemap.put(heads[i], fieldTypeList.get(i));
                }
            } catch (Exception e) {
                logger.error("获取字段类型失败");
            }
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
                            map.put(s.split("@qinjee@")[0], jsono.getString(s));
                        }
                    }

                }
            } catch (Exception e) {
                logger.error("获取字段值失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
            try {
                ExcelUtil.download(path, response, title, heads, mapList, fieldNameTypemap);
                return new ResponseResult(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("导出文件失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }

        }

        @Override
        public ResponseResult putFile (String path){
            //TODO 文件对象键的定义
            String key = "";
            try {
                UpAndDownUtil.putFile(path, key);
                return new ResponseResult(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("文件上传错误");
                return new ResponseResult(false, CommonCode.FAIL);
            }

        }

        @Override
        public ResponseResult UploadFileByForWard () {

            try {
                String s = UpAndDownUtil.TransToForward();
                ForWardPutFile forWardPutFile = new ForWardPutFile();
                forWardPutFile.setString(s);
                //TODO 对象键的定义需要制定规则，利用规则生成
                forWardPutFile.setKey("");
                return new ResponseResult(forWardPutFile, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("文件上传获取对象错误");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }

    }







