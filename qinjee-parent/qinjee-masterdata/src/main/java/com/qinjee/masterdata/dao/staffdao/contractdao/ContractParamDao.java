package com.qinjee.masterdata.dao.staffdao.contractdao;

import com.qinjee.masterdata.model.entity.ContractParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Repository
public interface ContractParamDao {
    /**
     * @param contractParam
     * @return
     */
    int insertSelective(ContractParam contractParam);
    List<ContractParam> findContractParamAll();
    List<ContractParam> findContractParamByCondition(@Param("id") Integer id);
    List<ContractParam> findContractParamByCompanyId(@Param("id") Integer id);
    List<ContractParam> findContractParamByIds(@Param("list") List<Integer> list);
    int insertContractParam(@Param("map") Map<String,Object> map);
    int insertContractParams(@Param("list") List<ContractParam> list);
    int updateContractParam(@Param("map") Map<String,Object> map);
    int updateContractParams(@Param("list") List< ContractParam> list);
    int updateContractSelective(ContractParam record);

    /**
     * @param id
     * @return
     */
    int deleteContractParamById(@Param("id") Integer id);

    /**
     * @param list
     * @return
     */
    int deleteContractParamByIds(@Param("list")List<Integer> list);


    List< Integer> selectRuleIdByCompanyId(@Param("companyId") Integer companyId);


}
