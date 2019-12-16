package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.staffdao.contractdao.ContractParamDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.ContractRenewalIntentionDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractChangeDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.LaborContractChangeVo;
import com.qinjee.masterdata.model.vo.staff.LaborContractVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.service.staff.IStaffContractService;
import com.qinjee.masterdata.utils.GetDayUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
    private static final String NOTCONFIRM = "待确认";
    private static final String CONFIRM = "已确认";
    private static final String  HAVEFFECT= "有效";
    private static final String NOTHAVAEFFECT = "无效";
    @Autowired
    private LaborContractDao laborContractDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private LaborContractChangeDao laborContractChangeDao;
    @Autowired
    private ContractRenewalIntentionDao contractRenewalIntentionDao;
    @Autowired
    private ContractParamDao contractParamDao;
    /**
     * 展示未签合同的人员信息
     *
     * @param orgId
     * @param pageSize
     * @return
     */
    @Override
    public PageResult<UserArchiveVo> selectNoLaborContract(Integer orgId,
                                                         Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        //根据合同id找到没有合同的档案
        List< UserArchiveVo > arcList=userArchiveDao.selectArcByNotCon(orgId);
        return new PageResult<>(arcList);
    }
    /**合同状态  新签、变更   续签、解除、终止
     *  合同标识  有效、无效（根据合同状态与合同起始状态确定是否有效）
     *   审批状态  未提交、审批中、已审批
     *   导入导出接口可以复用导入导出模块
     *   导入校验，此时先把excel转成list，需要对list的各个属性进行一一校验，这个规则还需要产品确定，交给前端进行筛选校验
     */

    @Override
    public PageResult<UserArchiveVo> selectLaborContractserUser(List<Integer> orgIdList, Integer currentPage,
                                                              Integer pageSize,String isEnable,
                                                              List<String> status) {

        List<LaborContract> noEffectLabList=new ArrayList<>();
        Set<Integer> conSet;
        //查看机构下的合同
        List<LaborContract> labList=laborContractDao.selectLabByorgId(orgIdList);
        //把返回的合同进行筛选，通过isEnable，得到合同id

        //将合同快到期的，解除合同，终止合同筛选为无效合同
        for (LaborContract laborContract : labList) {
            if(laborContract.getContractState().equals(ENDSTATUS) || laborContract.getContractState().equals(RELESESTATUS)
            || GetDayUtil.getDay(laborContract.getContractEndDate(), new Date()) <0
            ){
                noEffectLabList.add(laborContract);
            }
        }
        labList.removeAll(noEffectLabList);
        if(HAVEFFECT.equals (isEnable)){
             conSet = getConList(status, labList);
        }else if(NOTHAVAEFFECT.equals ( isEnable )) {
             conSet = getConList(status, noEffectLabList);
        }else{
            Set < Integer > conList = getConList ( status, labList );
            Set < Integer > conList1 = getConList ( status, noEffectLabList );
            conList.addAll ( conList1 );
            conSet=conList;
        }
        ArrayList < Integer > integers = new ArrayList <> ( conSet );
        if(!CollectionUtils.isEmpty ( integers )) {
            PageHelper.startPage ( currentPage, pageSize );
            List < UserArchiveVo > userArchiveList = userArchiveDao.selectByPrimaryKeyList ( integers );
            return new PageResult <> ( userArchiveList );
        }
        return null;
    }

    private Set<Integer> getConList(List<String> status, List<LaborContract> labList) {
        Set<Integer> conSet=new HashSet <> (  );
        for (LaborContract laborContract : labList) {
            for (String s : status) {
                if (laborContract.getContractState().equals(s)) {
                    conSet.add(laborContract.getArchiveId());
                }
            }
        }
        return conSet;
    }
    /**
     *
     此处不是逻辑删除，是真删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLaborContract(Integer laborContractId) {
            laborContractDao.deleteByPrimaryKey(laborContractId);
    }

    @Override
    public void insertLaborContract(LaborContractVo laborContractVo, Integer id, UserSession userSession) {

            //将合同vo设置进去
            mark(laborContractVo, id, userSession.getArchiveId(), NEWMARK);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertLaborContractBatch(LaborContractVo laborContractVo, List<Integer> list,
                                                   UserSession userSession) throws Exception {
        StringBuilder stringBuffer=new StringBuilder();
            //判断是否有已签合同，根据人员id寻找是否存在已签的
            List<Integer> integerList=laborContractDao.selectConByArcId(list);

            if(CollectionUtils.isEmpty(integerList)){
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
            }else {
                for (Integer integer : integerList) {
                    stringBuffer.append(integer);
                }
                throw new Exception("存在已签合同人员" + stringBuffer.toString());
            }
    }

    @Override
    public void saveLaborContract(LaborContractVo laborContractVo, Integer id, UserSession userSession) {
            mark(laborContractVo, id, userSession.getArchiveId(), NOTMARK);
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
        System.out.println(laborContract.getSignNumber());
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
    @Transactional(rollbackFor = Exception.class)
    public void insertLaborContractIntention(List<Integer> list,UserSession userSession) {
        List<ContractRenewalIntention> contractRenewalIntentions=new ArrayList<>();
        //根据档案id找到合同表中的合同信息
        List<LaborContract> laborContractList=laborContractDao.selectContractByarcIdList(list);
        for (LaborContract laborContract : laborContractList) {
            ContractRenewalIntention cri=new ContractRenewalIntention();
            BeanUtils.copyProperties(laborContract,cri);
            cri.setOperatorId(userSession.getArchiveId());
            cri.setIntensionStatus(NOTCONFIRM);
            contractRenewalIntentions.add(cri);
        }
        contractRenewalIntentionDao.insertBatch(contractRenewalIntentions);
    }

    @Override
    public List<ContractRenewalIntention> selectContractRenewalIntention(Integer id) {
      return contractRenewalIntentionDao.selectByArchiveId(id);

    }

    @Override
    public void agreeRenew(Integer xuqianId) {
        //查询续签意向表
        ContractRenewalIntention contractRenewalIntention =
                contractRenewalIntentionDao.selectByPrimaryKey(xuqianId);
        //更改续签意向表
            contractRenewalIntention.setRenewalOpinion(RENEWAGREE);
            contractRenewalIntention.setIntensionStatus(CONFIRM);
            //TODO 还剩续签意见还未设置
            contractRenewalIntentionDao.updateByPrimaryKey(contractRenewalIntention);
    }

    @Override
    public void rejectRenew(Integer xuqianId) {
        //查询续签意向表
        ContractRenewalIntention contractRenewalIntention =
                contractRenewalIntentionDao.selectByPrimaryKey(xuqianId);
            //更改续签意向表
            contractRenewalIntention.setRenewalOpinion(RENEWREJECT);
            contractRenewalIntention.setIntensionStatus(CONFIRM);
            contractRenewalIntentionDao.updateByPrimaryKey(contractRenewalIntention);
            //前端跳转至解除合同页面
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
    public List<UserArchive> selectArcDeadLine(Integer id,List<UserArchive> list) throws Exception {
        List<ContractParam> contractParams = contractParamDao.findContractParamByCompanyId (id);
        List<String> strings=new ArrayList<>();
        strings.add("正式");
        strings.add("试用");
        strings.add("实习");
        for (String string : strings) {
            Date date = userArchiveDao.selectDateByStatus(string);
            for (UserArchive userArchive : list) {
                if(GetDayUtil.getDay(new Date(),date) > getDeadLineDays(string,contractParams)){
                    list.remove(userArchive);
                }
            }
        }
        return list;
    }
    private Integer getDeadLineDays(String status,List<ContractParam> contractParams) throws Exception {
       List<ContractParam> list=new ArrayList<>();
        for (ContractParam contractParam : contractParams) {
            if(status.equals(contractParam.getApplicationScopeCode())){
                list.add(contractParam);
            }
        }
        if(list.size()>0){
            ExceptionCast.cast ( CommonCode.SET_DEADLINE_EXCEPTION );
        }
            return Integer.parseInt(list.get(0).getDateRule());
    }

    private void mark(LaborContractVo laborContractVo, Integer id, Integer archiveId, String state) {
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
        laborContractChangeDao.insertSelective(laborContractChange);
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
