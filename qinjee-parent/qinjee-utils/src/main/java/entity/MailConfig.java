/**
 * 文件名：MailConfig
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/10/22
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package entity;

import lombok.Data;
import lombok.ToString;

/**
 * 邮件配置类
 * @author 周赟
 * @date 2019/10/22
 */
@Data
@ToString
public class MailConfig {

    /**
     * 发送服务器需要身份验证(系统默认使用身份验证)
     */
    private String mailSmtpAuth;

    /**
     * 发送邮件协议名称
     */
    private String mailTransportProtocol;

    /**
     * 设置邮件服务器主机名
     */
    private String mailSmtpHost;

    /**
     * 发送服务器端口，可以不设置，默认是25
     */
    private String mailSmtpPort;

    /**
     * 发件人账号
     */
    private String username;

    /**
     * 发件人密码
     */
    private String password;

    /**
     * 发件人昵称
     */
    private String sendernick;

}
