package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.QuerySchemeSort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface QuerySchemeSortDao {
    int deleteByPrimaryKey(Integer querySchemeSortId);

    int insert(QuerySchemeSort record);

    int insertSelective(QuerySchemeSort record);

    QuerySchemeSort selectByPrimaryKey(Integer querySchemeSortId);

    int updateByPrimaryKeySelective(QuerySchemeSort record);

    int updateByPrimaryKey(QuerySchemeSort record);

    List<QuerySchemeSort> selectByQuerySchemeId(Integer id);

    void deleteBySchemeId(Integer querySchemeId);
}