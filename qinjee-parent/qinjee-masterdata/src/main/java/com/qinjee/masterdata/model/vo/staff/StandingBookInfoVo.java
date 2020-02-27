package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@Data
@ToString
@JsonInclude
public class StandingBookInfoVo implements Serializable {
    private StandingBookVo standingBookVo;
    private List<StandingBookFilterVo> listVo;

}
