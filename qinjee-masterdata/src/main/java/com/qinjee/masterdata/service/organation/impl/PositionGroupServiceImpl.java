package com.qinjee.masterdata.service.organation.impl;

import com.qinjee.masterdata.dao.PositionGroupDao;
import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.vo.organization.PositionGroupVo;
import com.qinjee.masterdata.service.organation.PositionGroupService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 09:39:00
 */
@Service
public class PositionGroupServiceImpl implements PositionGroupService {

    @Autowired
    private PositionGroupDao positionGroupDao;

    @Override
    public List<PositionGroup> getAllPositionGroup(UserSession userSession) {
        Integer companyId = userSession.getCompanyId();
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setCompanyId(companyId);
        positionGroup.setIsDelete((short) 0);
        return positionGroupDao.getPositionGroupByPosG(positionGroup);
    }

    @Transactional
    @Override
    public ResponseResult addPositionGroup(UserSession userSession, String positionGroupName) {
        Integer companyId = userSession.getCompanyId();
        Integer archiveId = userSession.getArchiveId();
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setCompanyId(companyId);
        positionGroup.setIsDelete((short) 0);
        List<PositionGroup> positionGroups = positionGroupDao.getPositionGroupByPosG(positionGroup);
        Integer sortId = null;
        if(!CollectionUtils.isEmpty(positionGroups)){
            if (isRepeatPosutionGroupName(positionGroupName, positionGroups))
                //有重名页面提示
                return new ResponseResult(CommonCode.POSITION_GROUP_NAME_REPEAT);
            PositionGroup positionGroup_1 = positionGroups.get(positionGroups.size() - 1);
            sortId = positionGroup_1.getSortId() + 1000;
        }
        positionGroup.setPositionGroupName(positionGroupName);
        positionGroup.setOperatorId(archiveId);
        positionGroup.setSortId(sortId);
        positionGroupDao.insertSelective(positionGroup);
        return new ResponseResult();
    }

    /**
     * 判断是否有重名的
     * @param positionGroupName
     * @param positionGroups
     * @return
     */
    private boolean isRepeatPosutionGroupName(String positionGroupName, List<PositionGroup> positionGroups) {
        List<PositionGroup> positionGroupList = positionGroups.stream().filter(positionGroup1 -> {
            String positionGroupName1 = positionGroup1.getPositionGroupName();
            if (positionGroupName1.equals(positionGroupName)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(positionGroupList)){
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public ResponseResult editPositionGroup(UserSession userSession, PositionGroupVo positionGroupVo) {
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setCompanyId(userSession.getCompanyId());
        positionGroup.setIsDelete((short) 0);
        List<PositionGroup> positionGroups = positionGroupDao.getPositionGroupByPosG(positionGroup);
        if(CollectionUtils.isEmpty(positionGroups)){
            boolean repeatPosutionGroupName = isRepeatPosutionGroupName(positionGroupVo.getPositionGroupName(), positionGroups);
            if(repeatPosutionGroupName){
                return new ResponseResult(CommonCode.POSITION_GROUP_NAME_REPEAT);
            }
        }
        PositionGroup positionGroup_1 = new PositionGroup();
        positionGroup_1.setPositionGroupName(positionGroupVo.getPositionGroupName());
        positionGroup_1.setPositionGroupId(positionGroupVo.getPositionGroupId());
        positionGroup_1.setOperatorId(userSession.getArchiveId());
        positionGroupDao.insertSelective(positionGroup);
        return new ResponseResult();
    }

    @Transactional
    @Override
    public void deletePositionGroup(List<Integer> positionGroupIds) {
        if(!CollectionUtils.isEmpty(positionGroupIds)){
            for (Integer positionGroupId : positionGroupIds) {
                PositionGroup positionGroup = new PositionGroup();
                positionGroup.setPositionGroupId(positionGroupId);
                positionGroup.setIsDelete((short) 1);
                positionGroupDao.updateByPrimaryKeySelective(positionGroup);
            }
        }
    }

    @Override
    public ResponseResult sortPositionGroup(Integer prePositionGroupId, Integer midPositionGroupId, Integer nextPositionGroupId) {
        PositionGroup prePositionGroup;
        PositionGroup nextPositionGroup;
        Integer midSort = null;
        if(nextPositionGroupId != null){
            //移动
            nextPositionGroup = positionGroupDao.selectByPrimaryKey(nextPositionGroupId);
            midSort = nextPositionGroup.getSortId() - 1;
        }else if(prePositionGroupId == null){
            //移动到最后
            prePositionGroup = positionGroupDao.selectByPrimaryKey(prePositionGroupId);
            midSort = prePositionGroup.getSortId() + 1;
        }
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setPositionGroupId(midPositionGroupId);
        positionGroup.setSortId(midSort);
        positionGroupDao.insertSelective(positionGroup);
        return new ResponseResult();
    }

    @Override
    public ResponseResult<List<PositionGroup>> getAllPosition(UserSession userSession) {
        Integer companyId = userSession.getCompanyId();
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setCompanyId(companyId);
        positionGroup.setIsDelete((short) 0);
        List<PositionGroup> positionGroups = positionGroupDao.getPositionGroupByPosG(positionGroup);
        for (PositionGroup group : positionGroups) {

        }
        return null;
    }
}
