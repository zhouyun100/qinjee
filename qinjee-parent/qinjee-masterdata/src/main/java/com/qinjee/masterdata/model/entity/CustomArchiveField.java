package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_archive_field
 * @author
 */
@Data
@ToString
@JsonInclude
public class CustomArchiveField implements Serializable {
    /**
     * 字段ID
     */
    private Integer fieldId;
    /**
     * 物理字段名
     */
    @NotNull
    private String fieldCode;
    /**
     * 字段名
     */
    @NotNull
    private String fieldName;
    /**
     * 字段类型
     */
    @NotNull
    private String fieldType;

    /**
     * 字段值类型(text/number/date/code)，默认text
     */
    private String textType;

    /**
     * 前端录入校验类型(text/number/password)
     */
    private String inputType;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 输入框提示信息
     */
    private String placeholder;

    /**
     * 文本最大长度
     */
    private Integer maxLength;

    /**
     * 文本最小长度
     */
    private Integer minLength;

    /**
     * 数字最大值
     */
    private Integer maxNumber;

    /**
     * 数字最小值
     */
    private Integer minNumber;

    /**
     * 浮点长度(精度)
     */
    private Integer floatLength;

    /**
     * 是否时间范围选择
     */
    private Short isTimeRange;

    /**
     * 最小时间
     */
    private String minTime;

    /**
     * 最大时间
     */
    private String maxTime;

    /**
     * 时间格式化规则(yyyy-MM-dd)
     */
    private String formatTime;

    /**
     * 代码型CODE
     */
    private String code;

    /**
     * 校验是否必填
     */
    private Short isMust;

    /**
     * 校验规则(email,idcard,phone)
     */
    private String rule;

    /**
     * 是否只读
     */
    private Short isOnlyRead;

    /**
     * 表ID
     */
    @NotNull
    private Integer tableId;

    /**
     * 组ID
     */
    private Integer groupId;

    /**
     * 是否显示
     */
    private Short isShow;

    /**
     * 是否系统定义
     */
    @NotNull
    private Short isSystemDefine;

    /**
     * 排序
     */
    @NotNull
    private Integer sort;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date createTime;

    /**
     * 操作人ID
     */
    private Integer creatorId;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;


}
