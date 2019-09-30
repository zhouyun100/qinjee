package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.contractdao.ContractRenewalIntentionDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractChangeDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.LaborContractDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.LaborContractChangeVo;
import com.qinjee.masterdata.model.vo.staff.LaborContractVo;
import com.qinjee.masterdata.service.staff.IStaffContractService;
import com.qinjee.masterdata.utils.GetDayUtil;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public ResponseResult<PageResult<UserArchive>> selectNoLaborContract(Integer orgId, Integer currentPage, Integer pageSize) {
        PageResult pageResult = new PageResult();
        List list = new ArrayList();
        //根据档案id找到对应权限下的机构
        try {
            //找到机构下的所有档案id
            List<Integer> arcList = userArchiveDao.selectByOrgId(orgId);
            //合同表里面筛选id不在机构档案的合同id
            List<Integer> newArcList = laborContractDao.seleltByArcId(arcList);
            //根据合同id找到对应的档案
            getArcByConId(currentPage, pageSize, pageResult, list, newArcList);
            return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查找未签合同人员失败失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult<PageResult<UserArchive>> selectLaborContractserUser(Integer orgId, Integer currentPage, Integer pageSize) {
        PageResult pageResult = new PageResult();
        List list = new ArrayList();
        try {
            List<Integer> arcList = userArchiveDao.selectByOrgId(orgId);
            //合同表里面筛选id在机构档案的合同id
            List<Integer> newArcList = laborContractDao.seleltByArcIdIn(arcList);
            //根据合同id找到对应的档案
            getArcByConId(currentPage, pageSize, pageResult, list, newArcList);
            return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查找已签合同人员失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }
    }

    private void getArcByConId(Integer currentPage, Integer pageSize, PageResult pageResult, List list, List<Integer> newArcList) {
        for (Integer integer : newArcList) {
            UserArchive userArchive = userArchiveDao.selectByPrimaryKey(integer);
            list.add(userArchive);
        }
        PageHelper.startPage(currentPage, pageSize);
        pageResult.setList(list);
    }

    /**
     * 此处不是逻辑删除，是真删除
     *
     * @param laborContractid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteLaborContract(Integer laborContractid) {
        try {
            laborContractDao.deleteByPrimaryKey(laborContractid);
            return new ResponseResult<>(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult insertLaborContract(LaborContractVo laborContractVo, Integer id, Integer archiveId) {
        try {
            //将合同vo设置进去
            Mark(laborContractVo, id, archiveId, NEWMARK);
            return new ResponseResult<>(true, CommonCode.SUCCESS);
        } catch (BeansException e) {
            logger.error("新签合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertLaborContractBatch(LaborContractVo laborContractVo, List<Integer> list, Integer archiveId) {
        try {
            //将合同vo设置进去
            LaborContract laborContract = new LaborContract();
            //批量新签合同
            for (Integer integer : list) {
                BeanUtils.copyProperties(laborContractVo, laborContract);
                laborContract.setArchiveId(integer);
                laborContract.setOperatorId(archiveId);
                laborContract.setContractState(NEWMARK);
                laborContractDao.insertSelective(laborContract);
            }
            return new ResponseResult<>(true, CommonCode.SUCCESS);
        } catch (BeansException e) {
            logger.error("批量新签合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult SaveLaborContract(LaborContractVo laborContractVo, Integer id, Integer archiveId) {
        try {
            Mark(laborContractVo, id, archiveId, NOTMARK);
            return new ResponseResult<>(true, CommonCode.SUCCESS);
        } catch (BeansException e) {
            logger.error("保存合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updatelaborContract(LaborContract laborContract, LaborContractChangeVo laborContractChangeVo,
                                              Integer id, Integer archiveId) {

        try {
            //更新合同表
            laborContract.setContractState(CHANGEMARK);
            laborContractDao.updateByPrimaryKey(laborContract);
            //新增变更记录
            change(laborContractChangeVo, COMMONCHANGE, id, archiveId);
            return new ResponseResult<>(true, CommonCode.SUCCESS);
        } catch (BeansException e) {
            logger.error("更新合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }


    @Override
    public ResponseResult<List<LaborContractChange>> selectLaborContractchange(Integer id) {
        List<LaborContractChange> list = new ArrayList<>();
        try {
            list = laborContractChangeDao.selectLaborContractchange(id);
            return new ResponseResult<>(list, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("展示合同更新失败");
            return new ResponseResult<>(list, CommonCode.FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertReNewLaborContract(LaborContractVo laborContractVo, Integer id,

                                                   LaborContractChangeVo laborContractChangeVo, Integer archiveId) {
        try {
            //新增续签
            Mark(laborContractVo, id, archiveId, RENEWMARK);
            //增加更改记录
            change(laborContractChangeVo, RENEWCHANGE, id, archiveId);
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("续签合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertReNewLaborContractBatch(LaborContractVo laborContractVo, List<Integer> list,
                                                        LaborContractChangeVo laborContractChangeVo, Integer archiveId) {
        try {
            for (Integer integer : list) {
                Mark(laborContractVo, integer, archiveId, RENEWMARK);
                change(laborContractChangeVo, RENEWCHANGE, integer, archiveId);
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("续签合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult endlaborContract(LaborContractChangeVo laborContractChangeVo, Integer id, Integer archiveId) {
        //将合同状态设置为终止
        try {
            LaborContract laborContract = laborContractDao.selectByPrimaryKey(id);
            laborContract.setContractState(ENDEMARK);
            //新增变更表
            change(laborContractChangeVo, ENDCHANGE, id, archiveId);
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("终止合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult endlaborContractBatch(LaborContractChangeVo laborContractChangeVo, List<Integer> list, Integer archiveId) {
        //将合同状态设置为终止
        try {
            for (Integer integer : list) {
                LaborContract laborContract = laborContractDao.selectByPrimaryKey(integer);
                laborContract.setContractState(ENDEMARK);
                //新增变更表
                change(laborContractChangeVo, ENDCHANGE, integer, archiveId);
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("批量终止合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult looselaborContract(LaborContractChangeVo laborContractChangeVo, Integer id, Integer archiveId) {
        try {
            LaborContract laborContract = laborContractDao.selectByPrimaryKey(id);
            //将合同状态设置为解除
            laborContract.setContractState(LOOSEMARK);
            //新增变更表
            change(laborContractChangeVo, LOOSECHANGE, id, archiveId);
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("解除合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult looselaborContractBatch(LaborContractChangeVo laborContractChangeVo, List<Integer> list, Integer archiveId) {
        //将合同状态设置为解除
        try {
            for (Integer integer : list) {
                LaborContract laborContract = laborContractDao.selectByPrimaryKey(integer);
                laborContract.setContractState(LOOSEMARK);
                //新增变更表
                change(laborContractChangeVo, COMMONCHANGE, integer, archiveId);
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("批量解除合同失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertLaborContractIntention(Integer id) {
        try {
            UserArchive userArchive = userArchiveDao.selectByPrimaryKey(id);
            ContractRenewalIntention contractRenewalIntention = new ContractRenewalIntention();
            contractRenewalIntention.setArchiveId(id);
            contractRenewalIntentionDao.insertSelective(contractRenewalIntention);
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("发送续签意向表失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult selectContractRenewalIntention(Integer id) {
        try {
            ContractRenewalIntention contractRenewalIntention = contractRenewalIntentionDao.selectByArchiveId(id);
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("展示续签意向表失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult agreeRenew(ContractRenewalIntention contractRenewalIntention) {
        try {
            //更改续签意向表
            contractRenewalIntention.setRenewalOpinion(RENEWAGREE);
            contractRenewalIntentionDao.updateByPrimaryKey(contractRenewalIntention);
            return new ResponseResult(true, CommonCode.SUCCESS);
            //前端跳转至续签页面
        } catch (Exception e) {
            logger.error("新增续签意向同意状态失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult rejectRenew(ContractRenewalIntention contractRenewalIntention) {
        try {
            //更改续签意向表
            contractRenewalIntention.setRenewalOpinion(RENEWREJECT);
            contractRenewalIntentionDao.updateByPrimaryKey(contractRenewalIntention);
            return new ResponseResult(true, CommonCode.SUCCESS);
            //前端跳转至解除页面
        } catch (Exception e) {
            logger.error("新增续签意向不同意状态失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }


    @Override
    public ResponseResult insertContractRenewalIntention(ContractRenewalIntention contractRenewalIntention) {
        try {
            contractRenewalIntentionDao.insert(contractRenewalIntention);
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("新增合同续签反馈表失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult selectArcNumberIn(Integer id) {
        try {
            Integer number = userArchiveDao.selectArcNumberIn(id);
            return new ResponseResult(number, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("获取在职人数失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult selectArcDeadLine(Integer id) {
        List<UserArchive> list = new ArrayList<>();
        try {
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
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("获取合同即将到期人员信息失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
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
