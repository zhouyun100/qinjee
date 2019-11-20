package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
 * 机构表
 *
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "机构实体类")
@ToString
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
     * 机构全称
     */
    @ApiModelProperty("机构全称")
    @QueryColumn("org.org_full_name")
    private String orgFullName;

    /**
     * 机构负责人Id
     */
    @ApiModelProperty("机构负责人Id")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date createTime;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用 0 封存、1 未封存")
    private Short isEnable;


    private static final long serialVersionUID = 1L;

}
