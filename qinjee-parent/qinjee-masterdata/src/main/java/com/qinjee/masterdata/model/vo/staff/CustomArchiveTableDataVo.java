package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.vo.custom.CheckCustomFieldVO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonInclude
public class CustomArchiveTableDataVo {
    /**
     * id
     */
    private  Integer id;

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
     * 字段集合
     */
    /**
     * 自定义字段列表
     */
    private List < CheckCustomFieldVO > customFieldVOList;
    private static final long serialVersionUID = 1L;
}
