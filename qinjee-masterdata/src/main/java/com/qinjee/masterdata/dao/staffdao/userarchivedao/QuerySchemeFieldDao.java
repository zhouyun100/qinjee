package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.QuerySchemeField;
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

    List<QuerySchemeField> selectByQuerySchemeId(Integer id);

    void deleteBySchemeId(Integer querySchemeId);
}
