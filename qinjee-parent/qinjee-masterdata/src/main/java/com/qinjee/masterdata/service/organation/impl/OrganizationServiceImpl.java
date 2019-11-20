package com.qinjee.masterdata.service.organation.impl;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.aop.OrganizationEditAnno;
import com.qinjee.masterdata.aop.OrganizationSaveAnno;
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVoo;
import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.UserRoleService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.masterdata.utils.pexcel.ExcelExportUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.ExcelUtil;
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
    public PageResult<OrganizationVO> getOrganizationPageTree(UserSession userSession, Short isEnable) {
        Integer archiveId = userSession.getArchiveId();

        List<OrganizationVO> organizationVOList = organizationDao.getAllOrganizationByArchiveId(archiveId, isEnable, new Date());

        //获取第一级机构
        List<OrganizationVO> organizationVOS = organizationVOList.stream().filter(organization -> {
            if (organization.getOrgParentId() != null && organization.getOrgParentId() == 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        //递归处理机构,使其以树形结构展示
        handlerOrganizationToTree(organizationVOList, organizationVOS);

        return new PageResult<>(organizationVOList);
    }

    /**
     * 获取第一级机构.
     *
     * @param organizationVOList
     * @return
     */
    private List<OrganizationVO> getFirstOrganizationList(List<OrganizationVO> organizationVOList) {
        List<OrganizationVO> organizationVOS = Collections.singletonList(organizationVOList.get(0));
        organizationVOList.removeAll(organizationVOS);
        return organizationVOS;
    }

    @Override
    public PageResult<OrganizationVO> getOrganizationList(OrganizationPageVo organizationPageVo, UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        //根据用户id进行分页查询, 其他条件的使用场景是什么? 可否为空?
        //  Optional<List<QueryFieldVo>> querFieldVos = Optional.of(organizationPageVo.getQuerFieldVos());
        //    String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, OrganizationVO.class);
        //  if(organizationPageVo.getCurrentPage() != null && organizationPageVo.getPageSize() != null){
        //   PageHelper.startPage(organizationPageVo.getCurrentPage(),organizationPageVo.getPageSize());
        //    }
        List<OrganizationVO> organizationVOList = organizationDao.getOrganizationList(organizationPageVo, null, archiveId, new Date());
        PageResult<OrganizationVO> pageResult = new PageResult<>(organizationVOList);
        return pageResult;
    }

    @Override
    public PageResult<OrganizationVO> getOrganizationGraphics(UserSession userSession, Short isEnable, Integer orgId) {
        Integer archiveId = userSession.getArchiveId();
        OrganizationVO organizationVO = organizationDao.selectByPrimaryKey(orgId);
        String orgCode = organizationVO.getOrgCode() + "%";
        List<OrganizationVO> organizationVOList = organizationDao.getOrganizationGraphics(archiveId, isEnable, orgCode, new Date());
        PageResult<OrganizationVO> pageResult = new PageResult<>(organizationVOList);
        return pageResult;
    }

    @Transactional
    @Override
    @OrganizationSaveAnno
    public ResponseResult addOrganization(UserSession userSession, OrganizationVoo organizationVo) {
        //根据父级机构id查询一些基础信息，构建Organization对象
        OrganizationVO orgBean = initOrganization(organizationVo.getOrgParentId());

        //OrganizationVO organization = getNewOrgCode(organizationVo.getOrgParentId());
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
        orgBean.setCreateTime(new Date());
        orgBean.setOperatorId(userSession.getArchiveId());
        orgBean.setIsEnable((short) 1);
        //设置企业id
        orgBean.setCompanyId(userSession.getCompanyId());
        int insert = organizationDao.insertSelective(orgBean);

        ResponseResult responseResult;
        if (insert == 1) {
            responseResult = new ResponseResult(CommonCode.SUCCESS);
        } else {
            responseResult = new ResponseResult(CommonCode.FAIL);
        }
        responseResult.setResult(orgBean);
        return responseResult;
    }

    private OrganizationVO initOrganization(Integer orgParentId) {
        OrganizationVO orgBean = new OrganizationVO();
        if (orgParentId > 0) {
            //查询父级机构
            OrganizationVO parentOrg = organizationDao.selectByPrimaryKey(orgParentId);
            //查询最新的同级机构
            List<OrganizationVO> brotherOrgList = organizationDao.getOrganizationListByParentOrgId(orgParentId);
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
    private OrganizationVO getNewOrgCode(Integer orgParentId) {
        OrganizationVO organizationVO = new OrganizationVO();
        if (orgParentId != 0) {
            //查询父级机构
            OrganizationVO parentOrganizationVO = organizationDao.selectByPrimaryKey(orgParentId);
            //查询父级机构的所有子机构
            List<OrganizationVO> organizationVOList = organizationDao.getOrganizationListByParentOrgId(orgParentId);
            String parentOrgCode = parentOrganizationVO.getOrgCode();
            String newOrgCode;
            Integer sortId;
            if (CollectionUtils.isEmpty(organizationVOList)) {
                //新增第一个子级
                newOrgCode = parentOrgCode + "001";
                sortId = 1000;
            } else {
                //有子级，获取最新的子机构
                OrganizationVO lastorganization = organizationVOList.get(0);
                String orgCode = lastorganization.getOrgCode();
                newOrgCode = culOrgCode(orgCode);
                sortId = lastorganization.getSortId() + 1000;
            }
            organizationVO.setSortId(sortId);
            organizationVO.setOrgCode(newOrgCode);
            organizationVO.setOrgFullName(parentOrganizationVO.getOrgFullName());
            return organizationVO;
        } else {
            organizationVO.setSortId(1);
            organizationVO.setOrgCode("001");
            return organizationVO;
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
    @OrganizationEditAnno
    public ResponseResult editOrganization(OrganizationVoo organizationVo) {
        OrganizationVO organizationVO = organizationDao.selectByPrimaryKey(organizationVo.getOrgId());
        BeanUtils.copyProperties(organizationVo, organizationVO);
        int result = organizationDao.updateByPrimaryKey(organizationVO);
        return result == 1 ? new ResponseResult() : new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 递归修改子级机构全名称
     *
     * @param organizationVO
     */
    private void updateOrganizationfull_name(OrganizationVO organizationVO) {
        List<OrganizationVO> organizationVOList = organizationDao.getOrganizationListByParentOrgId(organizationVO.getOrgId());
        if (!CollectionUtils.isEmpty(organizationVOList)) {
            for (OrganizationVO organizati : organizationVOList) {
                OrganizationHistory organizationHistory = new OrganizationHistory();
                BeanUtils.copyProperties(organizati, organizationHistory);
                //新增机构历史信息
                organizationHistoryService.addOrganizationHistory(organizationHistory);
                organizati.setOrgFullName(organizationVO.getOrgFullName() + "/" + organizationVO.getOrgName());
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
    private OrganizationVO addOrganizationHistoryByOrgId(Integer orgId) {
        OrganizationVO organizationVO = organizationDao.selectByPrimaryKey(orgId);
        OrganizationHistory organizationHistory = new OrganizationHistory();
        BeanUtils.copyProperties(organizationVO, organizationHistory);
        //新增机构历史信息
        organizationHistoryService.addOrganizationHistory(organizationHistory);
        return organizationVO;
    }

    /**
     * 删除机构的校验：
     * 1、被删除的机构（包含子机构）下若存在人员档案，提示“该机构下存在人员档案”，不能删除
     * 2、被删除的机构（包含子机构在人事异动表、工资、考勤等相关表存在记录，不能删除）
     * <p>
     * 要删除什么：
     * 删除机构（包含子机构）以及机构下的岗位，所有删除的信息都需要维护到历史表
     *
     * @param orgIds
     * @return
     */
    @Transactional
    @Override
    public ResponseResult deleteOrganizationById(List<Integer> orgIds) {
        List<Integer> idList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orgIds)) {
            for (Integer orgId : orgIds) {
                //递归至每一层机构
                idList.add(orgId);
                recursive(orgId, idList);
            }
        }
        //再遍历机构id列表，通过每一个机构id来查询人员档案表等表是否存在相关记录
        //TODO 人事异动表、工资、考勤暂时不考虑
        boolean isExsit = false;
        for (Integer orgId : idList) {
            List<Integer> userArchiveList = userArchiveDao.selectByOrgId(orgId);
            if (userArchiveList != null && userArchiveList.size() > 0) {
                isExsit = true;
                //只要有一个存在则跳出循环
                return new ResponseResult(CommonCode.ORG_EXIST_USER);
            }
        }
        //如果所有机构下都不存在相关人员资料，则全部删除（包含机构下的岗位）
        if (!isExsit) {
            for (Integer orgId : idList) {
                //删除前，通过aop进行历史存档
                OrganizationVO orgBean = organizationDao.selectByPrimaryKey(orgId);
                OrganizationHistory orgHisBean = new OrganizationHistory();
                if (Objects.nonNull(orgBean)) {
                    BeanUtils.copyProperties(orgBean, orgHisBean);
                }
                orgHisBean.setUpdateTime(new Date());
                orgHisBean.setCreateTime(orgBean.getCreateTime());
                //存档
                organizationHistoryService.addOrganizationHistory(orgHisBean);
                //删除
                organizationDao.deleteByPrimaryKey(orgId);
                //删除岗位,逻辑删除
                postDao.deleteByOrgId(orgId);
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 递归查找机构id
     *
     * @param orgId
     * @param idSet
     */
    public void recursive(Integer orgId, List idSet) {
        List<OrganizationVO> parentOrgList = organizationDao.getOrganizationListByParentOrgId(orgId);
        if (parentOrgList != null && parentOrgList.size() > 0) {
            for (OrganizationVO OrganizationVO : parentOrgList) {
                idSet.add(OrganizationVO.getOrgId());
                recursive(OrganizationVO.getOrgId(), idSet);
            }
        }
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
        List<OrganizationVO> organizationVOList = null;
        if (!CollectionUtils.isEmpty(orgIds)) {
            organizationVOList = organizationDao.getOrganizationListByOrgIds(orgIds);
        }
        //判断是否是同一个父级下的
        if (!CollectionUtils.isEmpty(organizationVOList)) {
            Set<Integer> OrgParentIds = organizationVOList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (OrgParentIds.size() != 1) {
                //不是
                return new ResponseResult(CommonCode.FAIL);
            }
            //获取新的合并机构
            OrganizationVO newOrganizationVO = getNewOrganization(newOrgName, targetOrgId, orgType, userSession);
            organizationDao.insertSelective(newOrganizationVO);
            updateOrganizationfull_nameAndOrgCode(organizationVOList, newOrganizationVO);
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
    private OrganizationVO getNewOrganization(String newOrgName, Integer targetOrgId, String orgType, UserSession userSession) {
        OrganizationVO newOrganizationVO = new OrganizationVO();
        newOrganizationVO.setOrgParentId(targetOrgId);
        newOrganizationVO.setOrgName(newOrgName);
        OrganizationVO newOrgCode = initOrganization(targetOrgId);
        String orgfull_name = newOrgCode.getOrgFullName();
        BeanUtils.copyProperties(newOrgCode, newOrganizationVO);
        newOrganizationVO.setIsEnable((short) 1);
        newOrganizationVO.setCompanyId(userSession.getCompanyId());
        newOrganizationVO.setOrgType(orgType);
        newOrganizationVO.setOrgFullName(orgfull_name + "/" + newOrganizationVO.getOrgName());
        return newOrganizationVO;
    }

    @Override
    public ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(String userName) {
        //TODO 调用人员的接口
        return null;
    }

    @Override
    @Transactional
    public ResponseResult sortOrganization(LinkedList<String> orgIds) {
        Integer i = organizationDao.updateBatchOrganizationSortid(orgIds);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult transferOrganization(List<Integer> orgIds, Integer targetOrgId) {

        List<OrganizationVO> organizationVOList = null;
        if (!CollectionUtils.isEmpty(orgIds)) {
            organizationVOList = organizationDao.getOrganizationListByOrgIds(orgIds);
        }

        //判断是否是同一个父级下的
        if (!CollectionUtils.isEmpty(organizationVOList)) {
            Set<Integer> OrgParentIds = organizationVOList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (OrgParentIds.size() != 1) {
                //不是
                return new ResponseResult(CommonCode.FAIL);
            }
            OrganizationVO parentOrganizationVO = organizationDao.selectByPrimaryKey(targetOrgId);

            updateOrganizationfull_nameAndOrgCode(organizationVOList, parentOrganizationVO);
        }

        return new ResponseResult();
    }

    @Override
    public ResponseResult<List<OrganizationVO>> getOrganizationPositionTree(UserSession userSession, Short isEnable) {
        PageResult<OrganizationVO> organizationTree = getOrganizationPageTree(userSession, isEnable);
        List<OrganizationVO> organizationVOTreeList = organizationTree.getList();
        if (!CollectionUtils.isEmpty(organizationVOTreeList)) {
            for (OrganizationVO organizationVO : organizationVOTreeList) {
                List<Post> postList = postDao.getPostListByOrgId(organizationVO.getOrgId(), isEnable);
                organizationVO.setPostList(postList);
                packOrganizationPosition(organizationVO, isEnable);
            }
        }
        return new ResponseResult<>(organizationVOTreeList);
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
        String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, OrganizationVO.class);
        List<OrganizationVO> organizationVOList = organizationDao.getOrganizationList(organizationPageVo, sortFieldStr, archiveId, new Date());
        //导出Excel
        exportExcel(response, organizationVOList);
        return new ResponseResult();
    }

    @Override
    public ResponseResult downloadExcelByOrgCodeId(List<Integer> orgIds, HttpServletResponse response, UserSession userSession) {
        List<OrganizationVO> organizationVOList = organizationDao.getOrganizationsByOrgIds(orgIds);
        System.out.println("organizationVOList:"+organizationVOList);
        ExcelExportUtil.exportToFile("e:\\aa.xls",organizationVOList);
        return null;
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

    @Override
    public OrganizationVO selectByPrimaryKey(Integer orgId) {
        return organizationDao.selectByPrimaryKey(orgId);
    }

    @Override
    public List<OrganizationVO> getOrganizationListByParentOrgId(Integer orgId) {
        return organizationDao.getOrganizationListByParentOrgId(orgId);
    }

    @Override
    public List<OrganizationVO> getAllOrganizationTree(UserSession userSession, Short isEnable) {
        Integer archiveId = userSession.getArchiveId();

        List<OrganizationVO> organizationVOList = organizationDao.getAllOrganizationByArchiveId(archiveId, isEnable, new Date());

        //获取第一级机构
        List<OrganizationVO> organizationVOS = organizationVOList.stream().filter(organization -> {
            if (organization.getOrgParentId() != null && organization.getOrgParentId() == 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        //递归处理机构,使其以树形结构展示
        handlerOrganizationToTree(organizationVOList, organizationVOS);

        return organizationVOList;
    }

    private List<OrganizationVO> parseFile(MultipartFile file) throws Exception {
        List<OrganizationVO> organizationVOList = new ArrayList<>();
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
                    OrganizationVO organizationVO = new OrganizationVO();
                    String orgCode = getCellValue(row.getCell(0));
                    if (StringUtils.isEmpty(orgCode)) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 1 + "列机构编码不能为空!");
                    }
                    organizationVO.setOrgCode(orgCode);
                    String orgName = getCellValue(row.getCell(1));
                    if (StringUtils.isEmpty(orgName)) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 2 + "列机构名称不能为空!");
                    }
                    organizationVO.setOrgName(orgName);

                    String orgType = getCellValue(row.getCell(2));
                    if (StringUtils.isEmpty(orgType)) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 3 + "列机构类型不能为空!");
                    }
                    if ("集团".equals(orgType)) {
                        orgNum++;
                        parentIsMust = false;
                    }
                    organizationVO.setOrgType(orgType);
                    String parentPostCode = getCellValue(row.getCell(3));
                    if (StringUtils.isEmpty(parentPostCode) && !parentIsMust) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 4 + "列上级机构编码不能为空!");
                    }
                    organizationVO.setOrgParentCode(parentPostCode);
                    String parentOrgName = getCellValue(row.getCell(4));
                    if (StringUtils.isEmpty(parentOrgName) && !parentIsMust) {
                        throw new Exception((firstRowNum + 1) + "行,第" + 5 + "列上级机构不能为空!");
                    }
                    organizationVO.setOrgParentName(parentOrgName);
                    String managerEmployeeNumber = getCellValue(row.getCell(5));
                    organizationVO.setManagerEmployeeNumber(managerEmployeeNumber);
                    String orgManagerName = getCellValue(row.getCell(6));
                    organizationVO.setOrgManagerName(orgManagerName);


                    //TODO 插入数据库并判断是更新还是新增,数据库是否存在


                    organizationVOList.add(organizationVO);
                }
            }
        }
        if (orgNum != 1) {
            throw new Exception("有且只能有一个'集团'类型的机构!");
        }
        return organizationVOList;
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
     * @param organizationVO
     * @param isEnable
     */
    private void packOrganizationPosition(OrganizationVO organizationVO, Short isEnable) {
        List<OrganizationVO> childList = organizationVO.getChildList();
        if (!CollectionUtils.isEmpty(childList)) {
            for (OrganizationVO org : childList) {
                List<Post> postList = postDao.getPostListByOrgId(org.getOrgId(), isEnable);
                org.setPostList(postList);
                packOrganizationPosition(organizationVO, isEnable);
            }
        }
    }

    /**
     * 修改本级的父级id和全名称及子级的全名称
     *
     * @param organizationVOList
     * @param parentOrganizationVO
     */
    private void updateOrganizationfull_nameAndOrgCode(List<OrganizationVO> organizationVOList, OrganizationVO parentOrganizationVO) {
        String newOrgCode = parentOrganizationVO.getOrgCode() + "000";
        for (OrganizationVO organizationVO : organizationVOList) {
            OrganizationHistory organizationHistory = new OrganizationHistory();
            BeanUtils.copyProperties(organizationVO, organizationHistory);
            organizationHistoryService.addOrganizationHistory(organizationHistory);
            organizationVO.setOrgParentId(parentOrganizationVO.getOrgId());
            organizationVO.setOrgFullName(parentOrganizationVO.getOrgFullName() + "/" + organizationVO.getOrgName());
            newOrgCode = culOrgCode(newOrgCode);
            organizationVO.setOrgCode(newOrgCode);
            organizationDao.updateByPrimaryKeySelective(organizationVO);

            List<OrganizationVO> organizationVOS = organizationDao.getOrganizationListByParentOrgId(organizationVO.getOrgId());
            if (!CollectionUtils.isEmpty(organizationVOS)) {
                //递归修改本级的父级id和全名称及子级的全名称
                updateOrganizationfull_nameAndOrgCode(organizationVOS, organizationVO);
            }

        }
    }

    /**
     * 处理所有机构以树形结构展示
     *
     * @param organizationVOList
     * @param organizationVOS
     */
    private void handlerOrganizationToTree(List<OrganizationVO> organizationVOList, List<OrganizationVO> organizationVOS) {
        for (OrganizationVO org : organizationVOS) {
            Integer orgId = org.getOrgId();
            List<OrganizationVO> childList = organizationVOList.stream().filter(organization -> {
                Integer orgParentId = organization.getOrgParentId();
                if (orgParentId != null && orgParentId > 0) {
                    return orgParentId.equals(orgId);
                }
                return false;
            }).collect(Collectors.toList());
            //判断是否还有子级
            if (childList != null && childList.size() > 0) {
                org.setChildList(childList);
                organizationVOList.removeAll(childList);
                handlerOrganizationToTree(organizationVOList, childList);
            }
        }
    }


    private static void exportExcel(HttpServletResponse response, List<OrganizationVO> organizationVOList) {
        try {
            //实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("sheet");
            //设置表头
            setTitle(workbook, sheet);
            //设置单元格并赋值
            setData(sheet, organizationVOList);
            //设置浏览器下载
            setBrowser(response, workbook, "机构信息.xls");
        } catch (Exception e) {
            e.printStackTrace();
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
    private static void setData(HSSFSheet sheet, List<OrganizationVO> organizationVOList) {
        for (int i = 0; i < organizationVOList.size(); i++) {
            OrganizationVO organizationVO = organizationVOList.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(organizationVO.getOrgCode());
            row.createCell(1).setCellValue(organizationVO.getOrgName());
            String orgType = organizationVO.getOrgType();
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
            row.createCell(3).setCellValue(organizationVO.getOrgParentCode());
            row.createCell(4).setCellValue(organizationVO.getOrgParentName());
            row.createCell(5).setCellValue(organizationVO.getOrgManagerName());
            row.createCell(6).setCellValue(organizationVO.getManagerEmployeeNumber());
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
