package com.qinjee.masterdata.dao.staffdao.contractdao;

import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import com.qinjee.masterdata.model.vo.staff.RenewIntionAboutUser;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.RenewalContractInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface ContractRenewalIntentionDao {
    int deleteByPrimaryKey(Integer renewalIntentionId);

    int insert(ContractRenewalIntention record);

    int insertSelective(ContractRenewalIntention record);

    ContractRenewalIntention selectByPrimaryKey(Integer renewalIntentionId);

    int updateByPrimaryKeySelective(ContractRenewalIntention record);

    int updateByPrimaryKey(ContractRenewalIntention record);

    List<ContractRenewalIntention> selectByArchiveId(@Param("id") Integer id);

    void insertBatch(@Param("contractRenewalIntentions") List<ContractRenewalIntention> contractRenewalIntentions);

    List<ContractRenewalIntention> selectByorgId(@Param("list") List<Integer> orgId);

    Integer selectCountRenew(@Param("archiveId") Integer archiveId);

    List<RenewIntionAboutUser> getRenewalContract(@Param("archiveId") Integer archiveId);

    List<RenewalContractInfoVo> listContractRenewalInfo(@Param("archiveIds") List<Integer> archiveIds, Date now);

}