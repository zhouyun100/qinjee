package com.qinjee.masterdata.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.AttachmentGroup;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude
public class ShowAttatchementVo extends AttachmentGroup implements Serializable {
     private List<ShowAttatchementVo> list;

}
