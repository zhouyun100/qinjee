package com.qinjee.masterdata.dao.staffdao.contractdao;

import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.vo.staff.ContractFormVo;
import com.qinjee.masterdata.model.vo.staff.ContractWithArchiveVo;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.RelieveContractInfoVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    List<LaborContract> selectLabByorgId(@Param("orgIdList") List<Integer> orgIdList);

    List<Integer> selectConByArcId(@Param("list") List<Integer> list);

    List<LaborContract> selectContractByarcIdList(@Param("arcList") List<Integer> arcList);

    @MapKey("contract_id")
    Map<Integer, Map<String, Object>> selectExportConList(@Param("list") List<Integer> list, @Param("companyId") Integer companyId);

    void insertBatch(@Param("laborContractList") List<LaborContract> laborContractList);

    Integer selectByarcIdAndStatus(@Param("archiveId") Integer archiveId, @Param("renewagree") String renewagree);

    List<LaborContract> selectContractByArchiveId(@Param("archiveId") Integer archiveId);

    List<ContractFormVo> selectContractForm(@Param("list") List<Integer> list, @Param("companyId") Integer companyId);

    List<ContractWithArchiveVo> selectHasPowerContract(@Param("orgIdList") List<Integer> orgIdList, @Param("status") List<String> status, @Param("companyId") Integer companyId,
                                                       @Param("whereSql") String whereSql, @Param("orderSql") String orderSql, @Param("mark") Short mark);


    /**
     * phs
     *
     * @param orgIds
     * @param companyId
     * @return
     */
    List<ContractWithArchiveVo> selectAboutToExpireContracts(List<Integer> orgIds, Integer companyId);

    /**
     * phs
     *
     * @param archiveIds
     * @return
     */
    List<RelieveContractInfoVo> listRelieveContract(@Param("archiveIds") List<Integer> archiveIds, Date now);
}