package com.qinjee.masterdata.dao.staffdao.contractdao;

import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import org.springframework.stereotype.Repository;

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

    ContractRenewalIntention selectByArchiveId(Integer id);
}