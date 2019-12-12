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
    public ResponseResult selectCustomTableForArc(HttpServletResponse response, @Param ("date") Date date,
                                                  @Param ( "templateId" ) Integer templateId,
                                                  @Param ( "companyId" ) Integer companyId) {
        //companyId, funcCode
        Boolean b = checkParam(response,date,templateId,companyId);
        if (b) {
            try {
                qrcodeService.dealQrcodeRequest(response,date,templateId,companyId);
            } catch (Exception e) {
                return new ResponseResult <> ( null,CommonCode.BUSINESS_EXCEPTION );
            }
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

    private ResponseResult failResponseResult(String message){
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        logger.error(message);
        return fail;
    }
}
