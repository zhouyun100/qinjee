package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.QuerySchemeField;
import com.qinjee.masterdata.model.entity.QuerySchemeSort;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ToString
@JsonInclude
public class QuerySchemeList implements Serializable {
    private Integer querySchemeId;
    private String querySchemeName;
    private List<QuerySchemeField> querySchemeFieldList;
    private List<QuerySchemeSort>  querySchemeSortList;
}
