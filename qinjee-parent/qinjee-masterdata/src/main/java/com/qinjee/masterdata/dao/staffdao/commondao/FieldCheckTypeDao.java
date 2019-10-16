package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.FieldCheckType;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldCheckTypeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FieldCheckType record);

    int insertSelective(FieldCheckType record);

    FieldCheckType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FieldCheckType record);

    int updateByPrimaryKey(FieldCheckType record);

    String selectCheckCode(Integer fieldId);
}