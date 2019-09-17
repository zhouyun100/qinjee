package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.CustomTable;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author Administrator
 */
public interface IStaffCommonService {
    ResponseResult updateByPrimaryKey(CustomTable customTable) ;
    ResponseResult insert(CustomTable customTable);
    ResponseResult pretenddeleteByPrimaryKey(List<Integer> list);
    ResponseResult<PageResult<CustomTable>> selectCustomTable(Integer currentPage,Integer pagesize);
}
