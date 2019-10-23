package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@Data
@ToString
public class StandingBookInfoVo implements Serializable {
    private StandingBookVo standingBookVo;
    private List<StandingBookFilterVo> listVo;

}
