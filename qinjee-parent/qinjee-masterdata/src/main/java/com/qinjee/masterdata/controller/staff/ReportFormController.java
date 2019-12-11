package com.qinjee.masterdata.controller.staff;

import com.github.liaochong.myexcel.core.HtmlToExcelFactory;
import com.github.liaochong.myexcel.core.WorkbookType;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import com.qinjee.exception.BusinessException;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.controller.organization.OrganizationController;
import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;
import com.qinjee.masterdata.model.vo.staff.RegulationCountVo;
import com.qinjee.masterdata.model.vo.staff.RegulationDetailVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.staff.ReportFormService;
import com.qinjee.masterdata.utils.DateUtil;
import com.qinjee.masterdata.utils.FileUtil;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

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


    @RequestMapping(value = "/selectOrgIncreaseMemberDetail", method = RequestMethod.POST)
    @ApiOperation(value = "显示机构增员明细表 {\"orgIds\":[12,25],\"startDate\":date,\"entDate\":date}", notes = "彭洪思")
    public ResponseResult selectOrgIncreaseMemberDetail(@RequestBody Map<String,Object> paramMap){
        //参数校验
        if (checkParam(paramMap)) {
            try {
                List<Integer> orgIds = null;
                Date startDate = null;
                Date endDate = null;
                if (paramMap.get("orgIds") != null && paramMap.get("orgIds") instanceof List) {
                    orgIds = (List<Integer>) paramMap.get("orgIds");
                }
                if (paramMap.get("startDate") != null  ) {
                    startDate=  DateUtil.date(String.valueOf(paramMap.get("startDate")));
                }
                if (paramMap.get("endDate") != null ) {
                    endDate=  DateUtil.date(String.valueOf(paramMap.get("endDate")));
                }
                List<RegulationDetailVo> regulationDetailList=reportFormService.selectOrgIncreaseMemberDetail(orgIds,startDate,endDate);
                return new ResponseResult(regulationDetailList, CommonCode.SUCCESS);
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
    @PostMapping (value = "/selectOrgDecreaseMemberDetail")
    @ApiOperation(value = "显示机构减员明细表 {\"orgIds\":[12,25],\"startDate\":date,\"entDate\":date}", notes = "彭洪思")
    public ResponseResult selectOrgDecreaseMemberDetail(@RequestBody Map<String,Object> paramMap){
        //参数校验
        if (checkParam(paramMap)) {
            try {
                List<Integer> orgIds = null;
                Date startDate = null;
                Date endDate = null;
                if (paramMap.get("orgIds") != null && paramMap.get("orgIds") instanceof List) {
                    orgIds = (List<Integer>) paramMap.get("orgIds");
                }
                if (paramMap.get("startDate") != null  ) {
                    startDate=  DateUtil.date(String.valueOf(paramMap.get("startDate")));
                }
                if (paramMap.get("endDate") != null ) {
                    endDate=  DateUtil.date(String.valueOf(paramMap.get("endDate")));
                }
                List<RegulationDetailVo> regulationDetailList=reportFormService.selectOrgDecreaseMemberDetail(orgIds,startDate,endDate);
                return new ResponseResult(regulationDetailList, CommonCode.SUCCESS);
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

    @PostMapping(value = "/selectOrgRegulationCount")
    @ApiOperation(value = "显示增减员统计表 {\"orgIds\":[12,25],\"startDate\":date,\"endDate\":date,\"layer\":2}", notes = "彭洪思")
    public ResponseResult selectOrgRegulationCount( @RequestBody Map<String,Object> paramMap){
        //参数校验 List<Integer> orgIds, Date startDate, Date endDate,Integer layer
        if (checkParam(paramMap)) {
            try {
                List<Integer> orgIds = null;
                Integer layer = null;
                Date startDate = null;
                Date endDate = null;
                if (paramMap.get("orgIds") != null && paramMap.get("orgIds") instanceof List) {
                    orgIds = (List<Integer>) paramMap.get("orgIds");
                }
                if (paramMap.get("startDate") != null  ) {
                    startDate=  DateUtil.date(String.valueOf(paramMap.get("startDate")));
                }
                if (paramMap.get("endDate") != null ) {
                    endDate=  DateUtil.date(String.valueOf(paramMap.get("endDate")));
                }
                if (paramMap.get("layer") != null ) {
                    layer = (Integer) paramMap.get("layer");
                }
                List<RegulationCountVo> regulationCountList=reportFormService.selectOrgRegulationCount(orgIds,startDate,endDate,layer,getUserSession());
                return new ResponseResult(regulationCountList, CommonCode.SUCCESS);
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

    @PostMapping(value = "/exportRegulation")
    @ApiOperation(value = "导出 ", notes = "彭洪思")
    public ResponseResult exportRegulation(@RequestBody  String htmlContent, HttpServletResponse response){
        //参数校验 List<Integer> orgIds, Date startDate, Date endDate,Integer layer
        if (checkParam(htmlContent)) {
            try {
                //TODO 将htmlContent写入file
                File tempFile = File.createTempFile("tempFile", ".html");

                System.out.println(htmlContent);
                FileUtil.write(tempFile,htmlContent, "UTF-8");
                Workbook workbook = HtmlToExcelFactory.readHtml(tempFile).workbookType(WorkbookType.SXLSX).build();
                tempFile.delete();
                // this is a example,you can write the workbook to any valid outputstream
                AttachmentExportUtil.export(workbook, "exportRegulation", response);
                return null;
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
