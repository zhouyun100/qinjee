package com.qinjee.masterdata.model.vo.organization.bo;

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
public class UserArchiveImportBO implements Serializable {

    private Integer lineNumber;
    private boolean checkResult=true;

    private String resultMsg;
    /**
     * 姓名
     */
    @ExcelColumn(order = 0, title = "姓名")
    private String userName;

    /**
     * 性别
     */
    @ExcelColumn(order = 1, title = "性别")
    private String gender;

    /**
     * 证件类型
     */

    @ExcelColumn(order = 2, title = "证件类型")
    private String idType;

    /**
     * 证件号码
     */
    @ExcelColumn(order = 3, title = "证件号码")
    private String idNumber;

    /**
     * 出生日期
     */
    @ExcelColumn(order =4, title = "出生日期")
    private String birthDate;

    /**
     * 年龄
     */
    @ExcelColumn(order =5, title = "年龄")
    private String age;

    /**
     * 籍贯
     */
    @ExcelColumn(order =6, title = "籍贯")
    private String birthplace;

    /**
     * 民族
     */
    @ExcelColumn(order =7, title = "民族")
    private String nationality;


    /**
     * 最高学历
     */
    @ExcelColumn(order =8, title = "最高学历")
    private String highestDegree;

    /**
     * 第一学历
     */
    @ExcelColumn(order =9, title = "第一学历")
    private String firstDegree;

    /**
     * 联系电话
     */
    @ExcelColumn(order =10, title = "联系电话")
    private String phone;

    /**
     * 电子邮箱
     */
    @ExcelColumn(order =11, title = "电子邮箱")
    private String email;

    /**
     * 婚姻状况
     */
    @ExcelColumn(order =12, title = "婚姻状况")
    private String maritalStatus;

    /**
     * 政治面貌
     */
    @ExcelColumn(order =13, title = "政治面貌")
    private String politicalStatus;


    /**
     * 现住址
     */
    @ExcelColumn(order =14, title = "现住址")
    private String address;
    /**
     * 职业资格
     */
    @ExcelColumn(order =15, title = "职业资格")
    private String professionalCertification;

    /**
     * 职称
     */
    @ExcelColumn(order =16, title = "职称")
    private String professionalTitle;


    /**
     * 职称等级
     */
    @ExcelColumn(order =17, title = "职称等级")
    private String professionalLevel;

    /**
     * 工号
     */
    @ExcelColumn(order =18, title = "工号")
    private String employeeNumber;

    /**
     * 部门名称
     */
    @ExcelColumn(order =19, title = "部门编码")
    private String orgCode;

    /**
     * 部门名称
     */
    @ExcelColumn(order =20, title = "部门")
    private String orgName;


    /**
     * 岗位名称
     */
    @ExcelColumn(order =21, title = "岗位编码")
    private String postCode;
    /**
     * 岗位名称
     */
    @ExcelColumn(order =22, title = "岗位")
    private String postName;
 /**
     * 岗位名称
     */
    @ExcelColumn(order =23, title = "职级")
    private String positionLevelName;


    /**
     * 入职时间
     */
    @ExcelColumn(order =24, title = "任职时间")
    private String servingDate;

    /**
     * 人员分类   多级代码：在职（正式、试用、实习）、不在职（离职、退休）
     */
    @ExcelColumn(order =25, title = "人员分类")
    private String userCategory;


    @ExcelColumn(order =26, title = "直接上级工号")
    private String supervisorEmployeeNumber;

    /**
     * 参加工作时间
     */
    @ExcelColumn(order =27, title = "参加工作时间")
    private String firstWorkDate;

    /**
     * 工龄
     */
    @ExcelColumn(order =28, title = "工龄")
    private String workingPeriod;

    /**
     * 入职时间
     */
    @ExcelColumn(order =29, title = "入职时间")
    private String hireDate;

    /**
     * 司龄
     */
    @ExcelColumn(order =30, title = "司龄")
    private String servingAge;


    @ExcelColumn(order =31, title = "试用期限(月)")
    private String probationPeriod;

    /**
     * 试用到期时间
     */
    @ExcelColumn(order =32, title = "试用到期时间")
    private String probationDueDate;

    /**
     * 转正时间
     */
    @ExcelColumn(order =33, title = "转正时间")
    private String converseDate;


    private static final long serialVersionUID = 1L;


}