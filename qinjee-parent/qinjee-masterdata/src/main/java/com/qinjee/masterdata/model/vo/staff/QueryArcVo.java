package com.qinjee.masterdata.model.vo.staff;

import com.qinjee.masterdata.model.entity.QueryScheme;
import com.qinjee.masterdata.model.entity.QuerySchemeField;
import com.qinjee.masterdata.model.entity.QuerySchemeSort;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class QueryArcVo implements Serializable {
    private List<QuerySchemeField> querySchemeFieldlist;
    private List<QuerySchemeSort> querySchemeSortlist;
    private QueryScheme queryScheme;
}
