package com.qinjee.masterdata.model.vo.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 职位表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "职位表实体类")
public class PositionVo implements Serializable {
    /**
     * 职位ID
     */
    @ApiModelProperty("职位ID")
    private Integer positionId;

    /**
     * 职位名称
     */
    @ApiModelProperty("职位名称")
    private String positionName;

    /**
     * 职位族ID
     */
    @ApiModelProperty("职位族ID")
    private Integer positionGroupId;

    private Short isDelete;

 /*   @ApiModelProperty("职位对应的职级")
    List<Integer> positionLevelIds;*/

 /*   @ApiModelProperty("职位对应的职等")
    List<Integer> positionGradeIds;*/

    private static final long serialVersionUID = 1L;

}

