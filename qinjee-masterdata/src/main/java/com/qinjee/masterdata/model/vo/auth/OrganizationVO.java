/**
 * 文件名：OrganizationVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/12
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月12日
 */
@ApiModel(description = "机构树")
@Data
@NoArgsConstructor
public class OrganizationVO {

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
    private String orgfull_name;

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

    /**
     * 子级机构列表
     */
    @ApiModelProperty("子级机构列表")
    private List<OrganizationVO> childOrganizationList;
}
