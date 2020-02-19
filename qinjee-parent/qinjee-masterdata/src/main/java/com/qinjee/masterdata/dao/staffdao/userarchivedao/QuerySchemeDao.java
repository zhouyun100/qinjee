package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.QueryScheme;
import org.apache.ibatis.annotations.Param;
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

    void deleteQuerySchemeList(@Param("list") List < Integer > list);

    List < QueryScheme > selectByArchiveId(@Param("archiveId") Integer archiveId);

    List< QueryScheme> selectQueryByArchiveId(@Param("archiveId") Integer archiveId);

    Integer selectDefaultQuerySchemaIdByArchiveId(@Param("archiveId") Integer archiveId);
}


