package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 增减员详细
 */
@Data
@JsonInclude
public class RegulationDetailVo implements Serializable {
    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    private Integer archiveId;

    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    private Integer businessUnitId;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String businessUnitName;

    private String changeReason;

    @ApiModelProperty("职位名称")
    private String positionName;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Integer orgId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String orgName;

    /**
     * 岗位ID
     */
    @ApiModelProperty("岗位ID")
    private Integer postId;

    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String postName;

    /**
     * 姓名
     */
    @NotNull
    @ApiModelProperty("姓名")
    private String userName;

    @NotNull
    @ApiModelProperty("变动日期")
    private Date changeDate;

    private static final long serialVersionUID = 1L;

}