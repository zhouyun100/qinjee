package com.qinjee.masterdata.model.vo.organization;

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
@ApiModel(description = "机构实体分页Vo类")
public class OrganizationPageVo extends PageQueryVo implements Serializable {

    @ApiModelProperty(value = "是否含有封存 0不含有、1含有", example = "0")
    private Short isEnable;

    @ApiModelProperty(value = "机构父级ID", example = "1")
    private Integer orgParentId;

    private static final long serialVersionUID = 1L;

}
