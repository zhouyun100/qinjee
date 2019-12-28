package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CustomTableVO;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.staff.CustomArchiveTableDataVo;
import com.qinjee.masterdata.model.vo.staff.InsertDataVo;
import com.qinjee.masterdata.model.vo.staff.OrganzitionVo;
import com.qinjee.masterdata.service.custom.CustomTableFieldService;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】公用设计相关接口")
public class CommonController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private IStaffCommonService staffCommonService;
    @Autowired
    private CustomTableFieldService customTableFieldService;

    /**
     * 新增自定义表
     */
//    @RequestMapping(value = "/insertArchiveCustomTable", method = RequestMethod.POST)
//    @ApiOperation(value = "新增自定义表", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveTable", value = "自定义表", paramType = "form" ,required = true)
    public ResponseResult insertCustomTable( @RequestBody @Valid CustomArchiveTable customArchiveTable) {
        Boolean b = checkParam(customArchiveTable);
        if (b) {
            try {
                staffCommonService.insertCustomArichiveTable(customArchiveTable, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义表失败");
            }

        }
        return failResponseResult("自定义表参数错误或者session错误");
    }
    /**
     * 删除自定义表
     */
//    @RequestMapping(value = "/deleteCustomArchiveTable", method = RequestMethod.POST)
//    @ApiOperation(value = "删除自定义表", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "自定义表id组成集合", paramType = "query", example = "{1,2}" ,allowMultiple = true)
    public ResponseResult deleteCustomArchiveTable(@RequestBody List<Integer> list) {
        Boolean b = checkParam(list);
        if (b) {
            try {
                staffCommonService.deleteCustomArchiveTable(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("逻辑删除自定义表失败");
            }
        }
        return failResponseResult("自定义表id集合参数错误");
    }

    /**
     * 自定义表修改
     */
//    @RequestMapping(value = "/updateCustomArchiveTable", method = RequestMethod.POST)
//    @ApiOperation(value = "修改自定义表", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveTable", value = "自定义字段表", paramType = "form" )
    public ResponseResult updateCustomArchiveTable(@RequestBody @Valid CustomArchiveTable customArchiveTable) {
        Boolean b = checkParam(customArchiveTable);
        if (b) {
            try {
                staffCommonService.updateCustomArchiveTable(customArchiveTable);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改自定义表失败");
            }
        }
        return failResponseResult("自定义表参数错误");
    }

    /**
     * 展示自定义表
     */
//    @RequestMapping(value = "/selectCustomArchiveTable", method = RequestMethod.POST)
//    @ApiOperation(value = "展示自定义表", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", required = true),
//    })
    public ResponseResult<PageResult<CustomArchiveTable>> selectCustomArchiveTable(Integer currentPage, Integer pageSize) {
        Boolean b = checkParam(currentPage, pageSize, getUserSession());
        if (b) {
            try {
                PageResult<CustomArchiveTable> pageResult = staffCommonService.selectCustomArchiveTable(currentPage, pageSize, getUserSession());
                if (!CollectionUtils.isEmpty(pageResult.getList())) {
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }
    /**
     * 根据企业ID和功能CODE查询自定义档案表
     */
    @RequestMapping(value = "/selectCustomTableForArc", method = RequestMethod.POST)
    @ApiOperation(value = "根据企业ID和功能CODE查询自定义档案表", notes = "hkt")
    public ResponseResult<List < CustomTableVO >> selectCustomTableForArc() {
        //companyId, funcCode
        Boolean b = checkParam(getUserSession ());
        if (b) {
            try {
                CustomTableVO customTableVO=new CustomTableVO ();
                customTableVO.setCompanyId ( getUserSession ().getCompanyId () );
                customTableVO.setFuncCode ( "ARC" );
                List < CustomTableVO > customTableVOS = customTableFieldService.searchCustomTableListByCompanyIdAndFuncCode ( customTableVO );
                return new ResponseResult <> ( customTableVOS, CommonCode.SUCCESS );
            } catch (Exception e) {
               return new ResponseResult <> ( null,CommonCode.BUSINESS_EXCEPTION );
            }
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }
    /**
     * 根据企业ID和功能CODE查询自定义预入职表
     */
    @RequestMapping(value = "/selectCustomTableForPre", method = RequestMethod.POST)
    @ApiOperation(value = "根据企业ID和功能CODE查询自定义预入职表", notes = "hkt")
    public ResponseResult<List < CustomTableVO >> selectCustomTableForPre() {
        //companyId, funcCode
        Boolean b = checkParam(getUserSession ());
        if (b) {
            try {
                CustomTableVO customTableVO=new CustomTableVO ();
                customTableVO.setCompanyId ( getUserSession ().getCompanyId () );
                customTableVO.setFuncCode ( "PRE" );
                List < CustomTableVO > customTableVOS = customTableFieldService.searchCustomTableListByCompanyIdAndFuncCode ( customTableVO );
                return new ResponseResult <> ( customTableVOS, CommonCode.SUCCESS );
            } catch (Exception e) {
                return new ResponseResult <> ( null,CommonCode.BUSINESS_EXCEPTION );
            }
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }
    /**
     * 根据企业ID和物理表名查询自定义组字段
     */
    @RequestMapping(value = "/searchCustomTableGroupFieldListByTableCodeArc", method = RequestMethod.POST)
    @ApiOperation(value = "根据企业ID和物理表名查询自定义档案组字段", notes = "hkt")
    public ResponseResult< CustomTableVO > searchCustomTableGroupFieldListByTableCodeArc() {
        Boolean b = checkParam(getUserSession ());
        if (b) {
            try {
                String tableCode="t_user_archive";
                CustomTableVO customTableVO = customTableFieldService.searchCustomTableGroupFieldListByTableCode ( getUserSession (), tableCode );
                return new ResponseResult<>  ( customTableVO, CommonCode.SUCCESS );
            } catch (Exception e) {
                return new ResponseResult <> ( null,CommonCode.BUSINESS_EXCEPTION );
            }
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }
    /**
     * 根据企业ID和物理表名查询自定义组字段
     */
    @RequestMapping(value = "/searchCustomTableGroupFieldListByTableCodePre", method = RequestMethod.POST)
    @ApiOperation(value = "根据企业ID和物理表名查询自定义预入职组字段", notes = "hkt")
    public ResponseResult< CustomTableVO > searchCustomTableGroupFieldListByTableCode() {
        Boolean b = checkParam(getUserSession ());
        if (b) {
            try {
                String tableCode="t_pre_employment";
                CustomTableVO customTableVO = customTableFieldService.searchCustomTableGroupFieldListByTableCode ( getUserSession (), tableCode );
                return new ResponseResult<>  ( customTableVO, CommonCode.SUCCESS );
            } catch (Exception e) {
                return new ResponseResult <> ( null,CommonCode.BUSINESS_EXCEPTION );
            }
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }
    /**
     * 根据表ID查询自定义组字段
     */
    @RequestMapping(value = "/searchCustomTableGroupFieldListByTableId", method = RequestMethod.GET)
    @ApiOperation(value = "根据表ID查询自定义组字段", notes = "hkt")
    public ResponseResult< CustomTableVO > searchCustomTableGroupFieldListByTableId( Integer tableId) {
        Boolean b = checkParam(tableId,getUserSession ());
        if (b) {
            try {
                CustomTableVO customTableVO = customTableFieldService.searchCustomTableGroupFieldListByTableId ( tableId ,getUserSession ());
                return new ResponseResult<>  ( customTableVO, CommonCode.SUCCESS );
            } catch (Exception e) {
                return new ResponseResult <> ( null,CommonCode.BUSINESS_EXCEPTION );
            }
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }
    /**
     * 根据企业ID和功能CODE查询字段集合
     */
//    @RequestMapping(value = "/selectFieldByFunc", method = RequestMethod.POST)
//    @ApiOperation(value = "根据企业ID和功能CODE查询字段集合", notes = "hkt")
    public ResponseResult< List< CustomFieldVO >> selectFieldByFunc( String funcCode) {
        Boolean b = checkParam(getUserSession (),funcCode);
        if (b) {
            try {
                List < CustomFieldVO > customFieldVOS = customTableFieldService.searchCustomFieldListByCompanyIdAndFuncCode ( getUserSession ().getCompanyId (), funcCode.toUpperCase () );
                return new ResponseResult<>  ( customFieldVOS, CommonCode.SUCCESS );
            } catch (Exception e) {
                return new ResponseResult <> ( null,CommonCode.BUSINESS_EXCEPTION );
            }
        }
        return new ResponseResult <> ( null,CommonCode.INVALID_PARAM );
    }





    /**
     * 新增自定义组
     */
//    @RequestMapping(value = "/insertCustomArchiveGroup", method = RequestMethod.POST)
//    @ApiOperation(value = "新增自定义组", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult insertCustomGroup(@RequestBody @Valid CustomArchiveGroup customArchiveGroup) {
        Boolean b = checkParam(customArchiveGroup);
        if (b) {
            try {
                staffCommonService.insertCustomArchiveGroup(customArchiveGroup, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义组失败");
            }
        }
        return failResponseResult("自定义组参数错误或session错误");
    }

    /**
     * 删除自定义组
     */
//    @RequestMapping(value = "/deleteCustomArchiveGroup", method = RequestMethod.POST)
//    @ApiOperation(value = "删除自定义组", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "自定义组id组成的集合", paramType = "form", allowMultiple = true,example = "{1,2}")
    public ResponseResult deleteCustomArchiveGroup(@RequestBody List<Integer> list) {
        Boolean b = checkParam(list);
        if (b) {
            try {
                staffCommonService.deleteCustomArchiveGroup(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
    }

    /**
     * 自定义组修改
     */
//    @RequestMapping(value = "/updateCustomArchiveGroup", method = RequestMethod.POST)
//    @ApiOperation(value = "修改自定义组", notes = "hkt")
//    @ApiImplicitParam(name = "CustomArchiveGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult updateCustomGroup(@RequestBody @Valid CustomArchiveGroup customArchiveGroup) {
        Boolean b = checkParam(customArchiveGroup);
        if (b) {
            try {
                staffCommonService.updateCustomArchiveGroup(customArchiveGroup);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改自定义组失败");
            }
        }
        return failResponseResult("自定义组参数错误");

    }

    /**
     * 新增自定义字段
     */

//    @RequestMapping(value = "/insertCustomArchiveField", method = RequestMethod.POST)
//    @ApiOperation(value = "新增自定义字段", notes = "hkt")
//    @ApiImplicitParam(name = "CustomArchiveField", value = "自定义字段对象", paramType = "form", required = true)
    public ResponseResult insertCustomField(@RequestBody @Valid CustomArchiveField customArchiveField) {
        Boolean b = checkParam(customArchiveField);
        if (b) {
            try {
                staffCommonService.insertCustomArchiveField(customArchiveField, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义字段失败");
            }
        }
        return failResponseResult("自定义组参数错误");
    }

    /**
     * 删除自定义字段
     */
     //TODO 自定义字段表有更新，curd需要重新编写
//    @RequestMapping(value = "/deleteCustomArchiveField", method = RequestMethod.POST)
//    @ApiOperation(value = "删除自定义字段", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "自定义字段id", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult deleteCustomArchiveField(@RequestBody List<Integer> list) {
        Boolean b = checkParam(list);
        if (b) {
            try {
                staffCommonService.deleteCustomArchiveField(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
    }

    /**
     * 修改自定义字段类型
     */

//    @RequestMapping(value = "/updateCustomArchiveField", method = RequestMethod.POST)
//    @ApiOperation(value = "修改自定义字段", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveField", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveField(@RequestBody @Valid CustomArchiveField customArchiveField) {
        Boolean b = checkParam(customArchiveField);
        if (b) {
            try {
                staffCommonService.updateCustomArchiveField(customArchiveField);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改自定义字段失败");
            }
        }
        return failResponseResult("自定义字段参数错误");
    }



    /**
     * 通过自定义字段id找到对应的自定义字段信息
     */
//    @RequestMapping(value = "/selectCustomArchiveFieldById", method = RequestMethod.POST)
//    @ApiOperation(value = "通过自定义字段id找到对应的自定义字段信息", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveFieldId", value = "自定义字段id", paramType = "query", required = true)
    public ResponseResult<CustomArchiveField> selectCustomArchiveFieldById(Integer customArchiveFieldId) {

        Boolean b = checkParam(customArchiveFieldId);
        if (b) {
            try {
                CustomArchiveField customArchiveField = staffCommonService.selectCustomArchiveFieldById(customArchiveFieldId);
                if (customArchiveField != null) {

                    return new ResponseResult<>(customArchiveField, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 通过字段id找到对应的字段值(代码型)
     */
//    @RequestMapping(value = "/selectFieldValueById", method = RequestMethod.POST)
//    @ApiOperation(value = "通过字段id找到对应的字段值", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveFieldId", value = "自定义字段id", paramType = "query", required = true)
    public ResponseResult<List<String>> selectFieldValueById(Integer customArchiveFieldId) {

        Boolean b = checkParam(customArchiveFieldId);
        if (b) {
            try {
                List<String> strings = staffCommonService.selectFieldValueById(customArchiveFieldId);
                if (CollectionUtils.isEmpty(strings)) {
                    return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
                }
                return new ResponseResult<>(strings, CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 将自定义字段信息存储到自定义表中
     * 这里还是需要参照数据库中自定义表数据里的字段内容
     */
//    @RequestMapping(value = "/insertCustomArchiveTableData", method = RequestMethod.POST)
//    @ApiOperation(value = "将自定义字段信息存储到自定义表中,需要将自定义表中的值封装成jsonObject形式传到后端，然后传给后台拼接", notes = "hkt")
//    @ApiImplicitParam(name = "JsonObject", value = "用户所填写的信息与操作人信息,处理之后存入自定义表数据", paramType = "form", required = true)

    /**
     * 将传过来的字段id与值进行入库操作
     */
    @RequestMapping(value = "/SaveFieldAndValue", method = RequestMethod.POST)
    @ApiOperation(value = "将传过来的字段id与值进行入库操作", notes = "hkt")

    public ResponseResult saveFieldAndValue(@RequestBody InsertDataVo insertDataVo) {
        Boolean b = checkParam(insertDataVo,getUserSession ());
        if(b) {
            try {
                staffCommonService.saveFieldAndValue (getUserSession (),insertDataVo );
                return new ResponseResult <> (null, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }


    /**
     * 在进行修改操作时，根据businessId与对应的表id找到值，进行回显
     */
    @RequestMapping(value = "/selectValue", method = RequestMethod.GET)
    @ApiOperation(value = "在进行修改操作时，根据businessId与对应的表id找到值，进行回显", notes = "hkt")

    public ResponseResult<List<CustomTableVO>> selectValue(Integer tableId,Integer businessId) {
        Boolean b = checkParam(tableId,businessId);
        if(b) {
            try {
                List<CustomTableVO> list=new ArrayList <> (  );
                CustomTableVO customTableVO = customTableFieldService.searchCustomTableGroupFieldListByTableId (tableId,getUserSession ());
                List<Map<Integer,String>>  mapList=staffCommonService.selectValue (tableId, businessId );
                for (Map < Integer, String > map : mapList) {
                    list.add (customTableFieldService.handlerCustomTableGroupFieldList ( customTableVO, map ));
                }
                return new ResponseResult <> (list, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult <> (null, CommonCode. REDIS_KEY_EXCEPTION);
            }
        }
            return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 删除预入职(根据业务id与funccode进行逻辑删除)
     */
    @RequestMapping(value = "/deletePreValue", method = RequestMethod.GET)
    @ApiOperation(value = "删除预入职(根据业务id与funccode进行逻辑删除)", notes = "hkt")

    public ResponseResult deletePreValue(Integer id) {
        Boolean b = checkParam(id,getUserSession ());
        if(b) {
            try {
                staffCommonService.deletePreValue (id ,getUserSession ());
                return new ResponseResult <> (null, CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult <> (null, CommonCode. BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }
    /**
     * 删除档案(根据业务id与funccode进行逻辑删除)
     */
    @RequestMapping(value = "/deleteArcValue", method = RequestMethod.GET)
    @ApiOperation(value = "删除档案(根据业务id与funccode进行逻辑删除)", notes = "hkt")

    public ResponseResult deleteArcValue(Integer businessId) {
        Boolean b = checkParam(businessId,getUserSession ());
        if(b) {
            try {
                staffCommonService.deleteArcValue (businessId,getUserSession ());
                return new ResponseResult <> (null, CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult <> (null, CommonCode. BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult <> (null, CommonCode.INVALID_PARAM);
    }

    /**
     * 新增或者修改自定义字段表中的数据
     */
    @RequestMapping(value = "/saveCustomArchiveTableData", method = RequestMethod.POST)
    @ApiOperation(value = "新增或者修改自定义字段表中的数据", notes = "hkt")
//    @ApiImplicitParam(name = "CustomArchiveTableData", value = "自定义表数据信息", paramType = "form", required = true)
    public ResponseResult saveCustomArchiveTableData(@RequestBody @Valid CustomArchiveTableDataVo customArchiveTableDataVo) {
        Boolean b = checkParam(customArchiveTableDataVo,getUserSession ());
        if (b) {
            try {
                staffCommonService.updateCustomArchiveTableData(customArchiveTableDataVo,getUserSession ());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改自定义数据失败");
            }
        }
        return failResponseResult("自定义数据参数错误");
    }
    /**
     * 删除自定义字段表中的数据
     */
    @RequestMapping(value = "/ deleteCustomArchiveTableData", method = RequestMethod.POST)
    @ApiOperation(value = "删除自定义字段表中的数据", notes = "hkt")
//    @ApiImplicitParam(name = "CustomArchiveTableData", value = "自定义表数据信息", paramType = "form", required = true)
    public ResponseResult deleteCustomArchiveTableData(@RequestParam List<Integer> list) {
        Boolean b = checkParam(list);
        if (b) {
            try {
                staffCommonService.deleteCustomArchiveTableData(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("删除自定义数据失败");
            }
        }
        return failResponseResult("自定义数据参数错误");
    }

    /**
     * 展示自定义表内容
     */
//    @RequestMapping(value = "/selectCustomArchiveTableData", method = RequestMethod.GET)
//    @ApiOperation(value = "展示自定义表数据内容,返回自定义表数据", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
//            @ApiImplicitParam(name = "CustomArchiveTableId", value = "自定义表id", paramType = "query", required = true)
//    })
    public ResponseResult<PageResult<CustomArchiveTableData>> selectCustomTableData(Integer currentPage, Integer pageSize,
                                                                                    Integer customArchiveTableId) {
        Boolean b = checkParam(currentPage, pageSize, customArchiveTableId);
        if (b) {
            try {
                PageResult<CustomArchiveTableData> pageResult =
                        staffCommonService.selectCustomArchiveTableData(currentPage, pageSize, customArchiveTableId);
                if (!CollectionUtils.isEmpty(pageResult.getList())) {
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(pageResult, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }
    /**
     * 根据档案显示对应权限下的单位
     */
    @RequestMapping(value = "/getCompany", method = RequestMethod.GET)
    @ApiOperation(value = "根据档案显示对应权限下的单位", notes = "hkt")
    public ResponseResult<Integer> getCompanyId() {
        Boolean b = checkParam(getUserSession());
        if (b) {
            try {
                Integer companyId= staffCommonService.getCompanyId ( getUserSession());
                return new ResponseResult(companyId, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 根据档案id显示对应权限下的子集部门
     */
    @RequestMapping(value = "/getOrgIdByCompanyId", method = RequestMethod.GET)
    @ApiOperation(value = "根据档案id显示对应权限下的子集部门与岗位", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true)
    public ResponseResult< List < OrganzitionVo > > getOrgIdByCompanyId(Integer companyId) {
        Boolean b = checkParam(companyId,getUserSession ());
        if (b) {
            try {
                List < OrganzitionVo > orgIdByCompanyId = staffCommonService.getOrgIdByCompanyId ( companyId, getUserSession () );
                if (orgIdByCompanyId!=null) {
                    return new ResponseResult<>(orgIdByCompanyId, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 显示部门下的岗位
     */
    @RequestMapping(value = "/getPostByOrgId", method = RequestMethod.GET)
    @ApiOperation(value = "显示部门下的岗位", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true)
    public ResponseResult<String> getPostByOrgId(Integer orgId) {
        Boolean b = checkParam(orgId,getUserSession ());
        if (b) {
            try {
                String postByOrgId = staffCommonService.getPostByOrgId ( orgId,getUserSession () );
                if (!StringUtils.isEmpty(postByOrgId)) {
                    return new ResponseResult<>(postByOrgId, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    /**
     * 根据id显示单位名称，部门名称，岗位名称
     * @param
     * @return
     */
    @RequestMapping(value = "/getNameForOrganzition", method = RequestMethod.GET)
    @ApiOperation(value = "获得部门名称，单位名称以及岗位名称", notes = "hkt")
    public  ResponseResult<Map<String,String>> getNameForOrganzition(Integer orgId,Integer postId){
        Boolean b = checkParam(orgId,postId,getUserSession ());
        if (b) {
            try {
                Map<String,String> map=staffCommonService.getNameForOrganzition ( orgId,getUserSession (),postId );
                if (!StringUtils.isEmpty(map)) {
                    return new ResponseResult<>(map, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 根据orgId获得org对象
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/getOrgById", method = RequestMethod.GET)
    @ApiOperation(value = "根据orgId获得org对象", notes = "hkt")
    public  ResponseResult getNameForOrganzition(Integer orgId){
        Boolean b = checkParam(orgId,getUserSession ());
        if (b) {
            try {
                OrganizationVO orgById = staffCommonService.getOrgById ( orgId, getUserSession () );
                return new ResponseResult<>(orgById, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
    /**
     * 根据PostId获得Post对象
     * @param postId
     * @return
     */
    @RequestMapping(value = "/getPostById", method = RequestMethod.GET)
    @ApiOperation(value = "根据PostId获得Post对象", notes = "hkt")
    public  ResponseResult getPostById(Integer postId){
        Boolean b = checkParam(postId,getUserSession ());
        if (b) {
            try {
                Post postById = staffCommonService.getPostById ( postId, getUserSession () );
                return new ResponseResult<>(postById, CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
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
