package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.PositionLevelDao;
import com.qinjee.masterdata.model.entity.Position;
import com.qinjee.masterdata.model.entity.PositionGrade;
import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.vo.organization.PositionLevelVo;
import com.qinjee.masterdata.service.organation.PositionGradeService;
import com.qinjee.masterdata.service.organation.PositionGroupService;
import com.qinjee.masterdata.service.organation.PositionLevelService;
import com.qinjee.masterdata.service.organation.PositionService;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:22:00
 */
@Service
public class PositionLevelServiceImpl implements PositionLevelService {

    @Autowired
    private PositionGroupService positionGroupService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private PositionGradeService positionGradeService;
    @Autowired
    private PositionLevelDao positionLevelDao;

    @Override
    public ResponseResult<List<PositionLevel>> showByPosition(UserSession userSession) {
        //获取所有职位族
        List<PositionLevel> positionLevels = new ArrayList<>();
        List<PositionGroup> allPositionGroup = positionGroupService.getAllPositionGroup(userSession, null).getResult().getList();
        if(!CollectionUtils.isEmpty(allPositionGroup)){
            for (PositionGroup positionGroup : allPositionGroup) {
                //获取职族下的所有职位
                List<Position> positionList = positionService.getPositionListByGroupId(positionGroup.getPositionGroupId());
                if(!CollectionUtils.isEmpty(positionList)){
                    Integer positionGroupNameRowSpan = 0;
                    List<PositionLevel> positLevels = new ArrayList<>();
                    for (Position position : positionList) {
                        Boolean positionRow = true;
                        //获取职位对应的职等
                        List<PositionGrade> positionGradeList = positionGradeService.getPositionGradeListByPositionId(position.getPositionId());
                        //获取职位对应的职级
                        List<PositionLevel> positionLevelList = positionLevelDao.getPositionLevelListByPositionId(position.getPositionId());

                        if(!CollectionUtils.isEmpty(positionGradeList)){
                            if(!CollectionUtils.isEmpty(positionLevelList)){
                                Integer rowSpan = positionGradeList.size() * positionLevelList.size();
                                //遍历职等
                                for (PositionGrade positionGrade : positionGradeList) {
                                    Boolean positionGradeRow = true;
                                    //遍历职级
                                    for (PositionLevel positionLevel : positionLevelList) {
                                        if(positionRow){
                                            //职位族
                                            positionGroupNameRowSpan += rowSpan;
                                            //职位
                                            positionLevel.setPositionName(position.getPositionName());
                                            positionLevel.setPositionNameRowSpan(rowSpan);
                                        }else {
                                            positionLevel.setPositionNameRowSpan(0);
                                        }

                                        if(positionGradeRow){
                                            //职等
                                            positionLevel.setPositionGradeName(positionGrade.getPositionGradeName());
                                            positionLevel.setPositionGradeNameRowSpan(positionLevelList.size());
                                        }else {
                                            positionLevel.setPositionGradeNameRowSpan(0);
                                        }
                                        positLevels.add(positionLevel);
                                    }
                                }
                            }

                        }
                    }
                    //设置职位族
                    PositionLevel positionLevel = positLevels.get(0);
                    positionLevel.setPositionGroupName(positionGroup.getPositionGroupName());
                    positionLevel.setPositionGroupNameRowSpan(positionGroupNameRowSpan);
                    positionLevels.addAll(positLevels);
                }
            }
        }
        return new ResponseResult<>(positionLevels);
    }

    @Override
    public List<PositionLevel> getPositionLevelByPositionId(Integer positionId) {
        return positionLevelDao.getPositionLevelListByPositionId(positionId);
    }

    @Override
    public ResponseResult<PageResult<PositionLevel>> getPositionLevelList(PageVo pageVo) {
        if (pageVo != null && (pageVo.getPageSize() != null && pageVo.getCurrentPage() != null)){
            PageHelper.startPage(pageVo.getCurrentPage(), pageVo.getPageSize());
        }
        List<PositionLevel> positionLevelList = positionLevelDao.getPositionLevelList();
        PageResult<PositionLevel> pageResult = new PageResult<>(positionLevelList);
        return new ResponseResult<>(pageResult);
    }

