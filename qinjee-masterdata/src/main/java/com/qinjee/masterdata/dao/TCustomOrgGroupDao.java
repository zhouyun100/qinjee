package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TCustomOrgGroup;

public interface TCustomOrgGroupDao {
    int deleteByPrimaryKey(Integer groupId);

    int insert(TCustomOrgGroup record);

    int insertSelective(TCustomOrgGroup record);

    TCustomOrgGroup selectByPrimaryKey(Integer groupId);

    int updateByPrimaryKeySelective(TCustomOrgGroup record);

    int updateByPrimaryKey(TCustomOrgGroup record);
}