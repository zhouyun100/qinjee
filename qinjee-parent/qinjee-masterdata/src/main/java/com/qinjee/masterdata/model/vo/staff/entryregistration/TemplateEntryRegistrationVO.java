/**
 * 文件名：TemplateEntryRegistrationVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2020/1/18
 * <p>
 * Copyright(C) 2020, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.staff.entryregistration;

import com.qinjee.masterdata.model.entity.TemplateEntryRegistration;
import lombok.Data;

/**
 * 入职登记模板
 * @author 周赟
 * @date 2020-01-18
 */
@Data
public class TemplateEntryRegistrationVO extends TemplateEntryRegistration {

    private String creator;
}
