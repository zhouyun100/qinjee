package com.qinjee.masterdata.model.vo.userinfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 企业注册参数VO类
 * @author zhouyun
 * @date 2020-03-05
 */
@Data
@JsonInclude
public class CompanyRegistParamVO  implements Serializable {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;


    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 企业类型
     */
    private String companyType;

    /**
     * 企业规模（企业最大数字），默认是0，表示无上线
     */
    private Integer userCount;

    /**
     * 用户姓名
     */
    private String userName;

}
