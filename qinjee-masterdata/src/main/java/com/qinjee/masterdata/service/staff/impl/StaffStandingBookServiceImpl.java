package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.staffstandingbookdao.StandingBookDao;
import com.qinjee.masterdata.dao.staffdao.staffstandingbookdao.StandingBookFilterDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchivePostRelationDao;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.StandingBook;
import com.qinjee.masterdata.model.entity.StandingBookFilter;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookFilterVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.masterdata.service.staff.IStaffStandingBookService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private UserArchivePostRelationDao userArchivePostRelationDao;

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
    public List<UserArchive> selectStaff(Integer stangdingBookId, String archiveType, Integer orgId, String type) {
        /**
         * 关于运算符的思路：
         */
        List<Integer> oneList=userArchiveDao.selectStaffNoStandingBook(archiveType,orgId);
        List<Integer> twoList=userArchivePostRelationDao.selectByType(type,oneList);
        //通过台账id找到台账筛选表，直接返回台账筛选表对象
        List<StandingBookFilter> filters=standingBookFilterDao.selectByStandingBookId(stangdingBookId);

        //通过字段id找到表id
        //判断是否为内置，内置的话直接返回档案id。
        //若不是内置，根据表id找到数据大字段,进行JSON解析。@+字段名+@为key，返回业务id
        //根据每条返回的链接福确定是去交集还是并集
        //查询没有经过台账筛选出来的id，取交集
        //将此id查询出集合返回
        return null;
    }
    public boolean getCondition(String Symbol,Object o1,Object o2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(o1 instanceof Integer && o2 instanceof Integer){
            if(Symbol.equals(">")){
                return (Integer)o1 > (Integer) o2;
            }
        }
        if(o1 instanceof String && o2 instanceof String){
            try {
                //没有报错说明是时间类型
                Date parse = sdf.parse((String) o1);
                Date parse1 = sdf.parse((String) o2);
            } catch (ParseException e) {
                //作为字符串处理
                if(Symbol.equals("包含")){
                    return ((String) o1).contains((CharSequence) o2);
                }
            }
        }
        return false;
    }
}
