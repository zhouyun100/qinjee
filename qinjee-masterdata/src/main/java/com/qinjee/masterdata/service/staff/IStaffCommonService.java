package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.*;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
public interface IStaffCommonService {

    /**
     * 新增自定义表
     * @param customArchiveTable
     * @return
     */
    ResponseResult insertCustomArichiveTable(CustomArchiveTable customArchiveTable);

    /**
     * 逻辑删除自定义表
     * @param list
     * @return
     */
    ResponseResult deleteCustomArchiveTable(List<Integer> list);

    /**
     * 自定义表修改
     * @param customArchiveTable
     * @return
     */
    ResponseResult updateCustomArchiveTable(CustomArchiveTable customArchiveTable);


    /**
     * 分页展示自定义表
     * @param currentPage
     * @param pageSize
     * @return
     */
    ResponseResult<PageResult<CustomArchiveTable>> selectCustomArchiveTable(Integer currentPage, Integer pageSize);

    /**
     * 新增自定义组
     * @param customGroup
     * @return
     */

    ResponseResult insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup);

    /**
     * 删除自定义组
     * @param list
     * @return
     */
    ResponseResult deleteCustomArchiveGroup(List<Integer> list);

    /**
     * 自定义组修改
     * @param customArchiveGroup
     * @return
     */
    ResponseResult updateCustomArchiveGroup(CustomArchiveGroup customArchiveGroup);

    /**
     *分页展示自定义组中的表
     * @param currentPage
     * @param pageSize
     * @param CustomArchiveGroupId
     * @return
     */
    ResponseResult<PageResult<CustomArchiveTable>> selectCustomTableFromGroup(Integer currentPage, Integer pageSize, Integer CustomArchiveGroupId);

    /**
     * 新增自定义字段
     * @param customArchiveField
     * @return
     */
    ResponseResult insertCustomArchiveField(CustomArchiveField customArchiveField);

    /**
     * 逻辑删除自定义字段
     * @param list
     * @return
     */
    ResponseResult deleteCustomArchiveField(List<Integer> list);

    /**
     * 修改自定义字段类型
     * @param customArchiveField
     * @return
     */
    ResponseResult updateCustomArchiveField(CustomArchiveField customArchiveField);

    /**
     *分页展示指定自定义表下的自定义字段
     * @param currentPage
     * @param pageSize
     * @param customArchiveTableId
     * @return
     */
    ResponseResult<PageResult<CustomArchiveField>> selectCustomArchiveField(Integer currentPage, Integer pageSize, Integer customArchiveTableId);
    /**
     * 通过字段id找到自定义字段信息
     * @param customArchiveFieldId
     * @return
     */
    ResponseResult<CustomArchiveField> selectCustomArchiveFieldById(Integer customArchiveFieldId);
    /**
     * 将数据插入自定义数据表
     * @param customArchiveTableData
     * @return
     */
    ResponseResult insertCustomArchiveTableData(CustomArchiveTableData customArchiveTableData);

    /**
     * 修改自定义字段表中的数据
     * @param  customArchiveTableData
     * @return
     */
    ResponseResult updateCustomArchiveTableData(CustomArchiveTableData customArchiveTableData);

    /**
     * 展示自定义表数据内容,返回自定义表数据
     * @param currentPage
     * @param pageSize
     * @param customArchiveTableId
     * @return
     */
    ResponseResult<PageResult<CustomArchiveTableData>> selectCustomArchiveTableData(Integer currentPage, Integer pageSize, Integer customArchiveTableId);

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
     * @param customArchiveTableDataId
     * @return
     */
    ResponseResult exportFile(String path, String title, Integer customArchiveTableDataId);

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



    /**
     * 据档案显示对应权限下的单位
     * @param archiveId
     * @return
     */
    ResponseResult getCompany(Integer archiveId);

    /**
     * 根据档案id显示对应权限下的子集部门
     * @param orgId
     * @return
     */
    ResponseResult getOrgIdByCompanyId(Integer orgId);

    /**
     * 显示部门下的岗位
     * @param orgId
     * @return
     */
    ResponseResult getPostByOrgId(@Param("orgId") Integer orgId);



}
