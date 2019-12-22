package com.qinjee.masterdata.dao.staffdao.staffstandingbookdao;

import com.qinjee.masterdata.model.entity.StandingBookFilter;
import com.qinjee.masterdata.model.vo.staff.StandingBookFilterVo;
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

    int updateByPrimaryKeySelective( StandingBookFilter record);

    int updateByPrimaryKey(StandingBookFilter record);

    void deleteStandingBookFilter(@Param("standingBookId") Integer standingBookId);

    List< StandingBookFilterVo > selectByStandingBookId(@Param("id") Integer id);


    String selectSqlById(@Param("stangdingBookId") Integer stangdingBookId);

    String selectLinkHandleById(@Param("id") Integer id);
}
