/**
 * 文件名：TemplateCustomTableVO
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

import java.util.List;

/**
 * @author 周赟
 * @date 2019/12/9
 */
@Data
public class TemplateCustomTableVO{

    /**
     * 模板自定义表主键ID
     */
    private Integer tctId;

    /**
     * 表ID
     */
    private Integer tableId;

    /**
     * 是否内置
     */
    private Short isSyetemDefine;
    /**
     * 表名
     */
    private String tableName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 字段列表
     */
    private List<TemplateCustomTableFieldVO> fieldList;
}
