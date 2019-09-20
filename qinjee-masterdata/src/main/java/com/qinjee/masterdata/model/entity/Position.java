package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 职位表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "职位表实体类")
public class Position implements Serializable {
    /**
     * 职位ID
     */
    @ApiModelProperty("职位ID")
    private Integer positionId;

    /**
     * 职位名称
     */
    @ApiModelProperty("职位名称")
    private String positionName;

    /**
     * 职位族ID
     */
    @ApiModelProperty("职位族ID")
    private Integer positionGroupId;

    /**
     * 排序ID
     */
    @ApiModelProperty("排序ID")
    private Integer sortId;

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

    @ApiModelProperty("职位对应的职级")
    List<PositionLevel> positionLevels;

    @ApiModelProperty("职位对应的职等")
    List<PositionGrade> positionGrades;

    /**
     * 职位族名称
     */
    private String positionGroupName;

    /**
     * 职位下的所有职等名称
     */
    private String positionGradeNames;

    /**
     * 职位下的所有职级名称
     */
    private String positionLevelNames;

    private static final long serialVersionUID = 1L;

}
