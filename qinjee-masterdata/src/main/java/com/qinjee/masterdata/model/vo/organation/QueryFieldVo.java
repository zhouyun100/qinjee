package com.qinjee.masterdata.model.vo.organation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月12日 10:27:00
 */
@ApiModel(description = "查询字段Vo类")
@Data
@NoArgsConstructor
public class QueryFieldVo {

    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名", example = "name")
    private String fieldName;

    /**
     * 字段值
     */
    @ApiModelProperty(value = "字段值", example = "张三")
    private String fieldValue;


    @ApiModelProperty(value = "是否筛选空值", example = "true")
    private Boolean isFilterNull;

    @ApiModelProperty(value = "是否正向排序、true正序(默认)、false逆序", example = "true")
    private Boolean isAscSort;

    private static final long serialVersionUID = 1L;
}
