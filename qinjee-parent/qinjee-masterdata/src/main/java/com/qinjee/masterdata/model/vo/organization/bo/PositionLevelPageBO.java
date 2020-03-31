package com.qinjee.masterdata.model.vo.organization.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 机构实体分页Vo类
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "职级实体分页Vo类")
public class PositionLevelPageBO implements Serializable {

    private Integer currentPage;
    private Integer pageSize;

    @ApiModelProperty(value = "是否含有封存(启用) 0 封存、1 未封存", example = "0")
    private Short isEnable;

    @ApiModelProperty(value = "机构父级ID", example = "1")
    private Integer orgParentId;
    @ApiModelProperty(value = "机构ID", example = "1")
    private Integer orgId;

    private static final long serialVersionUID = 1L;

}
