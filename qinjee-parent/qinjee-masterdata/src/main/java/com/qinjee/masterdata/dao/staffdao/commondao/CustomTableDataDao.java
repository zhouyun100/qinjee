package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomTableData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface CustomTableDataDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomTableData record);

    int insertSelective(CustomTableData record);

    CustomTableData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomTableData record);

    int updateByPrimaryKey(@Param("record") CustomTableData record);

    List<Integer> selectCustomTableId(@Param("customTableId") Integer customTableId);

}