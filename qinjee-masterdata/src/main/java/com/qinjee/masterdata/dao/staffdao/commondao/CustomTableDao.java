package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomTable;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author Administrator
 */
@Repository
public interface CustomTableDao {
    int deleteByPrimaryKey(Integer tableId);

    int insert(CustomTable record);

    int insertSelective(CustomTable record);

    CustomTable selectByPrimaryKey(Integer tableId);

    int updateByPrimaryKeySelective(@Param("record") CustomTable record);

    int updateByPrimaryKey(@Param("record") CustomTable record);
    int selectMaxPrimaryKey();
    int deleteCustomTable(@Param("tableId") Integer tableId);
    List<CustomTable> selectByPage();
}
