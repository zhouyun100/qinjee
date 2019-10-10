package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.contractdao.ContractRenewalIntentionDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractChangeDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchivePostRelationDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.LaborContractChangeVo;
import com.qinjee.masterdata.model.vo.staff.LaborContractVo;
import com.qinjee.masterdata.service.staff.IStaffContractService;
import com.qinjee.masterdata.utils.GetDayUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class StaffContractServiceImpl implements IStaffContractService {
    private static final Logger logger = LoggerFactory.getLogger(StaffContractServiceImpl.class);
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
    private static final String ENDSTATUS = "ENDSTATUS";
    private static final String RELESESTATUS = "RELESESTATUS";
    @Autowired
    private LaborContractDao laborContractDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private LaborContractChangeDao laborContractChangeDao;
    @Autowired
    private ContractRenewalIntentionDao contractRenewalIntentionDao;
    @Autowired
    private UserArchivePostRelationDao userArchivePostRelationDao;
    /**
     * 展示未签合同的人员信息
     *
     * @param orgId
     * @param pageSize
     * @return
     */
    @Override
    public PageResult<UserArchive> selectNoLaborContract(Integer orgId,
                                                         Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        //查看机构下的合同id
        List<Integer> conList=laborContractDao.selectByorgId(orgId);
        //根据合同id找到没有合同的档案id
        List<Integer> arcList=laborContractDao.selectArcByCon(conList);
        //根据档案id查询档案
        List<UserArchive> list=userArchiveDao.selectNotInList(arcList);
        return new PageResult<>(list);
    }
    /**合同状态  新签、变更   续签、解除、终止
     *  合同标识  有效、无效（根据合同状态与合同起始状态确定是否有效）
     *   审批状态  未提交、审批中、已审批
     *   导入导出接口可以复用导入导出模块
     *   导入校验，此时先把excel转成list，需要对list的各个属性进行一一校验，这个规则还需要产品确定，交给前端进行筛选校验
     */

    @Override
    public PageResult<UserArchive> selectLaborContractserUser(Integer orgId, Integer currentPage,
                                                              Integer pageSize,Boolean isEnable,
                                                              List<String> status) throws Exception {
        PageHelper.startPage(currentPage,pageSize);
        List<LaborContract> noEffectLabList=new ArrayList<>();
        List<LaborContract> effectLabList=new ArrayList<>();
        List<Integer> conList=new ArrayList<>();
        //查看机构下的合同
        List<LaborContract> labList=laborContractDao.selectLabByorgId(orgId);
        //把返回的合同进行筛选，通过isEnable，得到合同id


        //将合同快到期的，解除合同，终止合同筛选为无效合同
        for (LaborContract laborContract : labList) {
            if(!laborContract.getContractState().equals(ENDSTATUS) || laborContract.getContractState().equals(RELESESTATUS)
            || GetDayUtil.getMonth(laborContract.getContractEndDate(), new Date()) <0
            ){
                noEffectLabList.add(laborContract);
            }
        }
            if(labList.removeAll(effectLabList)){
                Collections.copy(effectLabList,labList);
            }else {
                throw new Exception("获取集合失败");
            }
        if(isEnable){
             conList = getConList(status, effectLabList);
        }else {
             conList = getConList(status, noEffectLabList);
        }


        //根据合同id找到有合同的档案id
        List<Integer> arcList=laborContractDao.seleltByArcIdIn(conList);
        //根据档案id查询档案
        List<UserArchive> list=userArchiveDao.selectNotInList(arcList);
        return new PageResult<>(list);
    }

    private List<Integer> getConList(List<String> status, List<LaborContract> effectLabList) {
        List<Integer> conList=new ArrayList<>();
        for (LaborContract laborContract : effectLabList) {
            for (String s : status) {
                if (laborContract.getContractState().equals(s)) {
                    conList.add(laborContract.getArchiveId());
                }
            }
        }
        return conList;
    }


    /**
     * 此处不是逻辑删除，是真删除
     *
     * @param laborContractid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLaborContract(Integer laborContractid) {
            laborContractDao.deleteByPrimaryKey(laborContractid);
    }

    @Override
    public void insertLaborContract(LaborContractVo laborContractVo, Integer id, UserSession userSession) {

            //将合同vo设置进去
            Mark(laborContractVo, id, userSession.getArchiveId(), NEWMARK);

    }

    @Override
    public void insertLaborContractBatch(LaborContractVo laborContractVo, List<Integer> list,
                                                   UserSession userSession) {
            //将合同vo设置进去
            LaborContract laborContract = new LaborContract();
            //批量新签合同
            for (Integer integer : list) {
                BeanUtils.copyProperties(laborContractVo, laborContract);
                laborContract.setArchiveId(integer);
                laborContract.setOperatorId(userSession.getArchiveId());
                laborContract.setContractState(NEWMARK);
                laborContractDao.insertSelective(laborContract);
            }
    }

    @Override
    public void SaveLaborContract(LaborContractVo laborContractVo, Integer id, UserSession userSession) {
            Mark(laborContractVo, id, userSession.getArchiveId(), NOTMARK);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatelaborContract(LaborContract laborContract, LaborContractChangeVo laborContractChangeVo,
                                              Integer id, UserSession userSession) {
        //更新合同表
        laborContract.setContractState(CHANGEMARK);
        laborContractDao.updateByPrimaryKey(laborContract);
        //新增变更记录
        change(laborContractChangeVo, COMMONCHANGE, id, userSession.getArchiveId());
    }


    @Override
    public List<LaborContractChange> selectLaborContractchange(Integer id) {
        return laborContractChangeDao.selectLaborContractchange(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertReNewLaborContract(LaborContractVo laborContractVo, Integer id,
                                                   LaborContractChangeVo laborContractChangeVo, UserSession userSession) {

        //新增续签
        LaborContract laborContract = new LaborContract();
        //设置其余字段
        BeanUtils.copyProperties(laborContractVo, laborContract);
        laborContract.setArchiveId(id);
        laborContract.setOperatorId(userSession.getArchiveId());
        laborContract.setContractState(RENEWMARK);
        //新增续签次数
        laborContract.setSignNumber(laborContractVo.getSignNumber()+1);
        laborContractDao.updateByPrimaryKeySelective(laborContract);
        //增加更改记录
        change(laborContractChangeVo, RENEWCHANGE, id, userSession.getArchiveId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertReNewLaborContractBatch(LaborContractVo laborContractVo, List<Integer> list,
                                                        LaborContractChangeVo laborContractChangeVo,
                                                        UserSession userSession) {
        for (Integer integer : list) {
            //新增续签
            LaborContract laborContract = new LaborContract();
            //设置其余字段
            laborContract.setArchiveId(integer);
            BeanUtils.copyProperties(laborContractVo, laborContract);
            laborContract.setOperatorId(userSession.getArchiveId());
            laborContract.setContractState(RENEWMARK);
            //新增续签次数
            laborContract.setSignNumber(laborContractVo.getSignNumber()+1);
            laborContractDao.updateByPrimaryKeySelective(laborContract);
            //增加更改记录
            change(laborContractChangeVo, RENEWCHANGE, integer, userSession.getArchiveId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void endlaborContract(LaborContractChangeVo laborContractChangeVo, Integer id, UserSession userSession) {
        //将合同状态设置为终止
            LaborContract laborContract = laborContractDao.selectByPrimaryKey(id);
            laborContract.setContractState(ENDEMARK);
            laborContractDao.updateByPrimaryKeySelective(laborContract);
            //新增变更表
            change(laborContractChangeVo, ENDCHANGE, id, userSession.getArchiveId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void endlaborContractBatch(LaborContractChangeVo laborContractChangeVo,
                                                List<Integer> list,UserSession userSession) {
        //将合同状态设置为终止

            for (Integer integer : list) {
                LaborContract laborContract = laborContractDao.selectByPrimaryKey(integer);
                laborContract.setContractState(ENDEMARK);
                laborContractDao.updateByPrimaryKeySelective(laborContract);
                //新增变更表
                change(laborContractChangeVo, ENDCHANGE, integer, userSession.getArchiveId());
            }

    }

    @Override
    public void looselaborContract(LaborContractChangeVo laborContractChangeVo,
                                             Integer id, UserSession userSession) {
            LaborContract laborContract = laborContractDao.selectByPrimaryKey(id);
            //将合同状态设置为解除
            laborContract.setContractState(LOOSEMARK);
            laborContractDao.updateByPrimaryKeySelective(laborContract);
            //新增变更表
            change(laborContractChangeVo, LOOSECHANGE, id, userSession.getArchiveId());
    }

    @Override
    public void looselaborContractBatch(LaborContractChangeVo laborContractChangeVo, List<Integer> list,
                                        UserSession userSession) {

        for (Integer integer : list) {
            LaborContract laborContract = laborContractDao.selectByPrimaryKey(integer);
            laborContract.setContractState(LOOSEMARK);
            laborContractDao.updateByPrimaryKeySelective(laborContract);
            //新增变更表
            change(laborContractChangeVo, COMMONCHANGE, integer, userSession.getArchiveId());
        }
    }

    @Override
    public void insertLaborContractIntention(Integer id,UserSession userSession) {
          //根据档案id找到员工档案关系表中的合同开始时间，结束时间以及类型
            UserArchivePostRelation relation=userArchivePostRelationDao.selectByArcId(id);
            ContractRenewalIntention contractRenewalIntention = new ContractRenewalIntention();
            contractRenewalIntention.setArchiveId(id);
            contractRenewalIntention.setContractBeginDate(relation.getEmploymentBeginDate());
            contractRenewalIntention.setContractEndDate(relation.getEmploymentEndDate());
            contractRenewalIntention.setContractPeriodType(relation.getEmploymentType());
            contractRenewalIntention.setOperatorId(userSession.getArchiveId());
            contractRenewalIntentionDao.insertSelective(contractRenewalIntention);
    }

    @Override
    public ContractRenewalIntention selectContractRenewalIntention(Integer id) {
      return contractRenewalIntentionDao.selectByArchiveId(id);

    }

    @Override
    public void agreeRenew(ContractRenewalIntention contractRenewalIntention) {
            //更改续签意向表
            contractRenewalIntention.setRenewalOpinion(RENEWAGREE);
            contractRenewalIntentionDao.updateByPrimaryKey(contractRenewalIntention);
    }

    @Override
    public void rejectRenew(ContractRenewalIntention contractRenewalIntention) {
            //更改续签意向表
            contractRenewalIntention.setRenewalOpinion(RENEWREJECT);
            contractRenewalIntentionDao.updateByPrimaryKey(contractRenewalIntention);
            //前端跳转至解除页面
    }
    @Override
    public void updateContractRenewalIntention(ContractRenewalIntention contractRenewalIntention) {
        contractRenewalIntentionDao.updateByPrimaryKey(contractRenewalIntention);
    }

    @Override
    public Integer selectArcNumberIn(Integer id) {
        return userArchiveDao.selectArcNumberIn(id);
    }

    @Override
    public List<UserArchive> selectArcDeadLine(Integer id) {
        List<UserArchive> list = new ArrayList<>();

        //根据机构id找到合同
        List<Integer> arcList = userArchiveDao.selectByOrgId(id);
        //合同表里面筛选在机构档案的合同id
        List<Integer> newArcList = laborContractDao.seleltByArcIdIn(arcList);
        for (Integer integer : newArcList) {
            LaborContract laborContract = laborContractDao.selectByPrimaryKey(integer);
            if (GetDayUtil.getMonth(laborContract.getContractEndDate(), new Date()) < 3) {
                //根据合同id找到档案id
                Integer achiveId = laborContractDao.seleltByArcIdSingle(integer);
                list.add(userArchiveDao.selectByPrimaryKey(achiveId));
            }
        }
        return list;
    }

    private void Mark(LaborContractVo laborContractVo, Integer id, Integer archiveId, String state) {
        //新增合同
        LaborContract laborContract = new LaborContract();
        //设置其余字段
        BeanUtils.copyProperties(laborContractVo, laborContract);
        laborContract.setArchiveId(id);
        laborContract.setOperatorId(archiveId);
        laborContract.setContractState(state);
        laborContractDao.insertSelective(laborContract);
    }

    private void change(LaborContractChangeVo laborContractChangeVo, String type, Integer id, Integer achiveId) {
        LaborContractChange laborContractChange = new LaborContractChange();
        BeanUtils.copyProperties(laborContractChangeVo, laborContractChange);
        laborContractChange.setChangeType(type);
        laborContractChange.setContractId(id);
        laborContractChange.setOperatorId(achiveId);
    }



/*
合同状态分为新签，续签，变更，解除，终止
 */
/**
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
