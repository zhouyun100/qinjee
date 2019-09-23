package com.qinjee.masterdata.controller.organization;

import com.alibaba.fastjson.JSON;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.vo.organization.PositionLevelVo;
import com.qinjee.masterdata.service.organation.PositionLevelService;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月11日 10:13:00
 */
@RestController
@RequestMapping("/positionLevel")
@Api(tags = "【机构管理】职级接口")
public class PositionLevelController extends BaseController {

    @Autowired
    private PositionLevelService positionLevelService;

    @ApiOperation(value = "按职级 展示职位体系", notes = "高雄")
    @GetMapping("/showByPositionLevel")
    public ResponseResult<Map<List<PositionGroup>,List<PositionLevel>>> showByPositionLevel(){
//        positionLevelService.showByPositionLevel(getUserSession());
        //TODO
        List<PositionGroup> positionGroups = new ArrayList<>();
        PositionGroup positionGroup = new PositionGroup();
        positionGroup.setPositionGroupName("研发族");
        positionGroups.add(positionGroup);

        PositionGroup positionGroup1 = new PositionGroup();
        positionGroup1.setPositionGroupName("销售族");
        positionGroups.add(positionGroup1);

        List<PositionLevel> positionLevelList = new ArrayList<>();

        PositionLevel positionLevel = new PositionLevel();
        positionLevel.setPositionLevelName("1级");

        List<PositionGroup> position_Groups = new ArrayList<>();
        PositionGroup position_Group = new PositionGroup();
        position_Group.setPositionGradeNames("专家");
        position_Group.setPositionGradeNamesRowSpan(3);
        position_Group.setPositionNames("前端开发");
        position_Group.setPositionNamesRowSpan(2);

        position_Groups.add(position_Group);
        positionLevel.setPositionGroups(position_Groups);
        positionLevelList.add(positionLevel);

        Map<List<PositionGroup>,List<PositionLevel>> map =new HashMap<>();
        map.put(positionGroups, positionLevelList);


        String s = JSON.toJSONString(map);
        System.out.println(s);
        return new ResponseResult<>(map);
    }

    @ApiOperation(value = "按职位 展示职位体系", notes = "高雄")
    @GetMapping("/showByPosition")
    public ResponseResult<List<PositionLevel>> showByPosition(){
        return positionLevelService.showByPosition(getUserSession());
    }

    @GetMapping("/getPositionLevelList")
    @ApiOperation(value = "分页查询职级列表",notes = "高雄")
    public ResponseResult<PageResult<PositionLevel>> getPositionLevelList(PageVo pageVo){
        return positionLevelService.getPositionLevelList(pageVo);
    }

    @PostMapping("/addPositionLevel")
    @ApiOperation(value = "新增职级",notes = "高雄")
    public ResponseResult addPositionLevel(PositionLevelVo positionLevelVo){
        return positionLevelService.addPositionLevel(positionLevelVo, getUserSession());
    }

    @PostMapping("/editPositionLevel")
    @ApiOperation(value = "编辑职级", notes = "高雄")
    public ResponseResult editPositionLevel(PositionLevelVo positionLevelVo){
        return positionLevelService.editPositionLevel(getUserSession(), positionLevelVo);
    }

    @GetMapping("/deletePositionLevel")
    @ApiOperation(value = "删除职级", notes = "高雄")
    @ApiImplicitParam(name="positionLevelIds", value = "职级id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
    public ResponseResult deletePositionLevel(List<Integer> positionLevelIds){
        return positionLevelService.deletePositionLevel(positionLevelIds, getUserSession());
    }

















}
