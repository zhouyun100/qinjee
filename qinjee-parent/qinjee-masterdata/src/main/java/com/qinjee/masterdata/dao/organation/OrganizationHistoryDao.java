package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.OrganizationHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationHistoryDao {

    /**
     * 根据主键查找
     * @param orgId
     * @return
     */
    OrganizationHistory getOrganizationHistoryByPK(Integer orgId);

    /**
     * 根据主键移除
     * @param orgId
     * @return
     */
    Integer removeOrganizationHistoryByPK(Integer orgId);

    /**
     * 保存
     * @param orgHis
     * @return
     */
    Integer saveOrganizationHistory(OrganizationHistory orgHis);

    /**
     * 保存选择的字段
     * @param orgHis
     * @return
     */
    Integer saveOrganizationHistorySelectived(OrganizationHistory orgHis);

    /**
     * 更新已选择的字段
     * @param orgHis
     * @return
     */
    Integer updateOrganizationHistorySelectived(OrganizationHistory orgHis);

    /**
     * 更新所有字段
     * @param orgHis
     * @return
     */
    Integer updateOrganizationHistory(OrganizationHistory orgHis);

}