    @Override
    public ResponseResult addPositionLevel(PositionLevelVo positionLevelVo, UserSession userSession) {
        PositionLevel positionLevel = new PositionLevel();
        BeanUtils.copyProperties(positionLevelVo, positionLevel);
        List<PositionLevel> positionLevelList = positionLevelDao.getPositionLevelList();
        if(!CollectionUtils.isEmpty(positionLevelList)){
            PositionLevel position_Level = positionLevelList.get(positionLevelList.size() - 1);
            String positionLevelName = position_Level.getPositionLevelName();
            int level = Integer.parseInt(positionLevelName);
            positionLevel.setPositionLevelName(String.valueOf(level + 1));
            positionLevel.setSortId(position_Level.getSortId() + 1000);
        }else {
            positionLevel.setPositionLevelName("1");
            positionLevel.setSortId(1000);
        }
        positionLevel.setIsDelete((short) 0);
        positionLevel.setOperatorId(userSession.getArchiveId());
        positionLevelDao.insert(positionLevel);
        return new ResponseResult();
    }

    @Override
    public ResponseResult editPositionLevel(UserSession userSession, PositionLevelVo positionLevelVo) {
        PositionLevel positionLevel = new PositionLevel();
        BeanUtils.copyProperties(positionLevelVo, positionLevel);
        positionLevel.setOperatorId(userSession.getArchiveId());
        positionLevelDao.insertSelective(positionLevel);
        return new ResponseResult();
    }

    @Override
    public ResponseResult deletePositionLevel(List<Integer> positionLevelIds, UserSession userSession) {
        if(!CollectionUtils.isEmpty(positionLevelIds)){
            for (Integer positionLevelId : positionLevelIds) {
                PositionLevel positionLevel = new PositionLevel();
                positionLevel.setOperatorId(userSession.getArchiveId());
                positionLevel.setIsDelete((short) 1);
                positionLevel.setPositionLevelId(positionLevelId);
                positionLevelDao.updateByPrimaryKeySelective(positionLevel);
            }
        }
        return new ResponseResult();
    }

