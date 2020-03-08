package com.qinjee.masterdata.model.vo.staff;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelTable;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.TransDictAnno;
import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Data

@ExcelTable(sheetName = "用户信息", useFieldNameAsTitle = false,includeAllField = false)
@Getter
public class UserArchiveVo implements Serializable {

    private Integer lineNumber;
    private boolean checkResult=true;

    private String resultMsg;
    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    private Integer archiveId;

    /**
     * 姓名
     */
    @QueryColumn("tua.user_name")
    @ApiModelProperty("姓名")
    @ExcelColumn(order = 0, title = "姓名")
    private String userName;

    /**
     * 性别
     */
    @QueryColumn("tua.gender")
    @ApiModelProperty("性别")
    @TransDictAnno
    @ExcelColumn(order = 1, title = "性别")
    private String gender;

    /**
     * 证件类型
     */

    @ApiModelProperty("证件类型")
    @ExcelColumn(order = 2, title = "证件类型")
    @TransDictAnno
    private String idType;

    /**
     * 证件号码
     */
    @ApiModelProperty("证件号码")
    @ExcelColumn(order = 3, title = "证件号码")
    private String idNumber;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")

    @ExcelColumn(order =4, title = "出生日期",format = "yyyy-MM-dd")
    private Date birthDate;

    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    @ExcelColumn(order =5, title = "年龄")
    private Integer age;

    /**
     * 籍贯
     */
    @ApiModelProperty("籍贯")
    @ExcelColumn(order =6, title = "籍贯")
    private String birthplace;

    /**
     * 民族
     */
    @ApiModelProperty("民族")
    @ExcelColumn(order =7, title = "民族")
    @TransDictAnno
    private String nationality;


    /**
     * 最高学历
     */
    @ApiModelProperty("最高学历")
    @ExcelColumn(order =8, title = "最高学历")
    @TransDictAnno
    private String highestDegree;

    /**
     * 第一学历
     */
    @ApiModelProperty("第一学历")
    @ExcelColumn(order =9, title = "第一学历")
    @TransDictAnno
    private String firstDegree;

    /**
     * 联系电话
     */
    @QueryColumn("tua.phone")
    @ApiModelProperty("联系电话")
    @ExcelColumn(order =10, title = "联系电话")
    private String phone;

    /**
     * 电子邮箱
     */
    @QueryColumn("tua.email")
    @ApiModelProperty("电子邮箱")
    @ExcelColumn(order =11, title = "电子邮箱")
    private String email;

    /**
     * 婚姻状况
     */
    @ApiModelProperty("婚姻状况")
    @ExcelColumn(order =12, title = "婚姻状况")
    @TransDictAnno
    private String maritalStatus;

    /**
     * 政治面貌
     */
    @ApiModelProperty("政治面貌")
    @ExcelColumn(order =13, title = "政治面貌")
    @TransDictAnno
    private String politicalStatus;


    /**
     * 现住址
     */
    @ApiModelProperty("现住址")
    @ExcelColumn(order =14, title = "现住址")
    private String address;
    /**
     * 职业资格
     */
    @ApiModelProperty("职业资格")
    @ExcelColumn(order =15, title = "职业资格")
    private String professionalCertification;

    /**
     * 职称
     */
    @ApiModelProperty("职称")
    @ExcelColumn(order =16, title = "职称")
    private String professionalTitle;


    /**
     * 职称等级
     */
    @ApiModelProperty("职称等级")
    @ExcelColumn(order =17, title = "职称等级")
    private String professionalLevel;

    /**
     * 工号
     */
    @ApiModelProperty("工号")
    @ExcelColumn(order =18, title = "工号")
    private String employeeNumber;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门编码")
    @ExcelColumn(order =19, title = "部门编码")
    private String orgCode;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @ExcelColumn(order =20, title = "部门")
    private String orgName;


    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位编码")
    @ExcelColumn(order =21, title = "岗位编码")
    private String postCode;
    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位")
    @ExcelColumn(order =22, title = "岗位")
    private String postName;


    /**
     * 入职时间
     */
    @ApiModelProperty("任职时间")
    @ExcelColumn(order =23, title = "任职时间",format = "yyyy-MM-dd")
    private Date servingDate;

    /**
     * 人员分类   多级代码：在职（正式、试用、实习）、不在职（离职、退休）
     */
    @ApiModelProperty("人员分类")
    @ExcelColumn(order =24, title = "人员分类")
    private String userCategory;


    @ApiModelProperty("直接上级工号")
    @ExcelColumn(order =25, title = "直接上级工号")
    private String supervisorEmployeeNumber;

    /**
     * 参加工作时间
     */
    @ApiModelProperty("参加工作时间")
    @ExcelColumn(order =26, title = "参加工作时间",format = "yyyy-MM-dd")
    private Date firstWorkDate;

    /**
     * 工龄
     */
    @ApiModelProperty("工龄")
    @ExcelColumn(order =27, title = "工龄")
    private Integer workingPeriod;

    /**
     * 入职时间
     */
    @QueryColumn("tua.hiredate")
    @ApiModelProperty("入职时间")
    @ExcelColumn(order =28, title = "入职时间",format = "yyyy-MM-dd")
    private Date hireDate;

    /**
     * 司龄
     */
    @ApiModelProperty("司龄")
    @ExcelColumn(order =29, title = "司龄")
    private Integer servingAge;


    @ApiModelProperty("试用期限(月)")
    @ExcelColumn(order =30, title = "试用期限(月)")
    private Integer probationPeriod;

    /**
     * 试用到期时间
     */
    @ApiModelProperty("试用到期时间")
    @ExcelColumn(order =31, title = "试用到期时间",format = "yyyy-MM-dd")
    private Date probationDueDate;

    /**
     * 转正时间
     */
    @ApiModelProperty("转正时间")
    @ExcelColumn(order =32, title = "转正时间",format = "yyyy-MM-dd")
    private Date converseDate;

    /**
     * 人员ID
     */
    @ApiModelProperty("人员ID")
    private Integer userId;



    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;



    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    @QueryColumn("tua.business_unit_id")
    private Integer businessUnitId;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String businessUnitName;
    @ApiModelProperty("职位名称")
    private String positionName;

    /**
     * 部门ID
     */
    @QueryColumn("tua.org_id")
    @ApiModelProperty("部门ID")
    private Integer orgId;



    /**
     * 岗位ID
     */
    @QueryColumn("tua.post_id")
    @ApiModelProperty("岗位ID")
    private Integer postId;



    /**
     * 减员时间
     */
    @ApiModelProperty("减员时间")
    private Date attritionDate;

    /**
     * 减员类型
     */
    @ApiModelProperty("减员类型")
    private String attritionType;

    /**
     * 上级领导Id
     */
    @ApiModelProperty("上级领导Id")
    @QueryColumn("tua.supervisor_id")
    private Integer supervisorId;

    /**
     * 上级领导姓名
     */
    @ApiModelProperty("上级领导姓名")
    private String supervisorUserName;

    /**
     * 任职类型
     */
    private String  employmentType;
    /**
     * 档案状态(默认在职)
     */
    private String  archiveStatus;
    /**
     * 用户头像
     */
    private String headImgUrl;
    /**
     * 职级id
     */
    private Integer positionLevelId;
    /**
     * 职等id
     */
    private Integer positionGradeId;
    /**
     * 职级名称
     */
    private Integer positionLevelName;
    /**
     * 职等名称
     */
    private Integer positionGradeName;

    private static final long serialVersionUID = 1L;


}