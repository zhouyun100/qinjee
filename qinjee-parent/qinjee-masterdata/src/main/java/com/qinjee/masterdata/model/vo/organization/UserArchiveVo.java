package com.qinjee.masterdata.model.vo.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月24日 17:46:00
 */
@Data
@NoArgsConstructor
@ApiModel(description = "用户档案Vo类")
public class UserArchiveVo {

    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    private Integer businessUnitId;


    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Integer orgId;


    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String userName;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 入职时间
     */
    @ApiModelProperty("入职时间")
    private Date hireDate;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String tel;

    /**
     * 电子邮箱
     */
    @ApiModelProperty("电子邮箱")
    private String email;

    private static final long serialVersionUID = 1L;

}
