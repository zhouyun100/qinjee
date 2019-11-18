package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.ArchiveCareerTrack;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Repository
public interface ArchiveCareerTrackDao {
    /**
     * @return
     */
    List<ArchiveCareerTrack> findArchiveCareerTrackAll();
    int findArchiveCareerTrackByCondition (Map<String,Object> map);
    List<ArchiveCareerTrack> findArchiveCareerTrackByIds(List<Integer> list);
    int insertArchiveCareerTrack(Map<String,Object> map);
    int insertArchiveCareerTracks(List<Map<String,Object>> list);
    int updateArchiveCareerTrack (Map<String,Object> map);
    int updateArchiveCareerTracks (List<Map<String,Object>> list);
    List<ArchiveCareerTrack> selectCareerTrack(@Param("id") Integer id);

    int deleteCareerTrack(@Param("id") Integer id);
}
