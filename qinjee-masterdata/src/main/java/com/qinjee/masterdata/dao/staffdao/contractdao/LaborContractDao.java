package com.qinjee.masterdata.dao.staffdao.contractdao;

import com.qinjee.masterdata.model.entity.LaborContract;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaborContractDao {


    int deleteByPrimaryKey(Integer contractId);

    int insert(LaborContract record);

    int insertSelective(LaborContract record);

    LaborContract selectByPrimaryKey(Integer contractId);

    int updateByPrimaryKeySelective(LaborContract record);

    int updateByPrimaryKey(LaborContract record);

    List<Integer> selectNoLaborContract(List<Integer> arichiveIds);
}