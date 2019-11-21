package com.qinjee.masterdata.model.vo.organization;

import com.alibaba.fastjson.annotation.JSONField;
import com.qinjee.masterdata.model.entity.Position;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.utils.pexcel.annotation.ExcelFieldAnno;
import com.qinjee.masterdata.utils.pexcel.annotation.ExcelSheetAnno;
import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.poi.hssf.util.HSSFColor;
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
@ApiModel(description = "机构实体VO类")
@ToString
@ExcelSheetAnno(name = "机构信息", headColor = HSSFColor.HSSFColorPredefined.LIGHT_GREEN)
public class OrganizationVO implements Serializable {

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
    @ExcelFieldAnno(name = "机构编码")
    private String orgCode;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    @QueryColumn("org.org_name")
    @ExcelFieldAnno(name = "机构名称",width = 10000)
    private String orgName;

    /**
     * 机构类型
     */
    @ApiModelProperty("机构类型  GROUP 集团、UNIT 单位、DEPT 部门")
    @QueryColumn("org.org_type")
    @ExcelFieldAnno(name = "机构类型",width = 3000)
    private String orgType;

    /**
     * 机构父级ID
     */
    @ApiModelProperty("机构父级ID")
    @QueryColumn("org_parent_id")
    private Integer orgParentId;


    /**
     * 机构父级编码
     */
    @ApiModelProperty("机构父级编码")
    @ExcelFieldAnno(name = "上级机构编码")
    private String orgParentCode;

    /**
     * 机构父级名称
     */
    @ApiModelProperty("机构父级名称")
    @QueryColumn("parent_org.org_name")
    @ExcelFieldAnno(name = "上级机构",width = 10000)
    private String orgParentName;

    /**
     * 机构负责人工号
     */
    @ApiModelProperty("机构负责人工号")
    @ExcelFieldAnno(name = "部门负责人编号",width = 4000)
    private String managerEmployeeNumber;

    /**
     * 机构负责人姓名
     */
    @ApiModelProperty("机构负责人姓名")
    @QueryColumn("tua.user_name")
    @ExcelFieldAnno(name = "部门负责人",width = 3000)
    private String orgManagerName;
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

    /**
     * 子机机构
     */
    private List<OrganizationVO> childList;

    /**
     * 图片url
     */
    private String attachmentUrl;

    /**
     * 机构下的职位
     */
    private List<Position> positionList;

    /**
     * 机构的下岗位
     */
    private List<Post> postList;

    private static final long serialVersionUID = 1L;

}
