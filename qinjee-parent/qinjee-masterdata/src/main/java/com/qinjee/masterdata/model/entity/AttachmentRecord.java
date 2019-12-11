package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
     * 组id
     */
    private Integer groupId;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
