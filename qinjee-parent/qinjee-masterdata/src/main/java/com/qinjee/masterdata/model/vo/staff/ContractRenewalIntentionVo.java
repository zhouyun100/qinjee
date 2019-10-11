package com.qinjee.masterdata.model.vo.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContractRenewalIntentionVo implements Serializable {
    /**
     * 续签意向ID
     */
    private Integer renewalIntentionId;

    /**
     * 档案ID
     */
    private Integer archiveId;


    /**
     * 续签意见
     */
    private String renewalOpinion;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
