package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.*;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.ForWardPutFile;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
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
    private CheckTypeDao checkTypeDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private PostDao postDao;

    @Override
    public ResponseResult insertCustomArichiveTable(CustomArchiveTable customArchiveTable) {
        if (customArchiveTable instanceof CustomArchiveTable) {
            try {
                customArchiveTableDao.insert(customArchiveTable);
            } catch (Exception e) {
                logger.error("插入自定义表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult deleteCustomArchiveTable(List<Integer> list) {
        Integer max = 0;
        try {
            max = customArchiveTableDao.selectMaxPrimaryKey();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                customArchiveTableDao.deleteCustomTable(list.get(i));
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除自定义表失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }
        @Override
        public ResponseResult updateCustomArchiveTable(CustomArchiveTable customArchiveTable) {
            if (customArchiveTable instanceof CustomArchiveTable) {
                try {
                    customArchiveTableDao.updateByPrimaryKey(customArchiveTable);
                    return new ResponseResult(true, CommonCode.SUCCESS);
                } catch (Exception e) {
                    logger.error("更新自定义表失败");
                    return new ResponseResult(false, CommonCode.FAIL);
                }
            } else {
                return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
            }
        }

    @Override
    public ResponseResult<PageResult<CustomArchiveTable>> selectCustomArchiveTable(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomArchiveTable> list = null;
        PageResult<CustomArchiveTable> pageResult = new PageResult<>();
        try {
            list = customArchiveTableDao.selectByPage();
            pageResult.setList(list);
            return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("展示自定义表失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup) {
        if (customArchiveGroup instanceof CustomArchiveGroup) {
            try {
                customArchiveGroupDao.insert(customArchiveGroup);
            } catch (Exception e) {
                logger.error("插入自定义组失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCustomArchiveGroup(List<Integer> list) {
        try {
            Integer max = 0;
            max = customArchiveGroupDao.selectMaxPrimaryKey();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                customArchiveGroupDao.deleteCustomGroup(list.get(i));
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除自定义组失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateCustomArchiveGroup(CustomArchiveGroup customArchiveGroup) {
        if (customArchiveGroup instanceof CustomArchiveGroup) {
            try {
                customArchiveGroupDao.updateByPrimaryKey(customArchiveGroup);
                return new ResponseResult(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("更新自定义组失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        } else {
            return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
        }
    }


        @Override
        public ResponseResult<PageResult<CustomArchiveTable>> selectCustomTableFromGroup (Integer currentPage, Integer
        pageSize, Integer customArchiveGroupId){
            PageHelper.startPage(currentPage, pageSize);
            List<CustomArchiveTable> list = null;
            PageResult<CustomArchiveTable> pageResult = new PageResult<>();
            try {
                List<Integer> integerList = customArchiveGroupDao.selectTableId(customArchiveGroupId);
                for (int i = 0; i < integerList.size(); i++) {
                    CustomArchiveTable customArchiveTable = customArchiveTableDao.selectByPrimaryKey(integerList.get(i));
                    list.add(customArchiveTable);
                }
                pageResult.setList(list);
                return new ResponseResult(pageResult, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("查询自定义表失败");
                return new ResponseResult(pageResult, CommonCode.FAIL);
            }
        }

    @Override
    public ResponseResult insertCustomArchiveField(CustomArchiveField customArchiveField) {
        if (customArchiveField instanceof CustomArchiveField) {
            try {
                customArchiveFieldDao.insert(customArchiveField);
            } catch (Exception e) {
                logger.error("插入自定义字段失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCustomArchiveField(List<Integer> list) {
        Integer max = 0;
        try {
            max = customArchiveFieldDao.selectMaxPrimaryKey();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                customArchiveFieldDao.deleteCustomField(list.get(i));
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除自定义字段失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult updateCustomArchiveField(CustomArchiveField customArchiveField) {
        if (customArchiveField instanceof CustomArchiveField) {
            try {
                customArchiveFieldDao.updateByPrimaryKey(customArchiveField);
                return new ResponseResult(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("更新自定义字段失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }

        } else {
            return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
        }
    }

    @Override
    public ResponseResult<PageResult<CustomArchiveField>> selectCustomArchiveField(Integer currentPage, Integer pageSize,
                                                                            Integer customArchiveTableId) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomArchiveField> list = null;
        PageResult<CustomArchiveField> pageResult = new PageResult<>();
        try {
            List<Integer> integerList = customArchiveFieldDao.selectFieldId(customArchiveTableId);
            for (int i = 0; i < integerList.size(); i++) {
                CustomArchiveField customArchiveField = customArchiveFieldDao.selectByPrimaryKey(integerList.get(i));
                list.add(customArchiveField);
            }
            pageResult.setList(list);
            return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("根据表查询自定义字段失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult<CustomArchiveField> selectCustomArchiveFieldById(Integer customArchiveFieldId) {
        CustomArchiveField customArchiveField = null;
        try {
            customArchiveField = customArchiveFieldDao.selectByPrimaryKey(customArchiveFieldId);
            return new ResponseResult(customArchiveField, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("根据id查询自定义字段失败");
            return new ResponseResult(customArchiveField, CommonCode.FAIL);
        }
    }




    @Override
        public ResponseResult getCompany (Integer archiveId){
            try {
                List<Integer> list = organizationDao.getCompanyIdByArchiveId(archiveId);
                return new ResponseResult(list, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("根据档案id获取单位id失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }

        @Override
        public ResponseResult getOrgIdByCompanyId (Integer orgId){
            try {
                List<Integer> list = organizationDao.getOrgIdByCompanyId(orgId);
                return new ResponseResult(list, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("获取单位下的部门id失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }

        @Override
        public ResponseResult getPostByOrgId (Integer orgId){
            try {
                List<Integer> list = postDao.getPostByOrgId(orgId);
                return new ResponseResult(list, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("获取单位id失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }

    @Override
    public ResponseResult insertCustomArchiveTableData(CustomArchiveTableData customArchiveTableData) {
        //TODO 需要弄清楚数组的形式
        if (customArchiveTableData instanceof CustomArchiveTableData) {
            try {
                //将前端传过来的大字段进行解析
                 JSONArray json = (JSONArray) JSONArray.toJSON(customArchiveTableData.getBigData());
                for (int i = 0; i < json.size(); i++) {
                    JSONObject jsono = JSONObject.parseObject(json.get(i).toString());
                    for (String s : jsono.keySet()) {
                        s.replace(s, "@" + s + "@");
                    }
                }
               customArchiveTableDataDao.insert(customArchiveTableData);
            } catch (Exception e) {
                logger.error("插入自定义表数据失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult updateCustomArchiveTableData(CustomArchiveTableData customArchiveTableData) {
        if (customArchiveTableData instanceof CustomArchiveTableData) {
            try {
                customArchiveTableDataDao.updateByPrimaryKey(customArchiveTableData);
                return new ResponseResult(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("更新自定义数据表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        } else {
            return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
        }
    }

    @Override
    public ResponseResult<PageResult<CustomArchiveTableData>> selectCustomArchiveTableData(Integer currentPage, Integer pageSize, Integer customArchiveTableId) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomArchiveTableData> list = null;
        PageResult<CustomArchiveTableData> pageResult = new PageResult<>();
        try {
            List<Integer> integerList = customArchiveTableDataDao.selectCustomArchiveTableId(customArchiveTableId);
            for (int i = 0; i < integerList.size(); i++) {
                CustomArchiveTableData customArchiveTableData = customArchiveTableDataDao.selectByPrimaryKey(integerList.get(i));
                list.add(customArchiveTableData);
            }
            pageResult.setList(list);
            return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("根据tableId查询自定义数据失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }
    }


        @Override
        public ResponseResult<List<String>> checkField (Integer fieldId){
            List<String> list = null;
            try {
                //通过字段名找到验证code
                list = customArchiveFieldCheckDao.selectCheckName(fieldId);
            } catch (Exception e) {
                logger.error("获取验证名称失败");
                return new ResponseResult<>(list, CommonCode.FAIL);
            }
            return new ResponseResult<>(list, CommonCode.SUCCESS);
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
                List<CustomArchiveField> list = new ArrayList<>();
                for (int i = 0; i < integerList.size(); i++) {
                    CustomArchiveField customArchiveField = customArchiveFieldDao.selectByPrimaryKey(integerList.get(i));
                    list.add(customArchiveField);
                }
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







