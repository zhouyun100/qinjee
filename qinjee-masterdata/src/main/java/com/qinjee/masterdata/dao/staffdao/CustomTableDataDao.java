package com.qinjee.masterdata.dao.staffdao;

import com.qinjee.masterdata.model.entity.CustomTableData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomTableDataDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomTableData record);

    int insertSelective(CustomTableData record);

    CustomTableData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomTableData record);

    int updateByPrimaryKey(CustomTableData record);

    List<Integer> selectCustomTableId(Integer customTableId);
}
