package com.qinjee.masterdata.model.vo.organation;

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
public class OrganationPageVo extends PageQueryVo implements Serializable {

    @ApiModelProperty("是否含有封存 0不含有、1含有")
    private Short isEnable;

    private static final long serialVersionUID = 1L;

}
