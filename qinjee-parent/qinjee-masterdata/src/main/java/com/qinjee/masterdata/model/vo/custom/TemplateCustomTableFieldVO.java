/**
 * 文件名：TemplateCustomTableFieldVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/9
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.custom;

import lombok.Data;

/**
 * @author 周赟
 * @date 2019/12/09
 */
@Data
public class TemplateCustomTableFieldVO {

    /**
     * 模板自定义字段主键ID
     */
    private Integer tctfId;

    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     * 表ID
     */
    private Integer tableId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段ID
     */
    private Integer fieldId;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 是否必填
     */
    private Integer isMust;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String placeholder;
    /**
     * 是否显示
     */
    private Integer isChecked;
    /**
     * 操作人ID
     */
    private Integer operatorId;
}
