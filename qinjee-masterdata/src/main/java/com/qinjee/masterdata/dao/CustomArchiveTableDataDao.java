package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomArchiveTableData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomArchiveTableDataDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomArchiveTableData record);

    int insertSelective(CustomArchiveTableData record);

    CustomArchiveTableData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomArchiveTableData record);

    int updateByPrimaryKeyWithBLOBs(CustomArchiveTableData record);

    int updateByPrimaryKey(CustomArchiveTableData record);

    List<Integer> selectCustomArchiveTableId(Integer customArchiveTableId);
}
