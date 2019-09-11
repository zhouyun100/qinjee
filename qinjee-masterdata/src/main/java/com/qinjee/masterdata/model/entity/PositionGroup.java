package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 职位族表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "职位族表实体类")
public class PositionGroup implements Serializable {
    /**
     * 职位族ID
     */
    @ApiModelProperty("职位族ID")
    private Integer positionGroupId;

    /**
     * 职位族名称
     */
    @ApiModelProperty("职位族名称")
    private String positionGroupName;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;

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


    @ApiModelProperty("职位族下的职位")
    private List<Position> positionList;

    private static final long serialVersionUID = 1L;

}
