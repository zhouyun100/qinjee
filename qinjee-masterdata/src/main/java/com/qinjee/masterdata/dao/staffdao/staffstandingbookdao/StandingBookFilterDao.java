package com.qinjee.masterdata.dao.staffdao.staffstandingbookdao;

import com.qinjee.masterdata.model.entity.StandingBookFilter;

/**
 * @author Administrator
 */
public interface StandingBookFilterDao {
    int deleteByPrimaryKey(Integer filterId);

    int insert(StandingBookFilter record);

    int insertSelective(StandingBookFilter record);

    StandingBookFilter selectByPrimaryKey(Integer filterId);

    int updateByPrimaryKeySelective(StandingBookFilter record);

    int updateByPrimaryKey(StandingBookFilter record);

    void deleteStandingBookFilter(Integer standingBookId);
}
