package com.qinjee.masterdata.model.vo.staff;

import javax.validation.constraints.NotNull;

public class CustomArchiveTableDataVo {

    /**
     * 表ID
     */
    @NotNull
    private Integer tableId;

    /**
     * 业务ID
     */
    @NotNull
    private Integer businessId;

    /**
     * 数据大字段
     */
    @NotNull
    private String bigData;

    private static final long serialVersionUID = 1L;
}
