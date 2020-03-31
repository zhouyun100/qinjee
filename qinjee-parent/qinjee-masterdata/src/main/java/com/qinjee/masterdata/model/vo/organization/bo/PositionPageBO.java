package com.qinjee.masterdata.model.vo.organization.bo;

import com.qinjee.masterdata.model.vo.staff.FieldValueForSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020年02月28日 16:39:00
 */
@ApiModel(description = "职位分页实体Vo类")
@Data
@NoArgsConstructor
public class PositionPageBO implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
    private Short isDelete;

    private List<FieldValueForSearch> tableHeadParamList;
    /**
     * 职位族ID
     */
    @ApiModelProperty("职位族ID")
    private Integer positionGroupId;
    @ApiModelProperty("职位ID")
    private Integer positionId;

}
