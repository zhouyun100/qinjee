/**
 * 文件名：CustomTableVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/28
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.custom;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/28
 */
@Data
public class CustomTableVO{
    /**
     * 表ID
     */
    private Integer tableId;
    /**
     * 表名
     */
    @NotNull
    private String tableName;
    /**
     * 物理表名
     */
    private String tableCode;
    /**
     * 功能code
     */
    @NotNull
    private String funcCode;

    /**
     * 企业ID
     */
    @NotNull
    private Integer companyId;

    /**
     * 是否系统定义
     */
    @NotNull
    private Short isSystemDefine;

    /**
     * 是否启用
     */
    @NotNull
    private Short isEnable;

    /**
     * 排序
     */
    @NotNull
    private Integer sort;

    /**
     * 操作人ID
     */
    private Integer creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    /**
     * 自定义字段组列表
     */
    private List<CustomGroupVO> customGroupVOList;
}
