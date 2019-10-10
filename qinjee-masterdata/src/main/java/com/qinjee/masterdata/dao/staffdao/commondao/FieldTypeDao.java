package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.FieldType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldTypeDao {
    int deleteByPrimaryKey(String typeCode);

    int insert(FieldType record);

    int insertSelective(FieldType record);

    FieldType selectByPrimaryKey(String typeCode);

    int updateByPrimaryKeySelective(FieldType record);

    int updateByPrimaryKey(FieldType record);

    String selectFieldCode(@Param("s") String s);
}
