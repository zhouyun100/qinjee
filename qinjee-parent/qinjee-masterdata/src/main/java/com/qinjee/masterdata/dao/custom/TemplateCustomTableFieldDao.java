/**
 * 文件名：TemplateCustomTableFieldDao
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/9
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.custom;

import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableFieldVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/12/9
 */
@Repository
public interface TemplateCustomTableFieldDao {
    /**
     * 根据企业ID和模板ID查询自定义表列表
     * @param companyId 企业ID
     * @param templateId 模板ID
     * @return
     */
    List<TemplateCustomTableVO> searchTableListByCompanyIdAndTemplateId(@Param("companyId") Integer companyId, @Param("templateId") Integer templateId);

    /**
     * 根据模板ID查询自定义表列表
     * @param templateId
     * @return
     */
    List<TemplateCustomTableVO> searchTableListByTemplateId(Integer templateId);

    /**
     * 根据模板ID查询所有表字段信息
     * @param templateId
     * @return
     */
    List<TemplateCustomTableFieldVO> searchTableFieldListByTemplateId(@Param("templateId") Integer templateId);

    /**
     * 根据表ID和模板ID查询对应表字段配置信息
     * @param tableId
     * @param templateId
     * @return
     */
    List<TemplateCustomTableFieldVO> searchFieldListByTableIdAndTemplateId(Integer tableId, Integer templateId);

    /**
     * 根据模板ID和自定义表ID删除表数据
     * @param templateId
     * @param templateCustomTableList
     * @param operatorId
     * @return
     */
    int deleteTemplateTable(Integer templateId, List<TemplateCustomTableVO> templateCustomTableList, Integer operatorId);

    /**
     * 根据模板ID和自定义表ID列表批量删除表字段数据
     * @param templateId
     * @param templateCustomTableList
     * @param operatorId
     * @return
     */
    int deleteTemplateTableField(Integer templateId, List<TemplateCustomTableVO> templateCustomTableList, Integer operatorId);

    /**
     * 根据模板ID和自定义表ID删除表字段数据
     * @param templateId
     * @param tableId
     * @param operatorId
     * @return
     */
    int deleteTemplateTableFieldByTemplateIdAndTableId(Integer templateId, Integer tableId, Integer operatorId);

    /**
     * 添加模板自定义表
     * @param templateId
     * @param templateCustomTableList
     * @param operatorId
     * @return
     */
    int addTemplateTable(Integer templateId, List<TemplateCustomTableVO> templateCustomTableList, Integer operatorId);

    /**
     * 添加模板自定义字段
     * @param templateId
     * @param templateCustomTableFieldList
     * @param operatorId
     * @return
     */
    int addTemplateTableField(Integer templateId, List<TemplateCustomTableFieldVO> templateCustomTableFieldList, Integer operatorId);

    /**
     * 修改模板自定义表信息
     * @param templateId
     * @param templateCustomTable
     * @param operatorId
     * @return
     */
    int updateTemplateTable(Integer templateId, TemplateCustomTableVO templateCustomTable, Integer operatorId);

    /**
     * 根据模板ID查询预入职登记表单所有的表
     * @param templateId
     * @return
     */
    List<EntryRegistrationTableVO> searchEntryRegistrationTableListByTemplateId(Integer templateId);

    /**
     * 根据模板ID和表ID查询所有字段列表
     * @param tableId
     * @return
     */
    List<CustomFieldVO> searchCustomFieldListByTemplateIdAndTableId(@Param("tableId") Integer tableId, @Param("templateId") Integer templateId);
}
