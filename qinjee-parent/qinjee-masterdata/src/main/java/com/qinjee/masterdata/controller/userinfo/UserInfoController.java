package com.qinjee.masterdata.controller.userinfo;


import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.service.userinfo.UserLoginService;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 周赟
 * @date 2020/03/05
 */
@Api(tags = "【用户信息】用户信息接口")
@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {


    private static Logger logger = LogManager.getLogger(UserInfoController.class);

    private ResponseResult responseResult;

    @Autowired
    private UserLoginService userLoginService;

    @ApiOperation(value="用户ID和企业ID登录", notes="用户ID和企业ID获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "企业ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/changeCompany",method = RequestMethod.POST)
    public ResponseResult<UserInfoVO> changeCompany(HttpServletResponse response, Integer companyId) {

        if(null == companyId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("用户信息或企业信息为空!");
            return responseResult;
        }

        try {
            UserInfoVO userInfo = userLoginService.searchUserInfoByUserIdAndCompanyId(userSession.getUserId(),companyId);
            if (null == userInfo) {
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("企业用户信息为空!");
                return responseResult;
            }

            setSessionAndCookie(response,userInfo);
            logger.info("changeCompany success！userId={};companyId={}", userSession.getUserId(), companyId);
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(userInfo);

        }catch(Exception e) {
            logger.info("changeCompany exception! userId={};companyId={};exception={}", userSession.getUserId(), companyId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("切换企业异常！");
        }
        return responseResult;

    }
}
