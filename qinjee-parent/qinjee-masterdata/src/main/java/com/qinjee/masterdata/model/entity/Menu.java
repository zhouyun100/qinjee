package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_menu
 * @author
 */
@Data
public class Menu implements Serializable {
    /**
     * 菜单ID
     */
    private Integer menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 图标url
     */
    private String iconUrl;

    /**
     * 功能类型
     */
    private String funcType;

    /**
     * 功能地址
     */
    private String funcUrl;

    /**
     * 按钮CODE
     */
    private String buttonCode;

    /**
     * 父级菜单ID
     */
    private Integer parentMenuId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
