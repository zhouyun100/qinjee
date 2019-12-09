package com.qinjee.masterdata.controller.staff;

import com.qinjee.exception.BusinessException;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.controller.organization.OrganizationController;
import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.staff.ReportFormService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 报表
 * @program: eTalent
 * @description:
 * @author: penghs
 * @create: 2019-12-09 10:00
 **/
@RestController
@RequestMapping("/staffreport")
@Api(tags = "【人员管理】报表相关接口")
public class ReportFormController extends BaseController {
    private static Logger logger = LogManager.getLogger(OrganizationController.class);
    @Autowired
    private ReportFormService reportFormService;
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";


    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }


    @RequestMapping(value = "/selectOrgIncreaseMemberDetail", method = RequestMethod.GET)
    @ApiOperation(value = "显示机构增员明细表", notes = "彭洪思")
    public ResponseResult selectOrgIncreaseMemberDetail(Integer orgId, Date startDate, Date endDate){
        //参数校验
        if (checkParam(orgId)) {
            try {
                List<UserArchiveVo> userArchiveList=reportFormService.selectOrgIncreaseMemberDetail(orgId,startDate,endDate);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

}
