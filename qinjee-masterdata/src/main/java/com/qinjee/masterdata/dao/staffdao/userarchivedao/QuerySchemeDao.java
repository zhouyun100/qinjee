package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.QueryScheme;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface QuerySchemeDao {
    int deleteByPrimaryKey(Integer querySchemeId);

    int insert(QueryScheme record);

    int insertSelective(QueryScheme record);

    QueryScheme selectByPrimaryKey(Integer querySchemeId);

    int updateByPrimaryKeySelective(QueryScheme record);

    int updateByPrimaryKey(QueryScheme record);

    Integer selectMaxPrimaryKey();

    void deleteQueryScheme(Integer integer);

    void deleteQuerySchemeList(List<Integer> list);
}
