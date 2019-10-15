package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 职级表
 * @author
 */
@Data
@ApiModel(description = "职级表实体类")
public class PositionLevel implements Serializable {
    /**
     * 职级ID
     */
    @ApiModelProperty("职级ID")
    private Integer positionLevelId;

    /**
     * 职级名称
     */
    @ApiModelProperty("职级名称")
    private String positionLevelName;

    /**
     * 排序ID
     */
    @ApiModelProperty("排序ID")
    private Integer sortId;

    /**
     * 职级描述
     */
    @ApiModelProperty("职级描述")
    private String positionLevelRemark;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private Integer operatorId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Short isDelete;

    /**
     * 职位族名称
     */
    @ApiModelProperty("职位族名称")
    private String positionGroupName;

    /**
     * 职位族名称合并行
     */
    @ApiModelProperty("职位族名称合并行")
    private Integer positionGroupNameRowSpan;

    /**
     * 职位名称
     */
    @ApiModelProperty("职位名称")
    private String positionName;

    /**
     * 职位名称合并行
     */
    @ApiModelProperty("职位名称合并行")
    private Integer positionNameRowSpan;

    /**
     * 职等名称
     */
    @ApiModelProperty("职等名称")
    private String positionGradeName;

    /**
     * 职等名称合并行
     */
    @ApiModelProperty("职等名称合并行")
    private Integer positionGradeNameRowSpan;

    private List<PositionGroup> positionGroups;


    private static final long serialVersionUID = 1L;

}
