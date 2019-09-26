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
import com.qinjee.masterdata.service.staff.IStaffContractService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class StaffContractServiceImpl implements IStaffContractService {
    private static final Logger logger = LoggerFactory.getLogger(StaffContractServiceImpl.class);
    @Autowired
    private LaborContractDao laborContractDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private LaborContractChangeDao laborContractChangeDao;
    @Autowired
    private ContractRenewalIntentionDao contractRenewalIntentionDao;

    @Override
    public ResponseResult<PageResult<UserArchive>> selectNoLaborContract(Integer archiveId, Integer currentPage, Integer pageSize) {
        PageResult<UserArchive> pageResult=new PageResult<>();
        try {
            PageHelper.startPage(currentPage,pageSize);
            //找到权限下的机构id
            List<Integer> list=userArchiveDao.selectOrgIdByArchiveId(archiveId);
            //找到这些机构下所有的档案id
            List<Integer> arichiveIds=new ArrayList<>();
            for (Integer integer : list) {
               arichiveIds.add(userArchiveDao.selectArchiveIdByOrgId(integer));
            }
            List<UserArchive> userArchiveList=userArchiveDao.selectNoLaborContract(arichiveIds);
            pageResult.setList(userArchiveList);
            return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查看合同失败");
            return new ResponseResult<>(pageResult,CommonCode.FAIL);
        }
    }

    /**
     * 此处不是逻辑删除，是真删除
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
            return new ResponseResult<>(false,CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult insertLaborContract(LaborContract laborContract) {
        try {
            laborContractDao.insertSelective(laborContract);
            return new ResponseResult<>(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("新增合同失败");
            return new ResponseResult<>(false,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertLaborContractBatch(List<LaborContract> list) {
        try {
            if(list!=null){
                for (LaborContract laborContract : list) {
                    laborContractDao.insertSelective(laborContract);
                }
                return new ResponseResult<>(true, CommonCode.SUCCESS);
            }
        } catch (Exception e) {
            logger.error("批量新增合同失败");
            return new ResponseResult<>(false,CommonCode.FAIL);
        }
        return new ResponseResult<>(false,CommonCode.INVALID_PARAM);
    }

    @Override
    public ResponseResult updatelaborContract(LaborContract laborContract, LaborContractChange laborContractChange) {
        try {
            //更新合同
            laborContractDao.updateByPrimaryKey(laborContract);
            //将更新记录保存至更新记录表
            laborContractChangeDao.insertSelective(laborContractChange);
            return new ResponseResult<>(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("更新合同失败");
            return new ResponseResult<>(false,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult<List<LaborContractChange>> selectLaborContractchange(Integer id) {
        List<LaborContractChange> list=new ArrayList<>();
        try {
          list=laborContractChangeDao.selectLaborContractchange(id);
            return new ResponseResult<>(list,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("展示合同更新失败");
            return new ResponseResult<>(list,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertReNewLaborContract(LaborContract laborContract) {
        try {
            laborContractDao.insertReNewLaborContract(laborContract);
            return new ResponseResult<>(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("续签合同失败");
            return new ResponseResult(false,CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertContractRenewalIntention(ContractRenewalIntention contractRenewalIntention) {
        try {
            contractRenewalIntentionDao.insert(contractRenewalIntention);
            return new ResponseResult(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("新增合同续签反馈表失败");
            return new ResponseResult(false,CommonCode.FAIL);
        }
    }
}
