package com.qinjee.masterdata.model.vo.userinfo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import entity.WeChatUserInfo;
import lombok.Data;

/**
 * 微信绑定参数VO类
 * @author zhouyun
 * @date 2020-03-05
 */
@Data
@JsonInclude
public class WechatLoginResultVO {

    private UserInfoVO loginInfo;

    private WeChatUserInfo bindInfo;

}
