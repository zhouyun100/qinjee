package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.StandingBook;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@Data
@ToString
@JsonInclude
public class StandingBookInfo implements Serializable {
    private StandingBook standingBook;
    private List<StandingBookFilterVo> list;
}
