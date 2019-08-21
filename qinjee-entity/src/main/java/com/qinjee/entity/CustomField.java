package com.qinjee.entity;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class CustomField {
    private Integer fieldId;
    private String fieldName;
    private String fieldType;
    private String validType;
    private Integer codeId;
    private String  codeName;
    private String defaultValue;
    private Integer valLength;
    private Integer valPrecision;
    private Integer tableId;
    private Integer isSystemDefine;
    private Integer sort;
    private Integer groupId;
    private Date createTime;
    private Integer creatorId;
    private Date operatorTime;
    private Integer isDelete;
}
