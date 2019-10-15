package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.QuerySchemeSort;
import org.apache.ibatis.annotations.Param;
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

    List<QuerySchemeSort> selectByQuerySchemeId(@Param("id") Integer id);

    void deleteBySchemeId(@Param("querySchemeId") Integer querySchemeId);

    List<Integer> selectSortId(@Param("schemeId") Integer schemeId);

    List<String> selectSortSort(@Param("schemeId") Integer schemeId);

    void deleteBySchemeIdList(@Param("list") List<Integer> list);

    void insertBatch(@Param("querySchemeSortlist") List<QuerySchemeSort> querySchemeSortlist);

    List<String> selectSortByIdList(List<Integer> integerList);
}
