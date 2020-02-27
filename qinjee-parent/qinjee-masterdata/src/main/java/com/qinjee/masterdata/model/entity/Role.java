package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 * @author
 */
@Data
public class Role implements Serializable {
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Integer roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色组ID
     */
    @ApiModelProperty("角色名称")
    private Integer roleGroupId;

    /**
     * 是否自动授权子机构
     */
    @ApiModelProperty("是否自动授权子机构")
    private Integer isAutoAuthChildOrg;

    /**
     * 是否系统定义
     */
    @ApiModelProperty("是否系统定义")
    private Short isSystemDefine;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private Integer operatorId;

    /**
     * 创建时间
     */
   // //@DateTimeFormat(pattern = "yyyy-MM-dd" )
    ////@JSONField(format = "yyyy-MM-dd ")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
   // //@DateTimeFormat(pattern = "yyyy-MM-dd" )
    ////@JSONField(format = "yyyy-MM-dd ")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
