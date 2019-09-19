package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.Blacklist;

import java.util.List;

/**
 * @author Administrator
 */
public interface BlacklistDao {
    int deleteByPrimaryKey(Integer blacklistId);

    int insert(Blacklist record);

    int insertSelective(Blacklist record);

    Blacklist selectByPrimaryKey(Integer blacklistId);

    int updateByPrimaryKeySelective(Blacklist record);

    int updateByPrimaryKey(Blacklist record);

    Integer selectMaxId();

    List<Blacklist> selectByPage();
}
