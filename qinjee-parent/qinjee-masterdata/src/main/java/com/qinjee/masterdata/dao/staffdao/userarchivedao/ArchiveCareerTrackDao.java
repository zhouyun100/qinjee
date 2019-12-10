package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.ArchiveCareerTrack;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ArchiveCareerTrackDao {
    List< ArchiveCareerTrack> selectCareerTrack(@Param("id") Integer id);

    void updateArchiveCareerTrack(Map< String, Object> map);

    void insertArchiveCareerTrack(Map< String, Object> map);

    void deleteCareerTrack(@Param("id") Integer id);
}
