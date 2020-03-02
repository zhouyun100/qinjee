package com.qinjee.masterdata.model.vo.staff.export;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class ExportStanding  implements Serializable  {
    /**
     * 人员集合
     */
    private List<Integer> list;
    /**
     * 台账id
     */
    private Integer stangdingBookId;
    /**
     * 任职类型
     */
    private List<String> archiveType;
    /**
     * 企业id
     */
    private List <Integer> orgIdList;
    /**
     * 兼职类型
     */
    private List<String> type;
    /**
     * 查询方案id
     */
    private Integer querySchemaId;
}
