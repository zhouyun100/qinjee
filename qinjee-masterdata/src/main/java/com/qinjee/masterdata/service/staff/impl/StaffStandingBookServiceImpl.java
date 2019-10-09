package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
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
import com.qinjee.model.request.UserSession;
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
    public void insertBlackList(List<BlackListVo> blackListVos, String dataSource, UserSession userSession) {
        for (BlackListVo blacklistVo : blackListVos) {
            Blacklist blacklist = new Blacklist();
            BeanUtils.copyProperties(blacklistVo, blacklist);
            blacklist.setOperatorId(userSession.getArchiveId());
            blacklist.setCompanyId(userSession.getCompanyId());
            blacklist.setDataSource(dataSource);
            blacklistDao.insertSelective(blacklist);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBlackList(List<Integer> list) throws Exception {
        Integer max = blacklistDao.selectMaxId();
        for (Integer integer : list) {
            if (max < integer) {
                throw new Exception("ID失败");
            }
        }
        blacklistDao.deleteBlackList(list);
    }

    @Override
    public void updateBalckList(Blacklist blacklist) {
        blacklistDao.updateByPrimaryKey(blacklist);
    }

    @Override
    public PageResult<Blacklist> selectBalckList(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Blacklist> blacklists = blacklistDao.selectByPage();
        return new PageResult<>(blacklists);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStandingBook(Integer standingBookId) {
        //删除台账属性
        standingBookDao.deleteStandingBook(standingBookId);
        //删除对应筛选表
        standingBookFilterDao.deleteStandingBookFilter(standingBookId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStandingBook(UserSession userSession, StandingBookInfo standingBookInfo) {
        StandingBook standingBook = new StandingBook();
        if (standingBookInfo.getStandingBookVo().getStandingBookId() == null) {
            //说明是新增操作
            //新增台账属性
            BeanUtils.copyProperties(standingBookInfo.getStandingBookVo(), standingBook);
            standingBook.setArchiveId(userSession.getArchiveId());
            standingBook.setCompanyId(userSession.getCompanyId());
            standingBook.setCreatorId(userSession.getArchiveId());
            standingBookDao.insert(standingBook);

            //设置台账属性表的id给筛选表
            for (StandingBookFilterVo standingBookFilterVo : standingBookInfo.getListVo()) {
                StandingBookFilter standingBookFilter = new StandingBookFilter();
                BeanUtils.copyProperties(standingBookFilterVo, standingBookFilter);
                standingBookFilter.setStandingBookId(standingBook.getStandingBookId());
                standingBookFilter.setOperatorId(userSession.getArchiveId());
                standingBookFilter.setSqlStr(getSql(standingBookFilterVo));
                standingBookFilterDao.insert(standingBookFilter);
            }
        }

        //说明是更新操作

        BeanUtils.copyProperties(standingBookInfo.getStandingBookVo(), standingBook);
        standingBook.setArchiveId(userSession.getArchiveId());
        standingBook.setCompanyId(userSession.getCompanyId());
        standingBook.setCreatorId(userSession.getArchiveId());
        standingBookDao.insert(standingBook);
        standingBookDao.updateByPrimaryKeySelective(standingBook);
        for (StandingBookFilterVo standingBookFilterVo : standingBookInfo.getListVo()) {
            StandingBookFilter standingBookFilter = new StandingBookFilter();
            BeanUtils.copyProperties(standingBookFilterVo, standingBookFilter);
            standingBookFilter.setStandingBookId(standingBook.getStandingBookId());
            standingBookFilter.setOperatorId(userSession.getArchiveId());
            standingBookFilter.setSqlStr(getSql(standingBookFilterVo));
            standingBookFilterDao.updateByPrimaryKey(standingBookFilter);
        }
    }

    //根据台账筛选表得到sql
    private String getSql(StandingBookFilterVo standingBookFilterVo) {


        return null;
    }

    @Override
    public StandingBookInfo selectStandingBook(Integer id) {
        StandingBook standingBook = standingBookDao.selectByPrimaryKey(id);
        List<StandingBookFilter> list = standingBookFilterDao.selectByStandingBookId(id);
        StandingBookInfo standingBookInfo = new StandingBookInfo();
        standingBookInfo.setList(list);
        standingBookInfo.setStandingBook(standingBook);
        return standingBookInfo;
    }

    @Override
    public List<StandingBook> selectMyStandingBook(UserSession userSession) {

        List<StandingBook> list = standingBookDao.selectByAchiveId(userSession.getArchiveId());
        return  list;

    }
    @Override
    public List<StandingBook> selectMyStandingBookShare(UserSession userSession) {

            List<StandingBook> list=new ArrayList<>();
            List<StandingBook> shareList=standingBookDao.selectShare(userSession.getCompanyId());
            list.addAll(standingBookDao.selectByAchiveId(userSession.getArchiveId()));
            list.addAll(shareList);
            return list;
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
