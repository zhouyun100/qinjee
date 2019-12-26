/**
 * 文件名：TemplateAttachmentGroupVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.staff.entryregistration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.AttachmentGroup;
import lombok.Data;

/**
 * @author 周赟
 * @date 2019/12/10
 */
@Data
@JsonInclude
public class TemplateAttachmentGroupVO extends AttachmentGroup {

    /**
     * 主键ID
     */
    private Integer tagId;

    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     * 组ID
     */
    private Integer groupId;

    /**
     * 填表说明
     */
    private String description;

    /**
     * 是否必填
     */
    private Integer isMust;

    /**
     * 排序
     */
    private Integer sort;
}
