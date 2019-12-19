package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;

import java.text.ParseException;
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
    PageResult< UserArchiveVo > selectNoLaborContract(Integer orgId, Integer currentPage, Integer pageSize);
    /**
     * 删除合同
     * @param laborContractid
     * @return
     */
    void deleteLaborContract(Integer laborContractid);

    /**
     * 新增单签合同
     * @param contractVo
     * @param userSession
     * @return
     */
    void insertLaborContract(ContractVo contractVo, UserSession userSession) throws ParseException;

    /**批量新签合同
     * @param contractVo
     * @param userSession
     * @return
     */
   void insertLaborContractBatch(ContractVo contractVo, UserSession userSession) throws Exception;

    /**更新合同，同时新增更新记录
     * @param userSession
     * @return
     */
    void updatelaborContract(ContractVo contractVo, UserSession userSession);

    /**
     *查询一个合同的变更历史
     * @param id
     * @param
     * @param
     * @return
     */
    List<LaborContractChange> selectLaborContractchange(Integer id);


    /**新增续签合同
     * @param contractVo
     * @param userSession
     * @return
     */
    void insertReNewLaborContract(ContractVo contractVo, UserSession userSession);
    /**
     * 新增续签意向表
     * @param contractRenewalIntention
     * @return
     */
    void updateContractRenewalIntention(ContractRenewalIntention contractRenewalIntention);

    /**
     * 保存合同
     * @param contractVo
     * @param userSession
     * @return
     */
    void saveLaborContract(ContractVo contractVo, UserSession userSession) throws ParseException;

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
     * @param orgIdList
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageResult<UserArchiveVo> selectLaborContractserUser(List<Integer> orgIdList, Integer currentPage,
                                                       Integer pageSize, String isEnable,
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
     * @param contractVo
     * @param userSession
     * @return
     */
    void looselaborContractBatch(ContractVo contractVo,
                                 UserSession userSession);

    /**
     * 发送续签意向
     * @param list
     * @return
     */
    void insertLaborContractIntention(List<Integer> list, UserSession userSession);

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
     * @param orgId
     * @return
     */
    PageResult<UserArchiveVo> selectArcDeadLine(Integer orgId,Integer pagesize,Integer currentPage) ;

    Integer getSignNumber(Integer archiveId);

    List< LaborContract > selectContractByArchiveId(Integer archiveId);

    PageResult< ContractFormVo > createContractForm(List< Integer> list,Integer pageSize,Integer currentPage,UserSession userSession);

   PageResult<ContractRenewalIntention> selectContractRenewalIntentionByOrg(List<Integer> orgId,Integer pageSIze,Integer currentPage);
}