    @Override
    public ResponseResult<Map<String, Object>> showByPositionLevel(UserSession userSession) {
        Map<String, Object> map = new HashMap<>();
        //职级集合
        List<PositionLevel> positionLevel_List = positionLevelDao.getPositionLevelList();
        if(!CollectionUtils.isEmpty(positionLevel_List)){
            List<PositionLevel> positionLevelArrayList = new ArrayList<>();
            for (int i = 0; i < positionLevel_List.size() ; i++){
                positionLevelArrayList.add(null);
            }
            //获取所有职位族
            List<PositionGroup> allPositionGroup = positionGroupService.getAllPositionGroup(userSession, null).getResult().getList();
            map.put("columns", allPositionGroup);
            map.put("dataSource", positionLevelArrayList);
            if(!CollectionUtils.isEmpty(allPositionGroup)){
                for (int i = 0; i < allPositionGroup.size(); i++) {
                    PositionGroup positionGroup = allPositionGroup.get(i);
                    //获取职族下的所有职位
                    List<Position> positionList = positionService.getPositionListByGroupId(positionGroup.getPositionGroupId());
                    if(!CollectionUtils.isEmpty(positionList)){
                        for (Position position : positionList) {
                            //遍历职位

                            //获取职位对应的职等
                            List<PositionGrade> positionGradeList = positionGradeService.getPositionGradeListByPositionId(position.getPositionId());
                            //获取职位对应的职级
                            List<PositionLevel> positionLevelList = positionLevelDao.getPositionLevelListByPositionId(position.getPositionId());
                            if(!CollectionUtils.isEmpty(positionLevelList)){
                                for (PositionLevel positionLevel : positionLevelList) {
                                    int index = positionLevel.getSortId() / 1000;
                                    PositionLevel position_Level;
                                    //遍历职级
                                    position_Level = positionLevelArrayList.get(index - 1);
                                    if(position_Level == null){
                                        position_Level = new PositionLevel();
                                        position_Level.setPositionLevelName(positionLevel.getPositionLevelName());
                                        positionLevelArrayList.add(index - 1,position_Level);
                                    }

                                    List<PositionGroup> positionGroups = position_Level.getPositionGroups();
                                    if(CollectionUtils.isEmpty(positionGroups)){
                                        //判断职级里是否含有职位族集合
                                        positionGroups = new ArrayList<>();
                                        position_Level.setPositionGroups(positionGroups);
                                    }

                                    PositionGroup position_Group;
                                    if(positionGroups.size() >= (i + 1)){
                                        //职位族集合中存在当前职位族
                                        position_Group = positionGroups.get(i);
                                    }else {
                                        //职位族集合中不存在当前职位族
                                        position_Group = new PositionGroup();
                                        positionGroups.add(position_Group);
                                    }
                                    String positionNames = position_Group.getPositionNames();
                                    if(StringUtils.isNotBlank(positionNames)){
                                        if(positionNames.indexOf(position.getPositionName()) < 0){
                                            position_Group.setPositionNames(positionNames + "," + position.getPositionName());
                                        }
                                    }else {
                                        position_Group.setPositionNames(position.getPositionName());
                                    }

                                    if(!CollectionUtils.isEmpty(positionGradeList)){
                                        //遍历职等
                                        for (PositionGrade positionGrade : positionGradeList) {
                                            String positionGradeNames = position_Group.getPositionGradeNames();
                                            if(StringUtils.isNotBlank(positionGradeNames)){
                                                position_Group.setPositionNames(positionGradeNames + "," + positionGrade.getPositionGradeName());
                                            }else {
                                                position_Group.setPositionNames(positionGrade.getPositionGradeName());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            mergPositionLevel(positionLevelArrayList);
        }
        return new ResponseResult<>(map);
    }

    /**
     * 合并相同的职等名称和职位名称
     * @param positionLevelArrayList
     */
    private void mergPositionLevel(List<PositionLevel> positionLevelArrayList) {
        if(CollectionUtils.isEmpty(positionLevelArrayList)){
            PositionLevel prePositionName_Level = null;
            PositionLevel prePositionGradeName_Level = null;

            for (int i = 1; i < positionLevelArrayList.size(); i++) {
                PositionLevel positionLevel = positionLevelArrayList.get(i);
                if(prePositionName_Level == null){
                    prePositionName_Level = positionLevelArrayList.get(i - 1);
                }
                if(prePositionGradeName_Level == null){
                    prePositionGradeName_Level = positionLevelArrayList.get(i - 1);
                }
                List<PositionGroup> positionGroups = positionLevel.getPositionGroups();
                for (int j = 0; j < positionGroups.size(); j++) {
                    PositionGroup positionGroup = positionGroups.get(j);
                    String positionNames = positionGroup.getPositionNames();

                    PositionGroup prePositionNameGroup = prePositionName_Level.getPositionGroups().get(j);
                    if(prePositionNameGroup.getPositionNames().equals(positionNames)){
                        positionGroup.setPositionNamesRowSpan(0);
                        prePositionNameGroup.setPositionNamesRowSpan(prePositionNameGroup.getPositionNamesRowSpan() + 1);
                    }else {
                        positionGroup.setPositionNamesRowSpan(1);
                        prePositionName_Level = positionLevel;
                    }

                    PositionGroup prePositionGradeNameGroup = prePositionGradeName_Level.getPositionGroups().get(j);
                    String positionGradeNames = positionGroup.getPositionGradeNames();

                    if(prePositionGradeNameGroup.getPositionGradeNames().equals(positionGradeNames)){
                        positionGroup.setPositionNamesRowSpan(0);
                        prePositionGradeNameGroup.setPositionGradeNamesRowSpan(prePositionNameGroup.getPositionGradeNamesRowSpan() + 1);
                    }else {
                        positionGroup.setPositionGradeNamesRowSpan(1);
                        prePositionGradeName_Level = positionLevel;
                    }
                }
            }
        }
    }

    /**
     * 获取上有几个相同职等名称数量
     * @param positionLevelArrayList
     * @param i
     * @param index
     * @param currentPositionGradeName
     */
    private int getPrePositionGradeNamePosiLevel(List<PositionLevel> positionLevelArrayList, int i, int index, String currentPositionGradeName,int count) {
        PositionLevel prePositionLevel = positionLevelArrayList.get(index);
        List<PositionGroup> positionGroupList = prePositionLevel.getPositionGroups();
        PositionGroup position_Group = positionGroupList.get(i);
        if(currentPositionGradeName.equals(position_Group.getPositionGradeNames())){
            count++;
            getPrePositionGradeNamePosiLevel(positionLevelArrayList, i, index - 1, currentPositionGradeName,count);
        }
        return count;
    }

    /**
     * 获取往下级有几个职位名称相同职级
     * @param positionLevelArrayList
     * @param i
     * @param index
     */
    private int getPrePositionNamePosiLevel(List<PositionLevel> positionLevelArrayList, int i, int index, String currentPositionName ,int sum) {
        PositionLevel prePositionLevel = positionLevelArrayList.get(index);
        List<PositionGroup> positionGroupList = prePositionLevel.getPositionGroups();
        PositionGroup position_Group = positionGroupList.get(i);
        if(currentPositionName.equals(position_Group.getPositionNames())){
            sum ++;
            return getPrePositionNamePosiLevel(positionLevelArrayList, i, index - 1, currentPositionName , sum);
        }
        return sum;
    }

}
