package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工档案岗位关系表
 * @author
 */
@ApiModel(description = "员工档案岗位关系表")
@Data
@NoArgsConstructor
public class UserArchivePostRelation implements Serializable {
    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Integer id;

    /**
     * 员工档案ID
     */
    @ApiModelProperty("员工档案ID")
    private Integer archiveId;

    /**
     * 岗位ID
     */
    @ApiModelProperty("岗位ID")
    private Integer postId;

    /**
     * 职级ID
     */
    @ApiModelProperty("职级ID")
    private Integer positionLevelId;

    /**
     * 职等ID
     */
    @ApiModelProperty("职等ID")
    private Integer positionGradeId;

    /**
     * 任职开始时间
     */
    @ApiModelProperty("任职开始时间")
    private Date employmentBeginDate;

    /**
     * 任职结束时间
     */
    @ApiModelProperty("任职结束时间")
    private Date employmentEndDate;

    /**
     * 任职类型
     */
    @ApiModelProperty("任职类型")
    private String employmentType;

    /**
     * 任免原因
     */
    @ApiModelProperty("任免原因")
    private String appointDismissalReason;

    /**
     * 上级领导ID
     */
    @ApiModelProperty("上级领导ID")
    private Integer supervisorId;

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
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
