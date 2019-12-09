package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude
public class UserArchiveVoAndHeader {
    private List<UserArchiveVo> list;
    private List<ArcHead> heads;
}
