package com.qinjee.masterdata.model.entity;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelTable;
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
@ExcelTable(sheetName = "岗位信息", useFieldNameAsTitle = false,includeAllField = false)
public class Post  implements Serializable {

    private boolean checkResult=true;
    private Integer lineNumber;
    private String resultMsg;
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
    @ExcelColumn(order = 0, title = "岗位编码")
    private String postCode;
    /**
     * 岗位名称
     */
    @QueryColumn("tp.post_name")
    @ApiModelProperty("岗位名称")
    @ExcelColumn(order = 1, title = "岗位名称")
    private String postName;
    /**
     * 父级机构编码
     */
    @ApiModelProperty("所属部门编码")
    @ExcelColumn(order = 2, title = "所属部门编码")
    private String orgCode;

    /**
     * 机构名称
     */
    @ApiModelProperty("所属部门")
    @ExcelColumn(order = 3, title = "所属部门")
    private String orgName;

    /**
     * 父级岗位编码
     */
    @ApiModelProperty("上级岗位编码")
    @ExcelColumn(order = 4, title = "上级岗位编码")
    private String parentPostCode;

    /**
     * 父级岗位名称
     */
    @ApiModelProperty("上级岗位")
    @ExcelColumn(order = 5, title = "上级岗位")
    private String parentPostName;

    /**
     * 职位名称
     */
    @QueryColumn("tps.position_name")
    @ApiModelProperty("职位")
    @ExcelColumn(order = 6, title = "职位")
    private String positionName;


    /**
     * 职级
     */
    @ApiModelProperty("职级id列表")
    private List<Integer> positionLevelIds;

    /**
     * 职级名称
     */
    @ApiModelProperty("职级名称，逗号拼接")
    @ExcelColumn(order = 7, title = "职级")
    private String positionLevelName;

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




    private List<Post> childList;

    private static final long serialVersionUID = 1L;
}
