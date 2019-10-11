package com.qinjee.masterdata.model.vo.staff;

import com.qinjee.masterdata.model.entity.StandingBook;
import com.qinjee.masterdata.model.entity.StandingBookFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StandingBookInfo implements Serializable {
    private StandingBookVo standingBookVo;
    private List<StandingBookFilterVo> listVo;
    private StandingBook standingBook;
    private List<StandingBookFilter> list;

}
