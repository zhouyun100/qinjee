package com.qinjee.masterdata.dao.staffdao.staffstandingbookdao;

import com.qinjee.masterdata.model.entity.StandingBook;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 */
@Repository
public interface StandingBookDao {
    int deleteByPrimaryKey(Integer standingBookId);

    int insert(StandingBook record);

    int insertSelective(StandingBook record);

    StandingBook selectByPrimaryKey(Integer standingBookId);

    int updateByPrimaryKeySelective(StandingBook record);

    int updateByPrimaryKey(StandingBook record);

    void deleteStandingBook(Integer standingBookId);
}
