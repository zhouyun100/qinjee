package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "机构实体类")
public class Organation implements Serializable {
    /**
     * 机构ID
     */
    @ApiModelProperty("机构ID")
    private Integer orgId;

    /**
     * 机构编码
     */
    @ApiModelProperty("机构编码")
    private String orgCode;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String orgName;

    /**
     * 机构类型
     */
    @ApiModelProperty("机构类型")
    private String orgType;

    /**
     * 机构父级ID
     */
    @ApiModelProperty("机构父级ID")
    private Integer orgParentId;

    /**
     * 机构全称
     */
    @ApiModelProperty("机构全称")
    private String orgFullname;

    /**
     * 机构负责人
     */
    @ApiModelProperty("机构负责人")
    private Integer orgManagerId;

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
    @ApiModelProperty("是否启用")
    private Short isEnable;

    private static final long serialVersionUID = 1L;

}
