package com.qinjee.masterdata.service.organation.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.liaochong.myexcel.core.DefaultExcelReader;
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
import com.qinjee.masterdata.model.vo.organization.page.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.auth.ApiAuthService;
import com.qinjee.masterdata.service.organation.AbstractOrganizationHelper;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.UserRoleService;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.MyCollectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月16日 09:12:00
 */
@Service
public class OrganizationServiceImpl extends AbstractOrganizationHelper<OrganizationVO> implements OrganizationService {
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
    public PageResult<OrganizationVO> getDirectOrganizationPageList(OrganizationPageVo organizationPageVo, UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        if (organizationPageVo.getCurrentPage() != null && organizationPageVo.getPageSize() != null) {
            PageHelper.startPage(organizationPageVo.getCurrentPage(), organizationPageVo.getPageSize());
        }
        List<OrganizationVO> organizationVOList = organizationDao.listDirectOrganizationByCondition(organizationPageVo, archiveId, new Date());
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
    @Transactional
    @Override
    @OrganizationSaveAnno
    public OrganizationVO addOrganization(String orgName, String orgCode, String orgType, String parentOrgId, String orgManagerId, UserSession userSession) {
        OrganizationVO orgBean = new OrganizationVO();
        //校验orgCode是否已存在
        OrganizationVO orgByCode = organizationDao.getOrganizationByOrgCodeAndCompanyId(orgCode, userSession.getCompanyId());
        if (Objects.nonNull(orgByCode)) {
            ExceptionCast.cast(CommonCode.CODE_USED);
        }
        //查询父级机构，用来生成机构全称
        OrganizationVO parentOrg = organizationDao.getOrganizationById(Integer.parseInt(parentOrgId));
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
    @Transactional
    @Override
    @OrganizationEditAnno
    //TODO 目前修改机构编码码 下级机构编码不会联动修改
    public void editOrganization(String orgCode, String orgId, String orgName, String orgType, String parentOrgId, String orgManagerId, UserSession userSession) {
        //反查organizationVO
        OrganizationVO organization = organizationDao.getOrganizationById(Integer.parseInt(orgId));
        OrganizationVO parentOrganization = organizationDao.getOrganizationById(Integer.parseInt(parentOrgId));

        //判断机构编码是否唯一
        if (!organization.getOrgCode().equals(orgCode)) {
            OrganizationVO orgBean = organizationDao.getOrganizationByOrgCodeAndCompanyId(orgCode, userSession.getCompanyId());
            if (orgBean != null && !orgId.equals(orgBean.getOrgId())) {
                //机构编码在同一企业下不唯一
                ExceptionCast.cast(CommonCode.CODE_USED);
            }
        }

        String newOrgFullName;
        if (Objects.nonNull(parentOrganization)) {
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
        recursiveUpdateOrgName(newOrgFullName, orgId);
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
        orgIdList = getOrgIdList(userSession.getArchiveId(), orgId, (layer - 1), isEnable);
        //查询所有相关的机构
        List<OrganizationVO> allOrg = organizationDao.getOrganizationGraphics(userSession.getArchiveId(), orgIdList, isEnable, new Date());

        //拿到根节点
        List<OrganizationVO> topOrgsList = allOrg.stream().filter(organization -> {
            if (organization.getOrgId() != null && organization.getOrgId().equals(orgId)) {
                return true;
            } else if (orgId == 0) {//TODO 如果是顶级机构
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
     * @param orgIds
     * @return
     */
    @Override
    public List<OrganizationVO> exportOrganization(Integer orgId, List<Integer> orgIds, Integer archiveId) {
        List<OrganizationVO> orgList = null;
        if (CollectionUtils.isEmpty(orgIds)) {
            List<Integer> orgIdList = getOrgIdList(archiveId, orgId, null, Short.parseShort("1"));
            orgList = organizationDao.listAllOrganizationByArchiveIdAndOrgId(orgIdList, archiveId, Short.parseShort("0"), new Date());
        } else {
            orgList = organizationDao.listOrganizationsByIds2(orgIds);
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
     * @param organizationPageVo
     * @param userSession
     * @return
     */
    @Override
    public PageResult<OrganizationVO> getAllOrganizationPageList(OrganizationPageVo organizationPageVo, UserSession userSession) {
        List<Integer> orgidList = new ArrayList<>();
        PageResult<OrganizationVO> pageResult = null;
        //拿到关联的所有机构id
        List<Integer> orgIdList = null;
        Short isEnable = organizationPageVo.getIsEnable();
        Integer orgId = organizationPageVo.getOrgParentId();
        //如果当前机构为空的话 返回空集合
        orgIdList = getOrgIdList(userSession.getArchiveId(), orgId, null, isEnable);
        if (!CollectionUtils.isEmpty(orgIdList)) {
            if (organizationPageVo.getCurrentPage() != null && organizationPageVo.getPageSize() != null) {
                PageHelper.startPage(organizationPageVo.getCurrentPage(), organizationPageVo.getPageSize());
            }
            List<OrganizationVO> organizationVOList = organizationDao.listOrganizationsByIds2(orgIdList);
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
                Integer orgManagerId = userArchiveDao.selectArchiveIdByNumber(vo.getManagerEmployeeNumber());
                vo.setOrgManagerId(orgManagerId);

                //导入时将“部门”转为"DEPT"
                List<SysDict> orgTypeDic = sysDictService.searchSysDictListByDictType("ORG_TYPE");
                for (SysDict dict : orgTypeDic) {
                    if (null != dict.getDictValue() && dict.getDictValue().equalsIgnoreCase(vo.getOrgType())) {
                        vo.setOrgType(dict.getDictCode());
                    }
                }

                //已存在 则更新
                if (Objects.nonNull(ifExistVo)) {
                    //根据机构编码进行更新
                    organizationDao.updateByOrgCode(vo);
                } else {
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
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession, HttpServletResponse response) throws Exception {
        return doUploadAndCheck(multfile, OrganizationVO.class, userSession, response);

    }


    @Override
    public void cancelImport(String redisKey, String errorInfoKey) {
        redisService.del(redisKey);
        redisService.del(errorInfoKey);
    }


    //=====================================================================


    //=====================================================================

    /**
     * 遍历搜集机构下所有子机构的id
     *
     * @param multiValuedMap
     * @param orgId
     * @param idsList
     */
    private void collectOrgIds(MultiValuedMap<Integer, Integer> multiValuedMap, Integer orgId, List<Integer> idsList, Integer layer) {
        idsList.add(orgId);
        Collection<Integer> sonOrgIds = multiValuedMap.get(orgId);
        for (Integer sonOrgId : sonOrgIds) {

            if (layer != null && layer > 0) {
                idsList.add(sonOrgId);
                if (multiValuedMap.get(sonOrgId).size() > 0 && layer > 0) {
                    collectOrgIds(multiValuedMap, sonOrgId, idsList, layer);
                    layer--;
                }
            } else {
                idsList.add(sonOrgId);
                if (multiValuedMap.get(sonOrgId).size() > 0) {
                    collectOrgIds(multiValuedMap, sonOrgId, idsList, layer);
                }
            }
        }

    }

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
                    boolean bool = organizationVOListMem.stream().anyMatch(a -> organizationVO.getOrgCode().equals(a.getOrgParentCode()));
                    if (bool) {
                        if (StringUtils.isNotBlank(organizationVO.getOrgName())) {
                            if (!organizationVOListMem.stream().anyMatch(a -> organizationVO.getOrgName().equals(a.getOrgParentName()))) {
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
     *
     * @param parentOrgFullName
     * @param orgId
     */
    private void recursiveUpdateOrgName(String parentOrgFullName, String orgId) {

        List<OrganizationVO> childOrgList = organizationDao.listSonOrganization(Integer.parseInt(orgId));
        for (OrganizationVO org : childOrgList) {
            if (!"".equals(parentOrgFullName)) {
                org.setOrgFullName(parentOrgFullName + "/" + org.getOrgName());
            }
            organizationDao.updateByPrimaryKey(org);
            List<OrganizationVO> childOrgList2 = organizationDao.listSonOrganization(org.getOrgId());
            if (!CollectionUtils.isEmpty(childOrgList2)) {
                recursiveUpdateOrgName(org.getOrgFullName(), String.valueOf(org.getOrgId()));
            }
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
        if (!CollectionUtils.isEmpty(orgIds)) {
            for (Integer orgId : orgIds) {
                //递归至每一层机构
                idList.add(orgId);
                recursiveFindOrgIds(orgId, idList);
            }
        }
        //去重
        idList = MyCollectionUtil.removeDuplicate(idList);
        //再遍历机构id列表，通过每一个机构id来查询人员档案表等表是否存在相关记录
        //TODO 人事异动表、工资、考勤暂时不考虑
        boolean isExsit = false;
        List<UserArchiveVo> userArchiveVos = userArchiveDao.selectByOrgListAndAuth(idList, userSession.getArchiveId(), userSession.getCompanyId());
        if (!CollectionUtils.isEmpty(userArchiveVos)) {
            isExsit = true;
            ExceptionCast.cast(CommonCode.EXIST_USER);
        }
        //如果所有机构下都不存在相关人员资料
        if (!isExsit) {
            //判断是否存在岗位
            List<Post> posts = postDao.listPostByOrgIds(idList);
            if (!CollectionUtils.isEmpty(posts) && !cascadeDeletePost) {
                ExceptionCast.cast(CommonCode.ORG_HAVE_POST);
            } else {
                //物理删除机构表
                organizationDao.batchDeleteOrganization(idList);
                //逻辑删除岗位表
                postDao.batchDelete(idList);
            }
        }
        // 回收机构权限
        apiAuthService.deleteOrg(idList, userSession.getArchiveId());
    }

    //=====================================================================

    /**
     * 递归查找机构子id
     *
     * @param orgId
     * @param idList
     */
    public void recursiveFindOrgIds(Integer orgId, List idList) {
        List<OrganizationVO> parentOrgList = organizationDao.listSonOrganization(orgId);
        if (parentOrgList != null && parentOrgList.size() > 0) {
            for (OrganizationVO OrganizationVO : parentOrgList) {
                idList.add(OrganizationVO.getOrgId());
                recursiveFindOrgIds(OrganizationVO.getOrgId(), idList);
            }
        }
    }

    //=====================================================================
    @Override
    public void sealOrganization(Integer archiveId, List<Integer> orgIds, Short isEnable) {
        List<Integer> idList = new ArrayList<>();
        for (Integer orgId : orgIds) {
            List<Integer> orgIdList = getOrgIdList(archiveId, orgId, null, null);
            idList.addAll(orgIdList);
        }
        if (!CollectionUtils.isEmpty(idList)) {
            organizationDao.updateEnable(idList, isEnable);
        } else {
            ExceptionCast.cast(CommonCode.FAIL);
        }
    }


    //=====================================================================
    @Transactional
    @Override
    public void mergeOrganization(String newOrgName, Integer parentOrgId, List<Integer> orgIds, UserSession userSession) {
        //查询机构列表
        List<OrganizationVO> organizationVOList = organizationDao.listOrgnizationByIds(orgIds);
        //判断是否是同一个父级下的
        if (!CollectionUtils.isEmpty(organizationVOList)) {
            Set<Integer> orgParentIds = organizationVOList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (orgParentIds.size() != 1) {
                ExceptionCast.cast(CommonCode.NOT_SAVE_LEVEL_EXCEPTION);
            }
            //根据归属机构构建新的机构实体
            OrganizationVO newOrgVO = new OrganizationVO();
            //查询父级机构，用来生成机构全称
            OrganizationVO parentOrg = organizationDao.getOrganizationById(parentOrgId);
            //查询同级机构用来生成机构sortid、orgCode
            List<OrganizationVO> brotherOrgList = organizationDao.listSonOrganization(parentOrgId);
            Integer sortId = generateOrgSortId(brotherOrgList);
            String orgCode = generateOrgCode(parentOrgId);
            newOrgVO.setOrgCode(orgCode);
            newOrgVO.setSortId(sortId);
            newOrgVO.setOrgName(newOrgName);
            newOrgVO.setCreateTime(new Date());
            newOrgVO.setOperatorId(userSession.getArchiveId());
            newOrgVO.setCompanyId(userSession.getCompanyId());
            newOrgVO.setOrgParentId(parentOrgId);
            newOrgVO.setIsEnable(Short.parseShort("1"));
            newOrgVO.setOrgType(organizationVOList.get(0).getOrgType());
            newOrgVO.setOrgFullName(parentOrg.getOrgFullName() + "/" + newOrgVO.getOrgName());
            organizationDao.insert(newOrgVO);

            //TODO 调用人员接口，将老机构下的人员迁移至新机构

            apiAuthService.mergeOrg(orgIds, newOrgVO.getOrgId(), userSession.getArchiveId());
            //doTransfer(organizationVOList, newOrganizationVO);
            organizationDao.batchDeleteOrganization(orgIds);
        }
    }

    //=====================================================================
    @Override
    public List<UserArchiveVo> getUserArchiveListByUserName(String userName, UserSession userSession) {
        //调用人员的接口
        List<UserArchiveVo> userArchives = userArchiveDao.selectUserArchiveByName(userName, userSession.getCompanyId());
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(userArchives)) {

        }
        return userArchives;
    }

    //=====================================================================

    /**
     * 同一级机构自由排序
     *
     * @param orgIds
     * @return
     */
    @Override
    @Transactional
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
            ExceptionCast.cast(CommonCode.NOT_SAVE_LEVEL_EXCEPTION);
        }
        organizationDao.sortOrganization(orgIds);
    }

    //=====================================================================
    @Override
    @Transactional
    @OrganizationTransferAnno
    public void transferOrganization(List<Integer> orgIds, Integer targetOrgId, UserSession userSession) {
        List<OrganizationVO> organizationVOList = null;
        if (!CollectionUtils.isEmpty(orgIds)) {
            organizationVOList = organizationDao.listOrgnizationByIds(orgIds);
        } else {
            //源对象不存在，带划转机构不存在
            ExceptionCast.cast(CommonCode.ORIGIN_NOT_EXIST);
        }
        //如果待划转机构已经在目标机构下，不允许重复划转
        for (OrganizationVO org : organizationVOList) {
            if (org.getOrgParentId().equals(targetOrgId)) {
                //请勿重复操作
                ExceptionCast.cast(CommonCode.TRANSFER_REPET_OPERATION);
            }
        }
        //判断是否是同一个父级下的
        if (!CollectionUtils.isEmpty(organizationVOList)) {
            Set<Integer> OrgParentIds = organizationVOList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (OrgParentIds.size() != 1) {
                ExceptionCast.cast(CommonCode.NOT_SAVE_LEVEL_EXCEPTION);
            }
            OrganizationVO parentOrganizationVO = organizationDao.getOrganizationById(targetOrgId);
            if (Objects.isNull(parentOrganizationVO)) {
                ExceptionCast.cast(CommonCode.TARGET_NOT_EXIST);
            }
            doTransfer(organizationVOList, parentOrganizationVO);
        }
        apiAuthService.transferOrg(orgIds, targetOrgId, userSession.getArchiveId());
    }

    //=====================================================================
    @Override
    public List<OrganizationVO> getOrganizationPostTree(UserSession userSession, Short isEnable) {
        //T拿到未被封存的机构树
        List<OrganizationVO> organizationVOTreeList = getAllOrganizationTree(userSession.getArchiveId(), Short.parseShort("1"));
        //递归设置机构下的岗位
        //TODO 暂时不设置岗位下的子岗位
        handlerOrganizationPost(organizationVOTreeList, isEnable);
        return organizationVOTreeList;
    }

    //=====================================================================
    public void handlerOrganizationPost(List<OrganizationVO> orgList, Short isEnable) {
        for (OrganizationVO organizationVO : orgList) {
            List<Post> postList = postDao.getPostListByOrgId(organizationVO.getOrgId(), isEnable);
            organizationVO.setPostList(postList);
            if (organizationVO.getChildList() != null && organizationVO.getChildList().size() > 0) {
                handlerOrganizationPost(organizationVO.getChildList(), isEnable);
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
                org.setStaffNumbers(userArchiveDao.countUserArchiveByOrgId(orgId));
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

}


