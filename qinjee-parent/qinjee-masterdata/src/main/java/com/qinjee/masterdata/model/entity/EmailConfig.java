package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_email_config
 * @author
 */
@Data
public class EmailConfig implements Serializable {
    /**
     * 邮件配置ID
     */
    private Integer emailConfigId;

    /**
     * 协议
     */
    private String transportProtocol;

    /**
     * 主机名
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 是否启用SSL安全链接(true/false)
     */
    private String sslEnable;

    /**
     * 用户名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 发件人昵称
     */
    private String senderNick;

    /**
     * 授权码
     */
    private String authCode;

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
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
