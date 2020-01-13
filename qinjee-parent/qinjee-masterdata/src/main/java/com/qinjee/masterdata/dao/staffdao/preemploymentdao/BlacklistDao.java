package com.qinjee.masterdata.dao.staffdao.preemploymentdao;

import com.qinjee.masterdata.model.entity.Blacklist;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BlacklistDao {
    int deleteByPrimaryKey(Integer blacklistId);

    int insert(Blacklist record);

    int insertSelective(Blacklist record);

    Blacklist selectByPrimaryKey(Integer blacklistId);

    int updateByPrimaryKeySelective(Blacklist record);

    int updateByPrimaryKey(Blacklist record);

    Integer selectMaxId();

    void deleteBlackList(List<Integer> list);

    List<Blacklist> selectByPage(@Param("companyId") Integer companyId);

    List<Blacklist> selectByPhone(@Param("phoneList") List<String> phoneList);

    @MapKey("blacklist_id")
    Map< Integer, Map< String, Object>> selectExportBlackList(@Param("list") List< Integer> list, @Param("companyId") Integer companyId);

    void insertBatch(@Param("blacklistList") List< Blacklist> blacklistList);

    Blacklist selectByIdNumberAndPhone(@Param("idNumber") String idNumber, @Param("phone") String phone, @Param("companyId") Integer companyId);
}