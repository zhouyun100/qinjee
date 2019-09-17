package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CheckType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckTypeDao {
    int deleteByPrimaryKey(String checkCode);

    int insert(CheckType record);

    int insertSelective(CheckType record);

    CheckType selectByPrimaryKey(String checkCode);

    int updateByPrimaryKeySelective(CheckType record);

    int updateByPrimaryKey(CheckType record);

    List<String> selectCheckName(String checkCode);
}
