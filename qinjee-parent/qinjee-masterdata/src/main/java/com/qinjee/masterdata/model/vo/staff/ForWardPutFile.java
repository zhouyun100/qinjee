package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@ToString
@JsonInclude
public class ForWardPutFile implements Serializable {
    private String string;
    private String key;
}
