package com.qinjee.masterdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 岗位说明书表
 * @author
 */
@Data
@NoArgsConstructor
public class TPostInstructions implements Serializable {
    /**
     * ID
     */
    private Integer instructionId;

    /**
     * 岗位ID
     */
    private Integer postId;

    /**
     * 说明书附件地址
     */
    private String instructionAttachmentUrl;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    /**
     * 说明书内容
     */
    private byte[] instructionContent;

    private static final long serialVersionUID = 1L;

}
