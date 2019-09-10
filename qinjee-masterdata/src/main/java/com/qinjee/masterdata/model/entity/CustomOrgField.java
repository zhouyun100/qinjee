package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 自定义字段
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "自定义字段")
public class CustomOrgField implements Serializable {
    /**
     * 字段ID
     */
    @ApiModelProperty("字段ID")
    private Integer fieldId;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String fieldName;

    /**
     * 字段类型
     */
    @ApiModelProperty("字段类型")
    private String fieldType;

    /**
     * 验证类型
     */
    @ApiModelProperty("验证类型")
    private String validType;

    /**
     * 企业代码ID
     */
    @ApiModelProperty("企业代码ID")
    private Integer codeId;

    /**
     * 企业代码名称
     */
    @ApiModelProperty("企业代码名称")
    private String codeName;

    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String defaultValue;

    /**
     * 长度
     */
    @ApiModelProperty("长度")
    private Integer valLength;

    /**
     * 精度
     */
    @ApiModelProperty("精度")
    private Integer valPrecision;

    /**
     * 表ID
     */
    @ApiModelProperty("表ID")
    private Integer tableId;

    /**
     * 组ID
     */
    @ApiModelProperty("组ID")
    private Integer groupId;

    /**
     * 是否系统定义
     */
    @ApiModelProperty("是否系统定义")
    private Short isSystemDefine;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private Integer creatorId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
