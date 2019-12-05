/**
 * 文件名：CustomGroupVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/29
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/29
 */
@Data
@JsonInclude
public class CustomGroupVO {

    /**
     * 组ID
     */
    private Integer groupId;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 表ID
     */
    private Integer tableId;

    /**
     * 排序
     */
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
     * 自定义字段列表
     */
    private List<CustomFieldVO> customFieldVOList;
}
