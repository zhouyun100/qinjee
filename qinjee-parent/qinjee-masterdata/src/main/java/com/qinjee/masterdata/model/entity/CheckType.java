package com.qinjee.masterdata.model.entity;

import java.io.Serializable;

/**
 * t_check_type
 * @author
 */
public class CheckType implements Serializable {
    /**
     * 验证code
     */
    private String checkCode;

    /**
     * 验证名称
     */
    private String checkName;

    private static final long serialVersionUID = 1L;

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }
}
