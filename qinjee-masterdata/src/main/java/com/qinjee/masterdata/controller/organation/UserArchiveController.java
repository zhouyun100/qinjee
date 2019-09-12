package com.qinjee.masterdata.controller.organation;

import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.organation.PageQueryVo;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月12日 10:44:00
 */
@Api(tags = "【机构管理】岗位用户信息接口")
@RequestMapping("/userArchive")
@RestController
public class UserArchiveController {


    @GetMapping("/getUserArchiveList")
    @ApiOperation(value = "根据条件分页查询注册用户信息", notes = "高雄")
    public ResponseResult<UserInfo> getUserArchiveList(PageQueryVo pageQueryVo){

        return null;
    }

    @GetMapping("/resetPassword")
    @ApiOperation(value = "根据条件分页查询注册用户信息", notes = "高雄")
    public ResponseResult resetPassword(@ApiParam(value = "档案ID", example = "1", required = true) Integer archiveId,
                                        @ApiParam(value = "新密码", example = "123456") String password){

        return null;
    }





















}
