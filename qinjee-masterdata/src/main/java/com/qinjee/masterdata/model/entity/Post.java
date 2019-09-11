package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 岗位表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "岗位表实体类")
public class Post implements Serializable {
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
     * 岗位编码
     */
    @ApiModelProperty("岗位编码")
    private String postCode;

    /**
     * 父级岗位
     */
    @ApiModelProperty("父级岗位")
    private Integer parentPostId;

    /**
     * 父级岗位名称
     */
    @ApiModelProperty("父级岗位名称")
    private String parentPostName;

    /**
     * 机构ID
     */
    @ApiModelProperty("机构ID")
    private Integer orgId;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;

    /**
     * 职位ID
     */
    @ApiModelProperty("职位ID")
    private Integer positionId;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private Integer operatorId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 排序ID
     */
    @ApiModelProperty("排序ID")
    private Integer sortId;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Short isEnable;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
