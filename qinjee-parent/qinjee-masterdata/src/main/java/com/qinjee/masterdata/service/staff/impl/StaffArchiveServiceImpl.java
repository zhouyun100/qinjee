package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveFieldDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomArchiveTableDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.*;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.QueryArcVo;
import com.qinjee.masterdata.model.vo.staff.QuerySchemeList;
import com.qinjee.masterdata.model.vo.staff.UserArchivePostRelationVo;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Administrator
 */
@Service
public class StaffArchiveServiceImpl implements IStaffArchiveService {
    //    private static final Logger logger = LoggerFactory.getLogger(StaffArchiveServiceImpl.class);
    private static final String ASC = "升序";
    private static final String DESC = "降序";
    private static final String ARCHIVE = "ARCHIVE";
    @Autowired
    private UserArchivePostRelationDao userArchivePostRelationDao;
    @Autowired
    private QuerySchemeDao querySchemeDao;
    @Autowired
    private QuerySchemeFieldDao querySchemeFieldDao;
    @Autowired
    private QuerySchemeSortDao querySchemeSortDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private CustomArchiveFieldDao customArchiveFieldDao;
    @Autowired
    private UserOrgAuthDao userOrgAuthDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private CustomArchiveTableDao customArchiveTableDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArchiveById(List<Integer> archiveid) throws Exception {
        Integer max = userArchiveDao.selectMaxId();
        for (Integer integer : archiveid) {
            if (integer > max) {
                throw new Exception("id不合理");
            }
        }
        userArchiveDao.deleteArchiveById(archiveid);

    }

    @Override
    public void resumeDeleteArchiveById(Integer archiveid) throws Exception {
        Integer max = userArchiveDao.selectMaxId();
        if (archiveid > max) {
            throw new Exception("id不合理");
        }
        userArchiveDao.resumeDeleteArchiveById(archiveid);
    }

    @Override
    public void updateArchive(UserArchive userArchive) {
        userArchiveDao.updateByPrimaryKeySelective(userArchive);
    }

    @Override
    public UserArchive selectArchive(UserSession userSession) {
        return userArchiveDao.selectByPrimaryKey(userSession.getArchiveId());

    }

