package com.qinjee.masterdata.dao.staffdao.contractdao;

import com.qinjee.masterdata.model.entity.LaborContract;
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


    List<LaborContract> selectLabByorgId(@Param("orgIdList") List<Integer> orgIdList);

    List<Integer> selectConByArcId(@Param("list") List<Integer> list);

    List<LaborContract> selectContractByarcIdList(@Param("arcList") List<Integer> arcList);
    @MapKey("contract_id")
    Map< Integer, Map< String, Object>> selectExportConList(@Param("list") List< Integer> list, @Param("companyId") Integer companyId);

    void insertBatch(@Param("laborContractList") List< LaborContract> laborContractList);
}