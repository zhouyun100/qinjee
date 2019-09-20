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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void showByPositionLevel(UserSession userSession) {
        Map<List<PositionGroup>,  List<PositionLevel>> map = new HashMap<>();
        //职级集合
        List<PositionLevel> positionLevelArrayList = new ArrayList<>();
        //获取所有职位族
        List<PositionGroup> allPositionGroup = positionGroupService.getAllPositionGroup(userSession, null).getResult().getList();
        if(!CollectionUtils.isEmpty(allPositionGroup)){
            for (int i = 0; i < allPositionGroup.size(); i++) {
            PositionGroup positionGroup = allPositionGroup.get(i);
                //获取职族下的所有职位
                List<Position> positionList = positionService.getPositionListByGroupId(positionGroup.getPositionGroupId());
                if(!CollectionUtils.isEmpty(positionList)) {
                    for (Position position : positionList) {
                        //获取职位对应的职等
                        List<PositionGrade> positionGradeList = positionGradeService.getPositionGradeListByPositionId(position.getPositionId());
                        //获取职位对应的职级
                        List<PositionLevel> positionLevelList = positionLevelDao.getPositionLevelListByPositionId(position.getPositionId());
                        if(!CollectionUtils.isEmpty(positionGradeList)){
                            Boolean isNewPositionLevel = true;
                            for (PositionGrade positionGrade : positionGradeList) {
                                //遍历职等
                                Boolean isNewPositionGrade = true;
                                String currentPositionGradeName = positionGrade.getPositionGradeName();
                                String currentPositionName = position.getPositionName();
                                if(!CollectionUtils.isEmpty(positionLevelList)){
                                    for (PositionLevel positionLevel : positionLevelList) {
                                        //遍历职级
                                        PositionLevel currentPositionLevel;

                                        int count = 0;
                                        int sum = 0;

                                        int positionLevelindex = positionLevel.getSortId() / 1000;
                                        if(positionLevelArrayList.size() >= (positionLevelindex + 1)){
                                            int index = positionLevelindex - 1;
                                            //根据等级排序获取第几级职级
                                            currentPositionLevel = positionLevelArrayList.get(positionLevelindex - 1);
                                            if(index > 0){
                                                //获取往下级有几个职等名称相同职级数量
                                                count = getPrePositionGradeNamePosiLevel(positionLevelArrayList, i, index,currentPositionGradeName,count);
                                                //获取往下级有几个职位名称相同职级
                                                sum = getPrePositionNamePosiLevel(positionLevelArrayList, i, index, currentPositionName, sum);
                                            }
                                        }else {
                                            //没有则新增职级
                                            currentPositionLevel = new PositionLevel();
                                            currentPositionLevel.setPositionLevelName(positionLevel.getPositionLevelName());
                                            positionLevelArrayList.add(currentPositionLevel);
                                        }
                                        //获取职级里的职位族集合
                                        List<PositionGroup> positionGroups = currentPositionLevel.getPositionGroups();
                                        PositionGroup position_Group;


                                        //职级里的职位族集合 是否存在当前职位族
                                        if(!CollectionUtils.isEmpty(positionGroups) && positionGroups.size() >= (i + 1)){
                                            //存在当前职位族
                                            //职级里的职位族集合
                                            position_Group = positionGroups.get(i);
                                            if(isNewPositionGrade){
                                                //设置职等名
                                                position_Group.setPositionGradeNames(position_Group.getPositionGradeNames() + "," + currentPositionGradeName);
                                                isNewPositionGrade = false;
                                            }

                                            if(isNewPositionLevel){
                                                //设置职位名
                                                position_Group.setPositionNames(position_Group.getPositionNames() + "," + position.getPositionName());
                                                //合并行
                                                position_Group.setPositionNamesRowSpan(position_Group.getPositionNamesRowSpan() + 1);
                                                position_Group.setPositionGradeNamesRowSpan(position_Group.getPositionGradeNamesRowSpan() + 1);

                                            }
                                        }else {
                                            //不存在当前职位族
                                            if(positionGroups == null){
                                                //职级第一次的时候
                                                positionGroups = new ArrayList<>();
                                                currentPositionLevel.setPositionGroups(positionGroups);
                                            }

                                            position_Group = new PositionGroup();
                                            //判断是否与之前一级职位名一样  一样就合并
                                            position_Group.setPositionNames(currentPositionName);
                                            position_Group.setPositionNamesRowSpan(1);
                                            position_Group.setPositionGradeNames(currentPositionGradeName);
                                            position_Group.setPositionGradeNamesRowSpan(1);

                                            //往下级有职等名称相同职级
                                            if(count > 0){
                                                PositionGroup positionGroup1 = positionLevelArrayList.get(positionLevelindex - count).getPositionGroups().get(i);
                                                String positionGradeNames = positionGroup1.getPositionGradeNames();
                                                if(positionGradeNames.equals(currentPositionGradeName)){
                                                    //职位合并一行
                                                    positionGroup1.setPositionGradeNamesRowSpan(positionGroup1.getPositionGradeNamesRowSpan() + 1);
                                                    position_Group.setPositionGradeNamesRowSpan(0);
                                                }
                                            }

                                            //往下级有职位名称相同职级
                                            if(sum > 0){
                                                PositionGroup positionGroup1 = positionLevelArrayList.get(positionLevelindex - sum).getPositionGroups().get(i);
                                                positionGroup1.setPositionNames(positionGroup1.getPositionNames() + "," + currentPositionName);



                                            }
                                            positionGroups.add(position_Group);
                                        }
                                    }
                                }
                                isNewPositionLevel = false;
                            }
                        }
                    }
                }
            }
        }
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
        }else {
            positionLevel.setPositionLevelName("1");
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
