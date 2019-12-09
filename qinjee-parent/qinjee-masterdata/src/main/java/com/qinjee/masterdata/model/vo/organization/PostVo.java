package com.qinjee.masterdata.model.vo.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 岗位表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "岗位表实体类")
public class PostVo  implements Serializable {

    /**
     * 岗位ID
     */
    @ApiModelProperty("岗位ID")
    private Integer postId;


    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String postName;

    /**
     * 岗位code
     */
    @ApiModelProperty("岗位code")
    private String postCode;

//    /**
//     * 岗位编码
//     */
//    @ApiModelProperty("岗位编码")
//    private String postCode;

    /**
     * 职位ID
     */
    @ApiModelProperty("职位ID")
    private Integer positionId;

    /**
     * 父级岗位
     */
    @ApiModelProperty("父级岗位")
    private Integer parentPostId;

    /**
     * 机构ID
     */
    @ApiModelProperty("机构ID")
    private Integer orgId;

    /**
     * 职级
     */
//    @ApiModelProperty("职级")
//    private List<Integer> positionLevels;

    /**
     * 职等
     */
//    @ApiModelProperty("职等")
//    private List<Integer> positionGrades;

//    /**
//     * 是否删除
//     */
//    @ApiModelProperty("是否删除")
//   private Integer isDelete;

    private static final long serialVersionUID = 1L;

}
