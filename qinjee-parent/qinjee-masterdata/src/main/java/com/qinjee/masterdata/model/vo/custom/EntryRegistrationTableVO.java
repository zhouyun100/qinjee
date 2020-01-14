/**
 * 文件名：EntryRegistrationTableVO
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
public class EntryRegistrationTableVO {

    /**
     * 表ID
     */
    private Integer tableId;
    /**
     * 表名
     */
    private String tableName;
    /**
     *排序sort
     */
    private Integer sort;
    /**
     * 是否内置
     */
    private Integer isSystemDefine;
    /**
     * 自定义字段列表
     */
    private List<CustomFieldVO> customFieldVOList;

}
