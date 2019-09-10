package entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class CustomField implements Serializable {
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
