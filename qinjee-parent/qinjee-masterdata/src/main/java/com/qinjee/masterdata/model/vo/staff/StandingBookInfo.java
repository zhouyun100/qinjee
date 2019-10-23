package com.qinjee.masterdata.model.vo.staff;

import com.qinjee.masterdata.model.entity.StandingBook;
import com.qinjee.masterdata.model.entity.StandingBookFilter;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@Data
@ToString
public class StandingBookInfo implements Serializable {
    private StandingBook standingBook;
    private List<StandingBookFilter> list;
}
