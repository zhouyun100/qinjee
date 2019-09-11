package com.qinjee.masterdata.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月10日 16:11:00
 */
@ApiModel(description = "自定义字段Vo类")
@Data
@NoArgsConstructor
public class CustomOrgFieldVo {

    /**
     * 字段ID
     */
    @ApiModelProperty("字段ID")
    private Integer fieldId;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String fieldName;


    @ApiModelProperty("是否筛选空值")
    private Boolean isFilterNull;

    @ApiModelProperty("是否正向排序、true正序(默认)、false逆序")
    private Boolean isAscSort;

    private static final long serialVersionUID = 1L;
}
