package com.qinjee.masterdata.model.vo.organization.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月12日 10:27:00
 */
@ApiModel(description = "查询字段Vo类")
@Data
@NoArgsConstructor
public class QueryField {

    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名", example = "name")
    private String fieldName;

    /**
     * 字段值
     */
    @ApiModelProperty(value = "查询字段值集合 ", notes = "时间转yyyy-MM-dd HH:mm:ss字符串")
    private List<String> fieldValues;

//    /**
//     * 开始时间
//     */
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty(value = "开始时间")
//    private Date startTime;

//    /**
//     * 结束时间
//     */
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty(value = "结束时间")
//    private Date endTime;

    /**
     * 查询条件
     */
    @ApiModelProperty(hidden = true)
    private String condition;

    @ApiModelProperty(value = "是否筛选空值", example = "true")
    private Boolean isFilterNull;

    @ApiModelProperty(value = "是否正向排序、true正序(默认)、false逆序", example = "true")
    private Boolean isAscSort;

    private static final long serialVersionUID = 1L;
}
