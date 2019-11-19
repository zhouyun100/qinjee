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
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

        List<Organization> organizationList = organizationDao.getAllOrganizationByArchiveId(archiveId, isEnable, new Date());

        //获取第一级机构
        List<Organization> organizations = organizationList.stream().filter(organization -> {
            if(organization.getOrgParentId() != null && organization.getOrgParentId() == 0){
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toList());

        //递归处理机构,使其以树形结构展示
        handlerOrganizationToTree(organizationList, organizations);

        return new PageResult<>(organizationList);
    }

    /**
     * 获取第一级机构.
     *
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
        //根据用户id进行分页查询, 其他条件的使用场景是什么? 可否为空?
        //  Optional<List<QueryFieldVo>> querFieldVos = Optional.of(organizationPageVo.getQuerFieldVos());
        //    String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Organization.class);
        //  if(organizationPageVo.getCurrentPage() != null && organizationPageVo.getPageSize() != null){
        //   PageHelper.startPage(organizationPageVo.getCurrentPage(),organizationPageVo.getPageSize());
        //    }
        List<Organization> organizationList = organizationDao.getOrganizationList(organizationPageVo, null, archiveId, new Date());
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
        //根据父级机构id查询一些基础信息，构建Organization对象
        Organization orgBean = initOrganization(organizationVo.getOrgParentId());

        //Organization organization = getNewOrgCode(organizationVo.getOrgParentId());
        String full_name = "";
        if (orgBean.getOrgFullName() != null) {
            full_name = orgBean.getOrgFullName() + "/" + organizationVo.getOrgName();
        } else {
            full_name = organizationVo.getOrgName();
        }
        //BeanUtils.copyProperties(organizationVo, organization);

        orgBean.setOrgParentId(organizationVo.getOrgParentId());
        orgBean.setOrgType(organizationVo.getOrgType());
        orgBean.setOrgName(organizationVo.getOrgName());
        orgBean.setOrgManagerId(organizationVo.getOrgManagerId());
        orgBean.setOrgFullName(full_name);
        orgBean.setOperatorId(userSession.getArchiveId());
        orgBean.setIsEnable((short) 1);
        //设置企业id
        orgBean.setCompanyId(userSession.getCompanyId());
        int insert = organizationDao.insertSelective(orgBean);
        return insert == 1 ? new ResponseResult(CommonCode.SUCCESS) : new ResponseResult(CommonCode.FAIL);
    }

    private Organization initOrganization(Integer orgParentId) {
        Organization orgBean = new Organization();
        if (orgParentId > 0) {
            //查询父级机构
            Organization parentOrg = organizationDao.selectByPrimaryKey(orgParentId);
            //查询最新的同级机构
            List<Organization> brotherOrgList = organizationDao.getOrganizationListByParentOrgId(orgParentId);
            //如果没有同级机构，则当前机构为parentOrg下第一个子机构
            //计算机构编码与排序id
            /**
             * 根据规则计算机构编码
             * 规则如下：顶级机构默认编码为1
             * 余下各层级编码规则：上级机构编码+2位数流水号。
             * 如顶级机构：XX集团，编码为1，下级A单位的编码为：101
             */
            String orgCode;
            Integer sortId;
            if (CollectionUtils.isEmpty(brotherOrgList)) {
                orgCode = parentOrg.getOrgCode() + "001";
                sortId = 1000;
            } else {
                sortId = brotherOrgList.get(0).getSortId() + 1000;
                orgCode = culOrgCode(brotherOrgList.get(0).getOrgCode());
            }
            orgBean.setSortId(sortId);
            orgBean.setOrgCode(orgCode);
            orgBean.setOrgFullName(parentOrg.getOrgFullName());
            return orgBean;

        } else {
            orgBean.setSortId(1);
            orgBean.setOrgCode("001");
            return orgBean;
        }
    }

    /**
     * 获取新编码和sortId的机构
     *
     * @param orgParentId
     * @return
     */
    @Deprecated
    private Organization getNewOrgCode(Integer orgParentId) {
        Organization organization = new Organization();
        if (orgParentId != 0) {
            //查询父级机构
            Organization parentOrganization = organizationDao.selectByPrimaryKey(orgParentId);
            //查询父级机构的所有子机构
            List<Organization> organizationList = organizationDao.getOrganizationListByParentOrgId(orgParentId);
            String parentOrgCode = parentOrganization.getOrgCode();
            String newOrgCode;
            Integer sortId;
            if (CollectionUtils.isEmpty(organizationList)) {
                //新增第一个子级
                newOrgCode = parentOrgCode + "001";
                sortId = 1000;
            } else {
                //有子级，获取最新的子机构
                Organization Lastorganization = organizationList.get(0);
                String orgCode = Lastorganization.getOrgCode();
                newOrgCode = culOrgCode(orgCode);
                sortId = Lastorganization.getSortId() + 1000;
            }
            organization.setSortId(sortId);
            organization.setOrgCode(newOrgCode);
            organization.setOrgFullName(parentOrganization.getOrgFullName());
            return organization;
        } else {
            organization.setSortId(1);
            organization.setOrgCode("001");
            return organization;
        }
    }


    /**
     * 获取新orgCode
     *
     * @param orgCode
     * @return
     */
    private String culOrgCode(String orgCode) {
        String number = orgCode.substring(orgCode.length() - 3);
        String preCode = orgCode.substring(0, orgCode.length() - 3);
        Integer new_OrgCode = Integer.parseInt(number) + 1;
        String code = new_OrgCode.toString();
        int i = 3 - code.length();
        if (i < 0) {
            ExceptionCast.cast(CommonCode.ORGANIZATION_OUT_OF_RANGE);
        }
        for (int k = 0; k < i; k++) {
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
        //业务场景不清楚, 代码出现问题
       /* Organization parentOrganization = organizationDao.selectByPrimaryKey(organization.getOrgParentId());
        String orgfull_name = parentOrganization.getOrgFullName();
        organization.setOrgFullName(orgfull_name + "/" + organization.getOrgName());*/
        int i = organizationDao.updateByPrimaryKeySelective(organization);

        //修改子级的机构全名称
        updateOrganizationfull_name(organization);

        return i == 1 ? new ResponseResult() : new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 递归修改子级机构全名称
     *
     * @param organization
     */
    private void updateOrganizationfull_name(Organization organization) {
        List<Organization> organizationList = organizationDao.getOrganizationListByParentOrgId(organization.getOrgId());
        if (!CollectionUtils.isEmpty(organizationList)) {
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
     *
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
        if (!CollectionUtils.isEmpty(orgIds)) {
            for (Integer orgId : orgIds) {
                //若被删除的机构在人事异动表、工资、考勤等相关数据表存在记录，也不允许删除

                //被删除的机构下若存在人员档案，提示“该机构下存在人员信息，不允许删除
                List<UserArchive> userArchiveList = userArchiveDao.getUserArchiveListByOrgId(orgId);
                if (!CollectionUtils.isEmpty(userArchiveList)) {
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
        if (!CollectionUtils.isEmpty(orgIds)) {
            organizationDao.UpdateIsEnableByOrgIds(orgIds, isEnable);
        }
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult mergeOrganization(String newOrgName, Integer targetOrgId, String orgType, List<Integer> orgIds, UserSession userSession) {
        List<Organization> organizationList = null;
        if (!CollectionUtils.isEmpty(orgIds)) {
            organizationList = organizationDao.getOrganizationListByOrgIds(orgIds);
        }
        //判断是否是同一个父级下的
        if (!CollectionUtils.isEmpty(organizationList)) {
            Set<Integer> OrgParentIds = organizationList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (OrgParentIds.size() != 1) {
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
     *
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
        Organization newOrgCode = initOrganization(targetOrgId);
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
        if (nextOrgId != null) {
            //移动
            nextOrganization = organizationDao.selectByPrimaryKey(nextOrgId);
            midSort = nextOrganization.getSortId() - 1;

        } else if (nextOrgId == null) {
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
        if (!CollectionUtils.isEmpty(orgIds)) {
            organizationList = organizationDao.getOrganizationListByOrgIds(orgIds);
        }

        //判断是否是同一个父级下的
        if (!CollectionUtils.isEmpty(organizationList)) {
            Set<Integer> OrgParentIds = organizationList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (OrgParentIds.size() != 1) {
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
        if (!CollectionUtils.isEmpty(organizationTreeList)) {
            for (Organization organization : organizationTreeList) {
                List<Post> postList = postDao.getPostListByOrgId(organization.getOrgId(), isEnable);
                organization.setPostList(postList);
                packOrganizationPosition(organization, isEnable);
            }
        }
        return new ResponseResult<>(organizationTreeList);
    }

    @Override
    public ResponseResult downloadTemplate(HttpServletResponse response) {
        ClassPathResource cpr = new ClassPathResource("/templates/" + "机构导入模板.xls");
        try {
            File file = cpr.getFile();
            String filename = cpr.getFilename();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            FileUtils.copyFile(file, outputStream);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"");
            response.getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            ExceptionCast.cast(CommonCode.FILE_EXPORT_FAILED);
        }
        return new ResponseResult();
    }

    @Override
    public ResponseResult downloadExcelByCondition(OrganizationPageVo organizationPageVo, HttpServletResponse response, UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        Optional<List<QueryFieldVo>> querFieldVos = Optional.of(organizationPageVo.getQuerFieldVos());
        String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Organization.class);
        List<Organization> organizationList = organizationDao.getOrganizationList(organizationPageVo, sortFieldStr, archiveId, new Date());
        //导出Excel
        exportExcel(response, organizationList);
        return new ResponseResult();
    }

    @Override
    public ResponseResult downloadExcelByOrgCodeId(List<Integer> orgIds, HttpServletResponse response, UserSession userSession) {
        List<Organization> organizationList = organizationDao.getOrganizationsByOrgIds(orgIds);
        //导出Excel
        exportExcel(response, organizationList);
        return new ResponseResult();
    }

    @Override
    public ResponseResult uploadExcel(MultipartFile file, UserSession userSession) {
        //解析文件
        try {
            parseFile(file);

        } catch (Exception e) {
            String message = e.getMessage();
            ResponseResult responseResult = new ResponseResult(CommonCode.FAIL);
            responseResult.setMessage(message);
            return responseResult;
        }
        return new ResponseResult();
    }

    private List<Organization> parseFile(MultipartFile file) throws Exception {
        List<Organization> organizationList = new ArrayList<>();
        // 检查文件类型
        String fileName = checkFile(file);
        if ("1".equals(fileName) || "2".equals(fileName)) {
            ExceptionCast.cast(CommonCode.FILE_FORMAT_ERROR);
        }
        Workbook workbook = null;
        InputStream inputStream = file.getInputStream();
        workbook = WorkbookFactory.create(inputStream);
        int numberOfSheets = workbook.getNumberOfSheets();
        int orgNum = 0;
        if (numberOfSheets > 0) {
            Sheet sheet = workbook.getSheetAt(0);
            if (null != sheet) {
                // 获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                // 获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                    Boolean parentIsMust = true;
                    // 获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
//                        // 获得当前行的开始列
//                        int firstCellNum = row.getFirstCellNum();
//                        // 获得当前行的列数
//                        int lastCellNum = row.getLastCellNum();
                    Organization organization = new Organization();
                    String orgCode = getCellValue(row.getCell(0));
                    if (StringUtils.isEmpty(orgCode)) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 1 + "列机构编码不能为空!");
                    }
                    organization.setOrgCode(orgCode);
                    String orgName = getCellValue(row.getCell(1));
                    if (StringUtils.isEmpty(orgName)) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 2 + "列机构名称不能为空!");
                    }
                    organization.setOrgName(orgName);

                    String orgType = getCellValue(row.getCell(2));
                    if (StringUtils.isEmpty(orgType)) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 3 + "列机构类型不能为空!");
                    }
                    if ("集团".equals(orgType)) {
                        orgNum++;
                        parentIsMust = false;
                    }
                    organization.setOrgType(orgType);
                    String parentPostCode = getCellValue(row.getCell(3));
                    if (StringUtils.isEmpty(parentPostCode) && !parentIsMust) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 4 + "列上级机构编码不能为空!");
                    }
                    organization.setOrgParentCode(parentPostCode);
                    String parentOrgName = getCellValue(row.getCell(4));
                    if (StringUtils.isEmpty(parentOrgName) && !parentIsMust) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 5 + "列上级机构不能为空!");
                    }
                    organization.setOrgParentName(parentOrgName);
                    String managerEmployeeNumber = getCellValue(row.getCell(5));
                    organization.setManagerEmployeeNumber(managerEmployeeNumber);
                    String orgManagerName = getCellValue(row.getCell(6));
                    organization.setOrgManagerName(orgManagerName);


                    //TODO 插入数据库并判断是更新还是新增,数据库是否存在


                    organizationList.add(organization);
                }
            }
        }
        if (orgNum != 1) {
            throw new Exception("有且只能有一个'集团'类型的机构!");
        }
        return organizationList;
    }

    /**
     * 根据不同类型获取值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String cellValue = null;
        if (cell == null) {
            return cellValue;
        }
        // 判断数据的类型
        switch (cell.getCellTypeEnum()) {
            case NUMERIC: // 数字
                cellValue = stringDateProcess(cell);
                break;
            case STRING: // 字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: // 公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: // 空值
                cellValue = "";
                break;
            case ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    /**
     * 时间格式转换
     *
     * @param cell
     * @return
     */
    public static String stringDateProcess(Cell cell) {
        String result = "";
        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                sdf = new SimpleDateFormat("HH:mm ");
            } else {// 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            }
            Date date = cell.getDateCellValue();
            result = sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 58) {
            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            double value = cell.getNumericCellValue();
            Date date = DateUtil.getJavaDate(value);
            result = sdf.format(date);
        } else {
            double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if (temp.equals("General")) {
                format.applyPattern("#");
            }
            result = format.format(value);
        }

        return result;
    }

    public static String checkFile(MultipartFile file) throws IOException {
        // 判断文件是否存在
        if (null == file) {
            //throw new GunsException(BizExceptionEnum.FILE_NOT_FOUND);
            return "1";//文件不存在
        }
        // 获得文件名
        String fileName = file.getOriginalFilename();
        // 判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            //throw new GunsException(BizExceptionEnum.UPLOAD_NOT_EXCEL_ERROR);
            return "2";//上传的不是excel文件
        }
        return fileName;
    }

    /**
     * 递归给机构树的机构设置下面的岗位
     *
     * @param organization
     * @param isEnable
     */
    private void packOrganizationPosition(Organization organization, Short isEnable) {
        List<Organization> childList = organization.getChildList();
        if (!CollectionUtils.isEmpty(childList)) {
            for (Organization org : childList) {
                List<Post> postList = postDao.getPostListByOrgId(org.getOrgId(), isEnable);
                org.setPostList(postList);
                packOrganizationPosition(organization, isEnable);
            }
        }
    }

    /**
     * 修改本级的父级id和全名称及子级的全名称
     *
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
            newOrgCode = culOrgCode(newOrgCode);
            organization.setOrgCode(newOrgCode);
            organizationDao.updateByPrimaryKeySelective(organization);

            List<Organization> organizations = organizationDao.getOrganizationListByParentOrgId(organization.getOrgId());
            if (!CollectionUtils.isEmpty(organizations)) {
                //递归修改本级的父级id和全名称及子级的全名称
                updateOrganizationfull_nameAndOrgCode(organizations, organization);
            }

        }
    }

    /**
     * 处理所有机构以树形结构展示
     *
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
            if (childList != null && childList.size() > 0) {
                org.setChildList(childList);
                organizationList.removeAll(childList);
                handlerOrganizationToTree(organizationList, childList);
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
        for (int i = 0; i < 7; i++) {
            sheet.setColumnWidth(i, 30 * 250);
        }
        //设置为居中加粗,格式化时间格式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);//字号
        font.setBold(true);//加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);//左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
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
            if (StringUtils.isEmpty(orgType)) {
                typeValue = "";
            } else if ("GROUP".equals(orgType.trim())) {
                typeValue = "集团";
            } else if ("UNIT".equals(orgType.trim())) {
                typeValue = "单位";
            } else if ("DEPT".equals(orgType.trim())) {
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
     *
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
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            //将excel写入到输出流中
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new Exception("文件导出失败!");
        }
    }


}
