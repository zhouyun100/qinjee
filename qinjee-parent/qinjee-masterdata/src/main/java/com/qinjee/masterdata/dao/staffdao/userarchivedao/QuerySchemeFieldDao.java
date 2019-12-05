package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.QuerySchemeField;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface QuerySchemeFieldDao {
    int deleteByPrimaryKey(Integer querySchemeFieldId);

    int insert(QuerySchemeField record);

    int insertSelective(QuerySchemeField record);

    QuerySchemeField selectByPrimaryKey(Integer querySchemeFieldId);

    int updateByPrimaryKeySelective(QuerySchemeField record);

    int updateByPrimaryKey(QuerySchemeField record);

    List<QuerySchemeField> selectByQuerySchemeId(@Param("id") Integer id);

    void deleteBySchemeId(Integer querySchemeId);

    List<Integer> selectFieldId(@Param("schemeId") Integer schemeId);


    void deleteBySchemeIdList(@Param("list") List<Integer> list);

    void insertBatch(@Param("querySchemeFieldlist") List<QuerySchemeField> querySchemeFieldlist);

    Integer selectIdBySort(@Param("sort") Integer integer);

    List<Integer> selectFieldIdWithSort(@Param("schemeId") Integer schemeId);
}
