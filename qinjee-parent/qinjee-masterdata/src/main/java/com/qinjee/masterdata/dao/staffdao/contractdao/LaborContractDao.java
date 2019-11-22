package com.qinjee.masterdata.dao.staffdao.contractdao;

import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.UserArchive;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LaborContractDao {


    int deleteByPrimaryKey(Integer contractId);

    int insert(LaborContract record);

    int insertSelective(LaborContract record);

    LaborContract selectByPrimaryKey(@Param("contractId") Integer contractId);

    int updateByPrimaryKeySelective(LaborContract record);

    int updateByPrimaryKey(LaborContract record);

    List<Integer> seleltByArcId(@Param("arcList") List<Integer> arcList);

    List<Integer> seleltByArcIdIn(@Param("arcList") List<Integer> arcList);


    List<LaborContract> selectLabByorgId(@Param("orgId") Integer orgId);


    List<UserArchive> selectArcByNotCon(@Param("conList") List<Integer> conList);

    List<Integer> selectByorgId(@Param("orgId") Integer orgId);

    List<Integer> selectConByArcId(@Param("list") List<Integer> list);

    List<LaborContract> selectContractByarcIdList(@Param("arcList") List<Integer> arcList);
    @MapKey("id")
    Map< Integer, Map< String, Object>> selectExportConList(@Param("list") List< Integer> list, @Param("companyId") Integer companyId);
}