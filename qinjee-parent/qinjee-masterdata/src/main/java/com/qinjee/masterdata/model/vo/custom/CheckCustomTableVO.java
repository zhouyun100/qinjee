/**
 * 文件名：CheckCustomTableVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/26
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.custom;

import lombok.Data;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/26
 */
@Data
public class CheckCustomTableVO {

    /**
     * 自定义字段对应的值列表
     */
    private List<CheckCustomFieldVO> customFieldVOList;

    /**
     * 校验是否成功(true:是，false:否)
     */
    private Boolean checkResult;

    /**
     * 校验结果信息
     */
    private String resultMsg;
}
