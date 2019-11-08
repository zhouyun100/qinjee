package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_attachment_record
 * @author
 */
@Data
public class AttachmentRecord implements Serializable {
    /**
     * 附件ID
     */
    private Integer attachmentId;

    /**
     * 附件名称
     */
    private String attachmentName;

    /**
     * 附件路径
     */
    private String attachmentUrl;

    /**
     * 附件大小(KB)
     */
    private Integer attachmentSize;

    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 业务模块
     */
    private String 	businessModule;
    /**
     * 附件类型
     */
    private String 	attachmentType;
    /**
     * 业务ID
     */
    private Integer businessId;

    /**
     * 企业ID
     */
    private Integer companyId;

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
