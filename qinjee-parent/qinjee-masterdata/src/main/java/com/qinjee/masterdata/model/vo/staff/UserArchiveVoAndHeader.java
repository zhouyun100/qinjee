package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.model.response.PageResult;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude
public class UserArchiveVoAndHeader {
    private PageResult <UserArchiveVo>  pageResult;
    private List<TableHead> heads;
}
