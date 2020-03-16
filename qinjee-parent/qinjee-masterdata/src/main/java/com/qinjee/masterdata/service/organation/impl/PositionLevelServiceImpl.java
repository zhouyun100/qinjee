package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.organation.PositionLevelDao;
import com.qinjee.masterdata.dao.organation.PostDao;
import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.vo.organization.PositionLevelVo;
import com.qinjee.masterdata.service.organation.PositionLevelService;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:22:00
 */
@Service
public class PositionLevelServiceImpl implements PositionLevelService {

    @Autowired
    private PositionLevelDao positionLevelDao;
    @Autowired
    private PostDao postDao;


    @Override
    public PageResult<PositionLevelVo> listPositionLevel(PageVo pageVo, UserSession userSession) {
        if (null != pageVo.getCurrentPage() && null != pageVo.getPageSize())
            PageHelper.startPage(pageVo.getCurrentPage(), pageVo.getPageSize());
        List<PositionLevelVo> positionLevelList = positionLevelDao.list(userSession.getCompanyId());
        PageResult<PositionLevelVo> pageResult = new PageResult<>(positionLevelList);
        return pageResult;

    }

    @Override
    public List<PositionLevelVo> listByPositionGradeId(Integer positionGradeId) {

        List<PositionLevelVo> positionLevelList = positionLevelDao.listByPositionGradeId(positionGradeId);
        return positionLevelList;
    }

    @Override
    public int addPositionLevel(PositionLevel positionLevel, UserSession userSession) {
        //查重
        PositionLevelVo pl = positionLevelDao.getByPositionLevelName(positionLevel.getPositionLevelName(),userSession.getCompanyId());
        if(Objects.nonNull(pl)){
            ExceptionCast.cast(CommonCode.NAME_ALREADY_USED);
        }

        positionLevel.setCompanyId(userSession.getCompanyId());
        positionLevel.setOperatorId(userSession.getArchiveId());
        //设置排序id
        int lastSortId = positionLevelDao.getLastSortId(userSession.getCompanyId());
        positionLevel.setSortId(++lastSortId);
        return positionLevelDao.insert(positionLevel);
    }

    @Override
    public int editPositionLevel(UserSession userSession, PositionLevel positionLevel) {
        //查重 排除自己
        PositionLevelVo pl = positionLevelDao.getByPositionLevelName(positionLevel.getPositionLevelName(),userSession.getCompanyId());
        if(Objects.nonNull(pl)&&!pl.getPositionLevelId().equals(positionLevel.getPositionLevelId())){
            ExceptionCast.cast(CommonCode.NAME_ALREADY_USED);
        }
        positionLevel.setOperatorId(userSession.getArchiveId());
        return positionLevelDao.update(positionLevel);
    }


    /**
     * 逻辑删除
     * 如果职级被岗位引用，就不能删除
     *
     * @param positionLevelIds
     * @param userSession
     * @return
     */
    @Override
    public int batchDeletePositionLevel(List<Integer> positionLevelIds, UserSession userSession) {

        //判断职级是否被岗位引用
        List<Post> posts = postDao.listPostsByPositionLevelId(positionLevelIds);
        if (CollectionUtils.isNotEmpty(posts))
            ExceptionCast.cast(CommonCode.LEVEL_USE_IN_POST);
        return positionLevelDao.batchDelete(positionLevelIds);
    }

    @Override
    public int sortPositionLevel(List<Integer> positionLevelIds, UserSession userSession) {
       return positionLevelDao.sort(positionLevelIds,userSession.getArchiveId());
    }

    @Override
    public PositionLevelVo getPositionLevelById(Integer id) {
       return positionLevelDao.get(id);
    }
}
