package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomFieldDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.*;
import com.qinjee.masterdata.model.entity.*;
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
 *
 */
@Service
public class StaffArchiveServiceImpl implements IStaffArchiveService {
//    private static final Logger logger = LoggerFactory.getLogger(StaffArchiveServiceImpl.class);
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
    private CustomFieldDao customFieldDao;
    @Autowired
    private UserOrgAuthDao userOrgAuthDao;
    @Autowired
    private OrganizationDao organizationDao;

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
    public void updateArchiveField(Map<Integer, String> map) {

        customFieldDao.updatePreEmploymentField(map);
    }

    @Override
    public PageResult<UserArchive> selectArchivebatch(UserSession userSession, Integer companyId) {
        List<UserArchive> list = new ArrayList<>();
        List<UserArchive> list1 = new ArrayList<>();
        //本用户的权限下有哪些机构
        List<Integer> orgList = userOrgAuthDao.selectCompanyIdByArchive(userSession.getArchiveId());
        if (companyId == null) {
            if (orgList != null) {
                for (Integer integer : orgList) {
                    //展示所有权限机构下的人员
                    List<Integer> achiveList = userOrgAuthDao.selectArchiveIdByOrg(integer);
                    list1 = userArchiveDao.selectByPrimaryKeyList(achiveList);
                    list.addAll(list1);
                }
                return new PageResult<>(list);
            } else {
                //展示自己的档案
                list.add(userArchiveDao.selectByPrimaryKey(userSession.getArchiveId()));
                return new PageResult<>(list);

            }
        } else {
            if (orgList != null) {
                for (Integer integer : orgList) {
                    //如果查看的在权限之内
                    if (companyId.equals(integer)) {
                        //展示机构下的人员信息
                        List<Integer> achiveList = userOrgAuthDao.selectArchiveIdByOrg(integer);
                        list1 = userArchiveDao.selectByPrimaryKeyList(achiveList);
                        list.addAll(list1);
                    }
                }

                return new PageResult<>(list);
            } else {
                return null;
            }
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
        String orgName = organizationDao.selectOrgName(id);
        return orgName;
    }

    @Override
    public PageResult<UserArchive> selectArchiveByQueryScheme(Integer schemeId, Integer orgId) {
        String select = null;
        String order = null;
        PageResult pageResult = new PageResult();
        /*
        根据查询方案id，找到对应的字段id与顺序和排序id与升降序
         */
            Map<Integer, Integer> fieldMap = new HashMap<>();
            List<Integer> fieldIdList = querySchemeFieldDao.selectFieldId(schemeId);
            List<Integer> fieldSortList = querySchemeFieldDao.selectFieldSort(schemeId);
            for (int i = 0; i < fieldIdList.size(); i++) {
                fieldMap.put(fieldSortList.get(i), fieldIdList.get(i));
            }
            Set<Integer> sorts = fieldMap.keySet();
            List<Integer> sortlist = new ArrayList();
            //先for each 遍历到list中（还能够通过迭代来放到list中，还能够通过for循环放到list中）
            for (Integer value : sorts) {
                sortlist.add(value);
            }
            //再排序
            Collections.sort(sortlist);
            //将字段排序按照顺序拼接成查询项
            for (Integer integer : sortlist) {
                //根据id查询字段名
                String s = customFieldDao.selectFieldName(integer);
                select = select + s + ",";
            }
        /*
        根据查询方案id，找到排序id与升降序
         */
            Map<Integer, String> sortMap = new HashMap<>();
            List<Integer> sortIdList = querySchemeSortDao.selectSortId(schemeId);
            List<String> sortSortList = querySchemeSortDao.selectSortSort(schemeId);
            for (int i = 0; i < sortIdList.size(); i++) {
                sortMap.put(sortIdList.get(i), sortSortList.get(i));
            }
            Set<Integer> integers = sortMap.keySet();
            for (Integer integer : integers) {
                String s = customFieldDao.selectFieldName(integer);
                order = order + s + getSort(sortMap.get(integer)) + ",";
            }
        /*
        进行sql查询，返回数据，档案在机构范围内
         */
            List<UserArchive> userArchiveList = userArchiveDao.getUserArchiveListCustom(select, order, orgId);

            return new PageResult<>(userArchiveList);
        }


    public String getSort(String sort) {
        if (sort != null) {
            if (sort.equals("升序")) {
                return "ASC";
            }
            if (sort.equals("降序")) {
                return "DESC";
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserArchivePostRelation(List<Integer> list) throws Exception {

        Integer max = userArchivePostRelationDao.selectMaxPrimaryKey();
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i)) {
                throw new Exception("id不合法");
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
        return new PageResult(relationList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQueryScheme(List<Integer> list) throws Exception {
                Integer max = querySchemeDao.selectMaxPrimaryKey();
                for (int i = 0; i < list.size(); i++) {
                    if (max < list.get(i)) {
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
    public void saveQueryScheme(QueryScheme queryScheme, List<QuerySchemeField> querySchemeFieldlist,
                                List<QuerySchemeSort> querySchemeSortlist) {

        if (queryScheme.getQuerySchemeId() == null) {
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
        for (int i = 0; i < querySchemeFieldlist.size(); i++) {
            querySchemeFieldDao.updateByPrimaryKey(querySchemeFieldlist.get(i));
        }
        for (int i = 0; i < querySchemeSortlist.size(); i++) {
            querySchemeSortDao.updateByPrimaryKey(querySchemeSortlist.get(i));
        }
    }
}






