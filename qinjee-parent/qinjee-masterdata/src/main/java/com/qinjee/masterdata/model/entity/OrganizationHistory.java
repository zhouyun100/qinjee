package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * t_organization_history
 * @author
 */
@Data
@ToString
public class OrganizationHistory implements Serializable {
    /**
     * 机构ID
     */
    private Integer orgId;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 机构父级ID
     */
    private Integer orgParentId;

    /**
     * 机构全称
     */
    private String orgFullName;

    /**
     * 机构负责人
     */
    private Integer orgManagerId;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 排序ID
     */
    private Integer sortId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date createTime;

    /**
     * 修改时间
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date updateTime;

    /**
     * 是否启用
     */
    private Short isEnable;

    private static final long serialVersionUID = 1L;

}
