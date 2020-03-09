package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.organation.PositionDao;
import com.qinjee.masterdata.dao.organation.PostDao;
import com.qinjee.masterdata.model.entity.Position;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.vo.organization.PositionVo;
import com.qinjee.masterdata.model.vo.organization.bo.PositionPageBO;
import com.qinjee.masterdata.service.organation.PositionService;
import com.qinjee.masterdata.utils.DealHeadParamUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:32:00
 */
@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionDao positionDao;
    @Autowired
    private PostDao postDao;


    @Override
    public List<Position> getPositionListByGroupId(Integer positionGroupId) {
        return positionDao.getPositionListByGroupId(positionGroupId);
    }

    @Override
    public ResponseResult<PageResult<Position>> getPositionPage(PositionPageBO pageVo) {

        if (pageVo != null && (pageVo.getPageSize() != null && pageVo.getCurrentPage() != null)) {
            PageHelper.startPage(pageVo.getCurrentPage(), pageVo.getPageSize());
        }
        String whereSql = DealHeadParamUtil.getWhereSql(pageVo.getTableHeadParamList(), "tp.");
        String orderSql = DealHeadParamUtil.getOrderSql(pageVo.getTableHeadParamList(), "tp.");
        List<Position> positionList = positionDao.getPositionPage(pageVo,whereSql,orderSql);
        PageResult<Position> pageResult = new PageResult<>(positionList);
        return new ResponseResult<>(pageResult);
    }

    @Transactional
    @Override
    public ResponseResult addPosition(PositionVo positionVo, UserSession userSession) {
        List<Position> allPositions = positionDao.getPositionListByCompanyId(userSession.getCompanyId());
        boolean bool = allPositions.stream().anyMatch(a -> positionVo.getPositionName().equals(a.getPositionName()));
        if(bool){
            return new ResponseResult(CommonCode.POSITION_NAME_REPEAT);

        }
        Position position = new Position();
        BeanUtils.copyProperties(positionVo, position);
        position.setOperatorId(userSession.getArchiveId());
        position.setIsDelete((short) 0);
        //设置排序id
        Integer sortId;
        List<Position> positionList = positionDao.getPositionListByGroupId(positionVo.getPositionGroupId());

        if (!CollectionUtils.isEmpty(positionList)) {
            Position lastPosition = positionList.get(positionList.size() - 1);
            sortId = lastPosition.getSortId() + 1000;
        } else {
            sortId = 1000;
        }
        position.setSortId(sortId);
        positionDao.insertSelective(position);

        return new ResponseResult();
    }



    @Transactional
    @Override
    public ResponseResult editPosition(PositionVo positionVo, UserSession userSession) {
        Position position = new Position();
        BeanUtils.copyProperties(positionVo, position);
        position.setOperatorId(userSession.getArchiveId());
        positionDao.updateByPrimaryKey(position);

        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult deletePosition(List<Integer> positionIds, UserSession userSession) {
        if(!CollectionUtils.isEmpty(positionIds)){

            List<Post> posts=postDao.listPostsByPisitionId(positionIds);
            if(!CollectionUtils.isEmpty(posts)){
                ExceptionCast.cast(CommonCode.POSITION_USED_NY_POST);
            }
            for (Integer positionId : positionIds) {
                Position position = new Position();
                position.setPositionId(positionId);
                position.setIsDelete((short) 1);
                position.setOperatorId(userSession.getArchiveId());
                positionDao.updateByPrimaryKeySelective(position);
            }
        }
        return new ResponseResult();
    }

    @Override
    @Transactional
    public void sortPosition(List<Integer> positionIds) {
        //查询出职位列表
        List<Position> positionList = positionDao.getSinglePositionList(positionIds);
        Set<Integer> parentOrgSet = new HashSet<>();
        for (Position position : positionList) {
            //将父机构id存储在set中
            parentOrgSet.add(position.getPositionGroupId());
        }
        //判断是否在同一级职位族下
        if (parentOrgSet.size() > 1) {
            ExceptionCast.cast(CommonCode.NOT_SAME_LEVEL_EXCEPTION);
        }
        positionDao.sortPosition(positionIds);
    }

    @Override
    public void determinePositionNameIsOnly(String positionName, UserSession userSession) {
        Position position = positionDao.getPositionByNameAndCompanyId(positionName, userSession.getCompanyId());
        if (Objects.nonNull(position)){
            ExceptionCast.cast(CommonCode.POSITION_NAME_REPEAT);
        }
    }


}
