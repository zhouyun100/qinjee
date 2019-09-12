package com.qinjee.masterdata.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月11日 16:39:00
 */
@ApiModel(description = "岗位分页实体Vo类")
public class PostPageVo {
    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String postName;

    /**
     * 岗位编码
     */
    @ApiModelProperty("岗位编码")
    private String postCode;

    /**
     * 父级岗位名称
     */
    @ApiModelProperty("父级岗位名称")
    private String parentPostName;

    /**
     * 职级
     */
    @ApiModelProperty("职级")
    private List<Integer> positionLevels;

    /**
     * 职等
     */
    @ApiModelProperty("职等")
    private List<Integer> positionGrades;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}
