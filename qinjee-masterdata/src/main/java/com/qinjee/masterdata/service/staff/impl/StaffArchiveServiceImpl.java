package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.UserArchivePostRelationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.QuerySchemeDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.QuerySchemeFieldDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.QuerySchemeSortDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.QuerySchemeList;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public ResponseResult deleteArchiveById(Integer archiveid) {
        try {
            userArchiveDao.deleteArchiveById(archiveid);
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
        try {
            userArchiveDao.updateByPrimaryKeySelective(userArchive);
            return new ResponseResult(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("档案更新失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult selectArchive(Integer archiveid) {
        try {
            userArchiveDao.selectByPrimaryKey(archiveid);
            return new ResponseResult(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("查看档案失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }

    @Override
    public ResponseResult insertArchive(UserArchive userArchive) {
        try {
            userArchiveDao.insertSelective(userArchive);
            return new ResponseResult(true,CommonCode.SUCCESS);
        } catch (Exception e) {
            logger.error("新增档案失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
    }


    @Override
    public ResponseResult insertUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        if (userArchivePostRelation instanceof UserArchivePostRelation) {
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
