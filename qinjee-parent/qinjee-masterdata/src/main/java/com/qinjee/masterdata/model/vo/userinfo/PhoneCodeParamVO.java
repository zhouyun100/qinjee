package com.qinjee.masterdata.model.vo.userinfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 手机号验证码参数VO类
 * @author zhouyun
 * @date 2020-03-05
 */
@Data
@JsonInclude
public class PhoneCodeParamVO implements Serializable {

    private String phone;

    private String code;
}
