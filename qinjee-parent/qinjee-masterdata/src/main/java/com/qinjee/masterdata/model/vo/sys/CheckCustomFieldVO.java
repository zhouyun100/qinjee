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

import lombok.Data;

/**
 * @author 周赟
 * @date 2019/11/26
 */
@Data
public class CheckCustomFieldVO {

    /**
     * 字段ID
     */
    private Integer fieldId;

    /**
     * 字段中文名
     */
    private String fieldName;

    /**
     * 字段值
     */
    private Integer fieldValue;

    /**
     * 校验是否成功(true:是，false:否)
     */
    private boolean checkResult;

    /**
     * 校验结果信息
     */
    private String resultMsg;
}
