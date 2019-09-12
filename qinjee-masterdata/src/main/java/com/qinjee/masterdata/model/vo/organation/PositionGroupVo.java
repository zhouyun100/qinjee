package com.qinjee.masterdata.model.vo.organation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 职位族表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "职位族表实体类")
public class PositionGroupVo implements Serializable {
    /**
     * 职位族ID
     */
    @ApiModelProperty("职位族ID")
    private Integer positionGroupId;

    /**
     * 职位族名称
     */
    @ApiModelProperty("职位族名称")
    private String positionGroupName;

    private static final long serialVersionUID = 1L;

}
