package com.qinjee.masterdata.model.vo.staff;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserArchivePostRelationVo implements Serializable {
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String userName;
    /**
     * 工号
     */
    @ApiModelProperty("工号")
    private String employeeNumber;

    /**
     * 员工档案ID
     */
    @ApiModelProperty("员工档案ID")
    private Integer archiveId;

    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    private Integer businessUnitId;
    /**
     * 部门ID
     */
    private Integer orgId;
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




    private static final long serialVersionUID = 1L;
}
