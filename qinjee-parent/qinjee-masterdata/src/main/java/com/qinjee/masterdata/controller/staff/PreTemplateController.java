package com.qinjee.masterdata.controller.staff;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.TemplateAttachmentGroup;
import com.qinjee.masterdata.model.entity.TemplateEntryRegistration;
import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableFieldVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableVO;
import com.qinjee.masterdata.model.vo.staff.PreRegistVo;
import com.qinjee.masterdata.model.vo.staff.entryregistration.*;
import com.qinjee.masterdata.service.custom.TemplateCustomTableFieldService;
import com.qinjee.masterdata.service.staff.EntryRegistrationService;
import com.qinjee.masterdata.service.staff.IPreTemplateService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffTemp")
@Api(tags = "【人员管理】预入职登记模板")
public class PreTemplateController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger ( PreTemplateController.class );
    @Autowired
    private IPreTemplateService preTemplateService;
    @Autowired
    private EntryRegistrationService entryRegistrationService;
    @Autowired
    private TemplateCustomTableFieldService templateCustomTableFieldService;
    /**
     *
     生成预入职登记二维码
     */
    @CrossOrigin
    @RequestMapping(value = "/createPreRegistQrcode", method = RequestMethod.GET)
    @ApiOperation(value = "生成预入职登记二维码", notes = "hkt")
    public ResponseResult  createPreRegistQrcode(Integer templateId, HttpServletResponse response) throws Exception {
        Boolean b = checkParam (templateId,response,getUserSession () );
        if (b) {
                preTemplateService.createPreRegistQrcode(templateId,response,getUserSession ());
                return null;
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 根据电话号码找到preId
     * @param phone
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/selectPreIdByPhone", method = RequestMethod.GET)
    @ApiOperation(value = "根据电话号码找到preId", notes = "hkt")
    public ResponseResult  selectPreIdByPhone(String phone) {
        Boolean b = checkParam (phone,getUserSession () );
        if (b) {
                List<Integer> preId=preTemplateService.selectPreIdByPhone(phone,getUserSession ());
                return new ResponseResult<> ( preId,CommonCode.SUCCESS );
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 扫描二维码跳到信息填写页面
     */
    @CrossOrigin
    @RequestMapping(value = "/ToCompleteMessage", method = RequestMethod.GET)
    @ApiOperation(value = "扫描二维码跳到信息填写页面", notes = "hkt")
    public ResponseResult  ToCompleteMessage(String phone, String s, String code) throws Exception {
        Boolean b = checkParam (phone,getUserSession (),s,code );
        if (b) {
                preTemplateService.toCompleteMessage (phone,s,code);
                return null;
        }
        return failResponseResult ( "参数错误或者session错误" );
    }
    /**
     * 发送预入职登记
     */
//    @CrossOrigin
    @RequestMapping(value = "/sendPreRegist", method = RequestMethod.POST)
    @ApiOperation(value = "发送预入职登记", notes = "hkt")
    public ResponseResult sendPreRegist(@RequestBody PreRegistVo preRegistVo) throws Exception {
        preTemplateService.sendRegisterMessage(preRegistVo,getUserSession ());
        return new ResponseResult<>(null, CommonCode.SUCCESS);
    }

    /**
     * 根据企业ID查询入职登记模板列表
     */
    @CrossOrigin
    @RequestMapping(value = "/searchTemplateEntryRegistrationList", method = RequestMethod.GET)
    @ApiOperation(value = "根据企业ID查询入职登记模板列表", notes = "hkt")
    public ResponseResult < List < TemplateEntryRegistrationVO > > searchTemplateEntryRegistrationList() {
        Boolean b = checkParam ( getUserSession () );
        if (b) {
                List <TemplateEntryRegistrationVO> templateEntryRegistrations =
                        entryRegistrationService.searchTemplateEntryRegistrationList ( getUserSession ().getCompanyId () );
                    return new ResponseResult <> ( templateEntryRegistrations, CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 生成logo背景图路径
     * @param file 文件
     */
    @CrossOrigin
    @RequestMapping(value = "/createBackGraundPhoto", method = RequestMethod.GET)
    @ApiOperation(value = "生成logo背景图路径", notes = "hkt")
    public ResponseResult<String> getLogoPath(MultipartFile file) throws Exception {
        Boolean b = checkParam ( file,getUserSession () );
       if(b){
               String s=preTemplateService.createBackGroundPhoto(file,getUserSession ());
                return new ResponseResult <> ( s,CommonCode.SUCCESS );
       }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 新增入职登记模板
     */
    @CrossOrigin
    @RequestMapping(value = "/addTemplateEntryRegistration", method = RequestMethod.POST)
    @ApiOperation(value = "新增入职登记模板", notes = "hkt")
    public ResponseResult addTemplateEntryRegistration(@RequestBody TemplateEntryRegistration templateEntryRegistration) {
        Boolean b = checkParam ( templateEntryRegistration );
        if (b) {
                entryRegistrationService.addTemplateEntryRegistration ( templateEntryRegistration );
                return new ResponseResult<> ( null, CommonCode.SUCCESS );
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 删除入职登记模板
     *
     * @param templateId 模板id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/deleteTemplateEntryRegistration", method = RequestMethod.GET)
    @ApiOperation(value = "删除入职登记模板", notes = "hkt")
    public ResponseResult deleteTemplateEntryRegistration(Integer templateId) {
        Boolean b = checkParam ( templateId, getUserSession () );
        if (b) {
                entryRegistrationService.deleteTemplateEntryRegistration ( templateId, getUserSession ().getArchiveId () );
                return new ResponseResult<> ( null, CommonCode.SUCCESS );
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 修改入职登记模板
     *
     * @param templateEntryRegistration 模板对象
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/modifyTemplateEntryRegistration", method = RequestMethod.POST)
    @ApiOperation(value = "修改入职登记模板", notes = "hkt")
    public ResponseResult modifyTemplateEntryRegistration(@RequestBody TemplateEntryRegistration
                                                                  templateEntryRegistration) {
        Boolean b = checkParam ( templateEntryRegistration );
        if (b) {
                entryRegistrationService.modifyTemplateEntryRegistration ( templateEntryRegistration );
                return new ResponseResult<> ( null, CommonCode.SUCCESS );
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 根据模板ID获取模板详情
     *
     * @param templateId 模板id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getTemplateEntryRegistrationByTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板ID获取模板详情", notes = "hkt")
    public ResponseResult < TemplateEntryRegistration > getTemplateEntryRegistrationByTemplateId(Integer templateId) {
        Boolean b = checkParam ( templateId );
        if (b) {
                TemplateEntryRegistration templateEntryRegistrationByTemplateId =
                        entryRegistrationService.getTemplateEntryRegistrationByTemplateId ( templateId );
                return new ResponseResult <> ( templateEntryRegistrationByTemplateId, CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 根据模板ID查询模板附件配置列表
     *
     * @param templateId 模板ID
     * @param isAll      是否显示全部(0：是[包含系统默认且未配置的信息]，1：否[仅显示模板已配置的附件信息])
     */
    @CrossOrigin
    @RequestMapping(value = "/searchTemplateAttachmentListByTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板ID查询模板附件配置列表", notes = "hkt")
    public ResponseResult < List < TemplateAttachmentGroupVO > > searchTemplateAttachmentListByTemplateId(Integer templateId, Integer isAll) {
        Boolean b = checkParam ( templateId );
        if (b) {
                List < TemplateAttachmentGroupVO > templateAttachmentGroupVOS =
                        entryRegistrationService.searchTemplateAttachmentListByTemplateId ( templateId, isAll );
                    return new ResponseResult <> ( templateAttachmentGroupVOS, CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
    /**
     * 根据模板附件ID查询模板附件详情
     * @param tagId 附件id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getTemplateAttachmentListByTagId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板附件ID查询模板附件详情", notes = "hkt")
    public ResponseResult<TemplateAttachmentGroupVO>  getTemplateAttachmentListByTagId(Integer tagId) {
        Boolean b = checkParam ( tagId );
        if (b) {
                TemplateAttachmentGroupVO templateAttachmentListByTagId = entryRegistrationService.getTemplateAttachmentListByTagId ( tagId );
                return new ResponseResult <> ( templateAttachmentListByTagId, CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 修改模板附件信息
     *
     * @param templateAttachmentGroup 附件组
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/modifyTemplateAttachmentGroup", method = RequestMethod.POST)
    @ApiOperation(value = "修改模板附件信息", notes = "hkt")
    public ResponseResult modifyTemplateAttachmentGroup(@RequestBody TemplateAttachmentGroup templateAttachmentGroup) {
        Boolean b = checkParam ( templateAttachmentGroup );
        if (b) {
                entryRegistrationService.modifyTemplateAttachmentGroup ( templateAttachmentGroup );
                return new ResponseResult <> ( null, CommonCode.SUCCESS );
        }
        return failResponseResult ( "参数错误或者session错误" );
    }


    /**
     * 新增模板附件信息
     * @param insertTemplateAttachmentVo 附件组信息
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/addTemplateAttachmentGroup", method = RequestMethod.POST)
    @ApiOperation(value = "新增模板附件信息", notes = "hkt")
    public ResponseResult addTemplateAttachmentGroup(@RequestBody InsertTemplateAttachmentVo insertTemplateAttachmentVo) {
        Boolean b = checkParam ( insertTemplateAttachmentVo, getUserSession () );
        if (b) {
            if (checkParam ( insertTemplateAttachmentVo.getTemplatedId () )) {
                entryRegistrationService.addTemplateAttachmentGroup ( insertTemplateAttachmentVo.getTemplatedId (), insertTemplateAttachmentVo.getList (), getUserSession ().getArchiveId () );

                return new ResponseResult <> ( null, CommonCode.SUCCESS );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }


    /**
     * 删除模板附件信息
     * @param tagId 附件id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/delTemplateAttachmentGroup", method = RequestMethod.GET)
    @ApiOperation(value = "删除模板附件信息", notes = "hkt")
    public ResponseResult delTemplateAttachmentGroup(Integer tagId) {
        Boolean b = checkParam ( tagId,getUserSession () );
        if (b) {
                entryRegistrationService.delTemplateAttachmentGroup ( tagId,getUserSession ().getArchiveId () );
                return new ResponseResult <> ( null, CommonCode.SUCCESS );
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 模板附件排序
     * @param templateAttachmentGroupList 附件组list
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/sortTemplateAttachmentGroup", method = RequestMethod.POST)
    @ApiOperation(value = "模板附件排序", notes = "hkt")
    public ResponseResult sortTemplateAttachmentGroup(@RequestBody List<TemplateAttachmentGroup> templateAttachmentGroupList) {
        Boolean b = checkParam ( templateAttachmentGroupList,getUserSession () );
        if (b) {
                entryRegistrationService.sortTemplateAttachmentGroup ( templateAttachmentGroupList, getUserSession ().getArchiveId () );
                return new ResponseResult <> ( null, CommonCode.SUCCESS );
        }
        return failResponseResult ( "参数错误或者session错误" );
    }
    /**
     * 根据企业ID和模板ID查询自定义表列表
     * 根据模板id或者全量查询企业下的自定义表，新增展示自定义表名时可复用此接口全量查询
     * @param templateId 模板ID
     */
    @CrossOrigin
    @RequestMapping(value = "/searchTableListByCompanyIdAndTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据企业ID和模板ID查询自定义表列表", notes = "hkt")
    public ResponseResult <List < TemplateCustomTableVO >> searchTableListByCompanyIdAndTemplateId(Integer templateId) {
        Boolean b = checkParam ( templateId, getUserSession () );
        if (b) {
            List < TemplateCustomTableVO > templateCustomTableVOS =
                    templateCustomTableFieldService.searchTableListByCompanyIdAndTemplateId ( getUserSession ().getCompanyId (), templateId );
                  return new ResponseResult <> ( templateCustomTableVOS, CommonCode.SUCCESS );
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }


    /**
     * 设置模板下的自定义表
     * @param list 模板自定义表
     */
    @CrossOrigin
    @RequestMapping(value = "/setCustomTableForTemplate", method = RequestMethod.POST)
    @ApiOperation(value = "设置模板下的自定义表", notes = "hkt")
    public ResponseResult  searchTableListByCompanyIdAndTemplateId(@RequestBody List < TemplateCustomTableVO > list) {
        Boolean b = checkParam ( list,getUserSession () );
        if (b) {
            templateCustomTableFieldService.setCustomTableForTemplate (list,getUserSession () );
               return new ResponseResult<> ( null,CommonCode.SUCCESS );
        }
        return failResponseResult ( "参数错误或者session错误" );
    }
    /**
     * 根据模板ID查询所有表字段信息
     * @param templateId 模板id
     */
    @CrossOrigin
    @RequestMapping(value = "/searchTableFieldListByTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板ID查询所有表字段信息", notes = "hkt")
    public ResponseResult <List < TemplateCustomTableVO >> searchTableFieldListByTemplateId(Integer templateId) {
        Boolean b = checkParam (templateId );
        if (b) {
                List < TemplateCustomTableVO > templateCustomTableVOS =
                        templateCustomTableFieldService.searchTableFieldListByTemplateId (templateId );
                    return new ResponseResult <> ( templateCustomTableVOS,CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 根据表ID和模板ID查询对应表字段配置信息
     * @param tableId 表id
     * @param templateId 模板id
     * @return
     */
//    @CrossOrigin
    @RequestMapping(value = "/searchFieldListByTableIdAndTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据表ID和模板ID查询对应表字段配置信息", notes = "hkt")
    public ResponseResult <List < TemplateCustomTableFieldVO >> searchTableFieldListByTemplateId(Integer tableId,Integer templateId) {
        Boolean b = checkParam (templateId,tableId );
        if (b) {
                List < TemplateCustomTableFieldVO > templateCustomTableFieldVOS
                        = templateCustomTableFieldService.searchFieldListByTableIdAndTemplateId ( tableId, templateId );
                    return new ResponseResult <> ( templateCustomTableFieldVOS,CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 保存模板信息以及欢迎页信息
     * @param saveTemplateVo 模板内容对象
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/saveTemplate", method = RequestMethod.POST)
    @ApiOperation(value = "保存自定义表字段配置", notes = "hkt")
    public ResponseResult  saveTemplate(@RequestBody SaveTemplateVo saveTemplateVo) {
        Boolean b = checkParam (getUserSession (),saveTemplateVo );
        if (b) {
            try {
                templateCustomTableFieldService.saveTemplate ( getUserSession (),saveTemplateVo );
                return new ResponseResult <> ( null, CommonCode.SUCCESS );
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 根据模板ID查询自定义表及字段信息
     * templateId：模板ID
     * preId：预入职ID
     * @param templateId 模板ID
     */
    @CrossOrigin
    @RequestMapping(value = "/searchCustomTableListByTemplateIdAndArchiveId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板ID查询自定义表及字段信息", notes = "hkt")
    public ResponseResult <List < EntryRegistrationTableVO >> searchCustomTableListByTemplateIdAndArchiveId(Integer templateId) {
        Boolean b = checkParam (templateId );
        if (b) {
                List < EntryRegistrationTableVO > entryRegistrationTableVOS =
                        templateCustomTableFieldService.searchCustomTableListByTemplateId ( templateId );
                    return new ResponseResult <> ( entryRegistrationTableVOS, CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 处理自定义表字段数据回填
     * @param preId 预入职id
     */
    @CrossOrigin
    @RequestMapping(value = "/handlerCustomTableGroupFieldList", method = RequestMethod.GET)
    @ApiOperation(value = "处理自定义表字段数据回填", notes = "hkt")
    public ResponseResult <List < EntryTableListWithValueVo >> handlerCustomTableGroupFieldList(Integer preId, Integer templateId, Integer companyId )  {
        Boolean b = checkParam (preId,templateId,companyId);
        if (b) {

            List < EntryTableListWithValueVo > list =
                    null;
            try {
                list = preTemplateService.handlerCustomTableGroupFieldList ( companyId, preId,templateId );
            } catch (IllegalAccessException e) {
                e.printStackTrace ();
            }
            return new ResponseResult <> ( list, CommonCode.SUCCESS);

        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if(param instanceof UserSession){
                if(null == param|| "".equals(param)){
                    ExceptionCast.cast ( CommonCode.INVALID_SESSION );
                    return false;
                }
            }
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    private ResponseResult failResponseResult(String message) {
        ResponseResult fail = ResponseResult.FAIL ();
        fail.setMessage ( message );
        logger.error ( message );
        return fail;
    }
}
