package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 * @author
 */
@ApiModel(description = "用户信息表")
@Data
@NoArgsConstructor
public class UserInfo implements Serializable {
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Integer userId;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * OPENID
     */
    @ApiModelProperty("OPENID")
    private String openId;

    /**
     * UNIONID
     */
    @ApiModelProperty("UNIONID")
    private String unionId;

    /**
     * 创建时间
     */
   // //@DateTimeFormat(pattern = "yyyy-MM-dd" )
    ////@JSONField(format = "yyyy-MM-dd ")
    private Date createTime;

    private static final long serialVersionUID = 1L;

}
