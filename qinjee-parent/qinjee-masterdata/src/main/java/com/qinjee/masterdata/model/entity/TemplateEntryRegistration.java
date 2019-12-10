package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 入职登记模板表(t_template_entry_registration)
 * 
 * @author 周赟
 * @version 2019-12-10
 */
@Data
public class TemplateEntryRegistration implements java.io.Serializable {

    /**
     * 版本号
     */
    private static final long serialVersionUID = -3783010786555757608L;

    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板描述
     */
    private String templateDescription;

    /**
     * 通知人
     */
    private String receiveIds;

    /**
     * 标题
     */
    private String title;

    /**
     * 欢迎语
     */
    private String welcomeSpeech;

    /**
     * 企业LOGO
     */
    private String logoUrl;

    /**
     * 背景图路径
     */
    private String backgroundImgUrl;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

}