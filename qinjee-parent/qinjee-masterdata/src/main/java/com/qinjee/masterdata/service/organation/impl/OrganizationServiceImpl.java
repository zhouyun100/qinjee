package com.qinjee.masterdata.service.organation.impl;

import com.alibaba.fastjson.JSON;
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
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.page.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.query.QueryField;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.auth.ApiAuthService;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.UserRoleService;
import com.qinjee.masterdata.utils.MyCollectionUtil;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.masterdata.utils.pexcel.ExcelImportUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
        String sortFieldStr = "";
        if (!CollectionUtils.isEmpty(organizationPageVo.getQuerFieldVos())) {
            Optional<List<QueryField>> querFieldVos = Optional.of(organizationPageVo.getQuerFieldVos());
            sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Organization.class);
        }

        List<OrganizationVO> organizationVOList = organizationDao.listDirectOrganizationByCondition(organizationPageVo, sortFieldStr, archiveId, new Date());
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
        OrganizationVO orgBean=new OrganizationVO();
        //校验orgCode是否已存在
        OrganizationVO orgByCode = organizationDao.getOrganizationByOrgCodeAndCompanyId(orgCode, userSession.getCompanyId());
        if (Objects.nonNull(orgByCode)) {
            ExceptionCast.cast(CommonCode.CODE_USED);
        }
        //查询父级机构，用来生成机构全称
        OrganizationVO parentOrg = organizationDao.getOrganizationById(Integer.parseInt(parentOrgId));
        //查询同级机构用来生成机构sortid、orgCode
        List<OrganizationVO> brotherOrgList = organizationDao.listSonOrganization(Integer.parseInt(parentOrgId));
        Integer sortId= generateOrgSortId(brotherOrgList);

        if(Objects.isNull(parentOrg)||parentOrg.getOrgType().equalsIgnoreCase("GROUP")){
            orgBean.setOrgFullName(orgName);
        }else{
            orgBean.setOrgFullName(parentOrg.getOrgFullName()+"/"+orgName);
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
        int sortId=1000;
        if(CollectionUtils.isEmpty(brotherOrgList)){
            sortId = 1000;
        }else{
            //取得最大sortId
            int maxSortId = brotherOrgList.stream().mapToInt(OrganizationVO::getSortId).max().getAsInt();
            sortId=maxSortId+1000;
        }
        return sortId;
    }

    @Override
    public String generateOrgCode(Integer parentOrgId) {
        //查询父级机构，用来生成机构全称和机构编码
        OrganizationVO parentOrg = organizationDao.getOrganizationById(parentOrgId);
        //查询同级机构用来生成机构sortid、orgCode
        List<OrganizationVO> brotherOrgList = organizationDao.listSonOrganization(parentOrgId);
        if(Objects.isNull(parentOrg)){
            //TODO 改成递增
            return "1";
        }
        if(CollectionUtils.isEmpty(brotherOrgList)){
            return parentOrg.getOrgCode()+"01";
        }else{
            //先过滤掉机构编码最后两位为非数字的，再筛选最大值
            //TODO 漏洞  如果getOrgCode的总长度小于2，会出现越界异常（正常业务情况不会出现）
            List<OrganizationVO> filterBrotherOrgList = brotherOrgList.stream().filter(o -> StringUtils.isNumeric(o.getOrgCode().substring(o.getOrgCode().length() - 2))).collect(Collectors.toList());
            //根据机构编码排序，并且只取最后两位位数字的
            Comparator comparator = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    String orgCode1=String.valueOf(o1);
                    String orgCode2=String.valueOf(o1);
                    String orgCode1Num = orgCode1.substring(orgCode1.length() - 2);
                    String orgCode2Num = orgCode1.substring(orgCode2.length() - 2);
                    boolean isNum= StringUtils.isNumeric(orgCode1Num)&&StringUtils.isNumeric(orgCode2Num);
                    if (isNum&&orgCode1.length() > orgCode2.length()) {
                        return -1;
                    } else if (isNum&&orgCode1.length() < orgCode2.length()) {
                        return 1;
                    }
                    return orgCode2.compareTo(orgCode1);
                }
            };
            String lastOrgCode =filterBrotherOrgList.stream().map(OrganizationVO::getOrgCode).max(comparator).get().toString();
            if(null==lastOrgCode||"".equals(lastOrgCode)){
                return parentOrg.getOrgCode()+"01";
            }
            //计算编码
            String orgCode = culOrgCode(lastOrgCode);
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
        organization.setOrgManagerId(Integer.parseInt(orgManagerId));
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
            List<OrganizationVO> orgLost = entry.getValue();
            orgLost.sort(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    OrganizationVO org1 = (OrganizationVO) o1;
                    OrganizationVO org2 = (OrganizationVO) o2;
                    return org1.getOrgCode().compareTo(org2.getOrgCode());
                    //return Long.compare(Long.parseLong(org1.getOrgCode()), Long.parseLong(org2.getOrgCode()));
                }
            });
            int sortId = 1000;
            for (OrganizationVO vo : orgLost) {
                OrganizationVO ifExistVo = organizationDao.getOrganizationByOrgCodeAndCompanyId(vo.getOrgCode(), userSession.getCompanyId());
                OrganizationVO parentOrg = organizationDao.getOrganizationByOrgCodeAndCompanyId(vo.getOrgParentCode(), userSession.getCompanyId());
                //转换机构类型
                if ("集团".equalsIgnoreCase(vo.getOrgType())) {
                    vo.setOrgType("GROUP");
                } else if ("单位".equalsIgnoreCase(vo.getOrgType())) {
                    vo.setOrgType("UNIT");
                } else if ("部门".equalsIgnoreCase(vo.getOrgType())) {
                    vo.setOrgType("DEPT");
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
                    if(Objects.isNull(parentOrg)){
                        vo.setOrgParentId(0);
                        vo.setOrgFullName(vo.getOrgName());
                    }else {
                        vo.setOrgParentId(parentOrg.getOrgId());
                        vo.setOrgFullName(parentOrg.getOrgFullName() + "/" + vo.getOrgName());
                        if(parentOrg.getOrgType().equalsIgnoreCase("GROUP")){
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
        ResponseResult responseResult = new ResponseResult();
        //将校验结果与原表格信息返回
        HashMap<Object, Object> resultMap = new HashMap<>();
        //判断文件名
        String filename = multfile.getOriginalFilename();
        if (!(filename.endsWith(xls) || filename.endsWith(xlsx))) {
            //文件格式不对
            ExceptionCast.cast(CommonCode.FILE_FORMAT_ERROR);
        }
        //导入excel
        List<Object> excelDataList = ExcelImportUtil.importExcel(multfile.getInputStream(), OrganizationVO.class);
        if (CollectionUtils.isEmpty(excelDataList)) {
            //excel为空
            ExceptionCast.cast(CommonCode.FILE_EMPTY);
        }
        List<OrganizationVO> orgList = new ArrayList<>();
        //记录行号
        Integer number = 1;
        for (Object row : excelDataList) {
            OrganizationVO org = (OrganizationVO) row;
            org.setLineNumber(++number);
            //排序前记录行号
            orgList.add(org);
        }
        //进行排序
        orgList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                OrganizationVO org1 = (OrganizationVO) o1;
                OrganizationVO org2 = (OrganizationVO) o2;
                if (org1.getOrgCode().length() > org2.getOrgCode().length()) {
                    return 1;
                } else if (org1.getOrgCode().length() < org2.getOrgCode().length()) {
                    return -1;
                }
                return org1.getOrgCode().compareTo(org2.getOrgCode());
                //return Long.compare(Long.parseLong(org1.getOrgCode()), Long.parseLong(org2.getOrgCode()));
            }
        });
        //将排序后的orgList存入redis
        //将orgList存入redis
        String redisKey = "tempOrgData" + String.valueOf(filename.hashCode());
        redisService.del(redisKey);
        String json = JSON.toJSONString(orgList);
        redisService.setex(redisKey, 60 * 60 * 2, json);
        //将原表信息及redis key置入返回对象
        resultMap.put("excelList", orgList);
        resultMap.put("redisKey", redisKey);

        //校验
        List<OrganizationVO> checkResultList = checkExcel(orgList, userSession);

        //过滤出错误校验列表
        List<OrganizationVO> failCheckList = checkResultList.stream().filter(check -> {
            if (!check.getCheckResult()) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //如果不为空则校验成功,将错误信息、原表数据存储到redis后抛出异常
        if (!CollectionUtils.isEmpty(failCheckList)) {
            StringBuilder errorSb = new StringBuilder();
            for (OrganizationVO error : failCheckList) {
                errorSb.append(error.getLineNumber() + "," + error.getResultMsg() + "\n");
            }
            String errorInfoKey = "errorOrgData" + String.valueOf(filename.hashCode());
            redisService.del(errorInfoKey);
            redisService.setex(errorInfoKey, 60 * 60 * 2, errorSb.toString());
            //将错误信息置入返回对象
            resultMap.put("failCheckList", failCheckList);
            resultMap.put("errorInfoKey", errorInfoKey);
            //
            response.setHeader("errorInfoKey", errorInfoKey);
            //文件解析失败
            responseResult.setResultCode(CommonCode.FILE_PARSE_FAILED);
        } else {
            responseResult.setResultCode(CommonCode.SUCCESS);
            responseResult.setMessage("文件校验成功");
        }
        response.setHeader("redisKey", redisKey);
        responseResult.setResult(resultMap);
        return responseResult;
    }

    @Override
    public void cancelImport(String redisKey, String errorInfoKey) {
        redisService.del(redisKey);
        redisService.del(errorInfoKey);
    }



    //=====================================================================


    private List<Integer> getOrgIdList(Integer archiveId, Integer orgId, Integer layer, Short isEnable) {
        List<Integer> idsList = new ArrayList<>();
        OrganizationVO currentOrg = organizationDao.getOrganizationById(orgId);
        if (Objects.isNull(currentOrg)) {
            return idsList;
        }
        //先查询到所有机构
        List<OrganizationVO> allOrgs = organizationDao.listAllOrganizationByArchiveId(archiveId, isEnable, new Date());
        //将机构的id和父id存入MultiMap,父id作为key，子id作为value，一对多
        MultiValuedMap<Integer, Integer> multiValuedMap = new HashSetValuedHashMap<>();
        for (OrganizationVO org : allOrgs) {
            multiValuedMap.put(org.getOrgParentId(), org.getOrgId());
        }
        //根据机构id递归，取出该机构下的所有子机构
        collectOrgIds(multiValuedMap, orgId, idsList, layer);
        return MyCollectionUtil.removeDuplicate(idsList);
    }


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
    public void deleteOrganizationById(List<Integer> orgIds, UserSession userSession) {
        List<Integer> idList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orgIds)) {
            for (Integer orgId : orgIds) {
                //递归至每一层机构
                idList.add(orgId);
                recursiveFindOrgIds(orgId, idList);
            }
        }
        //去重
        idList=MyCollectionUtil.removeDuplicate(idList);
        //再遍历机构id列表，通过每一个机构id来查询人员档案表等表是否存在相关记录
        //TODO 人事异动表、工资、考勤暂时不考虑
        boolean isExsit = false;
        List<UserArchiveVo> userArchiveVos = userArchiveDao.selectByOrgListAndAuth(idList, userSession.getArchiveId(), userSession.getCompanyId());
        if (!CollectionUtils.isEmpty(userArchiveVos)) {
            isExsit = true;
            ExceptionCast.cast(CommonCode.EXIST_USER);
        }
        //如果所有机构下都不存在相关人员资料，则全部删除（包含机构下的岗位）
        if (!isExsit) {
            //物理删除机构表
            organizationDao.batchDeleteOrganization(idList);
            //逻辑删除岗位表
            postDao.batchDelete(idList);
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
    public void sealOrganization(List<Integer> orgIds, Short isEnable) {
        organizationDao.updateEnable(orgIds, isEnable);
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
    public List<UserArchiveVo> getUserArchiveListByUserName(String userName) {
        //调用人员的接口
        List<UserArchiveVo> userArchives = userArchiveDao.selectUserArchiveByName(userName);
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
            sortId=sortId+1000;
            originOrg.setSortId(sortId);
            originOrg.setOrgCode(orgCode);
            orgCode=culOrgCode(orgCode);
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


    public List<OrganizationVO> checkExcel(List<OrganizationVO> voList, UserSession userSession) {
        List<OrganizationVO> checkVos = new ArrayList<>();
        int groupCount = 0;
        for (OrganizationVO organizationVO : voList) {
            OrganizationVO checkVo = new OrganizationVO();
            BeanUtils.copyProperties(organizationVO, checkVo);
            checkVo.setCheckResult(true);
            StringBuilder resultMsg = new StringBuilder();
            //验空
            if (Objects.isNull(organizationVO.getOrgCode())) {
                checkVo.setCheckResult(false);
                resultMsg.append("机构编码不能为空|");
            }
            if (Objects.isNull(organizationVO.getOrgName())) {
                checkVo.setCheckResult(false);
                resultMsg.append("机构名称不能为空|");
            }
            if (Objects.isNull(organizationVO.getOrgType())) {
                checkVo.setCheckResult(false);
                resultMsg.append("机构类型不能为空|");
            }
            if (Objects.isNull(organizationVO.getOrgParentCode()) && Objects.nonNull(organizationVO.getOrgType()) && !organizationVO.getOrgType().equals("集团")) {
                checkVo.setCheckResult(false);
                resultMsg.append("非集团类型机构的上级机构编码不能为空|");
            }
            if (Objects.isNull(organizationVO.getOrgParentName()) && Objects.nonNull(organizationVO.getOrgType()) && !organizationVO.getOrgType().equals("集团")) {
                checkVo.setCheckResult(false);
                resultMsg.append("非集团类型机构的上级机构名称不能为空|");
            }
            if (Objects.nonNull(organizationVO.getOrgType()) && (!(organizationVO.getOrgType().equals("集团") || organizationVO.getOrgType().equals("单位") || organizationVO.getOrgType().equals("部门")))) {
                checkVo.setCheckResult(false);
                resultMsg.append("机构类型“" + organizationVO.getOrgType() + "”不存在|");
            }

            if (Objects.nonNull(organizationVO.getOrgType()) && organizationVO.getOrgType().equals("GROUP")) {
                groupCount++;
            }
            if (groupCount > 1) {
                checkVo.setCheckResult(false);
                resultMsg.append("集团类型机构只能存在一个|");
            }

            //TODO
            if (null != organizationVO.getManagerEmployeeNumber() && !"".equals(organizationVO.getManagerEmployeeNumber())) {
                UserArchiveVo userArchive = userArchiveDao.selectArchiveByNumber(organizationVO.getManagerEmployeeNumber());
                if (Objects.isNull(userArchive)) {
                    checkVo.setCheckResult(false);
                    resultMsg.append("部门负责人不存在|");
                } else {
                    if (null == userArchive.getUserName() || userArchive.getUserName().equals("") || !userArchive.getUserName().equals(organizationVO.getOrgManagerName())) {
                        checkVo.setCheckResult(false);
                        resultMsg.append("部门负责人名字不一致|");
                    }
                }
            }


            //根据上级机构编码查询数据库 判断上级机构是否存在
            if (null != organizationVO.getOrgParentCode() && !"".equals(organizationVO.getOrgParentCode())) {
                OrganizationVO org = organizationDao.getOrganizationByOrgCodeAndCompanyId(organizationVO.getOrgParentCode(), userSession.getCompanyId());
                if (Objects.isNull(org)) {
                    checkVo.setCheckResult(false);
                    resultMsg.append("编码为" + organizationVO.getOrgParentCode() + "的上级机构不存在|");
                } else {
                    if (null == org.getOrgName() || "".equals(org.getOrgName()) || !organizationVO.getOrgParentName().equals(org.getOrgName())) {
                        checkVo.setCheckResult(false);
                        resultMsg.append("上级机构名称不匹配|");
                    }
                }
            }
            checkVo.setResultMsg(resultMsg);
            checkVos.add(checkVo);
        }
        return checkVos;
    }

}


