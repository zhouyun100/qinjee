package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description 机构controller
 * @createTime 2019年09月10日 10:19:00
 */
@RequestMapping("/organization")
@RestController
@Api(tags = "【机构管理】机构接口")
public class OrganizationController extends BaseController {
    private static Logger logger = LogManager.getLogger(OrganizationController.class);
    @Autowired
    private OrganizationService organizationService;

    //TODO 新增子机构时需要维护 子机构与角色之间的联系
    //TODO 新增没有父机构的机构时  机构编码递增
    @GetMapping("/addOrganization")
    @ApiOperation(value = "新增机构", notes = "高雄")
    public ResponseResult addOrganization(@RequestParam("orgName") String orgName,@RequestParam("orgType")String orgType,@RequestParam("orgParentId")String orgParentId,@RequestParam("orgManagerId")String orgManagerId) {
            return organizationService.addOrganization(orgName,orgType,orgParentId,orgManagerId,getUserSession());
    }

    @GetMapping("/editOrganization")
    @ApiOperation(value = "编辑机构", notes = "高雄")
    public ResponseResult editOrganization(@RequestParam("orgId")String orgId,@RequestParam("orgName")String orgName,@RequestParam("orgType")String orgType,@RequestParam("orgParentId")String orgParentId,@RequestParam("orgManagerId")String orgManagerId) {

            return organizationService.editOrganization(orgId,orgName,orgType,orgParentId,orgManagerId,getUserSession());

    }

    //TODO 删除机构时，需要回收权限（调用权限接口）
    @PostMapping("/deleteOrganizationById")
    @ApiOperation(value = "删除机构", notes = "高雄")
    public ResponseResult deleteOrganizationById(@ApiParam(value = "机构id列表") @RequestBody List<Integer> orgIds) {
        return organizationService.deleteOrganizationById(orgIds, getUserSession());
    }

