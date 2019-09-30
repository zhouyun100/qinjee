package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchivePostRelationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.CustomFieldDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.*;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.QuerySchemeList;
import com.qinjee.masterdata.model.vo.staff.UserArchivePostRelationVo;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(StaffArchiveServiceImpl.class);
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
    public ResponseResult deleteArchiveById(List<Integer> archiveid) {
        Integer integer = null;
        try {
            integer=userArchiveDao.selectMaxId();
            for (Integer integer1 : archiveid) {
                if(integer1>integer){
                    return new ResponseResult(false,CommonCode.INVALID_PARAM);
                }
                userArchiveDao.deleteArchiveById(integer1);
            }
            return new ResponseResult(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("逻辑删除失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult resumeDeleteArchiveById(Integer archiveid) {
        try {
            userArchiveDao.resumeDeleteArchiveById(archiveid);
            return new ResponseResult(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除恢复失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult updateArchive(UserArchive userArchive) {
        if(userArchive instanceof UserArchive){
            try {
                userArchiveDao.updateByPrimaryKeySelective(userArchive);
                return new ResponseResult(true,CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("档案更新失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(false,CommonCode.INVALID_PARAM);
    }

    @Override
    public ResponseResult selectArchive(Integer archiveid) {
        try {
            UserArchive userArchive = userArchiveDao.selectByPrimaryKey(archiveid);
            return new ResponseResult(userArchive,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查看档案失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertArchive(UserArchive userArchive) {
        try {
            if (userArchive instanceof  UserArchive) {
                userArchiveDao.insertSelective(userArchive);
                return new ResponseResult(true, CommonCode.SUCCESS);
            }
        } catch (Exception e) {
            logger.error("新增档案失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
        return new ResponseResult(false,CommonCode.INVALID_PARAM);
    }

    @Override
    public ResponseResult updateArchiveField(Map<Integer, String> map) {
        if(map!=null){
            try {
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    customFieldDao.updatePreEmploymentField(entry.getKey(),entry.getValue());
                    return new ResponseResult<>(true, CommonCode.SUCCESS);
                }
            } catch (Exception e) {
                logger.error("更新档案字段表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(false,CommonCode.INVALID_PARAM);
    }

    @Override
    public ResponseResult<PageResult<UserArchive>> selectArchivebatch(Integer archiveId, Integer companyId) {
        List<UserArchive> list = new ArrayList<>();
        PageResult<UserArchive> pageResult = new PageResult<>();
        try {
            //本用户的权限下有哪些机构
            List<Integer> orgList = userOrgAuthDao.selectCompanyIdByArchive(archiveId);
            if (companyId == null) {
                if (orgList != null) {
                    for (Integer integer : orgList) {
                        //展示所有权限机构下的人员
                        List<Integer> achiveList = userOrgAuthDao.selectArchiveIdByOrg(integer);
                        for (Integer userAchive : achiveList) {
                            list.add(userArchiveDao.selectByPrimaryKey(userAchive));
                        }
                    }
                    pageResult.setList(list);
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                } else {
                    //展示自己的档案
                    list.add(userArchiveDao.selectByPrimaryKey(archiveId));
                    pageResult.setList(list);
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);

                }
            } else {
                if (orgList != null) {
                    for (Integer integer : orgList) {
                        //如果查看的在权限之内
                        if (companyId.equals(integer)) {
                            //展示机构下的人员信息
                            List<Integer> achiveList = userOrgAuthDao.selectArchiveIdByOrg(integer);
                            for (Integer userAchive : achiveList) {
                                list.add(userArchiveDao.selectByPrimaryKey(userAchive));
                            }
                        }
                    }
                    pageResult.setList(list);
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                } else {
                    return new ResponseResult<>(pageResult, CommonCode.FAIL);
                }
            }
        } catch (Exception e) {
            logger.error("查询档案失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }
    }




    @Override
    public ResponseResult insertUserArchivePostRelation(UserArchivePostRelationVo  userArchivePostRelationVo,Integer archiveId) {
        UserArchivePostRelation userArchivePostRelation=new UserArchivePostRelation();
        if (userArchivePostRelationVo instanceof UserArchivePostRelationVo) {
            BeanUtils.copyProperties(userArchivePostRelationVo,userArchivePostRelation);
            //通过工号查询档案id
            Integer id=userArchiveDao.selectArchiveIdByNumber(userArchivePostRelationVo.getEmployeeNumber());
            userArchivePostRelation.setArchiveId(id);
            userArchivePostRelation.setOperatorId(archiveId);
            userArchivePostRelation.setIsDelete((short) 1);
            try {
                userArchivePostRelationDao.insertSelective(userArchivePostRelation);
            } catch (Exception e) {
                logger.error("插入人员岗位关系表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }
    @Override
    public ResponseResult<Map<String, String>> selectNameAndNumber(Integer id) {
        Map<String,String> map=new HashMap<>();
        try {
            String name=userArchiveDao.selectName(id);
            String number=userArchiveDao.selectNumber(id);
            map.put("name",name);
            map.put("number",number);
            return  new ResponseResult<>(map,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("获取姓名工号失败");
            return new ResponseResult<>(map, CommonCode.FAIL);
        }

    }

    @Override
    public ResponseResult selectOrgName(Integer id) {
        String orgName=null;
        try {
            orgName=organizationDao.selectOrgName(id);
            return new ResponseResult(orgName,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询机构名称失败");
            return new ResponseResult(orgName, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult<PageResult<UserArchive>> selectArchiveByQueryScheme(Integer schemeId, Integer orgId) {
        Integer temp=null;
        String select=null;
        String order=null;
        PageResult pageResult=new PageResult();
        List<UserArchive> list=new ArrayList<>();
        /*
        根据查询方案id，找到对应的字段id与顺序和排序id与升降序
         */
        try {
            Map<Integer,Integer> fieldMap=new HashMap<>();
            List<Integer> fieldIdList=querySchemeFieldDao.selectFieldId(schemeId);
            List<Integer> fieldSortList=querySchemeFieldDao.selectFieldSort(schemeId);
            for (int i = 0; i < fieldIdList.size(); i++) {
                fieldMap.put(fieldSortList.get(i),fieldIdList.get(i));
            }
            Set<Integer> sorts = fieldMap.keySet();
            //set转化为数组
            Integer[] array =(Integer[]) sorts.toArray();
            //排序id与升降序进行排列组合
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array.length-1-i; j++) {
                    if(array[i]>array[j]){
                        temp=array[i];
                        array[i]=array[j];
                        array[j]=temp;
                    }
                }
            }
            //将字段排序按照顺序拼接成查询项
            for (Integer integer : array) {
               //根据id查询字段名
                String s = customFieldDao.selectFieldName(integer);
                select=select+s+",";
            }




        /*
        根据查询方案id，找到排序id与升降序
         */
            Map<Integer,String> sortMap=new HashMap<>();
            List<Integer> sortIdList=querySchemeSortDao.selectSortId(schemeId);
            List<String> sortSortList=querySchemeSortDao.selectSortSort(schemeId);
            for (int i = 0; i < sortIdList.size(); i++) {
                sortMap.put(sortIdList.get(i),sortSortList.get(i));
            }
            Set<Integer> integers = sortMap.keySet();
            for (Integer integer : integers) {
                String s = customFieldDao.selectFieldName(integer);
                order=order+s+getSort(sortMap.get(integer))+",";
            }
        /*
        进行sql查询，返回数据，档案在机构范围内
         */
            List<UserArchive> userArchiveList=userArchiveDao.getUserArchiveListCustom(select,order,orgId);
            pageResult.setList(userArchiveList);
            return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("展示自定义人员失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }


    }
    public  String getSort(String sort){
        if(sort!=null) {
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
    public ResponseResult deleteUserArchivePostRelation(List<Integer> list) {
        Integer max = 0;
        try {
            max = userArchivePostRelationDao.selectMaxPrimaryKey();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                userArchivePostRelationDao.deleteUserArchivePostRelation(list.get(i));
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除人员岗位关系表失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult updateUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        if (userArchivePostRelation instanceof UserArchivePostRelation) {
            try {
                userArchivePostRelationDao.updateByPrimaryKeySelective(userArchivePostRelation);
                return new ResponseResult(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("更新人员岗位关系表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }

        } else {
            return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
        }
    }

    @Override
    public ResponseResult<PageResult<UserArchivePostRelation>> selectUserArchivePostRelation(Integer currentPage, Integer pageSize,
                                                                                             List<Integer> list) {
        PageHelper.startPage(currentPage, pageSize);
        List<UserArchivePostRelation> userArchivePostRelationlist = null;
        PageResult<UserArchivePostRelation> pageResult = new PageResult<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                UserArchivePostRelation userArchivePostRelation = userArchivePostRelationDao.selectByPrimaryKey(list.get(i));
                userArchivePostRelationlist.add(userArchivePostRelation);
            }
            pageResult.setList(userArchivePostRelationlist);
            return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询人员岗位关系表失败");
            return new ResponseResult<>(pageResult, CommonCode.FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteQueryScheme(List<Integer> list) {
        Integer max = 0;
        try {
            max = querySchemeDao.selectMaxPrimaryKey();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    return new ResponseResult(false, CommonCode.INVALID_PARAM);
                }
                querySchemeDao.deleteQueryScheme(list.get(i));
                //删除查询字段
                querySchemeFieldDao.deleteBySchemeId(list.get(i));
                //删除排序字段
                querySchemeSortDao.deleteBySchemeId(list.get(i));
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("删除查询方案表失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }

    }


    @Override
    public ResponseResult<QuerySchemeList> selectQueryScheme(Integer id) {
        List<QuerySchemeField> querySchemeFieldList;
        List<QuerySchemeSort> querySchemeSortList;
        try {
            QuerySchemeList querySchemeList = new QuerySchemeList();
            //通过查询方案id找到显示字段
            querySchemeFieldList = querySchemeFieldDao.selectByQuerySchemeId(id);
            //通过查询方案id找到排序字段
            querySchemeSortList = querySchemeSortDao.selectByQuerySchemeId(id);
            querySchemeList.setQuerySchemeFieldList(querySchemeFieldList);
            querySchemeList.setQuerySchemeSortList(querySchemeSortList);
            return new ResponseResult<>(querySchemeList, CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查询查询方案表失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveQueryScheme(QueryScheme queryScheme, List<QuerySchemeField> querySchemeFieldlist,
                                          List<QuerySchemeSort> querySchemeSortlist) {
        if (queryScheme instanceof QueryScheme && querySchemeFieldlist != null && querySchemeSortlist != null) {
            if (queryScheme.getQuerySchemeId() == null) {
                //说明是新增操作
                try {
                    querySchemeDao.insertSelective(queryScheme);
                } catch (Exception e) {
                    logger.error("新增查询方案表失败，字段表排序表无法进行新增");
                    return new ResponseResult(false, CommonCode.FAIL);
                }
                try {
                    for (QuerySchemeField querySchemeField : querySchemeFieldlist) {
                        querySchemeField.setQuerySchemeId(queryScheme.getQuerySchemeId());
                    }
                    for (QuerySchemeSort querySchemeSort : querySchemeSortlist) {
                        querySchemeSort.setQuerySchemeId(queryScheme.getQuerySchemeId());
                    }
                    for (int i = 0; i < querySchemeFieldlist.size(); i++) {
                        querySchemeFieldDao.insert(querySchemeFieldlist.get(i));
                    }
                    for (int i = 0; i < querySchemeSortlist.size(); i++) {
                        querySchemeSortDao.insert(querySchemeSortlist.get(i));
                    }
                    return new ResponseResult(true, CommonCode.SUCCESS);
                } catch (Exception e) {
                    logger.error("新增查询方案表失败");
                    return new ResponseResult(false, CommonCode.FAIL);
                }

            }
            try {
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
                return new ResponseResult(true, CommonCode.SUCCESS);
            } catch (Exception e) {
                logger.error("保存查询方案表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }

        }
        return new ResponseResult(false, CommonCode.INVALID_PARAM);
    }



}
