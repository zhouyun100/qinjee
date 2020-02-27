package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomArchiveFieldCheck;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomArchiveFieldCheckDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomArchiveFieldCheck record);

    int insertSelective(CustomArchiveFieldCheck record);

    CustomArchiveFieldCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomArchiveFieldCheck record);

    int updateByPrimaryKey(CustomArchiveFieldCheck record);


    List<String> selectCheckName(Integer fieldId);
}
