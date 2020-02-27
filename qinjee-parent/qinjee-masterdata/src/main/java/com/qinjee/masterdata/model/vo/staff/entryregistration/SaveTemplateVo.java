package com.qinjee.masterdata.model.vo.staff.entryregistration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.TemplateEntryRegistration;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class SaveTemplateVo implements Serializable {
    private Integer templateId;
    private List < TemplateCustomTableVO > templateCustomTableList;
    private TemplateEntryRegistration templateEntryRegistration;
}
