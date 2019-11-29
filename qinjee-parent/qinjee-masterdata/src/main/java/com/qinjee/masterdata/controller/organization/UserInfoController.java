package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.organization.query.PageQuery;
import com.qinjee.model.response.ResponseResult;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月12日 10:09:00
 */
//@Api(tags = "【机构管理】注册用户信息接口")
//@RestController
//@RequestMapping("/userInfo")
public class UserInfoController {

  //    @GetMapping("/getUserInfoList")
//    @ApiOperation(value = "根据条件分页查询注册用户信息", notes = "高雄")
  public ResponseResult<UserInfo> getUserInfoList(PageQuery pageQueryVo) {
    return null;
  }


}
