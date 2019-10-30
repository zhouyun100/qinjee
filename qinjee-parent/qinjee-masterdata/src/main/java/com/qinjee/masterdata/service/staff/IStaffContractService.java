package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.LaborContractChangeVo;
import com.qinjee.masterdata.model.vo.staff.LaborContractVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;

import java.util.List;

/**
 * @author Administrator
 */

public interface IStaffContractService {

    /**展示未签合同的人员

     * @param orgId
     * @param currentPage
     * @param pageSize
     * @param
     * @return
     */
    PageResult<UserArchive> selectNoLaborContract(Integer orgId, Integer currentPage, Integer pageSize);
    /**
     * 删除合同
     * @param laborContractid
     * @return
     */
    void deleteLaborContract(Integer laborContractid);

    /**
     * 新增单签合同
     * @param laborContractVo
     * @param id
     * @param userSession
     * @return
     */
    void insertLaborContract(LaborContractVo laborContractVo, Integer id, UserSession userSession);

    /**批量新签合同
     * @param laborContractVo
     * @param list
     * @param userSession
     * @return
     */
   void insertLaborContractBatch(LaborContractVo laborContractVo, List<Integer> list, UserSession userSession) throws Exception;

    /**更新合同，同时新增更新记录
     * @param laborContract
     * @param laborContractChangeVo
     * @param id
     * @param userSession
     * @return
     */
    void updatelaborContract(LaborContract laborContract, LaborContractChangeVo laborContractChangeVo,
                             Integer id, UserSession userSession);

    /**
     *查询一个合同的变更历史
     * @param id
     * @param
     * @param
     * @return
     */
    List<LaborContractChange> selectLaborContractchange(Integer id);


    /**新增续签合同
     * @param laborContractVo
     * @param id
     * @param laborContractChangeVo
     * @param userSession
     * @return
     */
    void insertReNewLaborContract(LaborContractVo laborContractVo, Integer id,
                                  LaborContractChangeVo laborContractChangeVo, UserSession userSession);
    /**
     * 新增续签意向表
     * @param contractRenewalIntention
     * @return
     */
    void updateContractRenewalIntention(ContractRenewalIntention contractRenewalIntention);

    /**
     * 保存合同
     * @param laborContractVo
     * @param id
     * @param userSession
     * @return
     */
    void saveLaborContract(LaborContractVo laborContractVo, Integer id, UserSession userSession);

    /**
     * 批量续签合同
     * @param laborContractVo
     * @param list
     * @param laborContractChangeVo
     * @param userSession
     * @return
     */
    void insertReNewLaborContractBatch(LaborContractVo laborContractVo,
                                       List<Integer> list,
                                       LaborContractChangeVo laborContractChangeVo,
                                       UserSession userSession);

    /**
     * 展示已签合同人员
     * @param orgId
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageResult<UserArchive> selectLaborContractserUser(Integer orgId, Integer currentPage,
                                                       Integer pageSize, Boolean isEnable,
                                                       List<String> status) throws Exception;

    /**
     * 终止合同
     * @param laborContractChangeVo
     * @param id
     * @param userSession
     * @return
     */
    void endlaborContract(LaborContractChangeVo laborContractChangeVo, Integer id, UserSession userSession);

    /**
     * 批量终止合同
     * @param laborContractChangeVo
     * @param list
     * @param userSession
     * @return
     */
    void endlaborContractBatch(LaborContractChangeVo laborContractChangeVo, List<Integer> list, UserSession userSession);

    /**
     * 解除合同
     * @param laborContractChangeVo
     * @param id
     * @param userSession
     * @return
     */
    void looselaborContract(LaborContractChangeVo laborContractChangeVo, Integer id, UserSession userSession);

    /**
     * 批量解除合同
     * @param laborContractChangeVo
     * @param list
     * @param userSession
     * @return
     */
    void looselaborContractBatch(LaborContractChangeVo laborContractChangeVo, List<Integer> list,
                                 UserSession userSession);

    /**
     * 发送续签意向
     * @param id
     * @return
     */
    void insertLaborContractIntention(Integer id, UserSession userSession);

    /**
     * 展示我的续签意向表
     * @param id
     * @return
     */
    List<ContractRenewalIntention> selectContractRenewalIntention(Integer id);

    /**
     * 同意续签
     * @param xuqianId
     * @return
     */
    void agreeRenew(Integer xuqianId);

    /**
     * 不同意续签
     * @param xuqianId
     * @return
     */
    void rejectRenew( Integer xuqianId);
    /**
     * 查询在职员工的人数
     */
    Integer selectArcNumberIn(Integer id);

    /**
     * 查询机构下合同即将到期的员工
     * @param id
     * @return
     */
    List<UserArchive> selectArcDeadLine(Integer id);
}
