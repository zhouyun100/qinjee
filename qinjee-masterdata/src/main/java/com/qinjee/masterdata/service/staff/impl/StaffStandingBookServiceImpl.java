package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.staffstandingbookdao.StandingBookDao;
import com.qinjee.masterdata.dao.staffdao.staffstandingbookdao.StandingBookFilterDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.StandingBook;
import com.qinjee.masterdata.model.entity.StandingBookFilter;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookFilterVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.masterdata.service.staff.IStaffStandingBookService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class StaffStandingBookServiceImpl implements IStaffStandingBookService {
    private static final Logger logger = LoggerFactory.getLogger(StaffStandingBookServiceImpl.class);

    @Autowired
    private StandingBookDao standingBookDao;
    @Autowired
    private StandingBookFilterDao standingBookFilterDao;
    @Autowired
    private BlacklistDao blacklistDao;
    @Autowired
    private UserArchiveDao userArchiveDao;

    @Override
    public ResponseResult insertBlackList(List<BlackListVo> blackListVos, String dataSource, Integer archiveId, Integer companyId) {

        if(blackListVos!=null){
            try {
                for (BlackListVo blacklistVo : blackListVos) {
                    Blacklist blacklist = new Blacklist();
                    BeanUtils.copyProperties(blacklistVo,blacklist);
                    blacklist.setOperatorId(archiveId);
                    blacklist.setCompanyId(companyId);
                    blacklist.setDataSource(dataSource);
                    blacklistDao.insertSelective(blacklist);
                }
                return new ResponseResult(true,CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("新增黑名单失败");
                return new ResponseResult(false, CommonCode.INVALID_PARAM);
            }
        }
        return new ResponseResult(false, CommonCode.INVALID_PARAM);
    }
    @Transactional
    @Override
    public ResponseResult deleteBlackList(List<Integer> list) {
        try {
            Integer max=blacklistDao.selectMaxId();
            for (Integer integer : list) {
                if(max<integer){
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                blacklistDao.deleteBlackList(integer);
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


    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveStandingBook(Integer archiveId,Integer companyId,StandingBookInfo standingBookInfo) {
        if(standingBookInfo instanceof StandingBookInfo ){
            StandingBook standingBook=new StandingBook();
            if(standingBookInfo.getStandingBookVo().getStandingBookId()==null){
                //说明是新增操作
                //新增台账属性
                try {
                    BeanUtils.copyProperties(standingBookInfo.getStandingBookVo(),standingBook);
                    standingBook.setArchiveId(archiveId);
                    standingBook.setCompanyId(companyId);
                    standingBook.setCreatorId(archiveId);
                    standingBookDao.insert(standingBook);
                } catch (Exception e) {
                    logger.error("新增台账属性失败，台账筛选表无法设置");
                    return new ResponseResult<>(false, CommonCode.FAIL);
                }
                //设置台账属性表的id给筛选表
                try {
                    for (StandingBookFilterVo standingBookFilterVo : standingBookInfo.getListVo()) {
                        StandingBookFilter standingBookFilter=new StandingBookFilter();
                        BeanUtils.copyProperties(standingBookFilterVo,standingBookFilter);
                        standingBookFilter.setStandingBookId(standingBook.getStandingBookId());
                        standingBookFilter.setOperatorId(archiveId);
                        standingBookFilter.setSqlStr(getSql(standingBookFilterVo));
                        standingBookFilterDao.insert(standingBookFilter);
                    }
                    return new ResponseResult(true,CommonCode.SUCCESS);
                } catch (Exception e) {
                    logger.error("新增台账筛选表失败");
                    return new ResponseResult<>(false, CommonCode.FAIL);
                }
            }
            //说明是更新操作
            try {
                BeanUtils.copyProperties(standingBookInfo.getStandingBookVo(),standingBook);
                standingBook.setArchiveId(archiveId);
                standingBook.setCompanyId(companyId);
                standingBook.setCreatorId(archiveId);
                standingBookDao.insert(standingBook);
                standingBookDao.updateByPrimaryKeySelective(standingBook);
                for (StandingBookFilterVo standingBookFilterVo : standingBookInfo.getListVo()) {
                    StandingBookFilter standingBookFilter=new StandingBookFilter();
                    BeanUtils.copyProperties(standingBookFilterVo,standingBookFilter);
                    standingBookFilter.setStandingBookId(standingBook.getStandingBookId());
                    standingBookFilter.setOperatorId(archiveId);
                    standingBookFilter.setSqlStr(getSql(standingBookFilterVo));
                    standingBookFilterDao.updateByPrimaryKey(standingBookFilter);
                }
                return new ResponseResult(true,CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("更新台账表失败");
                return new ResponseResult<>(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(false, CommonCode.INVALID_PARAM);
    }
    private String getSql(StandingBookFilterVo standingBookFilterVo) {
        return null;
    }

    @Override
    public ResponseResult selectStandingBook(Integer id) {
        try {
            StandingBook standingBook = standingBookDao.selectByPrimaryKey(id);
            List<StandingBookFilter> list=standingBookFilterDao.selectByStandingBookId(id);
            StandingBookInfo standingBookInfo=new StandingBookInfo();
            standingBookInfo.setList(list);
            standingBookInfo.setStandingBook(standingBook);
            return  new ResponseResult(standingBookInfo,CommonCode.SUCCESS);
        } catch (Exception e) {
           logger.error("台账查询失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult selectMyStandingBook(Integer archiveId) {
        try {
            List<StandingBook> list=standingBookDao.selectByAchiveId(archiveId);
            return  new ResponseResult(list,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询我的台账失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult selectMyStandingBookShare(Integer archiveId, Integer companyId) {
        try {
            List<StandingBook> list=new ArrayList<>();
            List<StandingBook> shareList=standingBookDao.selectShare(companyId);
            list.addAll(standingBookDao.selectByAchiveId(archiveId));
            list.addAll(shareList);
            return  new ResponseResult(list,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询我的台账失败,含共享");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult selectStaff(Integer stangdingBookId, String archiveType, Integer id, String type) {
        try {
            //根据台账id查询sql拼接串
            String sql=standingBookFilterDao.selectSqlById(stangdingBookId);
            //根据条件查询人员信息
            List<Integer> integerList=userArchiveDao.selectStaffNoType(sql,archiveType,id);
            List<UserArchive> list=userArchiveDao.selectStaff(integerList);
            return  new ResponseResult(list,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("台账查询失败");
            return new ResponseResult<>(false, CommonCode.FAIL);
        }
    }

}
