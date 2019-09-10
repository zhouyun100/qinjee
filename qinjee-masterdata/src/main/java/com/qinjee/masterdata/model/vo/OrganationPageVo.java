package com.qinjee.masterdata.model.vo;

import com.qinjee.model.request.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 机构实体分页Vo类
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "机构实体分页Vo类")
public class OrganationPageVo extends PageVo implements Serializable {


    @ApiModelProperty(value = "需要查询的字段")
    private List<CustomOrgFieldVo> customOrgFieldVos;

    private static final long serialVersionUID = 1L;

}
