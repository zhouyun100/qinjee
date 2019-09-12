package com.qinjee.masterdata.model.vo.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 职等表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "职等表实体类")
public class PositionGradeVo implements Serializable {
    /**
     * 职等ID
     */
    @ApiModelProperty(value = "职等ID", example = "1")
    private Integer positionGradeId;

    /**
     * 职等名称
     */
    @ApiModelProperty(value = "职等名称", example = "专家", required = true)
    private String positionGradeName;

    private static final long serialVersionUID = 1L;

}
