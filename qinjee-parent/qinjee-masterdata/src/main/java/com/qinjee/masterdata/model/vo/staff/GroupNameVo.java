package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude
public class GroupNameVo implements Serializable {
    private String groupName;
    private List<AttachmentVo> list;
}
