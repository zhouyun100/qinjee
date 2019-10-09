package com.qinjee.masterdata.model.entity;

import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @QueryColumn("tp.post_id")
    private Integer postId;

    /**
     * 岗位名称
     */
    @QueryColumn("tp.post_name")
    @ApiModelProperty("岗位名称")
    private String postName;

    /**
     * 岗位编码
     */
    @QueryColumn("tp.post_code")
    @ApiModelProperty("岗位编码")
    private String postCode;

    /**
     * 父级岗位Id
     */
    @ApiModelProperty("父级岗位Id")
    @QueryColumn("tparentorg.org_id")
    private Integer parentPostId;

    /**
     * 父级岗位名称
     */
    @ApiModelProperty("父级岗位名称")
    private String parentPostName;

    /**
     * 父级岗位编码
     */
    @ApiModelProperty("父级岗位编码")
    private String parentPostCode;



    /**
     * 机构ID
     */
    @QueryColumn("torg.org_id")
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

    /**
     * 职位名称
     */
    @QueryColumn("tps.position_name")
    @ApiModelProperty("职位名称")
    private String positionName;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String orgName;

    /**
     * 父级机构名称
     */
    @ApiModelProperty("父级机构名称")
    private String parentOrgName;

    /**
     * 父级机构Id
     */
    @ApiModelProperty("父级机构Id")
    private String parentOrgId;

    /**
     * 父级机构编码
     */
    @ApiModelProperty("父级机构编码")
    private String parentOrgCode;


    /**
     * 职级id集合
     */
    @ApiModelProperty("职级id集合")
    @QueryColumn("tpl.position_level_id")
    private List<Integer> positionLevelId;

    /**
     * 职级名称 逗号拼接
     */
    @ApiModelProperty("职级名称 逗号拼接")
    private String positionLevelNames;

    /**
     * 职等id集合
     */
    @ApiModelProperty("职等id集合")
    @QueryColumn("tpg.position_grade_id")
    private List<Integer> positionGradeId;

    /**
     * 职等名称 逗号拼接
     */
    @ApiModelProperty("职等名称 逗号拼接")
    private String positionGradeNames;

    private static final long serialVersionUID = 1L;
}
