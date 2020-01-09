package com.qinjee.masterdata.model.vo.staff.entryregistration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.TemplateAttachmentGroup;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class InsertTemplateAttachmentVo implements Serializable {
   private List < TemplateAttachmentGroup > list;
   private Integer templatedId;
}
