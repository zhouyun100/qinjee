/**
 * 文件名：MenuVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/12
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月12日
 */
@ApiModel(description = "菜单树")
@Data
@NoArgsConstructor
@JsonInclude
public class MenuVO implements Serializable {

    /**
     * 菜单ID
     */
    @ApiModelProperty("菜单ID")
    private Integer menuId;

    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String menuName;

    /**
     * 图标url
     */
    @ApiModelProperty("图标url")
    private String iconUrl;

    /**
     * 功能类型
     */
    @ApiModelProperty("功能类型")
    private String funcType;

    /**
     * 功能地址
     */
    @ApiModelProperty("功能地址")
    private String funcUrl;

    /**
     * 按钮CODE
     */
    @ApiModelProperty("按钮CODE")
    private String buttonCode;

    /**
     * 父级菜单ID
     */
    @ApiModelProperty("父级菜单ID")
    private Integer parentMenuId;

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
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 子级菜单列表
     */
    @ApiModelProperty("子级菜单列表")
    private List<MenuVO> childMenuList;

    /**
     * 是否拥有菜单权限(1：是，0：否)
     */
    @ApiModelProperty("是否拥有菜单权限")
    private Integer hasMenu;
}
