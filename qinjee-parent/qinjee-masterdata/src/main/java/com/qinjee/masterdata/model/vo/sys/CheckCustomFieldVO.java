/**
 * 文件名：CheckCustomFieldVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/26
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.CustomArchiveField;
import lombok.Data;

/**
 * @author 周赟
 * @date 2019/11/26
 */
@Data
//@JsonInclude
public class CheckCustomFieldVO extends CustomArchiveField {

    /**
     * 字段值
     */
    private String fieldValue;

    /**
     * 必填
     */
    private String isNotNull;

    /**
     * 整数
     */
    private String isInteger;

    /**
     * 日期
     */
    private String isDate;

    /**
     * 电子邮件
     */
    private String isEmail;

    /**
     * 身份证
     */
    private String isIdCard;

    /**
     * 校验是否成功(true:是，false:否)
     */
    private Boolean checkResult;

    /**
     * 校验结果信息
     */
    private String resultMsg;
}
