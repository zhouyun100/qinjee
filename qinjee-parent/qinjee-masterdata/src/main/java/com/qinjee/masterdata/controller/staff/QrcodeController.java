package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.service.staff.IQrcodeService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class QrcodeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QrcodeController.class);
    @Autowired
    private IQrcodeService qrcodeService;
    /**
     * 处理二维码请求类
     */
    @RequestMapping(value = "/dealQrcodeRequest", method = RequestMethod.GET)
    @ApiOperation(value = "处理二维码请求类", notes = "hkt")
    public ResponseResult selectCustomTableForArc( @Param ("date") Date date,
                                                  @Param ( "templateId" ) Integer templateId,
                                                  @Param ( "companyId" ) Integer companyId) {
        //companyId, funcCode
        Boolean b = checkParam(date,templateId,companyId);
        if (b) {
                qrcodeService.dealQrcodeRequest(date,templateId,companyId);
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }
    /**
     * 发送预入职验证码
     */
    @RequestMapping(value = "/sendPreCheckCode", method = RequestMethod.GET)
    @ApiOperation(value = "发送预入职验证码", notes = "hkt")
    public ResponseResult selectCustomTableForArc( String phone) {
        //companyId, funcCode
        Boolean b = checkParam(phone);
        if (b) {
                qrcodeService.sendPreCheckCode(phone);
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }
    /**
     * 检验是否登陆成功并跳转到数据页面
     */
    @RequestMapping(value = "/checkPreCodeAndRedirect", method = RequestMethod.GET)
    @ApiOperation(value = "检验是否登陆成功并跳转到数据页面", notes = "hkt")
    public ResponseResult checkPreCodeAndRedirect( String phone,String code,HttpServletResponse response,Integer templateId,Integer companyId ) throws Exception {
        //companyId, funcCode
        Boolean b = checkParam(phone,code,response,templateId,companyId);
        if (b) {
                qrcodeService.checkPreCodeAndRedirect(phone,code,response,templateId,companyId);
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }

    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }
}
