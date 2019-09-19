package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.staffstandingbookdao.StandingBookDao;
import com.qinjee.masterdata.dao.staffdao.staffstandingbookdao.StandingBookFilterDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.BlacklistDao;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.StandingBook;
import com.qinjee.masterdata.model.entity.StandingBookFilter;
import com.qinjee.masterdata.service.staff.IStaffStandingBookService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Administrator
 */
public class StaffStandingBookServiceImpl implements IStaffStandingBookService {
    private static final Logger logger = LoggerFactory.getLogger(StaffStandingBookServiceImpl.class);
    @Autowired
    private BlacklistDao blacklistDao;
    @Autowired
    private StandingBookDao standingBookDao;
    @Autowired
    private StandingBookFilterDao standingBookFilterDao;

    @Override
    /**
     * 有个数据来源，来自预入职表要有手机号，档案表要有职位。需要前端传
     */
    public ResponseResult insertBlackList(List<Blacklist> blacklists) {
        if(blacklists!=null){
            try {
                for (Blacklist blacklist : blacklists) {
                    blacklistDao.insert(blacklist);
                }
                return new ResponseResult(true,CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("新增黑名单失败");
                return new ResponseResult(false, CommonCode.INVALID_PARAM);
            }
        }
        return new ResponseResult(false, CommonCode.INVALID_PARAM);
    }

    @Override
    public ResponseResult deleteBlackList(List<Integer> list) {
        try {
            Integer max=blacklistDao.selectMaxId();
            for (Integer integer : list) {
                if(max<integer){
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                //TODO 逻辑删除黑名单表
            }
            return new ResponseResult(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除黑名单失败");
            return  new ResponseResult(false,CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult updateBalckList(Blacklist blacklist) {
        if (blacklist instanceof Blacklist) {
            try {
                blacklistDao.updateByPrimaryKey(blacklist);
                return new ResponseResult(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("更新黑名单失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        } else {
            return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
        }
    }

    @Override
    public ResponseResult<PageResult<Blacklist>> selectBalckList(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Blacklist> list = null;
        PageResult<Blacklist> pageResult = new PageResult<>();
        try {
            list = blacklistDao.selectByPage();
            pageResult.setList(list);
            return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询黑名单失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }
    }

    /**
     * 梳理：台账除了属性以外，还跟筛选表存在联系。新增还需要新增筛选表
     * @param standingBook
     * @return
     */
    @Override
    public ResponseResult insertStandingBook(StandingBook standingBook, StandingBookFilter standingBookFilter) {
        if(standingBook instanceof StandingBook && standingBookFilter instanceof StandingBookFilter){
            try {
                //新增台账属性
                standingBookDao.insert(standingBook);
                //新增台账所对应的筛选表
                standingBookFilterDao.insert(standingBookFilter);
                return new ResponseResult<>(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("新增台账失败");
                return new ResponseResult<>(false, CommonCode.FAIL);
            }

        }
        return new ResponseResult(false, CommonCode.INVALID_PARAM);
    }

    @Override
    public ResponseResult deleteStandingBook(Integer standingBookId) {
        try {
            //删除台账属性
            standingBookDao.deleteStandingBook(standingBookId);
            //删除对应筛选表
            standingBookFilterDao.deleteStandingBookFilter(standingBookId);
            return new ResponseResult<>(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除台账失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

}
