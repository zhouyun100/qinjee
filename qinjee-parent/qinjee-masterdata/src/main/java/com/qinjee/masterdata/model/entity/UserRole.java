package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工角色关系表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "员工角色关系表")
public class UserRole implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Integer id;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Integer roleId;

    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    private Integer archiveId;

    /**
     * 是否托管
     */
    @ApiModelProperty("是否托管")
    private Byte isTrusteeship;

    /**
     * 托管开始时间
     */
    @ApiModelProperty("托管开始时间")
    private Date trusteeshipBeginTime;

    /**
     * 托管结束时间
     */
    @ApiModelProperty("托管结束时间")
    private Date trusteeshipEndTime;

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
    @ApiModelProperty("是否删除  0未删除、1删除")
    private Short isDelete;

    private static final long serialVersionUID = 1L;


}
