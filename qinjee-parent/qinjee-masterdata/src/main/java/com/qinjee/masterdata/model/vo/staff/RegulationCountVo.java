package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 增减员统计
 */
@Data
@JsonInclude
public class RegulationCountVo implements Serializable {

    @Override
    public String toString() {
        return "RegulationCountVo{" +
            "businessUnitId=" + businessUnitId +
            ", businessUnitName='" + businessUnitName + '\'' +
            ", orgId=" + orgId +
            ", orgParentId=" + orgParentId +
            ", orgName='" + orgName + '\'' +
            ", orgType='" + orgType + '\'' +
            ", newJoinNum=" + newJoinNum +
            ", transferInNum=" + transferInNum +
            ", increasedNum=" + increasedNum +
            ", transferOutNum=" + transferOutNum +
            ", leaveNum=" + leaveNum +
            ", retiredNum=" + retiredNum +
            ", attritionNum=" + attritionNum +
            ", beginNum=" + beginNum +
            ", endNum=" + endNum +
            ", childList=" + childList +
            '}';
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("单位ID")
    private Integer businessUnitId;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String businessUnitName;

    @ApiModelProperty("部门ID")
    private Integer orgId;
    @ApiModelProperty("父部门ID")
    private Integer orgParentId;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String orgName;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门类型")
    private String orgType;



    /**
     * 新加入
     */
    private Integer newJoinNum;
    /**
     * 调入
     */
    private Integer transferInNum;
    /**
     * 增加的人数=新加入+调入
     */
    private Integer increasedNum;
    /**
     * 调出
     */
    private Integer transferOutNum;
    /**
     * 离职
     */
    private Integer leaveNum;
    /**
     * 退休
     */
    private Integer retiredNum;

    /**
     * 减员=调出+离职+退休
     */
    private Integer attritionNum;

    /**
     * 本期初人数
     */
    private Integer beginNum;

    /**
     * 本期末人数
     */
    private Integer endNum;

    List<RegulationCountVo> childList;


}