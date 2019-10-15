package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDataDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.staffstandingbookdao.StandingBookDao;
import com.qinjee.masterdata.dao.staffdao.staffstandingbookdao.StandingBookFilterDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchivePostRelationDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookFilterVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.masterdata.service.staff.IStaffStandingBookService;
import com.qinjee.masterdata.utils.GetDayUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        return list;

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
    public List<UserArchive> selectStaff(Integer stangdingBookId, String archiveType, Integer orgId, String type) throws ParseException {
        List<Integer> oneList = userArchiveDao.selectStaffNoStandingBook(archiveType, orgId);
        //存储大数据表字段解析出的档案id
        List<Integer> list1 = new ArrayList<>();
        //存储物理表字段解析出的档案id
        List<Integer> list2 = new ArrayList<>();
        //key是用来存是第几个筛选条件，value存档案id(可能多个)
        Map<Integer, List<Integer>> map = new HashMap<>();
        //没经过台账之前筛选的档案id
        List<Integer> twoList = userArchivePostRelationDao.selectByType(type, oneList);
        //筛选后的档案id
        List<Integer> threeList = new ArrayList<>();
        List<UserArchive> list = userArchiveDao.selectByPrimaryKeyList(twoList);
        //通过台账id找到台账筛选表，直接返回台账筛选表对象
        List<StandingBookFilter> filters = standingBookFilterDao.selectByStandingBookId(stangdingBookId);
        //临时存储的档案集合
        List<UserArchive> tempList=new ArrayList<>();
        for (int i = 0; i < filters.size(); i++) {
            //通过字段id找到表id
            Integer tableId = getTableId(filters.get(i).getFieldId());
            //判断是否为内置，内置的话直接返回档案id。
            boolean inside = isInside(tableId);
            if (!inside) {
                //若不是内置，根据表id找到数据大字段,进行JSON解析。@+字段名+@为key，返回业务id
                List<CustomArchiveTableData> bigdata = getBigdata(tableId);
                for (CustomArchiveTableData bigdatum : bigdata) {
                    JSONObject jsonObject = JSONObject.parseObject(bigdatum.getBigData());
                    if (getCondition(filters.get(i).getOperateSymbol(), jsonObject.get("@" + getFieldName(filters.get(i).getFieldId()) + "@"),
                            filters.get(i).getFieldValue())) {

                        list1.add(bigdatum.getBusinessId());
                        map.put(i, list1);
                    }

                }
            }
            //是内置表，根据字段名获取物理字段名，筛选返回人员档案id
            for (UserArchive userArchive : list) {
                if (getCondition(filters.get(i).getOperateSymbol(), getFieldValueByName(getPhysicName(filters.get(i).getFieldId()), userArchive),
                        filters.get(i).getFieldValue())) {
                    list2.add(userArchive.getArchiveId());
                    map.put(i, list2);
                }
            }
        }
        Map<Integer, Integer> linkFlag = getLinkFlag(filters);
        if (linkFlag != null) {
            ArrayList<Integer> integers = new ArrayList<>(map.keySet());
            for (Map.Entry<Integer, Integer> integerIntegerEntry : linkFlag.entrySet()) {
                for (int i = 0; i < integers.size() - 1; i++) {
                    String linkHandle = getLinkHandle(integers.get(i));
                    if(integers.get(i)>integerIntegerEntry.getKey() && integers.get(i)<integerIntegerEntry.getValue()){
                        map.remove(integers.get(i));
                        map.put(integers.get(i), getArichivIdList(map.get(integers.get(i)), map.get(integers.get(i + 1)), linkHandle));
                        tempList = userArchiveDao.selectByPrimaryKeyList(map.get(integers.get(integers.size()-1)));
                        map.remove(integers.get(i));
                        map.remove(integers.get(i+1));
                    }
                }
            }
            ArrayList<Integer> integers1 = new ArrayList<>(map.keySet());
            for (int i = 0; i < integers1.size()-1; i=i++) {
                List<UserArchive> list3 = getUserArchives(map, integers, i);
                tempList.addAll(list3);
                return tempList;
            }

        }else {
            ArrayList<Integer> integers = new ArrayList<>(map.keySet());
            for (int i = 0; i < integers.size()-1; i++) {
                List<UserArchive> list3 = getUserArchives(map, integers, i);
                return list3;
            }
        }

        return null;
    }

    private List<UserArchive> getUserArchives(Map<Integer, List<Integer>> map, ArrayList<Integer> integers, int i) {
        String linkHandle = getLinkHandle(integers.get(i));
        map.remove(integers.get(i));
        map.put(integers.get(i), getArichivIdList(map.get(integers.get(i)), map.get(integers.get(i + 1)), linkHandle));
        return userArchiveDao.selectByPrimaryKeyList(map.get(integers.get(integers.size() - 1)));
    }

    public List<Integer> getArichivIdList(List<Integer> list1,List<Integer> list2,String handleLink){
        if(list2==null){
            return list1;
        }else {
            if ("或者".equals(handleLink)) {
                list1.addAll(list2);
                return list1;
            }
            if ("并且".equals(handleLink)) {
                list1.retainAll(list2);
            }
        }
        return null;
    }
    public String getLinkHandle(Integer id){
        String handleLink=standingBookFilterDao.selectLinkHandleById(id);
        return handleLink;
    }

    //通过字段id找到物理字段名
    public String getPhysicName(Integer fieldId){
        String physicName=customArchiveFieldDao.selectPhysicName(fieldId);
        return physicName;
    }
    //通过字段id找到字段名
    public String getFieldName(Integer fieldId){
        String fieldName=customArchiveFieldDao.selectFieldName(fieldId);
        return fieldName;
    }
    //通过字段id找到表id
    public Integer getTableId(Integer fieldId) {
        Integer tableId = customArchiveFieldDao.selectTableId(fieldId);
        return tableId;
    }

    //判断表是否内置
    public boolean isInside(Integer tableId) {
        Integer integer = customArchiveTableDao.selectInside(tableId);
        if (integer > 0) {
            return true;
        }
        return false;
    }
    // 根据属性名获取属性值
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {

            return null;
        }
    }


    //找到物理表名
