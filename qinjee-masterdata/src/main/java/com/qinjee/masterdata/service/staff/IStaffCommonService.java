package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.CustomField;
import com.qinjee.masterdata.model.entity.CustomGroup;
import com.qinjee.masterdata.model.entity.CustomTable;
import com.qinjee.masterdata.model.entity.CustomTableData;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author Administrator
 */
public interface IStaffCommonService {
    ResponseResult updateCustomTable(CustomTable customTable) ;
    ResponseResult insert(CustomTable customTable);
    ResponseResult deleteCustomTable(List<Integer> list);
    ResponseResult<PageResult<CustomTable>> selectCustomTable(Integer currentPage,Integer pagesize);
    ResponseResult insertCustomGroup(CustomGroup customGroup);
    ResponseResult deleteCustomGroup(List<Integer> list);
    ResponseResult updateCustomGroup(CustomGroup customGroup);
    ResponseResult<PageResult<CustomTable>> selectCustomTableFromGroup(Integer currentPage, Integer pageSize, Integer customGroupId);
    ResponseResult insertCustomField(CustomField customField);
    ResponseResult deleteCustomField(List<Integer> list);
    ResponseResult updateCustomField(CustomField customField);
    ResponseResult<PageResult<CustomField>> selectCustomFieldFromTable(Integer currentPage, Integer pageSize, Integer customTableId);
    ResponseResult insertCustomTableData(CustomTableData customTableData);

    ResponseResult updateCustomTableData(CustomTableData customTableData);

    ResponseResult<PageResult<CustomTableData>> selectCustomTableData(Integer currentPage, Integer pageSize, Integer customTableId);

    ResponseResult<List<String>> checkField(Integer fieldId);
}
