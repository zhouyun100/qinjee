package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.LaborContractChangeVo;
import com.qinjee.masterdata.model.vo.staff.LaborContractVo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author Administrator
 */

public interface IStaffContractService {

    /**展示未签合同的人员
     * @param archiveId
     * @param currentPage
     * @param pageSize
     * @param
     * @return
     */
    ResponseResult<PageResult<UserArchive>> selectNoLaborContract(Integer  archiveId, Integer currentPage, Integer pageSize);
    /**
     * 删除合同
     * @param laborContractid
     * @return
     */
    ResponseResult deleteLaborContract(Integer laborContractid);

    /**
     * 新增单签合同
     * @param laborContractVo
     * @return
     */
    ResponseResult insertLaborContract(LaborContractVo laborContractVo, Integer id,Integer archiveId);

    /**批量新签合同
     * @param laborContractVo
     * @param list
     * @param archiveId
     * @return
     */
    ResponseResult insertLaborContractBatch(LaborContractVo laborContractVo,List<Integer> list,Integer archiveId);

    /**更新合同，同时新增更新记录
     * @param laborContract
     * @param laborContractChangeVo
     * @param id
     * @param archiveId
     * @return
     */
    ResponseResult updatelaborContract(LaborContract laborContract, LaborContractChangeVo laborContractChangeVo,
                                       Integer id, Integer archiveId);

    /**
     *查询一个合同的变更历史
     * @param id
     * @param
     * @param
     * @return
     */
    ResponseResult<List<LaborContractChange>> selectLaborContractchange(Integer id);


    /**新增续签合同
     * @param laborContractVo
     * @param id
     * @param laborContractChangeVo
     * @param archiveId
     * @return
     */
    ResponseResult insertReNewLaborContract(LaborContractVo laborContractVo, Integer id, LaborContractChangeVo laborContractChangeVo, Integer archiveId);
    /**
     * 新增续签意向表
     * @param contractRenewalIntention
     * @return
     */
    ResponseResult insertContractRenewalIntention(ContractRenewalIntention contractRenewalIntention);

    /**
     * 获得所有未签合同的id
     */
    ResponseResult selectLaborContractId(Integer archiveId);

    /**
     * 保存合同
     * @param laborContractVo
     * @param id
     * @param archiveId
     * @return
     */
    ResponseResult SaveLaborContract(LaborContractVo laborContractVo, Integer id, Integer archiveId);

    /**
     * 批量续签合同
     * @param laborContractVo
     * @param list
     * @param laborContractChangeVo
     * @param archiveId
     * @return
     */
    ResponseResult insertReNewLaborContractBatch(LaborContractVo laborContractVo, List<Integer> list, LaborContractChangeVo laborContractChangeVo, Integer archiveId);

    /**
     * 展示已签合同人员
     * @param archiveId
     * @param currentPage
     * @param pageSize
     * @return
     */
    ResponseResult<PageResult<UserArchive>> selectLaborContractserUser(Integer archiveId, Integer currentPage, Integer pageSize);

    /**
     * 终止合同
     * @param laborContractChangeVo
     * @param id
     * @param archiveId
     * @return
     */
    ResponseResult endlaborContract(LaborContractChangeVo laborContractChangeVo, Integer id, Integer archiveId);

    /**
     * 批量终止合同
     * @param laborContractChangeVo
     * @param list
     * @param archiveId
     * @return
     */
    ResponseResult endlaborContractBatch(LaborContractChangeVo laborContractChangeVo, List<Integer> list, Integer archiveId);

    /**
     * 解除合同
     * @param laborContractChangeVo
     * @param id
     * @param archiveId
     * @return
     */
    ResponseResult looselaborContract(LaborContractChangeVo laborContractChangeVo, Integer id, Integer archiveId);

    /**
     * 批量解除合同
     * @param laborContractChangeVo
     * @param list
     * @param archiveId
     * @return
     */
    ResponseResult looselaborContractBatch(LaborContractChangeVo laborContractChangeVo, List<Integer> list, Integer archiveId);

    /**
     * 发送续签意向
     * @param id
     * @return
     */
    ResponseResult insertLaborContractIntention(Integer id);

    /**
     * 展示我的续签意向表
     * @param id
     * @return
     */
    ResponseResult selectContractRenewalIntention(Integer id);

    /**
     * 同意续签
     * @param contractRenewalIntention
     * @return
     */
    ResponseResult agreeRenew(ContractRenewalIntention contractRenewalIntention);

    /**
     * 不同意续签
     * @param contractRenewalIntention
     * @return
     */
    ResponseResult rejectRenew(ContractRenewalIntention contractRenewalIntention);
}
