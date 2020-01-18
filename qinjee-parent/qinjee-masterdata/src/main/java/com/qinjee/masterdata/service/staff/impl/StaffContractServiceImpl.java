package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinjee.masterdata.dao.staffdao.contractdao.ContractRenewalIntentionDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractChangeDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.service.staff.IStaffContractService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class StaffContractServiceImpl implements IStaffContractService {
    private static final Logger logger = LoggerFactory.getLogger ( StaffContractServiceImpl.class );
    private static final String NEWMARK = "新签";
    private static final String NOTMARK = "未签";
    private static final String RENEWMARK = "续签";
    private static final String CHANGEMARK = "更改";
    private static final String ENDEMARK = "终止";
    private static final String LOOSEMARK = "解除";
    private static final String COMMONCHANGE = "普通更改";
    private static final String RENEWCHANGE = "续签更改";
    private static final String ENDCHANGE = "终止更改";
    private static final String LOOSECHANGE = "解除更改";
    private static final String RENEWAGREE = "同意续签";
    private static final String RENEWREJECT = "不同意续签";
    private static final String NOTCONFIRM = "待确认";
    private static final String CONFIRM = "已确认";
    @Autowired
    private LaborContractDao laborContractDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private LaborContractChangeDao laborContractChangeDao;
    @Autowired
    private ContractRenewalIntentionDao contractRenewalIntentionDao;

    /**
     * 展示未签合同的人员信息
     *
     * @param orgId
     * @param pageSize
     * @return
     */
    @Override
    public PageResult < UserArchiveVo > selectNoLaborContract(List < Integer > orgId, Integer currentPage, Integer pageSize) {
        PageHelper.startPage ( currentPage, pageSize );
        //根据合同id找到没有合同的档案
        List < UserArchiveVo > arcList = userArchiveDao.selectArcByNotCon ( orgId );
        return new PageResult <> ( arcList );
    }

    @Override
    public PageResult < UserArchiveVo > selectNoLaborContract(Integer companyId, Integer currentPage, Integer pageSize) {
        return null;
    }

    /**
     * 展示全量未签合同人员
     *
     * @param orgId
     * @return
     */
    public List < Integer > selectNoLaborContractAll(List < Integer > orgId) {
        List < UserArchiveVo > userArchiveVos = userArchiveDao.selectArcByNotCon ( orgId );
        List < Integer > integers = new ArrayList <> ();
        for (UserArchiveVo userArchiveVo : userArchiveVos) {
            integers.add ( userArchiveVo.getArchiveId () );
        }
        return integers;
    }

    @Override
    public List < LaborContract > selectContractByArchiveId(Integer archiveId) {
        return laborContractDao.selectContractByArchiveId ( archiveId );
    }

    @Override
    public PageResult < ContractFormVo > createContractForm(List < Integer > list, Integer pageSize, Integer currentPage, UserSession userSession) {

        PageHelper.startPage ( currentPage, pageSize );
        List < ContractFormVo > contractFormList = laborContractDao.selectContractForm ( list, userSession.getCompanyId () );
        PageInfo < ContractFormVo > pageInfo = new PageInfo <> ( contractFormList );
        PageResult < ContractFormVo > pageResult = new PageResult <> ( pageInfo.getList () );
        pageResult.setTotal ( pageInfo.getTotal () );
        return pageResult;
    }

    /**
     * 合同状态  新签、变更   续签、解除、终止
     * 合同标识  有效、无效（根据合同状态与合同起始状态确定是否有效）
     * 审批状态  未提交、审批中、已审批
     * 导入导出接口可以复用导入导出模块
     * 导入校验，此时先把excel转成list，需要对list的各个属性进行一一校验，这个规则还需要产品确定，交给前端进行筛选校验
     */

    @Override
    public PageResult < ContractWithArchiveVo > selectLaborContractserUser(List < Integer > orgIdList, Integer currentPage,
                                                                   Integer pageSize, List < String > status) {
        PageHelper.startPage (currentPage,pageSize );
        List < ContractWithArchiveVo > list = getContractWithArchiveVos ( orgIdList, status );
        return new PageResult <> ( list );
    }

    private List < ContractWithArchiveVo > getContractWithArchiveVos(List < Integer > orgIdList, List < String > status) {

        List < ContractWithArchiveVo > list=laborContractDao.selectHasPowerContract ( orgIdList,status );
            for (ContractWithArchiveVo contractWithArchiveVo : list) {
              if(ENDEMARK.equals ( contractWithArchiveVo.getContractState ()) || LOOSEMARK.equals ( contractWithArchiveVo.getContractState ()) ){
                  contractWithArchiveVo.setIsEnable ( ( short ) 0 );
              }else {
                  contractWithArchiveVo.setIsEnable ( ( short ) 1 );
              }
            }
        return list;
    }

    /**
     * 展示全量已签合同人员id
     *
     * @param orgIdList
     * @param status
     */
    public List < ContractWithArchiveVo > selectLaborContractserUserAll(List < Integer > orgIdList, List < String > status) {
        return getContractWithArchiveVos ( orgIdList, status );
    }

    /**
     * 此处不是逻辑删除，是真删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLaborContract(Integer laborContractId) {
        laborContractDao.deleteByPrimaryKey ( laborContractId );
    }

    @Override
    public void insertLaborContract(ContractVo contractVo, UserSession userSession) throws ParseException {
        //将合同vo设置进去
        mark ( contractVo.getLaborContractVo (), contractVo.getList ().get ( 0 ), userSession.getArchiveId (), NEWMARK );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertLaborContractBatch(ContractVo contractVo, UserSession userSession) throws Exception {
            //批量新签合同
            for (Integer integer : contractVo.getList ()) {
                mark ( contractVo.getLaborContractVo (), integer, userSession.getArchiveId (), NEWMARK );
            }
    }

    @Override
    public void saveLaborContract(ContractVo contractVo, UserSession userSession) throws ParseException {
        mark ( contractVo.getLaborContractVo (), contractVo.getList ().get ( 0 ), userSession.getArchiveId (), NOTMARK );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatelaborContract(ContractVo contractVo, UserSession userSession) throws ParseException {
        //更新合同表
        LaborContract laborContract = new LaborContract ();
        BeanUtils.copyProperties ( contractVo.getLaborContractVo (), laborContract );
        laborContract.setArchiveId ( contractVo.getList ().get ( 0 ) );
        laborContract.setContractState ( CHANGEMARK );
        laborContract.setOperatorId ( userSession.getArchiveId () );
        laborContract.setCreateTime ( new Date () );
        laborContract.setUpdateTime ( new Date () );
        laborContractDao.updateByPrimaryKeySelective ( laborContract );
        //新增变更记录
        change ( contractVo.getLaborContractChangeVo (), COMMONCHANGE, contractVo.getList ().get ( 0 ), userSession.getArchiveId () );
    }


    @Override
    public List < LaborContractChange > selectLaborContractchange(Integer id) {
        return laborContractChangeDao.selectLaborContractchange ( id );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertReNewLaborContract(ContractVo contractVo, UserSession userSession) {
        //新增续签
        insertContinue ( contractVo, userSession, contractVo.getList ().get ( 0 ) );
        //增加更改记录（经过确认续签不用更改记录）
//         change(contractVo.getLaborContractChangeVo (), RENEWCHANGE, contractVo.getList ().get ( 0 ), userSession.getArchiveId());
    }

    @Override
    public Integer getSignNumber(Integer archiveId) {
        //先通过人员找到合同，通过是续签合同的次数上加一
        Integer signNumber= laborContractDao.selectByarcIdAndStatus ( archiveId, RENEWMARK );
        if(signNumber != null && signNumber>1){
            return signNumber;
        }
        return 1;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertReNewLaborContractBatch(ContractVo contractVo, UserSession userSession) {
        for (Integer integer : contractVo.getList ()) {
            //新增续签
            insertContinue ( contractVo, userSession, integer );
            //增加更改记录
//            change(contractVo.getLaborContractChangeVo (), RENEWCHANGE, integer, userSession.getArchiveId());
        }
    }

    private void insertContinue(ContractVo contractVo, UserSession userSession, Integer id) {
        LaborContract laborContract = new LaborContract ();
        //设置其余字段
        BeanUtils.copyProperties ( contractVo.getLaborContractVo (), laborContract );
        laborContract.setArchiveId ( id );
        laborContract.setOperatorId ( userSession.getArchiveId () );
        laborContract.setContractState ( RENEWMARK );
        //新增续签次数
        laborContract.setSignNumber ( getSignNumber ( id  ) +1);
        laborContractDao.insertSelective ( laborContract );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void endlaborContract(LaborContractChangeVo laborContractChangeVo, Integer id, UserSession userSession) throws ParseException {
        //将合同状态设置为终止
        LaborContract laborContract = laborContractDao.selectByPrimaryKey ( id );
        laborContract.setContractState ( ENDEMARK );
        laborContractDao.updateByPrimaryKeySelective ( laborContract );
        //新增变更表
        change ( laborContractChangeVo, ENDCHANGE, id, userSession.getArchiveId () );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void endlaborContractBatch(ContractVo contractVo, UserSession userSession) throws ParseException {
        //将合同状态设置为终止
        for (Integer integer : contractVo.getList ()) {
            LaborContract laborContract = laborContractDao.selectByPrimaryKey ( integer );
            laborContract.setContractState ( ENDEMARK );
            laborContractDao.updateByPrimaryKeySelective ( laborContract );
            //新增变更表
            change ( contractVo.getLaborContractChangeVo (), ENDCHANGE, integer, userSession.getArchiveId () );
        }
    }

    @Override
    public void looselaborContract(LaborContractChangeVo laborContractChangeVo,
                                   Integer id, UserSession userSession) throws ParseException {
        LaborContract laborContract = laborContractDao.selectByPrimaryKey ( id );
        //将合同状态设置为解除
        laborContract.setContractState ( LOOSEMARK );
        laborContractDao.updateByPrimaryKeySelective ( laborContract );
        //新增变更表
        change ( laborContractChangeVo, LOOSECHANGE, id, userSession.getArchiveId () );
    }

    @Override
    public void looselaborContractBatch(ContractVo contractVo, UserSession userSession) throws ParseException {
        for (Integer integer : contractVo.getList ()) {
            LaborContract laborContract = laborContractDao.selectByPrimaryKey ( integer );
            laborContract.setContractState ( LOOSEMARK );
            laborContractDao.updateByPrimaryKeySelective ( laborContract );
            //新增变更表
            change ( contractVo.getLaborContractChangeVo (), LOOSECHANGE, integer, userSession.getArchiveId () );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertLaborContractIntention(List < Integer > list, UserSession userSession) {
        List < ContractRenewalIntention > contractRenewalIntentions = new ArrayList <> ();
        //根据档案id找到合同表中的合同信息
        List < LaborContract > laborContractList = laborContractDao.selectContractByarcIdList ( list );
        for (LaborContract laborContract : laborContractList) {
            ContractRenewalIntention cri = new ContractRenewalIntention ();
            BeanUtils.copyProperties ( laborContract, cri );
            cri.setCompanyId ( userSession.getCompanyId () );
            cri.setOperatorId ( userSession.getArchiveId () );
            cri.setIntentionStatus ( NOTCONFIRM );
            contractRenewalIntentions.add ( cri );
        }
        contractRenewalIntentionDao.insertBatch ( contractRenewalIntentions );
    }

    @Override
    public List < ContractRenewalIntention > selectContractRenewalIntention(Integer id) {
        return contractRenewalIntentionDao.selectByArchiveId ( id );

    }

    @Override
    public void agreeRenew(Integer xuqianId) {
        //查询续签意向表
        ContractRenewalIntention contractRenewalIntention =
                contractRenewalIntentionDao.selectByPrimaryKey ( xuqianId );
        //更改续签意向表
        contractRenewalIntention.setRenewalOpinion ( RENEWAGREE );
        contractRenewalIntention.setIntentionStatus ( CONFIRM );
        contractRenewalIntention.setIsAgree ( ( short ) 1 );
        //TODO 还剩续签意见还未设置
        contractRenewalIntentionDao.updateByPrimaryKey ( contractRenewalIntention );
    }

    @Override
    public void rejectRenew(Integer xuqianId) {
        //查询续签意向表
        ContractRenewalIntention contractRenewalIntention =
                contractRenewalIntentionDao.selectByPrimaryKey ( xuqianId );
        //更改续签意向表
        contractRenewalIntention.setRenewalOpinion ( RENEWREJECT );
        contractRenewalIntention.setIntentionStatus ( CONFIRM );
        contractRenewalIntention.setIsAgree ( ( short ) 0 );
        contractRenewalIntentionDao.updateByPrimaryKey ( contractRenewalIntention );
        //前端跳转至解除合同页面
    }

    @Override
    public void updateContractRenewalIntention(ContractRenewalIntention contractRenewalIntention) {
        contractRenewalIntentionDao.updateByPrimaryKey ( contractRenewalIntention );
    }

    @Override
    public PageResult < ContractRenewalIntention > selectContractRenewalIntentionByOrg(List < Integer > orgId, Integer pageSize, Integer currentPage) {
        PageHelper.startPage ( currentPage, pageSize );
        List < ContractRenewalIntention > list = contractRenewalIntentionDao.selectByorgId ( orgId );
        return new PageResult <> ( list );
    }

    private void mark(LaborContractVo laborContractVo, Integer id, Integer archiveId, String state)  {
        //新增合同
        LaborContract laborContract = new LaborContract ();
        //设置其余字段
        BeanUtils.copyProperties ( laborContractVo, laborContract );
        laborContract.setContractSignDate (  laborContractVo.getContractSignDate () );
        laborContract.setArchiveId ( id );
        laborContract.setOperatorId ( archiveId );
        laborContract.setContractState ( state );
        laborContractDao.insertSelective ( laborContract );
    }

    private void change(LaborContractChangeVo laborContractChangeVo, String type, Integer id, Integer achiveId)  {
        LaborContractChange laborContractChange = new LaborContractChange ();
        BeanUtils.copyProperties ( laborContractChangeVo, laborContractChange );
        laborContractChange.setChangeDate (laborContractChangeVo.getChangeDate () );
        laborContractChange.setChangeType ( type );
        laborContractChange.setContractId ( id );
        laborContractChange.setOperatorId ( achiveId );
        laborContractChangeDao.insertSelective ( laborContractChange );
    }





/*
合同状态分为新签，续签，变更，解除，终止
 * 档案状态
 * 1，试用期
 * 2，兼职
 * 3，离职
 * 4，入转调离
 * 5，借调
 * 6，挂职
 * 7，外派
 * 8，正常
 */
}
