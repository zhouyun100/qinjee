package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author Administrator
 */

public interface IStaffContractService {
    /**
     * 展示未签合同的人员
     * @param currentPage
     * @param pageSize
     * @param id
     * @return
     */
    ResponseResult<PageResult<UserArchive>> selectNoLaborContract(Integer currentPage, Integer pageSize, Integer id);

    /**
     * 删除合同
     * @param laborContractid
     * @return
     */
    ResponseResult deleteLaborContract(Integer laborContractid);

    /**
     * 新增单签合同
     * @param laborContract
     * @return
     */
    ResponseResult insertLaborContract(LaborContract laborContract);

    /**
     * 批量新签合同
     * @param list
     * @return
     */
    ResponseResult insertLaborContractBatch(List<LaborContract> list);

    /**
     * 更新合同，同时新增更新记录
     * @param laborContract
     * @param laborContractChange
     * @return
     */
    ResponseResult updatelaborContract(LaborContract laborContract, LaborContractChange laborContractChange);

    /**
     *查询一个合同的变更历史
     * @param id
     * @param currentPage
     * @param pageSize
     * @return
     */
    ResponseResult<PageResult<LaborContractChange>> selectLaborContractchange(Integer id, Integer currentPage, Integer pageSize);

    /**
     * 新增续签合同
     * @param laborContract
     * @return
     */
    ResponseResult insertReNewLaborContract(LaborContract laborContract);
}