//    public String getTableName(Integer tableId) {
//        boolean inside = isInside(tableId);
//        if (inside) {
//            String tableName = customArchiveTableDao.selectTableName(tableId);
//            return tableName;
//        }
//        return null;
//    }

    //根据表id找到数据表

    public List<CustomArchiveTableData> getBigdata(Integer tableId) {
        List<CustomArchiveTableData> list = customArchiveTableDataDao.selectByTableId(tableId);
        return list;
    }
    //根据筛选表的括号以及连接符进行选择合并
    public Map<Integer,Integer> getLinkFlag(List<StandingBookFilter> filters){
        //j代表左括号开始的筛选表，k代表右括号结束的筛选表
        Map<Integer,Integer> map=null;
        Integer j=null;
        Integer k=null;
        for (int i = 0; i < filters.size(); i++) {
            if(filters.get(i).getIsLeftBrackets()==1){
                j=i;
            }
            if(filters.get(i).getIsRightBrackets()==1){
                k=i;
            }
            if(j!=null && k!=null){
                map.put(j,k);
            }
        }
        return map;
    }

    //获得运算符
    public boolean getCondition(String symbol, Object o2,String o1) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd");
        if (o1.matches("^[0-9]*$ ")) {
            int i = Integer.parseInt(o1);
            switch (symbol) {
                case ">":
                    return (Integer) o2>i;
                case ">=":
                    return  (Integer) o2>=i;
                case "<":
                    return  (Integer) o2<i;
                case "<=":
                    return  (Integer) o2<=i;
                case "=":
                    return  (Integer) o2==i;
                case "!=":
                    return  (Integer) o2!=i;
                //如果运算符是包含，说明是代码型
                case "包含":
                    return  o2.equals(o1);
                case "不包含":
                    return !o2.equals(o1);
                default:
                    return false;
            }
        } else if (o1.matches("^(\\d{4})(\\-)(\\d{2})(\\-)(\\d{2})(\\s+)(\\d{2})(\\:)(\\d{2})(\\:)(\\d{2})$")) {
            Date parse1 = sdf.parse(o1);
            Date parse2 = sdf.parse((String) o2);
            return dateCondition(symbol, parse1, parse2);
        }else if (o1.matches("\\d{4}-(((0[1-9])|(1[0-2])))(-((0[1-9])|([1-2][0-9])|(3[0-1])))?")) {
            Date parse1 = sdf2.parse(o1);
            Date parse2 = sdf2.parse((String) o2);
            return dateCondition(symbol, parse1, parse2);
        }
        else {
            switch (symbol) {
                case "等于":
                    return o2.equals(o1);
                case "包含":
                    return  String.valueOf(o2).contains(o1);
                case "不等于":
                    return !o2.equals(o1);
                case "不包含":
                    return !String.valueOf(o2).contains(o1);
                default:
                    return false;
            }

        }

    }

    private boolean dateCondition(String symbol, Date parse1, Date parse2) {
        switch (symbol) {
            case ">":
                return GetDayUtil.getDay(parse2, parse1) > 0;
            case ">=":
                return GetDayUtil.getDay(parse2, parse1) >= 0;
            case "<":
                return GetDayUtil.getDay(parse2, parse1) < 0;
            case "<=":
                return GetDayUtil.getDay(parse2, parse1) <= 0;
            case "=":
                return GetDayUtil.getDay(parse2, parse1) == 0;
            case "!=":
                return GetDayUtil.getDay(parse2, parse1) != 0;
            default:
                return false;
        }
    }


}
