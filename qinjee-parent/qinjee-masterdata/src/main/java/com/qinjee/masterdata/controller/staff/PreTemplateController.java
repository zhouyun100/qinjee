package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.TemplateAttachmentGroup;
import com.qinjee.masterdata.model.entity.TemplateEntryRegistration;
import com.qinjee.masterdata.model.vo.SaveTemplateVo;
import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableFieldVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableVO;
import com.qinjee.masterdata.model.vo.staff.PreRegistVo;
import com.qinjee.masterdata.model.vo.staff.entryregistration.TemplateAttachmentGroupVO;
import com.qinjee.masterdata.service.custom.TemplateCustomTableFieldService;
import com.qinjee.masterdata.service.staff.EntryRegistrationService;
import com.qinjee.masterdata.service.staff.IPreTemplateService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    public ResponseResult  createPreRegistQrcode(Integer templateId, HttpServletResponse response) {
        Boolean b = checkParam (templateId,response,getUserSession () );
        if (b) {
            try {
                preTemplateService.createPreRegistQrcode(templateId,response,getUserSession ());
                return null;
            } catch (Exception e) {
                return failResponseResult ( "生成失败" );
            }
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
            try {
                Integer preId=preTemplateService.selectPreIdByPhone(phone,getUserSession ());
                return new ResponseResult ( preId,CommonCode.SUCCESS );
            } catch (Exception e) {
                return failResponseResult ( "获得id失败" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 扫描二维码跳到信息填写页面
     */
    @CrossOrigin
    @RequestMapping(value = "/ToCompleteMessage", method = RequestMethod.GET)
    @ApiOperation(value = "扫描二维码跳到信息填写页面", notes = "hkt")
    public ResponseResult  ToCompleteMessage(String phone, String s, String code) {
        Boolean b = checkParam (phone,getUserSession (),s,code );
        if (b) {
            try {
                preTemplateService.toCompleteMessage (phone,s,code);
                return null;
            } catch (Exception e) {
                return failResponseResult ( "获得id失败" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }
    /**
     * 发送预入职登记
     */
    @CrossOrigin
    @RequestMapping(value = "/sendPreRegist", method = RequestMethod.POST)
    @ApiOperation(value = "发送预入职登记", notes = "hkt")
    public ResponseResult sendPreRegist(@RequestBody @Valid PreRegistVo preRegistVo) {
        Boolean b = checkParam(preRegistVo,getUserSession ());
        if (b) {
            try {
                preTemplateService.sendRegisterMessage(preRegistVo,getUserSession ());
                return new ResponseResult<>(null, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 根据企业ID查询入职登记模板列表
     */
    @CrossOrigin
    @RequestMapping(value = "/searchTemplateEntryRegistrationList", method = RequestMethod.GET)
    @ApiOperation(value = "根据企业ID查询入职登记模板列表", notes = "hkt")
    public ResponseResult < List < TemplateEntryRegistration > > searchTemplateEntryRegistrationList(Integer companyId) {
        Boolean b = checkParam ( companyId );
        if (b) {
            try {
                List < TemplateEntryRegistration > templateEntryRegistrations =
                        entryRegistrationService.searchTemplateEntryRegistrationList ( companyId );
                if (CollectionUtils.isEmpty ( templateEntryRegistrations )) {
                    return failResponseResult ( "查询失败" );
                } else {
                    return new ResponseResult <> ( templateEntryRegistrations, CommonCode.SUCCESS );
                }
            } catch (Exception e) {
                return failResponseResult ( "查询异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 生成logo背景图路径
     * @param file
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/createBackGraundPhoto", method = RequestMethod.GET)
    @ApiOperation(value = "生成logo背景图路径", notes = "hkt")
    public ResponseResult<String> getLogoPath(MultipartFile file)  {
        Boolean b = checkParam ( file,getUserSession () );
       if(b){
           try {
               String s=preTemplateService.createBackGroundPhoto(file,getUserSession ());
                return new ResponseResult <> ( s,CommonCode.SUCCESS );
           } catch (Exception e) {
               e.printStackTrace ();
           }

       }
        return failResponseResult ( "文件为空或者session错误" );
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
            try {
                entryRegistrationService.addTemplateEntryRegistration ( templateEntryRegistration );
                return new ResponseResult ( null, CommonCode.SUCCESS );
            } catch (Exception e) {
                return failResponseResult ( "查询异常" );
            }

        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 删除入职登记模板
     *
     * @param templateId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/deleteTemplateEntryRegistration", method = RequestMethod.GET)
    @ApiOperation(value = "删除入职登记模板", notes = "hkt")
    public ResponseResult deleteTemplateEntryRegistration(Integer templateId) {
        Boolean b = checkParam ( templateId, getUserSession () );
        if (b) {
            try {
                entryRegistrationService.deleteTemplateEntryRegistration ( templateId, getUserSession ().getArchiveId () );
                return new ResponseResult ( null, CommonCode.SUCCESS );
            } catch (Exception e) {
                return failResponseResult ( "删除异常" );
            }

        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 修改入职登记模板
     *
     * @param templateEntryRegistration
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/modifyTemplateEntryRegistration", method = RequestMethod.POST)
    @ApiOperation(value = "修改入职登记模板", notes = "hkt")
    public ResponseResult modifyTemplateEntryRegistration(@RequestBody TemplateEntryRegistration
                                                                  templateEntryRegistration) {
        Boolean b = checkParam ( templateEntryRegistration );
        if (b) {
            try {
                entryRegistrationService.modifyTemplateEntryRegistration ( templateEntryRegistration );
                return new ResponseResult ( null, CommonCode.SUCCESS );
            } catch (Exception e) {
                return failResponseResult ( "修改异常" );
            }

        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 根据模板ID获取模板详情
     *
     * @param templateId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getTemplateEntryRegistrationByTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板ID获取模板详情", notes = "hkt")
    public ResponseResult < TemplateEntryRegistration > getTemplateEntryRegistrationByTemplateId(Integer templateId) {
        Boolean b = checkParam ( templateId );
        if (b) {
            try {
                TemplateEntryRegistration templateEntryRegistrationByTemplateId =
                        entryRegistrationService.getTemplateEntryRegistrationByTemplateId ( templateId );
                return new ResponseResult <> ( templateEntryRegistrationByTemplateId, CommonCode.SUCCESS );
            } catch (Exception e) {
                return failResponseResult ( "展示异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 根据模板ID查询模板附件配置列表
     *
     * @param templateId 模板ID
     * @param isAll      是否显示全部(0：是[包含系统默认且未配置的信息]，1：否[仅显示模板已配置的附件信息])
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/searchTemplateAttachmentListByTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板ID查询模板附件配置列表", notes = "hkt")
    public ResponseResult < List < TemplateAttachmentGroupVO > > searchTemplateAttachmentListByTemplateId(Integer templateId, Integer isAll) {
        Boolean b = checkParam ( templateId );
        if (b) {
            try {

                List < TemplateAttachmentGroupVO > templateAttachmentGroupVOS =
                        entryRegistrationService.searchTemplateAttachmentListByTemplateId ( templateId, isAll );
                if (CollectionUtils.isEmpty ( templateAttachmentGroupVOS )) {
                    return new ResponseResult <> ( null, CommonCode.FAIL_VALUE_NULL );

                } else {
                    return new ResponseResult <> ( templateAttachmentGroupVOS, CommonCode.SUCCESS );
                }
            } catch (Exception e) {
                return failResponseResult ( "展示异常" );
            }

        }
        return failResponseResult ( "参数错误或者session错误" );
    }
    /**
     * 根据模板附件ID查询模板附件详情
     * @param tagId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getTemplateAttachmentListByTagId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板附件ID查询模板附件详情", notes = "hkt")
    public ResponseResult<TemplateAttachmentGroupVO>  getTemplateAttachmentListByTagId(Integer tagId) {
        Boolean b = checkParam ( tagId );
        if (b) {
            try {
                TemplateAttachmentGroupVO templateAttachmentListByTagId = entryRegistrationService.getTemplateAttachmentListByTagId ( tagId );
                return new ResponseResult <> ( templateAttachmentListByTagId, CommonCode.SUCCESS );
            } catch (Exception e) {
                e.printStackTrace ();
                return failResponseResult ( "展示异常" );
            }

        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 修改模板附件信息
     *
     * @param templateAttachmentGroup
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/modifyTemplateAttachmentGroup", method = RequestMethod.POST)
    @ApiOperation(value = "修改模板附件信息", notes = "hkt")
    public ResponseResult modifyTemplateAttachmentGroup(@RequestBody TemplateAttachmentGroup templateAttachmentGroup) {
        Boolean b = checkParam ( templateAttachmentGroup );
        if (b) {
            try {
                entryRegistrationService.modifyTemplateAttachmentGroup ( templateAttachmentGroup );
                return new ResponseResult <> ( null, CommonCode.SUCCESS );

            } catch (Exception e) {
                return failResponseResult ( "修改异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }
    /**
     * 新增模板附件信息
     * @param list
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/addTemplateAttachmentGroup", method = RequestMethod.POST)
    @ApiOperation(value = "新增模板附件信息", notes = "hkt")
    public ResponseResult addTemplateAttachmentGroup(@RequestBody List<TemplateAttachmentGroup> list) {
        Boolean b = checkParam ( list );
        if (b) {
            try {
                entryRegistrationService.addTemplateAttachmentGroup ( list );
                return new ResponseResult <> ( null, CommonCode.SUCCESS );
            } catch (Exception e) {
                e.printStackTrace ();
                return failResponseResult ( "新增异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }


    /**
     * 删除模板附件信息
     * @param tagId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/delTemplateAttachmentGroup", method = RequestMethod.GET)
    @ApiOperation(value = "删除模板附件信息", notes = "hkt")
    public ResponseResult delTemplateAttachmentGroup(Integer tagId) {
        Boolean b = checkParam ( tagId,getUserSession () );
        if (b) {
            try {
                entryRegistrationService.delTemplateAttachmentGroup ( tagId,getUserSession ().getArchiveId () );
                return new ResponseResult <> ( null, CommonCode.SUCCESS );

            } catch (Exception e) {
                return failResponseResult ( "删除异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 模板附件排序
     * @param templateAttachmentGroupList
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/sortTemplateAttachmentGroup", method = RequestMethod.POST)
    @ApiOperation(value = "模板附件排序", notes = "hkt")
    public ResponseResult sortTemplateAttachmentGroup(@RequestBody List<TemplateAttachmentGroup> templateAttachmentGroupList) {
        Boolean b = checkParam ( templateAttachmentGroupList,getUserSession () );
        if (b) {
            try {
                entryRegistrationService.sortTemplateAttachmentGroup ( templateAttachmentGroupList, getUserSession ().getArchiveId () );
                return new ResponseResult <> ( null, CommonCode.SUCCESS );

            } catch (Exception e) {
                return failResponseResult ( "排序异常" );
            }
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
            if (!CollectionUtils.isEmpty ( templateCustomTableVOS )) {
                return new ResponseResult <> ( templateCustomTableVOS, CommonCode.SUCCESS );
            } else {
                return new ResponseResult <> ( null, CommonCode.FAIL_VALUE_NULL );
            }
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }


    /**
     * 设置模板下的自定义表
     * @param list
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/setCustomTableForTemplate", method = RequestMethod.POST)
    @ApiOperation(value = "设置模板下的自定义表", notes = "hkt")
    public ResponseResult  searchTableListByCompanyIdAndTemplateId(@RequestBody List < TemplateCustomTableVO > list) {
        Boolean b = checkParam ( list,getUserSession () );
        if (b) {
            try {
                        templateCustomTableFieldService.setCustomTableForTemplate (list,getUserSession () );
               return new ResponseResult ( null,CommonCode.SUCCESS );
            } catch (Exception e) {
                return failResponseResult ( "获取值异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }
    /**
     * 根据模板ID查询所有表字段信息
     * @param templateId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/searchTableFieldListByTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板ID查询所有表字段信息", notes = "hkt")
    public ResponseResult <List < TemplateCustomTableVO >> searchTableFieldListByTemplateId(Integer templateId) {
        Boolean b = checkParam (templateId );
        if (b) {
            try {
                List < TemplateCustomTableVO > templateCustomTableVOS =
                        templateCustomTableFieldService.searchTableFieldListByTemplateId (templateId );
                if(!CollectionUtils.isEmpty ( templateCustomTableVOS )){
                    return new ResponseResult <> ( templateCustomTableVOS,CommonCode.SUCCESS );
                }else {
                    return new ResponseResult <> ( null, CommonCode.FAIL_VALUE_NULL );
                }
            } catch (Exception e) {
                return failResponseResult ( "获取值异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 根据表ID和模板ID查询对应表字段配置信息
     * @param tableId
     * @param templateId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/searchFieldListByTableIdAndTemplateId", method = RequestMethod.GET)
    @ApiOperation(value = "根据表ID和模板ID查询对应表字段配置信息", notes = "hkt")
    public ResponseResult <List < TemplateCustomTableFieldVO >> searchTableFieldListByTemplateId(Integer tableId,Integer templateId) {
        Boolean b = checkParam (templateId,tableId );
        if (b) {
            try {

                List < TemplateCustomTableFieldVO > templateCustomTableFieldVOS
                        = templateCustomTableFieldService.searchFieldListByTableIdAndTemplateId ( tableId, templateId );
                if(!CollectionUtils.isEmpty ( templateCustomTableFieldVOS )){
                    return new ResponseResult <> ( templateCustomTableFieldVOS,CommonCode.SUCCESS );
                }else {
                    return new ResponseResult <> ( null, CommonCode.FAIL_VALUE_NULL );
                }
            } catch (Exception e) {
                return failResponseResult ( "获取值异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 保存模板信息以及欢迎页信息
     * @param saveTemplateVo
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/saveTemplate", method = RequestMethod.POST)
    @ApiOperation(value = "保存自定义表字段配置", notes = "hkt")
    public ResponseResult  saveTemplate(@RequestBody SaveTemplateVo saveTemplateVo) {
        Boolean b = checkParam (getUserSession (),saveTemplateVo ,getUserSession ());
        if (b) {
            try {
                templateCustomTableFieldService.saveTemplate ( getUserSession ().getArchiveId (),saveTemplateVo );
                    return new ResponseResult <> ( null, CommonCode.BUSINESS_EXCEPTION );
            } catch (Exception e) {
                return failResponseResult ( "更改异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 根据模板ID查询自定义表及字段信息
     * templateId：模板ID
     * preId：预入职ID
     * @param templateId 模板ID
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/searchCustomTableListByTemplateIdAndArchiveId", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板ID查询自定义表及字段信息", notes = "hkt")
    public ResponseResult <List < EntryRegistrationTableVO >> searchCustomTableListByTemplateIdAndArchiveId(Integer templateId) {
        Boolean b = checkParam (templateId );
        if (b) {
            try {
                List < EntryRegistrationTableVO > entryRegistrationTableVOS =
                        templateCustomTableFieldService.searchCustomTableListByTemplateId ( templateId );
                if(!CollectionUtils.isEmpty ( entryRegistrationTableVOS )){
                    return new ResponseResult <> ( entryRegistrationTableVOS, CommonCode.SUCCESS);
                }else{
                    return new ResponseResult <> ( entryRegistrationTableVOS, CommonCode.FAIL_VALUE_NULL );
                }
            } catch (Exception e) {
                return failResponseResult ( "更改异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    /**
     * 处理自定义表字段数据回填
     * @param preId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/handlerCustomTableGroupFieldList", method = RequestMethod.GET)
    @ApiOperation(value = "处理自定义表字段数据回填", notes = "hkt")
    public ResponseResult <List < EntryRegistrationTableVO >> handlerCustomTableGroupFieldList(Integer preId,Integer templateId,Integer companyId,HttpServletResponse response ) {
        Boolean b = checkParam (preId,templateId,companyId,response);
        if (b) {
            try {
                response.setHeader ( "Access-Control-Allow-Origin","*" );
                List < EntryRegistrationTableVO > list =
                        preTemplateService.handlerCustomTableGroupFieldList ( companyId, preId,templateId );
                if(!CollectionUtils.isEmpty ( list )){
                    return new ResponseResult <> ( list, CommonCode.SUCCESS);
                }else{
                    return new ResponseResult <> ( null, CommonCode.FAIL_VALUE_NULL );
                }
            } catch (Exception e) {
                e.printStackTrace ();
                return failResponseResult ( "更改异常" );
            }
        }
        return failResponseResult ( "参数错误或者session错误" );
    }

    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals ( param )) {
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
