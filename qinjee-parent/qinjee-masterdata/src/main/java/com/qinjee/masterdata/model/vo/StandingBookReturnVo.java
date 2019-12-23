package com.qinjee.masterdata.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude
@Data
public class StandingBookReturnVo {
    /**
     * 台账id
     */
    private Integer stangdingBookId;
    /**
     * 任职类型
     */
    private String archiveType;
    /**
     * 企业id
     */
    private Integer orgId;
    /**
     * 兼职类型
     */
    private String type;
    /**
     * 页大小
     */
    private Integer pageSize;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 总条数
     */
    private Integer total;
    /**
     * 查询方案id
     */
    private Integer querySchemaId;
}
