package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@ToString
public class ForWardPutFile implements Serializable {
    private String string;
    private String key;
}
