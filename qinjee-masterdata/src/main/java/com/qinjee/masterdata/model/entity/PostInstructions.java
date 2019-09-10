package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_post_instructions
 * @author
 */
public class PostInstructions implements Serializable {
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

    public Integer getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(Integer instructionId) {
        this.instructionId = instructionId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getInstructionAttachmentUrl() {
        return instructionAttachmentUrl;
    }

    public void setInstructionAttachmentUrl(String instructionAttachmentUrl) {
        this.instructionAttachmentUrl = instructionAttachmentUrl;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    public byte[] getInstructionContent() {
        return instructionContent;
    }

    public void setInstructionContent(byte[] instructionContent) {
        this.instructionContent = instructionContent;
    }
}
