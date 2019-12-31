package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * t_pre_employment
 * @author 
 */
@Data
@ToString
@JsonInclude
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreEmployment implements Serializable {

        /**
         * 预入职ID
         */
        private Integer employmentId;

        /**
         * 用户姓名
         */
        private String userName;

        /**
         * 电话
         */
        private String phone;

        /**
         * 性别
         */
        private String gender;

        /**
         * 邮箱
         */
        private String email;

        /**
         * 证件类型
         */
        private String idType;

        /**
         * 证件号码
         */
        private String idNumber;

        /**
         * 年龄
         */
        private Integer age;

        /**
         * 参加工作时间
         */
        @JsonFormat(pattern="yyyy-MM-dd")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private Date firstWorkDate;

        /**
         * 婚姻状况
         */
        private String maritalStatus;

        /**
         * 最高学历
         */
        private String highestDegree;

        /**
         * 毕业院校
         */
        private String graduatedSchool;

        /**
         * 毕业专业
         */
        private String graduatedSpeciality;

        /**
         * 最近工作单位
         */
        private String lastWorkCompany;

        /**
         * 是否已育
         */
        private Integer isGiveBirth;

        /**
         * 户口性质
         */
        private String residentCharacter;

        /**
         * 身高
         */
        private Integer height;

        /**
         * 血型
         */
        private String bloodType;

        /**
         * 英文名
         */
        private String englishName;

        /**
         * 民族
         */
        private Integer nationality;

        /**
         * 出生日期
         */
        @JsonFormat(pattern="yyyy-MM-dd")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private Date birthDate;

        /**
         * 政治面貌
         */
        private Integer politicalStatus;

        /**
         * 藉贯
         */
        private String birthplace;

        /**
         * 应聘岗位
         */
        private String applicationPosition;

        /**
         * 试用期限(月)
         */
        private Integer probationPeriod;

        /**
         * 入职日期
         */
        @JsonFormat(pattern="yyyy-MM-dd")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private Date hireDate;

        /**
         * 入职部门
         */
        private Integer orgId;

        /**
         * 入职岗位
         */
        private Integer postId;

        /**
         * 入职状态
         */
        private String employmentState;

        /**
         * 入职登记
         */
        private String employmentRegister;

        /**
         * 备注
         */
        private String description;

        /**
         * 数据来源
         */
        private String dataSource;

        /**
         * 企业ID
         */
        private Integer companyId;

        /**
         * 操作人ID
         */
        private Integer operatorId;

        /**
         * 创建时间
         */
        @JsonFormat(pattern="yyyy-MM-dd")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private Date createTime;

        /**
         * 更新时间
         */
        @JsonFormat(pattern="yyyy-MM-dd")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private Date updateTime;

        /**
         * 是否删除
         */
        private Short isDelete;

}

