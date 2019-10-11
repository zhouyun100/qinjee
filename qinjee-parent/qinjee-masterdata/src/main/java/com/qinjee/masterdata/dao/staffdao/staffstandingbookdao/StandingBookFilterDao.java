package com.qinjee.masterdata.dao.staffdao.staffstandingbookdao;

import com.qinjee.masterdata.model.entity.StandingBookFilter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface StandingBookFilterDao {
    int deleteByPrimaryKey(Integer filterId);

    int insert(StandingBookFilter record);

    int insertSelective(StandingBookFilter record);

    StandingBookFilter selectByPrimaryKey(Integer filterId);

    int updateByPrimaryKeySelective(StandingBookFilter record);

    int updateByPrimaryKey(StandingBookFilter record);

    void deleteStandingBookFilter(Integer standingBookId);

    List<StandingBookFilter> selectByStandingBookId(@Param("id") Integer id);

    String selectSqlById(@Param("stangdingBookId") Integer stangdingBookId);
}
