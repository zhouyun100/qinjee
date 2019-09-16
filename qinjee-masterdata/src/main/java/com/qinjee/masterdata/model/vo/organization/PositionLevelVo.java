package com.qinjee.masterdata.model.vo.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 职级表
 * @author
 */
@Data
@ApiModel(description = "职级表实体Vo类")
@NoArgsConstructor
public class PositionLevelVo implements Serializable {
    /**
     * 职级ID
     */
    @ApiModelProperty(value = "职级ID", example = "1", required = true)
    private Integer positionLevelId;

    /**
     * 职级名称
     */
    @ApiModelProperty(value = "职级名称", example = "一级", required = true)
    private String positionLevelName;

    private static final long serialVersionUID = 1L;

}