    @Override
    public void insertArchive(UserArchive userArchive) {
        userArchiveDao.insertSelective(userArchive);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArchiveField(Map<Integer, String> map) {
        customArchiveFieldDao.updatePreEmploymentField(map);
    }

    @Override
    public PageResult<UserArchive> selectArchivebatch(UserSession userSession, Integer companyId) {
        List<UserArchive> list = new ArrayList<>();
        //本用户的权限下有哪些机构
        List<Integer> orgList = userOrgAuthDao.selectCompanyIdByArchive(userSession.getArchiveId());
        if (orgList != null) {
            for (Integer integer : orgList) {
                //如果查看的在权限之内
                if (companyId.equals(integer)) {
                    //展示机构下的人员信息
                    List<Integer> achiveList = userOrgAuthDao.selectArchiveIdByOrg(integer);
                    list = userArchiveDao.selectByPrimaryKeyList(achiveList);
                }
            }
            return new PageResult<>(list);
        } else {
            return null;
        }
    }
    @Override
    public void insertUserArchivePostRelation(UserArchivePostRelationVo userArchivePostRelationVo, UserSession userSession) {
        UserArchivePostRelation userArchivePostRelation = new UserArchivePostRelation();
        BeanUtils.copyProperties(userArchivePostRelationVo, userArchivePostRelation);
        //通过工号查询档案id
        Integer id = userArchiveDao.selectArchiveIdByNumber(userArchivePostRelationVo.getEmployeeNumber());
        userArchivePostRelation.setArchiveId(id);
        userArchivePostRelation.setOperatorId(userSession.getArchiveId());
        userArchivePostRelation.setIsDelete((short) 1);
        userArchivePostRelationDao.insertSelective(userArchivePostRelation);
    }

    @Override
    public Map<String, String> selectNameAndNumber(Integer id) {
        Map<String, String> map = new HashMap<>();
        String name = userArchiveDao.selectName(id);
        String number = userArchiveDao.selectNumber(id);
        map.put("name", name);
        map.put("number", number);
        return map;
    }
    @Override
    public String selectOrgName(Integer id) {
        return organizationDao.selectOrgName(id);
    }

    @Override
    @Transactional
    public Map<Integer, Map<String, Object>> selectArchiveByQueryScheme(Integer schemeId, UserSession userSession,List<Integer> archiveIdList) {
        StringBuffer stringBuffer = new StringBuffer();
        String order=null;
        //根据查询方案id，找到对应的字段id与顺序和  排序id与升降序
        //查询字段排序sort
        List<Integer> fieldSortList = querySchemeFieldDao.selectFieldSort(schemeId);
        Collections.sort(fieldSortList);
        //将字段排序按照顺序拼接成查询项
        //根据排序id找到字段id
        List<Integer> sortList = querySchemeFieldDao.selectIdBySortList(fieldSortList);
        //根据id查询字段名
        List<String> stringList = customArchiveFieldDao.selectFieldNameByList(sortList);
        System.out.println(sortList.get(0));


        //根据查询方案id，找到排序id与升降序
        //查询查询档案下的排序字段id
        List<Integer> integerList = querySchemeSortDao.selectSortId(schemeId);
        //根据id查询字段名
        List<String> strings = customArchiveFieldDao.selectFieldNameByList(integerList);
        //查询升降序
        List<String> list = querySchemeSortDao.selectSortByIdList(integerList);
        Map<String, String> map = new HashMap<>();
        for (int j = 0; j < strings.size(); j++) {
            map.put(strings.get(j), list.get(j));
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringBuffer.append(entry.getKey()+"\t");
            stringBuffer.append(getSort(entry.getValue()));
            stringBuffer.append(",");
            order = stringBuffer.toString();
        }
        int i = order.lastIndexOf(",");
        order = order.substring(0, i);
        //调用接口查询机构权限范围内的档案集合
        //进行sql查询，返回数据，档案在机构范围内

        Map<Integer,Map<String,Object>> userArchiveListCustom =
                userArchiveDao.getUserArchiveListCustom(getBaseSql(userSession.getCompanyId(), stringList,userSession.getCompanyId()),order);
        ArrayList<Integer> integers = new ArrayList<>(userArchiveListCustom.keySet());
        archiveIdList.retainAll(integers);
        Map<Integer,Map<String,Object>> mapMap=new HashMap<>();
        for (Integer integer : archiveIdList) {
            mapMap.put(integer,userArchiveListCustom.get(integer));
        }
        return mapMap;
    }
    //list是所查询项的集合
    private String getBaseSql(Integer orgId,List<String> strings,Integer companyId){
        List<String> fieldNameNotInside = getFieldNameNotInside(orgId);
        List<String> custom=new ArrayList<>();
        StringBuffer stringBuffer=new StringBuffer();
        StringBuffer stringBuffer2=new StringBuffer();
        StringBuffer stringBuffer3=new StringBuffer();
        stringBuffer3.append("select  t.archive_id ,");
        for (String string : strings) {
            stringBuffer3.append("t.").append(string).append(",");
        }
        int i1 = stringBuffer3.toString().lastIndexOf(",");
        String a=stringBuffer3.toString().substring(0,i1);
        a=a+" from( select t0.* ,";
        System.out.println(a);
        for (String s1 : fieldNameNotInside) {
            custom.add("substring_index(SUBSTRING(t2.big_data,instr(t2.big_data,'@@"+s1+"@@')+LENGTH('@@"+s1+"@@')+1),';@@',1)as "+s1+"\t"+",");
        }
        for (String s : custom) {
            stringBuffer2.append(s);
        }
        int i = stringBuffer2.toString().lastIndexOf(",");
        String substring = stringBuffer2.toString().substring(0, i);
        System.out.println(substring);
        String c="from t_user_archive t0,t_custom_archive_table t1,t_custom_archive_table_data t2 where t0.company_id = " + companyId +"\t"+"and t1.func_code = 'ARCHIVE'"+"\t"+"and t0.company_id = t1.company_id and t2.table_id=t1.table_id"+"\t"+" and t0.archive_id = t2.business_id)t"+"\t";
        return a+substring+c;
    }
    //找到非内置字段的物理字段名
    private List<String> getFieldNameNotInside(Integer companyId){
        //找到企业下的人员表
        List<Integer> tableIdList=customArchiveTableDao.selectNotInsideTableId(companyId,ARCHIVE);
        //根据id找到物理字段名
        return customArchiveFieldDao.selectFieldNameListByTableIdList(tableIdList);
    }
    private String getSort(String sort) {
        if (sort != null) {
            if (sort.equals(ASC)) {
                return "ASC";
            }
            if (sort.equals(DESC)) {
                return "DESC";
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserArchivePostRelation(List<Integer> list) throws Exception {
        Integer max = userArchivePostRelationDao.selectMaxPrimaryKey();
        for (Integer integer : list) {
            if(max<integer) {
                throw new Exception("id有误");
            }
        }
        userArchivePostRelationDao.deleteUserArchivePostRelation(list);
    }

    @Override
    public void updateUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        userArchivePostRelationDao.updateByPrimaryKeySelective(userArchivePostRelation);
    }

    @Override
    public PageResult<UserArchivePostRelation> selectUserArchivePostRelation(Integer currentPage, Integer pageSize,
                                                                             List<Integer> list) {
        PageHelper.startPage(currentPage, pageSize);
        List<UserArchivePostRelation> relationList = userArchivePostRelationDao.selectByPrimaryKeyList(list);
        return new PageResult<>(relationList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQueryScheme(List<Integer> list) throws Exception {
        Integer max = querySchemeDao.selectMaxPrimaryKey();
        for (Integer integer : list) {
            if(max<integer) {
                throw new Exception("id有误");
            }
        }
        querySchemeDao.deleteQuerySchemeList(list);
        //删除查询字段
        querySchemeFieldDao.deleteBySchemeIdList(list);
        //删除排序字段
        querySchemeSortDao.deleteBySchemeIdList(list);
    }


    @Override
    public QuerySchemeList selectQueryScheme(Integer id) {
        QuerySchemeList querySchemeList = new QuerySchemeList();
        //通过查询方案id找到显示字段
        List<QuerySchemeField> querySchemeFields = querySchemeFieldDao.selectByQuerySchemeId(id);
        //通过查询方案id找到排序字段
        List<QuerySchemeSort> querySchemeSorts = querySchemeSortDao.selectByQuerySchemeId(id);
        querySchemeList.setQuerySchemeFieldList(querySchemeFields);
        querySchemeList.setQuerySchemeSortList(querySchemeSorts);
        return querySchemeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQueryScheme(QueryArcVo queryArcVo) {
        QueryScheme queryScheme = queryArcVo.getQueryScheme();
        List<QuerySchemeField> querySchemeFieldlist = queryArcVo.getQuerySchemeFieldlist();
        List<QuerySchemeSort> querySchemeSortlist = queryArcVo.getQuerySchemeSortlist();
        if (queryScheme.getQuerySchemeId() == null || queryScheme.getQuerySchemeId()==0) {
            //说明是新增操作，新增查询方案
            querySchemeDao.insertSelective(queryScheme);
            //新增查询字段与排序
            for (QuerySchemeField querySchemeField : querySchemeFieldlist) {
                querySchemeField.setQuerySchemeId(queryScheme.getQuerySchemeId());
            }
            for (QuerySchemeSort querySchemeSort : querySchemeSortlist) {
                querySchemeSort.setQuerySchemeId(queryScheme.getQuerySchemeId());
            }

            querySchemeFieldDao.insertBatch(querySchemeFieldlist);
            querySchemeSortDao.insertBatch(querySchemeSortlist);
        }
        //说明是更新操作
        //将list里面的queryschemeId设置为传过来的id
        for (QuerySchemeField querySchemeField : querySchemeFieldlist) {
            querySchemeField.setQuerySchemeId(queryScheme.getQuerySchemeId());
        }
        for (QuerySchemeSort querySchemeSort : querySchemeSortlist) {
            querySchemeSort.setQuerySchemeId(queryScheme.getQuerySchemeId());
        }
        querySchemeDao.updateByPrimaryKeySelective(queryScheme);
        for (QuerySchemeField querySchemeField : querySchemeFieldlist) {
            querySchemeFieldDao.updateByPrimaryKey(querySchemeField);
        }
        for (QuerySchemeSort querySchemeSort : querySchemeSortlist) {
            querySchemeSortDao.updateByPrimaryKey(querySchemeSort);
        }
    }

}







