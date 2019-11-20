/**
 * 文件名：OrganizationArchiveVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/20
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/20
 */
@Data
@NoArgsConstructor
@ApiModel(description = "机构人员封装对象")
@ToString
@JsonInclude
public class OrganizationArchiveVO implements Serializable {

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
    @ApiModelProperty("机构类型  GROUP 集团、UNIT 单位、DEPT 部门")
    private String orgType;

    /**
     * 机构全称
     */
    @ApiModelProperty("机构全称")
    private String orgFullName;

    /**
     * 机构父级ID
     */
    @ApiModelProperty("机构父级ID")
    private Integer orgParentId;

    /**
     * 排序ID
     */
    @ApiModelProperty("排序ID")
    private Integer sortId;

    /**
     * 子机机构
     */
    private List<OrganizationArchiveVO> childOrgList;

    /**
     * 子集人员
     */
    private List<ArchiveInfoVO> childArchiveList;
}
