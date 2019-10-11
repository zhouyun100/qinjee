package com.qinjee.masterdata.model.vo.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ForWardPutFile implements Serializable {
    private String string;
    private String key;
}
