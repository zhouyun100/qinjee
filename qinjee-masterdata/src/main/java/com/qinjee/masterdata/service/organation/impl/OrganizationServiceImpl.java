package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVo;
import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.UserRoleService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月16日 09:12:00
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private OrganizationHistoryService organizationHistoryService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserArchiveDao userArchiveDao;

    @Override
    public PageResult<Organization> getOrganizationTree(UserSession userSession, Short isEnable) {
        Integer archiveId = userSession.getArchiveId();
        List<UserRole> userRoleList =  userRoleService.getUserRoleList(userSession.getArchiveId());
        Set<Integer> roleIds = userRoleList.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toSet());

        List<Organization> organizationList = organizationDao.getAllOrganization(archiveId, isEnable, roleIds, new Date());

        //获取第一级机构
        List<Organization> organizations = getFirstOrganizationList(organizationList);

        //递归处理机构,使其以树形结构展示
        handlerOrganizationToTree(organizationList, organizations);

        return new PageResult<>(organizationList);
    }

    /**
     * 获取第一级机构
     * @param organizationList
     * @return
     */
    private List<Organization> getFirstOrganizationList(List<Organization> organizationList) {
        List<Organization> organizations = Collections.singletonList(organizationList.get(0));
        organizationList.removeAll(organizations);
        return organizations;
    }

    @Override
    public PageResult<Organization> getOrganizationList(OrganizationPageVo organizationPageVo, UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        Optional<List<QueryFieldVo>> querFieldVos = Optional.of(organizationPageVo.getQuerFieldVos());
        String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Organization.class);
        if(organizationPageVo.getCurrentPage() != null && organizationPageVo.getPageSize() != null){
            PageHelper.startPage(organizationPageVo.getCurrentPage(),organizationPageVo.getPageSize());
        }
        List<Organization> organizationList = organizationDao.getOrganizationList(organizationPageVo,sortFieldStr,archiveId, new Date());
        PageResult<Organization> pageResult = new PageResult<>(organizationList);
        return pageResult;
    }

    @Override
    public PageResult<Organization> getOrganizationGraphics(UserSession userSession, Short isEnable, Integer orgId) {
        Integer archiveId = userSession.getArchiveId();
        Organization organization = organizationDao.selectByPrimaryKey(orgId);
        String orgCode = organization.getOrgCode() + "%";
        List<Organization> organizationList = organizationDao.getOrganizationGraphics(archiveId, isEnable, orgCode, new Date());
        PageResult<Organization> pageResult = new PageResult<>(organizationList);
        return pageResult;
    }

    @Transactional
    @Override
    public ResponseResult addOrganization(UserSession userSession, OrganizationVo organizationVo) {
        Organization organization = getNewOrgCode(organizationVo.getOrgParentId());
        String full_name = organization.getOrgFullName() + "/" + organizationVo.getOrgName();
        BeanUtils.copyProperties(organizationVo, organization);
        organization.setOrgFullName(full_name);
        organization.setOperatorId(userSession.getArchiveId());
        organization.setIsEnable((short) 1);
        int insert = organizationDao.insertSelective(organization);
        return insert == 1 ? new ResponseResult(CommonCode.SUCCESS) :  new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 获取新编码和sortId的机构
     * @param orgParentId
     * @return
     */
    private Organization getNewOrgCode(Integer orgParentId) {
        Organization parentOrganization = organizationDao.selectByPrimaryKey(orgParentId);
        List<Organization> organizationList = organizationDao.getOrganizationListByParentOrgId(orgParentId);
        String parentOrgCode = parentOrganization.getOrgCode();
        String newOrgCode;
        Integer sortId;
        if(CollectionUtils.isEmpty(organizationList)){
            //新增第一个子级
            newOrgCode = parentOrgCode + "001";
            sortId = 1000;
        }else {
            //有子级
            Organization Lastorganization = organizationList.get(0);
            String orgCode = Lastorganization.getOrgCode();
            newOrgCode = getNewCode(orgCode);
            sortId = Lastorganization.getSortId() + 1000;
        }

        Organization organization = new Organization();
        organization.setSortId(sortId);
        organization.setOrgCode(newOrgCode);
        organization.setOrgFullName(parentOrganization.getOrgFullName());
        return organization;
    }

    /**
     * 获取新orgCode
     * @param orgCode
     * @return
     */
    private String getNewCode(String orgCode) {
        String number = orgCode.substring(orgCode.length() - 3);
        String preCode = orgCode.substring(0, orgCode.length() - 3);
        Integer new_OrgCode = Integer.parseInt(number) + 1;
        String code = new_OrgCode.toString();
        int i = 3 - code.length();
        if(i < 0){
            ExceptionCast.cast(CommonCode.ORGANIZATION_OUT_OF_RANGE);
        }
        for (int k = 0; k < i; k ++){
            code = "0" + code;
        }
        String newOrgCode = preCode + code;
        return newOrgCode;
    }

    @Transactional
    @Override
    public ResponseResult editOrganization(OrganizationVo organizationVo) {
        //通过机构id新增一条机构历史表
        Organization organization = addOrganizationHistoryByOrgId(organizationVo.getOrgId());
        BeanUtils.copyProperties(organizationVo, organization);
        Organization parentOrganization = organizationDao.selectByPrimaryKey(organization.getOrgParentId());
        String orgfull_name = parentOrganization.getOrgFullName();
        organization.setOrgFullName(orgfull_name + "/" + organization.getOrgName());
        int i = organizationDao.updateByPrimaryKeySelective(organization);

        //修改子级的机构全名称
        updateOrganizationfull_name(organization);

        return i == 1 ? new ResponseResult() :  new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 递归修改子级机构全名称
     * @param organization
     */
    private void updateOrganizationfull_name(Organization organization) {
        List<Organization> organizationList = organizationDao.getOrganizationListByParentOrgId(organization.getOrgId());
        if(!CollectionUtils.isEmpty(organizationList)){
            for (Organization organizati : organizationList) {
                OrganizationHistory organizationHistory = new OrganizationHistory();
                BeanUtils.copyProperties(organizati, organizationHistory);
                //新增机构历史信息
                organizationHistoryService.addOrganizationHistory(organizationHistory);
                organizati.setOrgFullName(organization.getOrgFullName() + "/" + organization.getOrgName());
                organizationDao.updateByPrimaryKeySelective(organizati);
                updateOrganizationfull_name(organizati);
            }
        }
    }

    /**
     * 通过机构id新增一条机构历史表
     * @param orgId
     * @return
     */
    private Organization addOrganizationHistoryByOrgId(Integer orgId) {
        Organization organization = organizationDao.selectByPrimaryKey(orgId);
        OrganizationHistory organizationHistory = new OrganizationHistory();
        BeanUtils.copyProperties(organization, organizationHistory);
        //新增机构历史信息
        organizationHistoryService.addOrganizationHistory(organizationHistory);
        return organization;
    }

    @Transactional
    @Override
    public ResponseResult deleteOrganizationById(List<Integer> orgIds) {
        if(!CollectionUtils.isEmpty(orgIds)){
            for (Integer orgId : orgIds) {
                //若被删除的机构在人事异动表、工资、考勤等相关数据表存在记录，也不允许删除

                //被删除的机构下若存在人员档案，提示“该机构下存在人员信息，不允许删除
                List<UserArchive> userArchiveList = userArchiveDao.getUserArchiveListByOrgId(orgId);
                if(!CollectionUtils.isEmpty(userArchiveList)){
                    return new ResponseResult(CommonCode.ORG_EXIST_USER);
                }
                addOrganizationHistoryByOrgId(orgId);
                organizationDao.deleteByPrimaryKey(orgId);
            }
        }
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult sealOrganizationByIds(List<Integer> orgIds, Short isEnable) {
        if(!CollectionUtils.isEmpty(orgIds)){
            organizationDao.UpdateIsEnableByOrgIds(orgIds, isEnable);
        }
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult mergeOrganization(String newOrgName, Integer targetOrgId, String orgType, List<Integer> orgIds, UserSession userSession) {
        List<Organization> organizationList = null;
        if(!CollectionUtils.isEmpty(orgIds)){
            organizationList = organizationDao.getOrganizationListByOrgIds(orgIds);
        }

        //判断是否是同一个父级下的
        if(!CollectionUtils.isEmpty(organizationList)){
            Set<Integer> OrgParentIds = organizationList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if(OrgParentIds.size() != 1){
                //不是
                return new ResponseResult(CommonCode.FAIL);
            }

            //获取新的合并机构
            Organization newOrganization = getNewOrganization(newOrgName, targetOrgId, orgType, userSession);
            organizationDao.insertSelective(newOrganization);

            updateOrganizationfull_nameAndOrgCode(organizationList, newOrganization);
        }
        return new ResponseResult();
    }

    /**
     * 获取新的合并机构
     * @param newOrgName
     * @param targetOrgId
     * @param orgType
     * @param userSession
     * @return
     */
    private Organization getNewOrganization(String newOrgName, Integer targetOrgId, String orgType, UserSession userSession) {
        Organization newOrganization = new Organization();
        newOrganization.setOrgParentId(targetOrgId);
        newOrganization.setOrgName(newOrgName);
        Organization newOrgCode = getNewOrgCode(targetOrgId);

        String orgfull_name = newOrgCode.getOrgFullName();
        BeanUtils.copyProperties(newOrgCode, newOrganization);
        newOrganization.setIsEnable((short) 1);
        newOrganization.setCompanyId(userSession.getCompanyId());
        newOrganization.setOrgType(orgType);
        newOrganization.setOrgFullName(orgfull_name + "/" + newOrganization.getOrgName());
        return newOrganization;
    }

    @Override
    public ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(String userName) {
         //TODO 调用人员的接口
        return null;
    }

    @Override
    public ResponseResult sortOrganization(Integer preOrgId, Integer midOrgId, Integer nextOrgId) {
        Organization preOrganization;
        Organization nextOrganization;
        Integer midSort = null;
        if(nextOrgId != null){
            //移动
            nextOrganization = organizationDao.selectByPrimaryKey(nextOrgId);
            midSort = nextOrganization.getSortId() - 1;

        }else if(nextOrgId == null){
            //移动到最后
            preOrganization = organizationDao.selectByPrimaryKey(preOrgId);
            midSort = preOrganization.getSortId() + 1;
        }
        Organization organization = new Organization();
        organization.setOrgId(midOrgId);
        organization.setSortId(midSort);
        organizationDao.insertSelective(organization);
        return new ResponseResult();
    }

    @Override
    public ResponseResult transferOrganization(List<Integer> orgIds, Integer targetOrgId) {

        List<Organization> organizationList = null;
        if(!CollectionUtils.isEmpty(orgIds)){
            organizationList = organizationDao.getOrganizationListByOrgIds(orgIds);
        }

        //判断是否是同一个父级下的
        if(!CollectionUtils.isEmpty(organizationList)){
            Set<Integer> OrgParentIds = organizationList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if(OrgParentIds.size() != 1){
                //不是
                return new ResponseResult(CommonCode.FAIL);
            }
            Organization parentOrganization = organizationDao.selectByPrimaryKey(targetOrgId);

            updateOrganizationfull_nameAndOrgCode(organizationList, parentOrganization);
        }

        return new ResponseResult();
    }

    @Override
    public ResponseResult<List<Organization>> getOrganizationPositionTree(UserSession userSession, Short isEnable) {
        PageResult<Organization> organizationTree = getOrganizationTree(userSession, isEnable);
        List<Organization> organizationTreeList = organizationTree.getList();
        if (!CollectionUtils.isEmpty(organizationTreeList)){
            for (Organization organization : organizationTreeList) {
                List<Post> postList = postDao.getPostListByOrgId(organization.getOrgId(), isEnable);
                organization.setPostList(postList);
                packOrganizationPosition(organization,isEnable);
            }
        }
        return new ResponseResult<>(organizationTreeList);
    }

    @Override
    public ResponseResult downloadTemplate(HttpServletResponse response) {
        ClassPathResource cpr = new ClassPathResource("/templates/"+"机构导入模板.xls");
        try {
            File file = cpr.getFile();
            String filename = cpr.getFilename();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            FileUtils.copyFile(file,outputStream);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"");
            response.getOutputStream().write(outputStream.toByteArray());
        }catch (Exception e){
            ExceptionCast.cast(CommonCode.FILE_EXPORT_FAILED);
        }
        return new ResponseResult();
    }

    @Override
    public ResponseResult downloadExcelByCondition(OrganizationPageVo organizationPageVo, HttpServletResponse response, UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        Optional<List<QueryFieldVo>> querFieldVos = Optional.of(organizationPageVo.getQuerFieldVos());
        String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Organization.class);
        List<Organization> organizationList = organizationDao.getOrganizationList(organizationPageVo,sortFieldStr,archiveId, new Date());
        //导出Excel
        exportExcel(response, organizationList);
        return new ResponseResult();
    }

    @Override
    public ResponseResult downloadExcelByOrgCodeId(List<Integer> orgIds, HttpServletResponse response, UserSession userSession) {
//        List<Organization> organizationList = organizationDao.getOrganizationsByOrgIds(orgIds);
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new Organization();
        organization.setOrgCode("10101");
        organization.setOrgName("深圳勤杰软件有限公司");
        organization.setOrgType("GROUP");
        organization.setOrgParentCode("101");
        organization.setOrgParentName("深圳勤杰软件有限公司");
        organization.setManagerEmployeeNumber("123456");
        organization.setOrgManagerName("张三");
        organizationList.add(organization);
        //导出Excel
        exportExcel(response, organizationList);
        return new ResponseResult();
    }

    /**
     * 递归给机构树的机构设置下面的岗位
     * @param organization
     * @param isEnable
     */
    private void packOrganizationPosition(Organization organization, Short isEnable){
        List<Organization> childList = organization.getChildList();
        if (!CollectionUtils.isEmpty(childList)){
            for (Organization org : childList) {
                List<Post> postList = postDao.getPostListByOrgId(org.getOrgId(),isEnable);
                org.setPostList(postList);
                packOrganizationPosition(organization, isEnable);
            }
        }
    }

    /**
     * 修改本级的父级id和全名称及子级的全名称
     * @param organizationList
     * @param parentOrganization
     */
    private void updateOrganizationfull_nameAndOrgCode(List<Organization> organizationList, Organization parentOrganization) {
        String newOrgCode = parentOrganization.getOrgCode() + "000";
        for (Organization organization : organizationList) {
            OrganizationHistory organizationHistory = new OrganizationHistory();
            BeanUtils.copyProperties(organization, organizationHistory);
            organizationHistoryService.addOrganizationHistory(organizationHistory);
            organization.setOrgParentId(parentOrganization.getOrgId());
            organization.setOrgFullName(parentOrganization.getOrgFullName() + "/" + organization.getOrgName());
            newOrgCode = getNewCode(newOrgCode);
            organization.setOrgCode(newOrgCode);
            organizationDao.updateByPrimaryKeySelective(organization);

            List<Organization> organizations = organizationDao.getOrganizationListByParentOrgId(organization.getOrgId());
            if(!CollectionUtils.isEmpty(organizations)){
                //递归修改本级的父级id和全名称及子级的全名称
                updateOrganizationfull_nameAndOrgCode(organizations, organization);
            }

        }
    }

    /**
     * 处理所有机构以树形结构展示
     * @param organizationList
     * @param organizations
     */
    private void handlerOrganizationToTree(List<Organization> organizationList, List<Organization> organizations) {
        for (Organization org : organizations) {
            Integer orgId = org.getOrgId();
            List<Organization> childList = organizationList.stream().filter(organization -> {
                Integer orgParentId = organization.getOrgParentId();
                if (orgParentId != null && orgParentId > 0) {
                    return orgParentId == orgId;
                }
                return false;
            }).collect(Collectors.toList());
            //判断是否还有子级
            if(childList != null && childList.size() > 0){
                org.setChildList(childList);
                organizationList.removeAll(childList);
                handlerOrganizationToTree(organizationList,childList);
            }
        }
    }


    private static void exportExcel(HttpServletResponse response, List<Organization> organizationList) {
        try {
            //实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("sheet");
            //设置表头
            setTitle(workbook, sheet);
            //设置单元格并赋值
            setData(sheet, organizationList);
            //设置浏览器下载
            setBrowser(response, workbook, "机构信息.xls");
        } catch (Exception e) {
            ExceptionCast.cast(CommonCode.FILE_EXPORT_FAILED);
        }
    }

    private static void setTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位set是1/256个字符宽度
        for(int i = 0; i < 7; i++){
            sheet.setColumnWidth(i, 30 * 250);
        }
        //设置为居中加粗,格式化时间格式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);//字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//加粗
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd HH:mm:ss"));
        List<String> strList = new ArrayList<>();
        strList.add("机构编码");
        strList.add("机构名称");
        strList.add("机构类型");
        strList.add("上级机构编码");
        strList.add("上级机构");
        strList.add("部门负责人工号");
        strList.add("部门负责人");
        //创建表头名称
            HSSFCell cell;
            for (int j = 0; j < strList.size(); j++) {
                cell = row.createCell(j);
                cell.setCellValue(strList.get(j));
                cell.setCellStyle(style);
            }
    }

    /**
     * 方法名：setData
     * 功能：表格赋值
     * 描述：
     * 创建人：typ
     * 创建时间：2018/10/19 16:11
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    private static void setData(HSSFSheet sheet, List<Organization> organizationList) {
            for (int i = 0; i < organizationList.size(); i++) {
                Organization organization = organizationList.get(i);
                HSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(organization.getOrgCode());
                row.createCell(1).setCellValue(organization.getOrgName());
                String orgType = organization.getOrgType();
                String typeValue = null;
                if(StringUtils.isEmpty(orgType)){
                    typeValue = "";
                }else if("GROUP".equals(orgType.trim())){
                    typeValue = "集团";
                }else if("UNIT".equals(orgType.trim())){
                    typeValue = "单位";
                }else if("DEPT".equals(orgType.trim())){
                    typeValue = "部门";
                }
                row.createCell(2).setCellValue(typeValue);
                row.createCell(3).setCellValue(organization.getOrgParentCode());
                row.createCell(4).setCellValue(organization.getOrgParentName());
                row.createCell(5).setCellValue(organization.getOrgManagerName());
                row.createCell(6).setCellValue(organization.getManagerEmployeeNumber());
            }
    }

    /**
     * 使用浏览器下载
     * @param response
     * @param workbook
     * @param fileName
     */
    private static void setBrowser(HttpServletResponse response, HSSFWorkbook workbook, String fileName) throws Exception {
        try {
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            //清空response
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            //将excel写入到输出流中
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new Exception("文件导出失败!");
        }
    }





}
