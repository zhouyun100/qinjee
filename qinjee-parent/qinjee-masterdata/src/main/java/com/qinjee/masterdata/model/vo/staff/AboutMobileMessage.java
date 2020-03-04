package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.vo.staff.entryregistration.EntryTableListWithValueVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class AboutMobileMessage implements Serializable {
    /**
     * 表结构与数据集合
     */
    private List<EntryTableListWithValueVo> list;
    /**
     * 是否已经进行了提交hr操作
     */
    private Boolean isCommitHr;
}
