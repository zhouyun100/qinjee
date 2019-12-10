package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.organation.PositionGroupDao;
import com.qinjee.masterdata.model.entity.Position;
import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.vo.organization.PositionGroupVo;
import com.qinjee.masterdata.service.organation.PositionGroupService;
import com.qinjee.masterdata.service.organation.PositionService;
import com.qinjee.masterdata.utils.pexcel.ExcelExportUtil;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
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
    @Autowired
    private PositionService positionService;

    @Override
    public ResponseResult<PageResult<PositionGroup>> getAllPositionGroup(UserSession userSession, PageVo pageVo) {
        Integer companyId = userSession.getCompanyId();
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setCompanyId(companyId);
        positionGroup.setIsDelete((short) 0);
        if (pageVo != null && (pageVo.getCurrentPage() != null && pageVo.getPageSize() != null)) {
            PageHelper.startPage(pageVo.getCurrentPage(), pageVo.getPageSize());
        }
        List<PositionGroup> positionGroupByPosG = positionGroupDao.getPositionGroupByPosG(positionGroup);
        PageResult<PositionGroup> pageResult = new PageResult<>(positionGroupByPosG);
        return new ResponseResult<>(pageResult);
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
        if (!CollectionUtils.isEmpty(positionGroups)) {
            if (isRepeatPosutionGroupName(positionGroupName, positionGroups)) {
                //有重名页面提示
                return new ResponseResult(CommonCode.POSITION_GROUP_NAME_REPEAT);
            }
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
     *
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
        if (!CollectionUtils.isEmpty(positionGroupList)) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public ResponseResult editPositionGroup(UserSession userSession, PositionGroupVo positionGroupVo) {
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setCompanyId(userSession.getCompanyId());
        List<PositionGroup> positionGroups = positionGroupDao.getPositionGroupByPosG(positionGroup);
        if (!CollectionUtils.isEmpty(positionGroups)) {
            boolean repeatPosutionGroupName = isRepeatPosutionGroupName(positionGroupVo.getPositionGroupName(), positionGroups);
            if (repeatPosutionGroupName) {
                return new ResponseResult(CommonCode.POSITION_GROUP_NAME_REPEAT);
            }
        }
        PositionGroup oldPositionGroup = positionGroupDao.selectByPrimaryKey(positionGroupVo.getPositionGroupId());
        BeanUtils.copyProperties(positionGroupVo,oldPositionGroup);
        positionGroupDao.updateByPrimaryKey(oldPositionGroup);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Transactional
    @Override
    public int deletePositionGroup(List<Integer> positionGroupIds) {
       return positionGroupDao.batchDeleteByGroupIds(positionGroupIds);
    }

    @Override
    public ResponseResult sortPositionGroup(LinkedList<String> positionGroupIds) {
        positionGroupDao.sortPositionGroup(positionGroupIds);
        return new ResponseResult();
    }

    @Override
    public ResponseResult<List<PositionGroup>> getAllPositionGroupTree(UserSession userSession) {
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setCompanyId(userSession.getCompanyId());
        positionGroup.setIsDelete((short) 0);
        //获取职位族
        List<PositionGroup> positionGroupByPosG = positionGroupDao.getPositionGroupByPosG(positionGroup);
        if (!CollectionUtils.isEmpty(positionGroupByPosG)) {
            for (PositionGroup group : positionGroupByPosG) {
                //获取职族下的所有职位
                List<Position> positionList = positionService.getPositionListByGroupId(group.getPositionGroupId());
                group.setPositionList(positionList);
            }
        }
        return new ResponseResult<>(positionGroupByPosG);
    }

    /**
     * 导出用户下所有职位族，根据企业id
     *
     * @param filePath
     * @param userSession
     * @return
     */
    @Override
    public ResponseResult downloadAllPositionGroupToExcel(String filePath, UserSession userSession) {
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setCompanyId(userSession.getCompanyId());
        List<PositionGroup> positionGroupByPosG = positionGroupDao.getPositionGroupByPosG(positionGroup);

        ExcelExportUtil.exportToFile(filePath, positionGroupByPosG);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 导出选中的职位族
     *
     * @param filePath
     * @param positionGroupIds
     * @param userSession
     * @return
     */
    @Override
    public ResponseResult downloadPositionGroupToExcelByOrgId(String filePath, List<Integer> positionGroupIds, UserSession userSession) {
        List<PositionGroup> positionGroups = positionGroupDao.selectBypositionGroupIds(positionGroupIds);
        ExcelExportUtil.exportToFile(filePath, positionGroups);
        return new ResponseResult(CommonCode.SUCCESS);
    }

}
