package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.CheckTypeDao;
import com.qinjee.masterdata.dao.FieldCheckTypeDao;
import com.qinjee.masterdata.dao.staffdao.CustomFieldDao;
import com.qinjee.masterdata.dao.staffdao.CustomGroupDao;
import com.qinjee.masterdata.dao.staffdao.CustomTableDao;
import com.qinjee.masterdata.dao.staffdao.CustomTableDataDao;
import com.qinjee.masterdata.model.entity.CustomField;
import com.qinjee.masterdata.model.entity.CustomGroup;
import com.qinjee.masterdata.model.entity.CustomTable;
import com.qinjee.masterdata.model.entity.CustomTableData;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class StaffCommonServiceImpl implements IStaffCommonService {
    private static final Logger logger = LoggerFactory.getLogger(StaffCommonServiceImpl.class);
    @Autowired
    private CustomTableDao customTableDao;
    @Autowired
    private CustomGroupDao customGroupDao;
    @Autowired
    private CustomFieldDao customFieldDao;
    @Autowired
    private CustomTableDataDao customTableDataDao;
    @Autowired
    private FieldCheckTypeDao fieldCheckTypeDao;
    @Autowired
    private CheckTypeDao checkTypeDao;

    @Override
    public ResponseResult insert(CustomTable customTable) {
        if (customTable instanceof CustomTable) {
            try {
                customTableDao.insert(customTable);
            } catch (Exception e) {
                logger.error("插入自定义表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult deleteCustomTable(List<Integer> list) {
        Integer max = 0;
        try {
            max = customTableDao.selectMaxPrimaryKey();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                customTableDao.deleteCustomTable(list.get(i));
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
           logger.error("删除自定义表失败");
           return new ResponseResult(false,CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult updateCustomTable(CustomTable customTable) {
        if (customTable instanceof CustomTable) {
            try {
                customTableDao.updateByPrimaryKey(customTable);
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
    public ResponseResult<PageResult<CustomTable>> selectCustomTable(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomTable> list = null;
        PageResult<CustomTable> pageResult = new PageResult<>();
        try {
            list = customTableDao.selectByPage();
            pageResult.setList(list);
        } catch (Exception e) {
            logger.error("查询数据库失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }
        return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult insertCustomGroup(CustomGroup customGroup) {
        if (customGroup instanceof CustomGroup) {
            try {
                customGroupDao.insert(customGroup);
            } catch (Exception e) {
                logger.error("插入自定义组失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult deleteCustomGroup(List<Integer> list) {
        try {
            Integer max = 0;
            max = customGroupDao.selectMaxPrimaryKey();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                customGroupDao.deleteCustomGroup(list.get(i));
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除自定义组失败");
            return new ResponseResult(false,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult updateCustomGroup(CustomGroup customGroup) {
        if (customGroup instanceof CustomGroup) {
            try {
                customGroupDao.updateByPrimaryKey(customGroup);
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
    public ResponseResult<PageResult<CustomTable>> selectCustomTableFromGroup(Integer currentPage, Integer pageSize, Integer customGroupId) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomTable> list = null;
        PageResult<CustomTable> pageResult = new PageResult<>();
        try {
            List<Integer> integerList=customGroupDao.selectTableId(customGroupId);
            for (int i = 0; i < integerList.size(); i++) {
                CustomTable customTable = customTableDao.selectByPrimaryKey(integerList.get(i));
                list.add(customTable);
            }
            pageResult.setList(list);
            return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询自定义表失败");
            return new ResponseResult<>(pageResult,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertCustomField(CustomField customField) {
        if (customField instanceof CustomField) {
            try {
                customFieldDao.insert(customField);
            } catch (Exception e) {
                logger.error("插入自定义字段失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult deleteCustomField(List<Integer> list) {
        Integer max = 0;
        try {
            max = customFieldDao.selectMaxPrimaryKey();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                customFieldDao.deleteCustomField(list.get(i));
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除自定义字段失败");
            return new ResponseResult(false,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult updateCustomField(CustomField customField) {
        if (customField instanceof CustomField) {
            try {
                customFieldDao.updateByPrimaryKey(customField);
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
    public ResponseResult<PageResult<CustomField>> selectCustomFieldFromTable(Integer currentPage, Integer pageSize, Integer customTableId) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomField> list = null;
        PageResult<CustomField> pageResult = new PageResult<>();
        try {
            List<Integer> integerList=customFieldDao.selectFeildId(customTableId);
            for (int i = 0; i < integerList.size(); i++) {
                CustomField customField = customFieldDao.selectByPrimaryKey(integerList.get(i));
                list.add(customField);
            }
            pageResult.setList(list);
            return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询自定义字段失败");
            return new ResponseResult<>(pageResult,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertCustomTableData(CustomTableData customTableData) {
        if (customTableData instanceof CustomTableData) {
            try {
                //将前端传过来的大字段进行解析
                JSONArray json= JSONArray.parseArray(customTableData.getBigData());
                for(int i=0;i<json.size();i++) {
                    JSONObject jsono = JSONObject.parseObject(json.get(i).toString());
                    for (String s : jsono.keySet()) {
                        s.replace(s,s+"@qinjee@");
                    }
                }
                customTableDataDao.insert(customTableData);
            } catch (Exception e) {
                logger.error("插入自定义表数据失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult updateCustomTableData(CustomTableData customTableData) {
        if (customTableData instanceof CustomTableData) {
            try {
                customTableDataDao.updateByPrimaryKey(customTableData);
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
    public ResponseResult<PageResult<CustomTableData>> selectCustomTableData(Integer currentPage, Integer pageSize, Integer customTableId) {
        PageHelper.startPage(currentPage, pageSize);
        List<CustomTableData> list = null;
        PageResult<CustomTableData> pageResult = new PageResult<>();
        try {
            List<Integer> integerList=customTableDataDao.selectCustomTableId(customTableId);
            for (int i = 0; i < integerList.size(); i++) {
                CustomTableData customTableData = customTableDataDao.selectByPrimaryKey(integerList.get(i));
                list.add(customTableData);
            }
            pageResult.setList(list);
            return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("根据tableId查询自定义数据失败");
            return new ResponseResult<>(pageResult,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult<List<String>> checkField(Integer fieldId) {
        List<String> list= null;
        try {
            //通过字段名找到验证code
            String checkCode=fieldCheckTypeDao.selectCheckCode(fieldId);
            //通过验证code找到验证名称
            list = checkTypeDao.selectCheckName(checkCode);
        } catch (Exception e) {
            logger.error("获取验证名称失败");
            return new ResponseResult<>(list,CommonCode.FAIL);
        }
        return new ResponseResult<>(list,CommonCode.SUCCESS);
    }


}


