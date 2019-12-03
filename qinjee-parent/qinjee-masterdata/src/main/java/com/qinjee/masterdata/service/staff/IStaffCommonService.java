package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.masterdata.model.entity.CustomArchiveGroup;
import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import com.qinjee.masterdata.model.entity.CustomArchiveTableData;
import com.qinjee.masterdata.model.vo.staff.BigDataVo;
import com.qinjee.masterdata.model.vo.staff.CustomArchiveTableDataVo;
import com.qinjee.masterdata.model.vo.staff.OrganzitionVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface IStaffCommonService {

    /**
     * 新增自定义表
     * @param customArchiveTable
     * @return
     */
   void  insertCustomArichiveTable(CustomArchiveTable customArchiveTable,UserSession userSession);

    /**
     * 逻辑删除自定义表
     * @param list
     * @return
     */
     void deleteCustomArchiveTable(List<Integer> list) throws Exception;

    /**
     * 自定义表修改
     * @param customArchiveTable
     * @return
     */
   void updateCustomArchiveTable(CustomArchiveTable customArchiveTable);


    /**
     * 分页展示自定义表
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageResult<CustomArchiveTable> selectCustomArchiveTable(Integer currentPage, Integer pageSize,UserSession userSession);

    /**
     * 新增自定义组
     * @param customArchiveGroup
     * @return
     */

    void insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup,UserSession userSession);

    /**
     * 删除自定义组
     * @param list
     * @return
     */
    void deleteCustomArchiveGroup(List<Integer> list) throws Exception;

    /**
     * 自定义组修改
     * @param customArchiveGroup
     * @return
     */
    void updateCustomArchiveGroup(CustomArchiveGroup customArchiveGroup);

    /**
     *分页展示自定义组中的表
     * @param currentPage
     * @param pageSize
     * @param customArchiveGroupId
     * @return
     */
  PageResult<CustomArchiveField>selectArchiveFieldFromGroup(Integer currentPage, Integer pageSize, Integer customArchiveGroupId);

    /**
     * 新增自定义字段
     * @param customArchiveField
     * @return
     */
  void insertCustomArchiveField(CustomArchiveField customArchiveField,UserSession userSession);

    /**
     * 逻辑删除自定义字段
     * @param list
     * @return
     */
     void deleteCustomArchiveField(List<Integer> list) throws Exception;

    /**
     * 修改自定义字段类型
     * @param customArchiveField
     * @returnvoid
     */
    void updateCustomArchiveField(CustomArchiveField customArchiveField);

    /**
     *分页展示指定自定义表下的自定义字段
     * @param currentPage
     * @param pageSize
     * @param customArchiveTableId
     * @return
     */
    PageResult<CustomArchiveField> selectCustomArchiveField(Integer currentPage, Integer pageSize, Integer customArchiveTableId);
    /**
     * 通过字段id找到自定义字段信息
     * @param customArchiveFieldId
     * @return
     */
    CustomArchiveField selectCustomArchiveFieldById(Integer customArchiveFieldId);
    /**
     * 将数据插入自定义数据表
     * @param bigDataVo
     * @return
     */
    void insertCustomArchiveTableData(BigDataVo bigDataVo, UserSession userSession);

    /**
     * 修改自定义字段表中的数据
     * @param  customArchiveTableDataVo
     * @return
     */
    void updateCustomArchiveTableData(CustomArchiveTableDataVo customArchiveTableDataVo,UserSession userSession);

    /**
     * 展示自定义表数据内容,返回自定义表数据
     * @param currentPage
     * @param pageSize
     * @param customArchiveTableId
     * @return
     */
   PageResult<CustomArchiveTableData> selectCustomArchiveTableData(Integer currentPage, Integer pageSize, Integer customArchiveTableId);
    /**
     * 据档案显示对应权限下的单位
     * @param userSession
     * @return
     */
    Integer getCompanyId(UserSession userSession);
    /**
     * 根据档案id显示对应权限下的子集部门
     * @param companyId
     * @return
     */
    OrganzitionVo getOrgIdByCompanyId(Integer companyId, UserSession userSession);

    /**
     * 显示部门下的岗位
     * @param orgId
     * @return
     */
    String getPostByOrgId(@Param("orgId") Integer orgId);

    /**
     * 展示自定义字段的值
     * @param customArchiveFieldId
     * @return
     */
    List<String> selectFieldValueById(Integer customArchiveFieldId);

    void saveFieldAndValue(List< Map< Integer, String>> list, UserSession userSession, String funcCode) throws Exception;

    /**
     *
     * @param tableId
     * @param businessId
     * @return
     * @throws IllegalAccessException
     */
    Map< Integer, String> selectValue(Integer tableId, Integer businessId) throws IllegalAccessException;

    void deletePreValue(Integer id,UserSession userSession);

    void deleteArcValue(Integer businessId,UserSession userSession);
}
