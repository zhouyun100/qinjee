/**
 * 文件名：WeChatToken
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/18
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package entity;

import lombok.Data;

/**
 * 微信TOKEN
 * @author 周赟
 * @date 2019/12/18
 */
@Data
public class WeChatToken {

    /**
     * 接口调用凭证
     */
    private String accessToken;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    private Integer expiresIn;

    /**
     * 用户刷新access_token
     */
    private String refreshToken;

    /**
     * 授权用户唯一标识
     */
    private String openid;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;

    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */
    private String unionid;
}
