package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CompanyCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyCodeDao {
    int deleteByPrimaryKey(Integer codeId);

    int insert(CompanyCode record);

    int insertSelective(CompanyCode record);

    CompanyCode selectByPrimaryKey(Integer codeId);

    int updateByPrimaryKeySelective(CompanyCode record);

    int updateByPrimaryKey(CompanyCode record);

    List<String> selectValue(@Param("id") Integer id);
}
