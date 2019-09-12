package com.qinjee.masterdata.controller.organation;

import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.organation.PageQueryVo;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月12日 10:09:00
 */
@Api(tags = "注册用户信息相关接口")
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @GetMapping("/getUserInfoList")
    @ApiOperation(value = "根据条件分页查询注册用户信息", notes = "高雄")
    public ResponseResult<UserInfo> getUserInfoList(PageQueryVo pageQueryVo){
        return null;
    }


















}
