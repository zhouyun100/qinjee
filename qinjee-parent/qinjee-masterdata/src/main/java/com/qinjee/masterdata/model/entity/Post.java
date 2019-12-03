package com.qinjee.masterdata.model.entity;

import com.qinjee.masterdata.model.vo.organization.ImportExport;
import com.qinjee.masterdata.utils.pexcel.annotation.ExcelFieldAnno;
import com.qinjee.masterdata.utils.pexcel.annotation.ExcelSheetAnno;
import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.util.HSSFColor;

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
@ExcelSheetAnno(name = "岗位信息", headColor = HSSFColor.HSSFColorPredefined.LIGHT_GREEN)
public class Post  implements Serializable {

    private Boolean checkResult;
    private Integer lineNumber;
    private StringBuilder resultMsg;
    /**
     * 岗位ID
     */
    @ApiModelProperty("岗位ID")
    @QueryColumn("tp.post_id")
    private Integer postId;

    private Integer staffNumbers;
    private Integer planNumbers;
    /**
     * 岗位编码
     */
    @QueryColumn("tp.post_code")
    @ApiModelProperty("岗位编码")
    @ExcelFieldAnno(name = "岗位编码")
    private String postCode;
    /**
     * 岗位名称
     */
    @QueryColumn("tp.post_name")
    @ApiModelProperty("岗位名称")
    @ExcelFieldAnno(name = "岗位名称",width = 10000)
    private String postName;
    /**
     * 父级机构编码
     */
    @ApiModelProperty("所属部门编码")
    @ExcelFieldAnno(name = "所属部门编码")
    private String orgCode;

    /**
     * 机构名称
     */
    @ApiModelProperty("所属部门")
    @ExcelFieldAnno(name = "所属部门")
    private String orgName;

    /**
     * 父级岗位编码
     */
    @ApiModelProperty("上级岗位编码")
    @ExcelFieldAnno(name = "上级岗位编码")
    private String parentPostCode;

    /**
     * 父级岗位名称
     */
    @ApiModelProperty("上级岗位")
    @ExcelFieldAnno(name = "上级岗位")
    private String parentPostName;

    /**
     * 职位名称
     */
    @QueryColumn("tps.position_name")
    @ApiModelProperty("职位")
    @ExcelFieldAnno(name = "职位")
    private String positionName;


    /**
     * 父级岗位Id
     */
    @ApiModelProperty("父级岗位Id")
    @QueryColumn("tparentorg.org_id")
    private Integer parentPostId;






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
    private List<Post> childList;

    private static final long serialVersionUID = 1L;
}
