package com.qinjee.masterdata.dao.staffdao.staffstandingbookdao;

import com.qinjee.masterdata.model.entity.StandingBook;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    void deleteStandingBook(@Param("standingBookId") Integer standingBookId);

    List<StandingBook> selectByAchiveId(@Param("archiveId") Integer archiveId);

    List<StandingBook> selectShare(@Param("archiveId") Integer archiveId, @Param("companyId") Integer companyId);

    void updateStandingBook(@Param("standingBookId") Integer standingBookId, @Param("name") String name, @Param("isShare") Short isShare);
}
