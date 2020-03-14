package com.qinjee.masterdata.model.vo.organization;

import io.swagger.annotations.ApiModel;
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
    private Integer positionLevelId;
    /**
     * 职等ID
     */
    private Integer positionGradeId;
    private Integer companyId;

    /**
     * 职级名称
     */
    private String positionLevelName;
    /**
     * 职等名称
     */
    private String positionGradeName;

    /**
     * 职级描述
     */
    private String positionLevelRemark;

    private static final long serialVersionUID = 1L;

}
