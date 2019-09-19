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
    /**
     * 自定义表修改
     * @param customTable
     * @return
     */
    ResponseResult updateCustomTable(CustomTable customTable) ;

    /**
     * 新增自定义表
     * @param customTable
     * @return
     */
    ResponseResult insert(CustomTable customTable);

    /**
     * 逻辑删除自定义表
     * @param list
     * @return
     */
    ResponseResult deleteCustomTable(List<Integer> list);

    /**
     * 分页展示自定义表
     * @param currentPage
     * @param pagesize
     * @return
     */
    ResponseResult<PageResult<CustomTable>> selectCustomTable(Integer currentPage,Integer pagesize);

    /**
     * 新增自定义组
     * @param customGroup
     * @return
     */

    ResponseResult insertCustomGroup(CustomGroup customGroup);

    /**
     * 删除自定义组
     * @param list
     * @return
     */
    ResponseResult deleteCustomGroup(List<Integer> list);

    /**
     * 自定义组修改
     * @param customGroup
     * @return
     */
    ResponseResult updateCustomGroup(CustomGroup customGroup);

    /**
     *分页展示自定义组中的表
     * @param currentPage
     * @param pageSize
     * @param customGroupId
     * @return
     */
    ResponseResult<PageResult<CustomTable>> selectCustomTableFromGroup(Integer currentPage, Integer pageSize, Integer customGroupId);

    /**
     * 新增自定义字段
     * @param customField
     * @return
     */
    ResponseResult insertCustomField(CustomField customField);

    /**
     * 逻辑删除自定义字段
     * @param list
     * @return
     */
    ResponseResult deleteCustomField(List<Integer> list);

    /**
     * 修改自定义字段类型
     * @param customField
     * @return
     */
    ResponseResult updateCustomField(CustomField customField);

    /**
     *分页展示指定自定义表下的自定义字段
     * @param currentPage
     * @param pageSize
     * @param customTableId
     * @return
     */
    ResponseResult<PageResult<CustomField>> selectCustomFieldFromTable(Integer currentPage, Integer pageSize, Integer customTableId);

    /**
     * 将数据插入自定义数据表
     * @param customTableData
     * @return
     */
    ResponseResult insertCustomTableData(CustomTableData customTableData);

    /**
     * 修改自定义字段表中的数据
     * @param customTableData
     * @return
     */
    ResponseResult updateCustomTableData(CustomTableData customTableData);

    /**
     * 展示自定义表数据内容,返回自定义表数据
     * @param currentPage
     * @param pageSize
     * @param customTableId
     * @return
     */
    ResponseResult<PageResult<CustomTableData>> selectCustomTableData(Integer currentPage, Integer pageSize, Integer customTableId);

    /**
     * 获取字段校验类型
     * @param fieldId
     * @return
     */
    ResponseResult<List<String>> checkField(Integer fieldId);

    /**
     * 模板导入
     * @param path
     * @return
     */
    ResponseResult importFile(String path);

    /**
     * 模板导出
     * @param path
     * @param title
     * @param customTableId
     * @return
     */
    ResponseResult exportFile(String path, String title, Integer customTableId);

    /**
     * 文件上传
     * @param path
     * @return
     */
    ResponseResult putFile(String path);

    /**
     * 返回临时对象给前端
     * @return
     */
    ResponseResult UploadFileByForWard();
}
