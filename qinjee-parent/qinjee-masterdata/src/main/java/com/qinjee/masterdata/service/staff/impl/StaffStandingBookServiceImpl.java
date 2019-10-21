package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDataDao;
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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class StaffStandingBookServiceImpl implements IStaffStandingBookService {
    private static final Logger logger = LoggerFactory.getLogger(StaffStandingBookServiceImpl.class);
    private static final String ARCHIVE="档案";
    private static final String TYPEDATE="DATE";
    private static final String TYPENUMBER="NUMBER";
    private static final String TYPETEXT="TEXT";
    private static final String TYPECODE="CODE";
    private static final String DENGYU="等于";
    private static final String BUDENGYU="不等于";
    private static final String BAOHAN="包含";
    private static final String BUBAOHAN="不包含";
    private static final String AND="并且";
    private static final String OR="或者";

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
    @Autowired
    private CustomArchiveTableDao customArchiveTableDao;
    @Autowired
    private CustomArchiveFieldDao customArchiveFieldDao;
    @Autowired
    private CustomArchiveTableDataDao customArchiveTableDataDao;

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
                standingBookFilter.setSqlStr(getWhereSql(standingBookFilter));
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
            standingBookFilter.setSqlStr(getWhereSql(standingBookFilter));
            standingBookFilterDao.updateByPrimaryKey(standingBookFilter);
        }
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

        return standingBookDao.selectByAchiveId(userSession.getArchiveId());

    }

    @Override
    public List<StandingBook> selectMyStandingBookShare(UserSession userSession) {

        List<StandingBook> list = new ArrayList<>();
        List<StandingBook> shareList = standingBookDao.selectShare(userSession.getCompanyId());
        list.addAll(standingBookDao.selectByAchiveId(userSession.getArchiveId()));
        list.addAll(shareList);
        return list;
    }

    @Override
    public List<UserArchive> selectStaff(Integer stangdingBookId, String archiveType, Integer orgId, String type) {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(" where");
        List<Integer> oneList = userArchiveDao.selectStaffNoStandingBook(archiveType, orgId);
        //存储大数据表字段解析出的档案id
        //key是用来存是第几个筛选条件，value存档案id(可能多个)
        //没经过台账之前筛选的档案id
        List<Integer> twoList = userArchivePostRelationDao.selectByType(type, oneList);
        //通过id查询档案
        List<UserArchive> list = userArchiveDao.selectByPrimaryKeyList(twoList);
        //通过台账id找到台账筛选表，直接返回台账筛选表对象
        List<StandingBookFilter> filters = standingBookFilterDao.selectByStandingBookId(stangdingBookId);
        for (StandingBookFilter filter : filters) {
            String whereSql = getWhereSql(filter);
            stringBuffer.append(whereSql);
        }
        String baseSql = getBaseSql(orgId);
        String sql=baseSql+stringBuffer.toString();
        List<Integer> integerList=userArchiveDao.selectStaff(sql);
        List<UserArchive> userArchives = userArchiveDao.selectByPrimaryKeyList(integerList);
        userArchives.retainAll(list);
        return userArchives;
    }

    private String getBaseSql(Integer orgId){
        List<String> fieldNameNotInside = getFieldNameNotInside(orgId);
        List<String> custom=new ArrayList<>();
        StringBuffer stringBuffer=new StringBuffer();
        StringBuffer stringBuffer2=new StringBuffer();
        String a="select t.archiveId from";
        String b="( select t0.* ";
        for (String s1 : fieldNameNotInside) {
            stringBuffer.append(s1).append(",");
            custom.add("substring_index(SUBSTRING(t2.big_data,instr(t2.big_data,"+
                    "'@@"+s1+"@@')+LENGTH('@@"+s1+"@@')+1),';@@',1),");
        }
        String c="from t_user_archive t0,t_custom_archive_table t1,t_custom_archive_table_data t2 ";
        String d="where t0.company_id = " +1 +
                "and t1.func_code = 'ARCHIVE'\n" +
                "and t0.company_id = t1.company_id\n" +
                "and t2.table_id=t1.table_id "+
                "and t0.archive_id = t2.business_id";
        String e=")t";
        for (String s : custom) {
            stringBuffer2.append(s);
        }
        int i = stringBuffer2.toString().lastIndexOf(",");
        String substring = stringBuffer2.toString().substring(0, i);
        String s = stringBuffer.toString();
        return a+b+s+substring+c+d+e;
    }

    /**
     * @return String
     * 字段类型分四种：
     * 日期 DATE，数字 NUMBER，代码 CODE，文本 TEXT
     */
    private String getWhereSql(StandingBookFilter filter){
        String physicName = getPhysicName(filter.getFieldId());
        String condition=null;
        //根据id获得字段类型
        String type=customArchiveFieldDao.selectTypeByFieldId(filter.getFieldId());
        if(type!=null) {
            if (TYPEDATE.equals(type) || TYPENUMBER.equals(type)) {
                condition = physicName + "" + filter.getOperateSymbol() + "" + filter.getFieldValue();
            }
            if (TYPETEXT.equals(type)) {
                if (DENGYU.equals(filter.getOperateSymbol())) {
                    condition = physicName + " = " + filter.getFieldValue();
                }
                if (BUDENGYU.equals(filter.getOperateSymbol())) {
                    condition = physicName + " != " + filter.getFieldValue();
                }
                if (BAOHAN.equals(filter.getOperateSymbol())) {
                    condition = physicName + " like " + " %" + filter.getFieldValue() + "% ";
                }

                if (BUBAOHAN.equals(filter.getOperateSymbol())) {
                    condition = physicName + "not like" + " %" + filter.getFieldValue() + "% ";
                }
            }
            if (TYPECODE.equals(type)) {
                if (BAOHAN.equals(filter.getOperateSymbol())) {
                    condition = physicName + " = " + filter.getFieldValue();
                }

                if (BUBAOHAN.equals(filter.getOperateSymbol())) {
                    condition = physicName + "not like" + " %" + filter.getFieldValue() + "% ";
                }
            }
        }
        if(filter.getIsLeftBrackets()!=null) {
            if (!StringUtils.isEmpty(filter.getIsLeftBrackets())) {
                return "(" + condition + getLinkSymbol(filter);
            }
            if (!StringUtils.isEmpty(filter.getIsRightBrackets())) {
                return condition + ")";
            }
        }else {
            return condition+getLinkSymbol(filter);
        }

        return null;
    }
    //找到非内置字段的物理字段名
    private List<String> getFieldNameNotInside(Integer companyId){
        //找到企业下的人员表
        List<Integer> tableIdList=customArchiveTableDao.selectNotInsideTableId(companyId,ARCHIVE);
        //根据id找到物理字段名
        return customArchiveFieldDao.selectFieldNameListByTableIdList(tableIdList);


    }
    //通过字段id找到物理字段名
    private String getPhysicName(Integer fieldId){
        return customArchiveFieldDao.selectPhysicName(fieldId);
    }
    private String getLinkSymbol(StandingBookFilter filter){
        if(AND.equals(filter.getLinkSymbol())){
            return "AND";
        }
        if(OR.equals(filter.getLinkSymbol())){
            return "OR";
        }
        return null;
    }

}
