package com.qinjee.masterdata.model.vo.staff.entryregistration;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EntryTableListWithValueVo implements Serializable {
    /**
     * 表ID
     */
    private Integer tableId;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 排序sort
     */
    private Integer sort;
    /**
     * 自定义字段列表
     */
    private List<EntryTemplateValueVo> entryTemplateValueVos;
}
