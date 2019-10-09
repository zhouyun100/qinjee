package com.qinjee.masterdata.model.vo.staff;

import com.qinjee.masterdata.model.entity.QuerySchemeField;
import com.qinjee.masterdata.model.entity.QuerySchemeSort;
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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuerySchemeList implements Serializable {
    private List<QuerySchemeField> querySchemeFieldList;
    private List<QuerySchemeSort>  querySchemeSortList;

}
