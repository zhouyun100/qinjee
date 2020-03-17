package com.qinjee.masterdata.service.organation.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.aop.OrganizationDeleteAnno;
import com.qinjee.masterdata.aop.OrganizationEditAnno;
import com.qinjee.masterdata.aop.OrganizationSaveAnno;
import com.qinjee.masterdata.aop.OrganizationTransferAnno;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.organation.PostDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.SysDict;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationExportBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationMergeBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationPageBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationTransferBO;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.auth.ApiAuthService;
import com.qinjee.masterdata.service.organation.AbstractOrganizationHelper;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.UserRoleService;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.masterdata.utils.DealHeadParamUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrganizationServiceImpl extends AbstractOrganizationHelper<OrganizationVO, OrganizationVO> implements OrganizationService {
    private static Logger logger = LogManager.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private RedisClusterService redisService;
    @Autowired
    private OrganizationHistoryService organizationHistoryService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private ApiAuthService apiAuthService;

    @Autowired
    private SysDictService sysDictService;
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    //=====================================================================
    @Override
    public PageResult<OrganizationVO> getOrganizationPageTree(UserSession userSession, Short isEnable) {
        Integer archiveId = userSession.getArchiveId();
        List<OrganizationVO> allOrgsList = organizationDao.listAllOrganizationByArchiveId(archiveId, isEnable, new Date());
        //获取第一级机构
        List<OrganizationVO> topOrgsList = allOrgsList.stream().filter(organization -> {
            if (organization.getOrgParentId() != null && organization.getOrgParentId() == 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //递归处理机构,使其以树形结构展示
        handlerOrganizationToTree(allOrgsList, topOrgsList);
        return new PageResult<>(allOrgsList);
    }

    //=====================================================================
    @Override
    public PageResult<OrganizationVO> getDirectOrganizationPageList(OrganizationPageBO organizationPageBO, UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        if (organizationPageBO.getCurrentPage() != null && organizationPageBO.getPageSize() != null) {
            PageHelper.startPage(organizationPageBO.getCurrentPage(), organizationPageBO.getPageSize());
        }
        String whereSql = DealHeadParamUtil.getWhereSql(organizationPageBO.getTableHeadParamList(), "lastTable.");
        String orderSql = DealHeadParamUtil.getOrderSql(organizationPageBO.getTableHeadParamList(), "lastTable.");
        List<OrganizationVO> organizationVOList = organizationDao.listDirectOrganizationByCondition(organizationPageBO, archiveId, new Date(), whereSql, orderSql);
        PageInfo<OrganizationVO> pageInfo = new PageInfo<>(organizationVOList);
        PageResult<OrganizationVO> pageResult = new PageResult<>(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    //=====================================================================

    /**
     * 不要service处理异常，否则事务可能会失效
     *
     * @param orgName
     * @param orgType
     * @param parentOrgId
     * @param orgManagerId
     * @param userSession
     * @return
     */
    @Override
    @OrganizationSaveAnno
    public OrganizationVO addOrganization(String orgName, String orgCode, String orgType, String parentOrgId, String orgManagerId, UserSession userSession) {
        //同级机构不能重名：
        List<OrganizationVO> sonOrgs = organizationDao.listSonOrganization(Integer.valueOf(parentOrgId));
        boolean bool = sonOrgs.stream().anyMatch(a -> orgName.equals(a.getOrgName()));
        if (bool) {
            ExceptionCast.cast(CommonCode.NAME_ALREADY_USED_AT_SAME_LEVEL);
        }

        OrganizationVO orgBean = new OrganizationVO();
        //校验orgCode是否已存在
        OrganizationVO orgByCode = organizationDao.getOrganizationByOrgCodeAndCompanyId(orgCode, userSession.getCompanyId());
        if (Objects.nonNull(orgByCode)) {
            ExceptionCast.cast(CommonCode.CODE_USED);
        }
        //查询父级机构，用来生成机构全称
        OrganizationVO parentOrg = organizationDao.getOrganizationById(Integer.valueOf(parentOrgId));

        if (Objects.nonNull(parentOrg)) {
            //机构类型的校验  单位的上级机构不能是部门、集团的上级机构不能是单位或部门
            if (orgType.equals("UNIT") && parentOrg.getOrgType().equals("DEPT")) {
                ExceptionCast.cast(CommonCode.UNIT_PARENT_NOT_BE_DEPT);
            } else if (orgType.equals("GROUP") && (parentOrg.getOrgType().equals("DEPT") || parentOrg.getOrgType().equals("UNIT"))) {
                ExceptionCast.cast(CommonCode.GROUP_PARENT_NOT_BE_UNIT_OR_DEPT);
            }
        }

        //查询同级机构用来生成机构sortid、orgCode
        List<OrganizationVO> brotherOrgList = organizationDao.listSonOrganization(Integer.parseInt(parentOrgId));
        Integer sortId = generateOrgSortId(brotherOrgList);

        if (Objects.isNull(parentOrg) || parentOrg.getOrgType().equalsIgnoreCase("GROUP")) {
            orgBean.setOrgFullName(orgName);
        } else {
            orgBean.setOrgFullName(parentOrg.getOrgFullName() + "/" + orgName);
        }
        //String orgCode = generateOrgCode(brotherOrgList, parentOrg);
        orgBean.setSortId(sortId);
        orgBean.setOrgParentId(Integer.parseInt(parentOrgId));
        orgBean.setOrgType(orgType);
        orgBean.setOrgName(orgName);
        orgBean.setOrgCode(orgCode);
        if (orgManagerId != null && !orgManagerId.equals("")) {
            orgBean.setOrgManagerId(Integer.parseInt(orgManagerId));
        }
        orgBean.setCreateTime(new Date());
        orgBean.setOperatorId(userSession.getArchiveId());
        orgBean.setIsEnable((short) 1);
        //设置企业id
        orgBean.setCompanyId(userSession.getCompanyId());
        organizationDao.insertSelective(orgBean);
        //维护机构与角色
        apiAuthService.addOrg(orgBean.getOrgId(), Integer.parseInt(parentOrgId), userSession.getArchiveId());
        return orgBean;
    }

    private Integer generateOrgSortId(List<OrganizationVO> brotherOrgList) {
        int sortId = 1000;
        if (CollectionUtils.isEmpty(brotherOrgList)) {
            sortId = 1000;
        } else {
            //取得最大sortId
            int maxSortId = brotherOrgList.stream().mapToInt(OrganizationVO::getSortId).max().getAsInt();
            sortId = maxSortId + 1000;
        }
        return sortId;
    }

    @Override
    public String generateOrgCode(Integer parentOrgId) {
        logger.info("根据父机构id生成机构编码：parentOrgId=" + parentOrgId);
        //查询父级机构，用来生成机构全称和机构编码
        OrganizationVO parentOrg = organizationDao.getOrganizationById(parentOrgId);
        //查询同级机构用来生成机构sortid、orgCode
        List<OrganizationVO> brotherOrgList = organizationDao.listSonOrganization(parentOrgId);
        if (Objects.isNull(parentOrg)) {
            //TODO 改成递增
            return "1";
        }
        if (CollectionUtils.isEmpty(brotherOrgList)) {
            return parentOrg.getOrgCode() + "01";
        } else {
            //先过滤掉机构编码最后两位为非数字的，再筛选最大值
            List<OrganizationVO> filterBrotherOrgList = brotherOrgList.stream().filter(o -> (o.getOrgCode().length() > 2 && StringUtils.isNumeric(o.getOrgCode().substring(o.getOrgCode().length() - 2)))).collect(Collectors.toList());
            logger.info("滤掉后的filterBrotherOrgList：" + filterBrotherOrgList);
            //根据机构编码排序，并且只取最后两位位数字的
            Comparator comparator = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    String orgCode1 = String.valueOf(o1);
                    String orgCode2 = String.valueOf(o2);
                    String orgCode1Num = orgCode1.substring(orgCode1.length() - 2);
                    String orgCode2Num = orgCode2.substring(orgCode2.length() - 2);
                    boolean isNum = StringUtils.isNumeric(orgCode1Num) && StringUtils.isNumeric(orgCode2Num);
                    if (isNum && orgCode1.length() > orgCode2.length()) {
                        return -1;
                    } else if (isNum && orgCode1.length() < orgCode2.length()) {
                        return 1;
                    }
                    return orgCode1.compareTo(orgCode2);
                }
            };
            String lastOrgCode = filterBrotherOrgList.stream().map(OrganizationVO::getOrgCode).max(comparator).get().toString();
            logger.info("当前机构下的lastOrgCode：" + lastOrgCode);
            if (null == lastOrgCode || "".equals(lastOrgCode)) {
                return parentOrg.getOrgCode() + "01";
            }
            //计算编码
            String orgCode = culOrgCode(lastOrgCode);
            logger.info("计算生成的orgCode：" + orgCode);
            return orgCode;
        }
    }

    @Override
    public Integer getBusunessUnitIdByOrgId(Integer orgId) {
        //判断当前机构类型是否是单位，如果是 则直接返回机构id，如果不是则查询上级机构，直至机构类型为单位或到顶级机构为止
        OrganizationVO org = organizationDao.getOrganizationById(orgId);
        int tempUnitId = 0;
        if (Objects.isNull(org)) {
            return orgId;
        }
        if (org.getOrgParentId().equals(0)) {
            return orgId;
        }
        if (!"UNIT".equalsIgnoreCase(org.getOrgType())) {
            tempUnitId = getBusunessUnitIdByOrgId(org.getOrgParentId());
        } else {
            return org.getOrgId();
        }
        return tempUnitId;
    }

    /**
     * 获取新orgCode
     *
     * @param orgCode
     * @return
     */
    private String culOrgCode(String orgCode) {
        //后缀二位数字，如果不是数字呢
        String number = orgCode.substring(orgCode.length() - 2);
        String preCode = orgCode.substring(0, orgCode.length() - 2);
        Integer new_OrgCode = Integer.parseInt(number) + 1;
        String code = new_OrgCode.toString();
        int i = 2 - code.length();
        if (i < 0) {
            ExceptionCast.cast(CommonCode.ORGANIZATION_OUT_OF_RANGE);
        }
        for (int k = 0; k < i; k++) {
            code = "0" + code;
        }
        String newOrgCode = preCode + code;
        return newOrgCode;
    }

    //=====================================================================
    @Override
    @OrganizationEditAnno
    //TODO 目前修改机构编码码 下级机构编码不会联动修改
    public void editOrganization(String orgCode, String orgId, String orgName, String orgType, String parentOrgId, String orgManagerId, UserSession userSession) {
        //判断重名
        judgeDuplicateName(orgId, orgName, Integer.valueOf(parentOrgId));

        //反查organizationVO
        OrganizationVO organization = organizationDao.getOrganizationById(Integer.parseInt(orgId));
        OrganizationVO parentOrganization = organizationDao.getOrganizationById(Integer.parseInt(parentOrgId));

        if (Objects.nonNull(parentOrganization)) {
            //机构类型的校验  单位的上级机构不能是部门、集团的上级机构不能是单位或部门
            if (orgType.equals("UNIT") && parentOrganization.getOrgType().equals("DEPT")) {
                ExceptionCast.cast(CommonCode.UNIT_PARENT_NOT_BE_DEPT);
            } else if (orgType.equals("GROUP") && (parentOrganization.getOrgType().equals("DEPT") || parentOrganization.getOrgType().equals("UNIT"))) {
                ExceptionCast.cast(CommonCode.GROUP_PARENT_NOT_BE_UNIT_OR_DEPT);
            }
        }

        //判断机构编码是否唯一
        if (!organization.getOrgCode().equals(orgCode)) {
            OrganizationVO orgBean = organizationDao.getOrganizationByOrgCodeAndCompanyId(orgCode, userSession.getCompanyId());
            if (orgBean != null && !orgId.equals(orgBean.getOrgId())) {
                //机构编码在同一企业下不唯一
                ExceptionCast.cast(CommonCode.CODE_USED);
            }
        }
        String newOrgFullName;
        if (Objects.nonNull(parentOrganization) && !parentOrganization.getOrgType().equals("GROUP")) {
            newOrgFullName = parentOrganization.getOrgFullName() + "/" + orgName;
        } else {
            newOrgFullName = orgName;
        }
        organization.setOrgParentId(Integer.parseInt(parentOrgId));
        if (null != orgManagerId && !"".equals(orgManagerId)) {
            organization.setOrgManagerId(Integer.parseInt(orgManagerId));
        } else {
            organization.setOrgManagerId(null);
        }
        organization.setOrgType(orgType);
        organization.setOrgName(orgName);
        organization.setOrgFullName(newOrgFullName);
        organization.setOrgCode(orgCode);
        organizationDao.updateByPrimaryKey(organization);
        //修改子机构名称
        recursiveUpdateOrgFullName(organization);
    }

    private void judgeDuplicateName(String orgId, String orgName, Integer parentOrgId) {
        //同级机构不能重名：
        List<OrganizationVO> sonOrgs = organizationDao.listSonOrganization(parentOrgId);
        boolean bool = sonOrgs.stream().anyMatch(a -> (orgName.equals(a.getOrgName()) && !String.valueOf(a.getOrgId()).equals(orgId)));
        if (bool) {
            ExceptionCast.cast(CommonCode.NAME_ALREADY_USED_AT_SAME_LEVEL);
        }
    }

    //=====================================================================

    /**
     * @param userSession
     * @param layer                   查询机构层级数
     * @param isContainsCompiler
     * @param isContainsActualMembers
     * @param orgId
     * @param isEnable
     * @return
     */
    @Override
    public List<OrganizationVO> getOrganizationGraphics(UserSession userSession, Integer layer, boolean isContainsCompiler, boolean isContainsActualMembers, Integer orgId, Short isEnable) {
        //拿到关联的所有机构id
        List<Integer> orgIdList = null;
        if (layer < 1) {
            layer = 2;
        }
        orgIdList = organizationDao.getOrgIds(orgId, userSession.getArchiveId(), isEnable, new Date());
        //orgIdList = getOrgIdList(userSession.getArchiveId(), orgId, (layer - 1), isEnable);
        //查询所有相关的机构
        List<OrganizationVO> allOrg = organizationDao.getOrganizationGraphics(userSession.getArchiveId(), orgIdList, isEnable, new Date());
        //拿到根节点
        List<OrganizationVO> topOrgsList = allOrg.stream().filter(organization -> {
            if (organization.getOrgId() != null && organization.getOrgId().equals(orgId)) {
                return true;
            } else if (orgId == 0) {         //TODO 如果是顶级机构
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //递归处理机构,使其以树形结构展示
        handlerOrganizationToGraphics(allOrg, topOrgsList, isContainsCompiler, isContainsActualMembers);
        return allOrg;
    }

    //=====================================================================

    /**
     * 导出机构
     *
     * @return
     */
    @Override
    public List<OrganizationVO> exportOrganization(OrganizationExportBO orgExportBO, Integer archiveId) {
        List<OrganizationVO> orgList = null;

        if (CollectionUtils.isEmpty(orgExportBO.getOrgIds())) {

            List<Integer> orgIdList = organizationDao.getOrgIds(orgExportBO.getOrgId(), archiveId, orgExportBO.getIsEnable(), new Date());
            String whereSql = DealHeadParamUtil.getWhereSql(orgExportBO.getTableHeadParamList(), "lastTable.");
            String orderSql = DealHeadParamUtil.getOrderSql(orgExportBO.getTableHeadParamList(), "lastTable.");
            orgList = organizationDao.listAllOrganizationByArchiveIdAndOrgId(orgIdList, archiveId, new Date(), whereSql, orderSql);
        } else {
            //如果已经勾选了id，就不需要再进行表头筛选了
            //String whereSql = DealHeadParamUtil.getWhereSql(orgExportBO.getTableHeadParamList(), "lastTable.");
            // String orderSql = DealHeadParamUtil.getOrderSql(orgExportBO.getTableHeadParamList(), "lastTable.");
            orgList = organizationDao.listOrganizationsByCondition(orgExportBO.getOrgIds(), null, null);
        }
        if (CollectionUtils.isEmpty(orgList)) {
            ExceptionCast.cast(CommonCode.FILE_EXPORT_FAILED);
        }
        return orgList;
    }


    //==========================================================

    /**
     * 分页查询用户下所有机构包含子孙
     *
     * @param organizationPageBO
     * @param userSession
     * @return
     */
    @Override
    public PageResult<OrganizationVO> getAllOrganizationPageList(OrganizationPageBO organizationPageBO, UserSession userSession) {
        PageResult<OrganizationVO> pageResult = null;
        //拿到关联的所有机构id
        List<Integer> orgIdList = null;
        Short isEnable = organizationPageBO.getIsEnable();
        //如果当前机构为空的话 返回空集合
        orgIdList = organizationDao.getOrgIds(organizationPageBO.getOrgParentId(), userSession.getArchiveId(), isEnable, new Date());
        if (!CollectionUtils.isEmpty(orgIdList)) {
            if (organizationPageBO.getCurrentPage() != null && organizationPageBO.getPageSize() != null) {
                PageHelper.startPage(organizationPageBO.getCurrentPage(), organizationPageBO.getPageSize());
            }
            String whereSql = DealHeadParamUtil.getWhereSql(organizationPageBO.getTableHeadParamList(), "lastTable.");
            String orderSql = DealHeadParamUtil.getOrderSql(organizationPageBO.getTableHeadParamList(), "lastTable.");
            List<OrganizationVO> organizationVOList = organizationDao.listOrganizationsByCondition(orgIdList, whereSql, orderSql);
            PageInfo<OrganizationVO> pageInfo = new PageInfo<>(organizationVOList);
            pageResult = new PageResult<>(pageInfo.getList());
            pageResult.setTotal(pageInfo.getTotal());
        }

        return pageResult;
    }


    /**
     * 入库
     *
     * @param userSession
     * @return
     */
    @Override
    public void importToDatabase(String orgExcelRedisKey, UserSession userSession) {
        String data = redisService.get(orgExcelRedisKey.trim());
        //将其转为对象集合
        List<OrganizationVO> list = JSONArray.parseArray(data, OrganizationVO.class);

        LinkedMultiValueMap<String, OrganizationVO> multiValuedMap = new LinkedMultiValueMap<String, OrganizationVO>();
        for (OrganizationVO organizationVO : list) {
            //如果夫机构编码为空，则为顶级机构，一共只有一个顶级哦
            if (null == organizationVO.getOrgParentCode() || "".equals(organizationVO.getOrgParentCode())) {
                multiValuedMap.add("topOrg", organizationVO);
            } else {
                multiValuedMap.add(organizationVO.getOrgParentCode(), organizationVO);
            }
        }
        for (Map.Entry<String, List<OrganizationVO>> entry : multiValuedMap.entrySet()) {
            List<OrganizationVO> orgList = entry.getValue();
            orgList.sort(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    OrganizationVO org1 = (OrganizationVO) o1;
                    OrganizationVO org2 = (OrganizationVO) o2;
                    return org1.getOrgCode().compareTo(org2.getOrgCode());
                    //return Long.compare(Long.parseLong(org1.getOrgCode()), Long.parseLong(org2.getOrgCode()));
                }
            });
            int sortId = 1000;
            for (OrganizationVO vo : orgList) {
                OrganizationVO ifExistVo = organizationDao.getOrganizationByOrgCodeAndCompanyId(vo.getOrgCode(), userSession.getCompanyId());
                OrganizationVO parentOrg = organizationDao.getOrganizationByOrgCodeAndCompanyId(vo.getOrgParentCode(), userSession.getCompanyId());
                Integer orgManagerId = userArchiveDao.selectArchiveIdByNumber(vo.getManagerEmployeeNumber(), userSession.getCompanyId());

                //导入时将“部门”转为"DEPT"
                List<SysDict> orgTypeDic = sysDictService.searchSysDictListByDictType("ORG_TYPE");
                for (SysDict dict : orgTypeDic) {
                    if (null != dict.getDictValue() && dict.getDictValue().equalsIgnoreCase(vo.getOrgType())) {
                        vo.setOrgType(dict.getDictCode());
                    }
                }

                //构建vo
                vo.setOrgManagerId(orgManagerId);
                vo.setOperatorId(userSession.getArchiveId());
                vo.setCompanyId(userSession.getCompanyId());
                vo.setSortId(sortId);
                sortId += 1000;
                //TODO 根据部门负责人编号  查询档案id
                vo.setOrgManagerId(userSession.getArchiveId());
                //查询父机构的全称  设置全称
                if (Objects.isNull(parentOrg)) {
                    vo.setOrgParentId(0);
                    vo.setOrgFullName(vo.getOrgName());
                } else {
                    vo.setOrgParentId(parentOrg.getOrgId());
                    vo.setOrgFullName(parentOrg.getOrgFullName() + "/" + vo.getOrgName());
                    if (parentOrg.getOrgType().equalsIgnoreCase("GROUP")) {
                        vo.setOrgFullName(vo.getOrgName());
                    }
                }

                //已存在 则更新
                if (Objects.nonNull(ifExistVo)) {
                    //根据机构编码与企业id进行更新
                    organizationDao.updateByOrgCode(vo);
                } else {
                    organizationDao.insertSelective(vo);
                    //维护机构与角色
                    apiAuthService.addOrg(vo.getOrgId(), vo.getOrgParentId(), userSession.getArchiveId());
                }
            }
        }
    }


    /**
     * 导入并校验
     *
     * @param multfile
     * @param userSession
     * @return
     * @throws Exception
     */
    @Override
    public ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession) throws Exception {
        return doUploadAndCheck(multfile, OrganizationVO.class, userSession);

    }


    @Override
    public void cancelImport(String redisKey, String errorInfoKey) {
        redisService.del(redisKey);
        redisService.del(errorInfoKey);
    }


    //=====================================================================


    //=====================================================================


    @Override
    protected List<OrganizationVO> checkExcel(List<OrganizationVO> dataList, UserSession userSession) {
        //统一查找出一些公共不变的数据
        List<SysDict> orgDictListMem = sysDictService.searchSysDictListByDictType("ORG_TYPE");
        List<UserArchiveVo> userArchiveVosMem = userArchiveDao.listUserArchiveByCompanyId(userSession.getCompanyId());
        List<OrganizationVO> organizationVOListMem = organizationDao.listOrganizationByCompanyId(userSession.getCompanyId());
        dataList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                OrganizationVO org1 = (OrganizationVO) o1;
                OrganizationVO org2 = (OrganizationVO) o2;
                return org1.getLineNumber().compareTo(org2.getLineNumber());
                //return Long.compare(Long.parseLong(org1.getOrgCode()), Long.parseLong(org2.getOrgCode()));
            }
        });

        List<OrganizationVO> checkList = new ArrayList<>(dataList.size());
        Map<String, String> excelOrgNameMap = new HashMap<>(dataList.size());
        for (OrganizationVO organizationVO : dataList) {
            //用于判断机构编码在excel中是否存在
            excelOrgNameMap.put(organizationVO.getOrgCode(), organizationVO.getOrgName());
        }
        int groupCount = 0;
        for (OrganizationVO organizationVO : dataList) {
            //OrganizationVO checkVo = new OrganizationVO();
            //BeanUtils.copyProperties(organizationVO, checkVo);
            StringBuilder resultMsg = new StringBuilder(1024);
            //验空
            if (StringUtils.isBlank(organizationVO.getOrgCode())) {
                organizationVO.setCheckResult(false);
                resultMsg.append("机构编码不能为空 | ");
            }
            if (StringUtils.isBlank(organizationVO.getOrgName())) {
                organizationVO.setCheckResult(false);
                resultMsg.append("机构名称不能为空| ");
            }
            if (StringUtils.isBlank(organizationVO.getOrgType())) {
                organizationVO.setCheckResult(false);
                resultMsg.append("机构类型不能为空 | ");
            }
            if (StringUtils.isBlank(organizationVO.getOrgParentCode()) && Objects.nonNull(organizationVO.getOrgType()) && !organizationVO.getOrgType().equals("集团")) {
                organizationVO.setCheckResult(false);
                resultMsg.append("非集团类型机构的上级机构编码不能为空 | ");
            }
            if (StringUtils.isBlank(organizationVO.getOrgParentName()) && Objects.nonNull(organizationVO.getOrgType()) && !organizationVO.getOrgType().equals("集团")) {
                organizationVO.setCheckResult(false);
                resultMsg.append("非集团类型机构的上级机构名称不能为空 | ");
            }
            if (StringUtils.isNotBlank(organizationVO.getOrgType()) && (!(organizationVO.getOrgType().equals("集团") || organizationVO.getOrgType().equals("单位") || organizationVO.getOrgType().equals("部门")))) {
                //TODO
                boolean bool = orgDictListMem.stream().anyMatch(a -> a.getDictValue().equals(organizationVO.getOrgType()));
                if (!bool) {
                    organizationVO.setCheckResult(false);
                    resultMsg.append("机构类型[" + organizationVO.getOrgType() + "]不存在 | ");
                }
            }

            if (StringUtils.isNotBlank(organizationVO.getOrgType()) && organizationVO.getOrgType().equals("GROUP")) {
                groupCount++;
            }
            if (groupCount > 1) {
                organizationVO.setCheckResult(false);
                resultMsg.append("集团类型机构只能存在一个 | ");
            }

            //TODO 判断同级下机构是否重名 这个逻辑存在一个小问题，影响不大
            if (StringUtils.isNotBlank(organizationVO.getOrgName()) && StringUtils.isNotBlank(organizationVO.getOrgCode())&&StringUtils.isNotBlank(organizationVO.getOrgParentCode())) {
                List<OrganizationVO> filterList = dataList.stream().filter(a -> null!=a.getOrgParentCode()&&a.getOrgParentCode().equals(organizationVO.getOrgParentCode())
                        && organizationVO.getOrgName().equals(a.getOrgName())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(filterList) && filterList.size() > 1) {
                    organizationVO.setCheckResult(false);
                    resultMsg.append("机构名称在excel同级下重名 | ");
                }//如果excel中不重名那么到数据库中再查一次
                else{
                    //根据父级机构编码机构和机构名称查询(数据库中已有数据理论上不会重名)
                   List<OrganizationVO> filterList2 = organizationVOListMem.stream().filter(a ->null!=a.getOrgParentCode()&& a.getOrgParentCode().equals(organizationVO.getOrgParentCode())
                           &&organizationVO.getOrgName().equals(a.getOrgName())).collect(Collectors.toList());
                    if(filterList2.size()>1){
                        System.out.println("数据库中存在同级下的同名机构脏数据");
                    }
                    //如果filterList2不为空，并且机构编码不等 ，则判断重名
                    if(CollectionUtils.isNotEmpty(filterList2)&&!filterList2.get(0).getOrgCode().equals(organizationVO.getOrgCode())){
                        organizationVO.setCheckResult(false);
                        resultMsg.append("excel中机构名称与数据库中机构名称在同级下重名 | ");
                    }
                }
            }
            if (StringUtils.isNotBlank(organizationVO.getOrgType()) && StringUtils.isNotBlank(organizationVO.getOrgParentCode())) {
                String parentOrgType = organizationVOListMem.stream().filter(a -> organizationVO.getOrgParentCode().equals(a.getOrgParentCode())).map(a -> a.getOrgType()).findFirst().get();
                if ("集团".equals(organizationVO.getOrgType())) {
                    if ("UNIT".equals(parentOrgType) || "DEPT".equals(parentOrgType)) {
                        organizationVO.setCheckResult(false);
                        resultMsg.append("集团的上级机构不能是单位或部门 | ");
                    }
                } else if ("单位".equals(organizationVO.getOrgType())) {
                    if ("DEPT".equals(parentOrgType)) {
                        organizationVO.setCheckResult(false);
                        resultMsg.append("单位的上级机构不能是部门 | ");
                    }
                }

            }


            //判断机构类型 1、单位的上级机构不能是部门 2、集团的上级机构不能是单位、部门


            if (StringUtils.isNotBlank(organizationVO.getManagerEmployeeNumber())) {
                boolean bool = userArchiveVosMem.stream().anyMatch(a -> organizationVO.getManagerEmployeeNumber().equals(a.getEmployeeNumber()));
                if (!bool) {
                    organizationVO.setCheckResult(false);
                    resultMsg.append("部门负责人不存在 | ");
                } else {
                    if (StringUtils.isNotBlank(organizationVO.getOrgManagerName())) {
                        boolean bool2 = userArchiveVosMem.stream().anyMatch(a -> organizationVO.getOrgManagerName().equals(a.getUserName()));
                        if (!bool2) {
                            organizationVO.setCheckResult(false);
                            resultMsg.append("部门负责人名字不一致 | ");
                        }
                    }
                }
            }
            //根据上级机构编码查询数据库 判断上级机构是否存在
            if (StringUtils.isNotBlank(organizationVO.getOrgParentCode())) {
                //先判断上级机构在表中是否存在，只需要再缓存中验证就好
                String orgName = excelOrgNameMap.get(organizationVO.getOrgParentCode());
                if (StringUtils.isNotBlank(orgName)) {
                    //检查excel中的父级机构名称是否一致
                    if (!orgName.equals(organizationVO.getOrgParentName())) {
                        organizationVO.setCheckResult(false);
                        resultMsg.append("上级机构名称在excel中不匹配 | ");
                    }
                } else {
                    //上级编码在excel中不存在，那就去缓存中查
                    boolean bool = organizationVOListMem.stream().anyMatch(a -> organizationVO.getOrgParentCode().equals(a.getOrgCode()));
                    if (bool) {
                        if (StringUtils.isNotBlank(organizationVO.getOrgName())) {
                            if (!organizationVOListMem.stream().anyMatch(a -> organizationVO.getOrgParentName().equals(a.getOrgName()))) {
                                organizationVO.setCheckResult(false);
                                resultMsg.append("上级机构名称在数据库中不匹配 | ");
                            }
                        }
                    } else {
                        organizationVO.setCheckResult(false);
                        resultMsg.append("上级机构编码为[" + organizationVO.getOrgParentCode() + "]的机构不存在 | ");

                    }
                }
            }
            if (resultMsg.length() > 2) {
                resultMsg.deleteCharAt(resultMsg.length() - 2);
            }
            organizationVO.setResultMsg(resultMsg.toString());
            if (!organizationVO.isCheckResult()) {
                checkList.add(organizationVO);
            }
        }
        orgDictListMem = null;
        userArchiveVosMem = null;
        organizationVOListMem = null;
        excelOrgNameMap = null;
        return checkList;
    }


    //=====================================================================

    /**
     * 递归修改机构全称
     */
    private void recursiveUpdateOrgFullName(OrganizationVO parent) {

        List<OrganizationVO> childOrgList = organizationDao.listSonFullNameAndType(parent.getOrgId());
        for (OrganizationVO org : childOrgList) {
            System.out.println(parent.getOrgParentId() + ":" + parent.getOrgName() + "-" + parent.getOrgId());
            //只要上级不是顶级
            if (0 == parent.getOrgParentId()) {

            } /*else if ("UNIT".equals(organizationVO.getOrgType())) {
                org.setOrgFullName(org.getOrgName());
            }*/ else {
                org.setOrgFullName(parent.getOrgFullName() + "/" + org.getOrgName());
            }
            organizationDao.updateByPrimaryKey(org);

        }
        for (OrganizationVO org : childOrgList) {
            recursiveUpdateOrgFullName(org);
        }
    }


    //=====================================================================
    @Transactional
    @Override
    @OrganizationDeleteAnno
/**
 * 删除机构时，如果该机构以及其下级机构存在人员则无法删除
 * 如果机构以及其下级机构没有人员，但有岗位时，则给出提示“机构下含有岗位，请确认是否一并删除这些岗位”
 */
    public void deleteOrganizationById(List<Integer> orgIds, boolean cascadeDeletePost, UserSession userSession) {
        List<Integer> idList = new ArrayList<>();
        if (CollectionUtils.isEmpty(orgIds)) {
            return;
        }
        for (int i = orgIds.size() - 1; i >= 0; i--) {
            boolean b = ensureRight(orgIds.get(i), userSession.getArchiveId(), new Date());
            if (b) {
                idList.add(orgIds.get(i));
            }
        }
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        /*          //去重
        idList = MyCollectionUtil.removeDuplicate(idList);*/
        //再遍历机构id列表，通过每一个机构id来查询人员档案表等表是否存在相关记录
        //TODO 人事异动表、工资、考勤暂时不考虑
        boolean isExsit = false;
        List<UserArchiveVo> userArchiveVos = userArchiveDao.getUserArchiveList(idList, false, null, null);
        if (!CollectionUtils.isEmpty(userArchiveVos)) {
            isExsit = true;
            ExceptionCast.cast(CommonCode.EXIST_USER);
        }
        //如果所有机构下都不存在相关人员资料
        if (!isExsit) {
            //判断是否存在岗位
            List<Post> posts = postDao.listPostsByOrgIds(idList, null, null);
            if (!CollectionUtils.isEmpty(posts) && !cascadeDeletePost) {
                ExceptionCast.cast(CommonCode.ORG_HAVE_POST);
            } else {
                //物理删除机构表
                organizationDao.batchDeleteOrganization(idList);
                //逻辑删除岗位表
                postDao.batchDeletePosts(idList);
            }
        }
        // 回收机构权限
        apiAuthService.deleteOrg(idList, userSession.getArchiveId());
    }

    //=====================================================================
    @Override
    public void sealOrganization(Integer archiveId, List<Integer> orgIds, Short isEnable) {

        //查询用户有权的机构
        List<Integer> idList = new ArrayList<>();
        for (int i = orgIds.size() - 1; i >= 0; i--) {

            boolean b = ensureRight(orgIds.get(i), archiveId, new Date());
            if (b) {
                idList.add(orgIds.get(i));
            }
        }
        if (!CollectionUtils.isEmpty(idList)) {
            organizationDao.updateEnable(idList, isEnable);
        }
    }


    //=====================================================================

    @Override
    public void mergeOrganization(OrganizationMergeBO orgMergeBO, UserSession userSession) {

        List<Integer> orgIds = orgMergeBO.getOrgIds();
        Integer parentOrgId = orgMergeBO.getParentOrgId();
        String newOrgName = orgMergeBO.getNewOrgName();
        //查询机构列表
        List<OrganizationVO> organizationVOList = organizationDao.listOrgnizationByIds(orgIds);

        if (!CollectionUtils.isEmpty(organizationVOList)) {

            //判断是否是同一个父级下的
            Set<Integer> orgParentIds = organizationVOList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (orgParentIds.size() != 1) {
                ExceptionCast.cast(CommonCode.NOT_SAME_LEVEL_EXCEPTION);
            }
            //根据归属机构构建新的机构实体
            OrganizationVO newOrgVO = new OrganizationVO();
            //查询父级机构，用来生成机构全称
            OrganizationVO parentOrg = organizationDao.getOrganizationById(parentOrgId);
            //查询同级机构用来生成机构sortid、orgCode
            List<OrganizationVO> sonOrgs = organizationDao.listSonOrganization(parentOrgId);
            //不能重名
            boolean bool = sonOrgs.stream().anyMatch(a -> newOrgName.equals(a.getOrgName()));
            if (bool) {
                ExceptionCast.cast(CommonCode.NAME_ALREADY_USED_AT_SAME_LEVEL);
            }


            Integer sortId = generateOrgSortId(sonOrgs);
            String orgCode = generateOrgCode(parentOrgId);
            newOrgVO.setOrgCode(orgCode);
            newOrgVO.setSortId(sortId);
            newOrgVO.setOrgName(newOrgName);
            newOrgVO.setCreateTime(new Date());
            newOrgVO.setOperatorId(userSession.getArchiveId());
            newOrgVO.setCompanyId(userSession.getCompanyId());
            newOrgVO.setOrgParentId(parentOrgId);
            newOrgVO.setIsEnable(Short.parseShort("1"));
            //TODO 新机构的类型如何设置
            newOrgVO.setOrgType(organizationVOList.get(0).getOrgType());
            newOrgVO.setOrgFullName(parentOrg.getOrgFullName() + "/" + newOrgVO.getOrgName());
            organizationDao.insert(newOrgVO);

            //TODO 将老机构下的人员迁移至新机构
            //只改变档案的机构id即可，新机构的单位id和老机构一样
            int i = userArchiveDao.moveToNewOrg(orgIds, newOrgVO.getOrgId());


            //给合并后的新机构授权
            apiAuthService.mergeOrg(orgIds, newOrgVO.getOrgId(), userSession.getArchiveId());
            //doTransfer(organizationVOList, newOrganizationVO);
            organizationDao.batchDeleteOrganization(orgIds);
        }
    }


    //=====================================================================

    /**
     * 同一级机构自由排序
     *
     * @param orgIds
     * @return
     */
    @Override
    public void sortOrganization(LinkedList<Integer> orgIds) {
        //查询出机构列表
        List<OrganizationVO> organizationList = organizationDao.listOrgnizationByIds(orgIds);
        Set<Integer> parentOrgSet = new HashSet<>();
        for (OrganizationVO organizationVO : organizationList) {
            //将父机构id存储在set中
            parentOrgSet.add(organizationVO.getOrgParentId());
        }
        //判断是否在同一级机构下
        if (parentOrgSet.size() > 1) {
            ExceptionCast.cast(CommonCode.NOT_SAME_LEVEL_EXCEPTION);
        }
        organizationDao.sortOrganization(orgIds);
    }

    //=====================================================================
    @Override
    @OrganizationTransferAnno
    public void transferOrganization(OrganizationTransferBO orgTransferBO, UserSession userSession) {
        List<OrganizationVO> ready2TransferOrgs = null;
        Integer targetOrgId = orgTransferBO.getTargetOrgId();
        List<Integer> orgIds = orgTransferBO.getOrgIds();

        if (!CollectionUtils.isEmpty(orgTransferBO.getOrgIds())) {
            ready2TransferOrgs = organizationDao.listOrgnizationByIds(orgIds);
            //判断重名
            for (OrganizationVO org : ready2TransferOrgs) {
                judgeDuplicateName(String.valueOf(org.getOrgId()), org.getOrgName(), orgTransferBO.getTargetOrgId());
            }

        } else {
            //待划转机构不存在
            ExceptionCast.cast(CommonCode.ORIGIN_NOT_EXIST);
        }
        //如果待划转机构已经在目标机构下，不允许重复划转
        boolean bool = ready2TransferOrgs.stream().anyMatch(org -> null != org.getOrgParentId() && org.getOrgParentId().equals(targetOrgId));
        if (bool) {
            //请勿重复操作
            ExceptionCast.cast(CommonCode.TRANSFER_REPET_OPERATION);
        }
        //判断是否是同一个父级下的，不是同一个父级下的待划转机构不允许划转
        if (!CollectionUtils.isEmpty(ready2TransferOrgs)) {
            //如果查到不同的父级id，说明不再同层级下
            Set<Integer> OrgParentIds = ready2TransferOrgs.stream().map(org -> org.getOrgParentId()).collect(Collectors.toSet());
            if (OrgParentIds.size() != 1) {
                ExceptionCast.cast(CommonCode.NOT_SAME_LEVEL_EXCEPTION);
            }
            OrganizationVO targetOrg = organizationDao.getOrganizationById(targetOrgId);
            if (Objects.isNull(targetOrg)) {
                ExceptionCast.cast(CommonCode.TARGET_NOT_EXIST);
            }
            //注释1：如果带划转机构的单位id(取其中一个就可以了，为了提升性能)和目标机构单位id不一致  则修改划转机构下人员档案的单位id
            Integer sourceBusunessUnitId = getBusunessUnitIdByOrgId(orgIds.get(0));
            Integer targetBusunessUnitId = getBusunessUnitIdByOrgId(targetOrgId);
            //Integer不能使用==来判断
            if (!sourceBusunessUnitId.equals(targetBusunessUnitId)) {
                userArchiveDao.batchUpdateBusunessUnitId(orgIds, targetBusunessUnitId);
            }
            //!!!  划重点，doTransfer必须要在注释1代码执行之后
            doTransfer(ready2TransferOrgs, targetOrg);
        }
        //没必要再分配权限了，因为划转的机构，机构id不变
        //TODO apiAuthService.transferOrg(orgIds, targetOrgId, userSession.getArchiveId());
    }

    //=====================================================================
    @Override
    public List<OrganizationVO> getOrganizationPostTree(UserSession userSession, Short isEnable) {
        //T拿到未被封存的机构树
        List<OrganizationVO> organizationVOTreeList = getAllOrganizationTree(userSession.getArchiveId(), Short.parseShort("1"));
        //递归设置机构下的岗位
        //获取企业下所有的岗位作为缓存
        List<Post> posts = postDao.listPostsByCompanyIdAndEnable(userSession.getCompanyId(), isEnable);
        //TODO 暂时不设置岗位下的子岗位
        handlerOrganizationPost(posts, organizationVOTreeList, userSession, isEnable);
        return organizationVOTreeList;
    }

    //=====================================================================
    public void handlerOrganizationPost(List<Post> posts, List<OrganizationVO> orgList, UserSession userSession, Short isEnable) {
        for (OrganizationVO organizationVO : orgList) {
            List<Post> postList = posts.stream().filter(a -> {
                if (a.getOrgId().equals(organizationVO.getOrgId())) {
                    return true;
                }
                return false;
            }).sorted(Comparator.comparing(Post::getSortId)).collect(Collectors.toList());
            organizationVO.setPostList(postList);
            if (organizationVO.getChildList() != null && organizationVO.getChildList().size() > 0) {
                handlerOrganizationPost(posts, organizationVO.getChildList(), userSession, isEnable);
            }
        }
    }


    @Override
    public OrganizationVO selectByPrimaryKey(Integer orgId) {
        return organizationDao.getOrganizationById(orgId);
    }

    @Override
    public List<OrganizationVO> getOrganizationListByParentOrgId(Integer orgId) {
        return organizationDao.listSonOrganization(orgId);
    }

    //=====================================================================

    /**
     * 获取所有的机构树
     *
     * @param archiveId
     * @param isEnable
     * @return
     */
    @Override
    public List<OrganizationVO> getAllOrganizationTree(Integer archiveId, Short isEnable) {
        //拿到所有未被封存的机构
        List<OrganizationVO> organizationVOList = organizationDao.listAllOrganizationByArchiveId(archiveId, isEnable, new Date());
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


    //=====================================================================

    /**
     * 执行机构划转
     *
     * @param targetOrg
     */
    private void doTransfer(List<OrganizationVO> originOrgList, OrganizationVO targetOrg) {
        String orgType = "DEPT";
        if (targetOrg.getOrgType().equalsIgnoreCase("GROUP")) {
            orgType = "UNIT";
        } else if (targetOrg.getOrgType().equalsIgnoreCase("UNIT")) {
            orgType = "DEPT";
        } else {
            orgType = "DEPT";
        }
        List<OrganizationVO> listSonOrganization = organizationDao.listSonOrganization(targetOrg.getOrgId());
        Integer sortId = generateOrgSortId(listSonOrganization);
        String orgCode = generateOrgCode(targetOrg.getOrgId());
        for (OrganizationVO originOrg : originOrgList) {
            sortId = sortId + 1000;
            originOrg.setSortId(sortId);
            originOrg.setOrgCode(orgCode);
            orgCode = culOrgCode(orgCode);
            originOrg.setOrgType(orgType);
            originOrg.setOrgParentId(targetOrg.getOrgId());
            originOrg.setOrgFullName(targetOrg.getOrgFullName() + "/" + originOrg.getOrgName());
            organizationDao.updateOrganization(originOrg);
            List<OrganizationVO> secondOriginOrgList = organizationDao.listSonOrganization(originOrg.getOrgId());
            //递归
            if (!CollectionUtils.isEmpty(secondOriginOrgList)) {
                doTransfer(secondOriginOrgList, originOrg);
            }
        }
    }

    //=====================================================================

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
    //=====================================================================

    /**
     * 处理所有机构以图形结构展示
     *
     * @param allOrg
     * @param topOrgsList
     */
    private void handlerOrganizationToGraphics(List<OrganizationVO> allOrg, List<OrganizationVO> topOrgsList, boolean isContainsCompiler, boolean isContainsActualMembers) {
        for (OrganizationVO org : topOrgsList) {
            Integer orgId = org.getOrgId();
            //设置实有人数
            if (isContainsActualMembers) {
                //TODO  org.setStaffNumbers(userArchiveDao.countUserArchiveByOrgId(orgId));
                org.setStaffNumbers(100);
            }
            //TODO 设置编制人数,先写死
            if (isContainsCompiler) {
                org.setPlanNumbers(120);
            }
            List<OrganizationVO> childList = allOrg.stream().filter(organization -> {
                Integer orgParentId = organization.getOrgParentId();
                if (orgParentId != null && orgParentId >= 0) {
                    return orgParentId.equals(orgId);
                }
                return false;
            }).collect(Collectors.toList());
            //判断是否还有子级
            if (childList != null && childList.size() > 0) {
                org.setChildList(childList);
                allOrg.removeAll(childList);
                handlerOrganizationToGraphics(allOrg, childList, isContainsCompiler, isContainsActualMembers);
            }
        }
    }

    /**
     * 判断是否有操作权限
     *
     * @return
     */
    private boolean ensureRight(Integer orgId, Integer archiveId, Date now) {
        int result = organizationDao.ensureRight(orgId, archiveId, now);
        return result > 0 ? true : false;
    }

}