    /**
     * @Description: 查询用户下所有机构及子机构
     * @Param:
     * @return:
     * @Author: penghs
     * @Date: 2019/11/20 0020
     */
    @ApiOperation(value = "查询用户下所有的机构及子机构,树形展示", notes = "彭洪思")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isEnable", value = "是否包含封存：0 封存、1 解封（默认）", paramType = "query", dataType = "short")
    })
    @GetMapping("/getAllOrganizationTree")
    public ResponseResult<PageResult<OrganizationVO>> getAllOrganizationTree(@RequestParam(value = "isEnable", required = false) Short isEnable) {
        UserSession userSession = getUserSession();
        List<OrganizationVO> organizationVOList = organizationService.getAllOrganizationTree(userSession, isEnable);
        PageResult<OrganizationVO> pageResult = new PageResult<>(organizationVOList);
        return new ResponseResult<>(pageResult);
    }

    @PostMapping("/getOrganizationPageList")
    @ApiOperation(value = "分页按条件查询用户下所有的机构", notes = "高雄")
    public ResponseResult<PageResult<OrganizationVO>> getOrganizationPageList(@RequestBody OrganizationPageVo organizationPageVo) {
        UserSession userSession = getUserSession();
        PageResult<OrganizationVO> pageResult = organizationService.getOrganizationList(organizationPageVo, userSession);
        return new ResponseResult<>(pageResult);
    }

    /**
     * 默认显示所有层级，显示多少级由前端控制，后端只要返回全部结果即可
     * 图数据结构需要含有机构负责人照片、姓名、机构名称、实有人数和编制人数
     *
     * @return
     */
    //TODO 实有人数、编制人数暂时不考虑
    public ResponseResult getOrganizationGraphics() {
        return null;
    }

    //TODO
    @ApiOperation(value = "岗位维护机构岗位树状图展示", notes = "高雄")
    @PostMapping("/getOrganizationPositionTree")
    public ResponseResult<List<OrganizationVO>> getOrganizationPositionTree(@ApiParam(value = "是否含有封存 0不含有、1含有", example = "0") Short isEnable) {
        return organizationService.getOrganizationPositionTree(getUserSession(), isEnable);
    }

    @PostMapping("/lockOrganizationByIds")
    @ApiOperation(value = "封存机构", notes = "高雄")
    public ResponseResult lockOrganizationByIds(@RequestBody List<Integer> orgIds) {
        return organizationService.sealOrganizationByIds(orgIds, Short.parseShort("0"));
    }

    @PostMapping("/unlockOrganizationByIds")
    @ApiOperation(value = "解封机构", notes = "高雄")
    public ResponseResult unlockOrganizationByIds(@RequestBody List<Integer> orgIds) {
        return organizationService.sealOrganizationByIds(orgIds, Short.parseShort("1"));
    }



    @ApiOperation(value = "导出机构到excel，如果不选中的话导入用户下所有机构", notes = "彭洪思")
    @PostMapping("/downloadOrganizationToExcelByOrgId")
    public ResponseResult downloadOrganizationToExcelByOrgId(@ApiParam(value = "导出路径", required = true) @RequestParam("filePath") String filePath, @RequestBody List<Integer> orgIds) {

        if (CollectionUtils.isEmpty(orgIds)) {
            return organizationService.downloadAllOrganizationToExcel(filePath, getUserSession());
        }
        return organizationService.downloadOrganizationToExcelByOrgId(filePath, orgIds, getUserSession());
    }

    /**
     * @Description: AorgId:2                                        AorgId：1
     * BorgId:1    传参  在后端进行排序id更新-----》   BorgId：2
     * CorgId:3                                        CorgId：3
     * @Author: penghs
     * @Date: 2019/11/20 0020
     */
    @PostMapping("/sortOrganization")
    @ApiOperation(value = "机构排序，只能同一级别下机构排序（需要将该级下所有机构的id按顺序传参）", notes = "彭洪思")
    public ResponseResult sortOrganizationInOrg( @RequestBody LinkedList<Integer> orgIds) {
        ResponseResult responseResult = organizationService.sortOrganization(orgIds);
        return responseResult;
    }

    //TODO
    @GetMapping("/getUserArchiveListByUserName")
    @ApiOperation(value = "机构负责人查询，如果带负责人姓名，则根据姓名模糊查询，不带参则全量查询")
    public ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(@ApiParam(value = "姓名", example = "张三", required = true)@RequestParam(value = "userName",required = false) String userName) {
        return organizationService.getUserArchiveListByUserName(userName);
    }

    //TODO
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgIds", value = "需要合并机构id", paramType = "query", dataType = "int", allowMultiple = true, required = true),
            @ApiImplicitParam(name = "targetOrgId", value = "目标机构Id", paramType = "query", dataType = "int", required = true, example = "1"),
    })
    @GetMapping("/transferOrganization")
    @ApiOperation(value = "划转机构", notes = "高雄")
    /**
     * 将勾选的机构（至少一个）划转到目标机构下，成为目标机构的子机构
     * 机构id不变
     * 机构编码改变
     */
    public ResponseResult transferOrganization(@RequestParam("orgIds") List<Integer> orgIds,
                                               @RequestParam("targetOrgId") Integer targetOrgId) {
        return organizationService.transferOrganization(orgIds, targetOrgId, getUserSession());
    }

    //TODO
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newOrgName", value = "新机构名称", paramType = "query", dataType = "String", required = true, example = "新机构名称"),
            @ApiImplicitParam(name = "targetOrgId", value = "归属机构Id", paramType = "query", dataType = "int", required = true, example = "01"),
            @ApiImplicitParam(name = "orgIds", value = "待合并机构Id", paramType = "query", dataType = "int", allowMultiple = true, required = true),
    })
    @GetMapping("/mergeOrganization")
    @ApiOperation(value = "合并机构", notes = "高雄")
    public ResponseResult mergeOrganization(@RequestParam("newOrgName") String newOrgName,
                                            @RequestParam("targetOrgId") Integer targetOrgId,
                                            @RequestParam("orgIds") List<Integer> orgIds) {
        UserSession userSession = getUserSession();
        return organizationService.mergeOrganization(newOrgName, targetOrgId, orgIds, userSession);
    }


    //TODO
    @ApiOperation(value = "导入机构Excel", notes = "高雄")
    @PostMapping("/uploadExcel")
    public ResponseResult uploadExcelInOrg(@ApiParam(value = "需要导入的Excel文件", required = true) MultipartFile file) {
        return organizationService.uploadExcel(file, getUserSession());
    }


    //TODO
    @ApiOperation(value = "下载模板", notes = "高雄")
    @PostMapping("/downloadTemplate")
    public ResponseResult downloadTemplateInOrg(HttpServletResponse response) {
        return organizationService.downloadTemplate(response);
    }
}
