package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 模板附件组表(t_template_attachment_group)
 * 
 * @author 周赟
 * @version 2019-12-10
 */
@Data
public class TemplateAttachmentGroup implements java.io.Serializable {

    /** 版本号 */
    private static final long serialVersionUID = -30592621790828503L;

    /** 主键ID */
    private Integer tagId;

    /** 模板ID */
    private Integer templateId;

    /** 组ID */
    private Integer groupId;

    /** 填表说明 */
    private String description;

    /** 是否必填 */
    private Integer isMust;

    /** 排序 */
    private Integer sort;

    /** 操作人ID */
    private Integer operatorId;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 是否删除 */
    private Integer isDelete;

}