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
 * 机构表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "机构实体类")
public class Organization implements Serializable {
    /**
     * 机构ID
     */
    @ApiModelProperty("机构ID")
    @QueryColumn("org_id")
    private Integer orgId;

    /**
     * 机构编码
     */
    @ApiModelProperty("机构编码")
    @QueryColumn("org.org_code")
    private String orgCode;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    @QueryColumn("org.org_name")
    private String orgName;

    /**
     * 机构类型
     */
    @ApiModelProperty("机构类型  GROUP 集团、UNIT 单位、DEPT 部门")
    @QueryColumn("org.org_type")
    private String orgType;

    /**
     * 机构父级ID
     */
    @ApiModelProperty("机构父级ID")
    @QueryColumn("org_parent_id")
    private Integer orgParentId;

    /**
     * 机构父级名称
     */
    @ApiModelProperty("机构父级名称")
    @QueryColumn("parent_org.org_name")
    private Integer orgParentName;

    /**
     * 机构全称
     */
    @ApiModelProperty("机构全称")
    @QueryColumn("org.org_fullname")
    private String orgFullname;

    /**
     * 机构负责人
     */
    @ApiModelProperty("机构负责人")
    private Integer orgManagerId;

    /**
     * 机构负责姓名
     */
    @ApiModelProperty("机构负责姓名")
    @QueryColumn("tua.user_name")
    private Integer orgManagerName;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;

    /**
     * 排序ID
     */
    @ApiModelProperty("排序ID")
    private Integer sortId;

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
     * 是否启用
     */
    @ApiModelProperty("是否启用 0 封存、1 未封存")
    private Short isEnable;

    /**
     * 子机机构
     */
    private List<Organization> childList;

    /**
     * 图片url
     */
    private String attachmentUrl;

    private static final long serialVersionUID = 1L;

}
