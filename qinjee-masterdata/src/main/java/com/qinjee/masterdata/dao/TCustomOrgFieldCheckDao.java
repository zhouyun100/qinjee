package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TCustomOrgFieldCheck;

public interface TCustomOrgFieldCheckDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TCustomOrgFieldCheck record);

    int insertSelective(TCustomOrgFieldCheck record);

    TCustomOrgFieldCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TCustomOrgFieldCheck record);

    int updateByPrimaryKey(TCustomOrgFieldCheck record);
}