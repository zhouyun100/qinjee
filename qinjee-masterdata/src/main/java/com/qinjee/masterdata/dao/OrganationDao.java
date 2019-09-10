package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.Organation;

public interface OrganationDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(Organation record);

    int insertSelective(Organation record);

    Organation selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(Organation record);

    int updateByPrimaryKey(Organation record);
}
