/*
 * Welcome to use the TableGo Tools.
 * 
 * http://vipbooks.iteye.com
 * http://blog.csdn.net/vipbooks
 * http://www.cnblogs.com/vipbooks
 * 
 * Author:bianj
 * Email:edinsker@163.com
 * Version:5.8.0
 */

package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonInclude
public class EmailRecord {
    /** 版本号 */
    private static final long serialVersionUID = 2587262595458960728L;

    /** 邮件记录ID */
    private Integer emailRecordId;

    /** 模板ID */
    private Integer emailTemplateId;

    /** 邮件配置ID */
    private Integer emailConfigId;

    /** 邮件标题 */
    private String emailTitle;

    /** 邮件正文 */
    private String emailContent;

    /** 业务类型 */
    private String businessType;

    /** 发件人 */
    private String fromUser;

    /** 收件人 */
    private String toUser;

    /** 抄送人 */
    private String ccUser;

    /** 操作人 */
    private Integer operatorId;

    /** 创建时间 */
    private Timestamp createTime;

    /** 发送状态 */
    private String sendStatus;

}