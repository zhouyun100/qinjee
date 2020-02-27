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
package com.qinjee.masterdata.model.vo.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.lang.Cloneable;

/**
 * @author 周赟
 * @date 2019/11/26
 */
@Data
@JsonInclude
public class CheckCustomFieldVO extends CustomFieldVO implements Serializable, Cloneable {


    /**
     * 字段值
     */
    private String fieldValue;

    /**
     * 校验是否成功(true:是，false:否)
     */
    private Boolean checkResult;

    /**
     * 校验结果信息
     */
    private String resultMsg;

    @Override
    public CheckCustomFieldVO clone() {
        CheckCustomFieldVO stu = null;
            try{
                stu = (CheckCustomFieldVO)super.clone();
            }catch(CloneNotSupportedException e) {
              e.printStackTrace();
            }
        return stu;
    }
}
