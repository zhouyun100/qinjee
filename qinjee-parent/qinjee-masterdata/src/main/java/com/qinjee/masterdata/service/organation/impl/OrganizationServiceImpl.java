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
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.check.OrganizationCheckVo;
import com.qinjee.masterdata.model.vo.organization.page.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.query.QueryField;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.auth.ApiAuthService;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.UserRoleService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.masterdata.utils.pexcel.ExcelImportUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
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
import org.springframework.util.LinkedMultiValueMap;
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

    //=====================================================================
    @Override
    public PageResult<OrganizationVO> getOrganizationPageTree(UserSession userSession, Short isEnable) {
        Integer archiveId = userSession.getArchiveId();
        List<OrganizationVO> allOrgsList = organizationDao.getAllOrganizationByArchiveId(archiveId, isEnable, new Date());
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

        List<OrganizationVO> organizationVOList = organizationDao.getDirectOrganizationList(organizationPageVo, sortFieldStr, archiveId, new Date());
        PageInfo<OrganizationVO> pageInfo = new PageInfo<>(organizationVOList);
        PageResult<OrganizationVO> pageResult = new PageResult<>(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    //=====================================================================
    @Transactional
    @Override
    @OrganizationSaveAnno
    public ResponseResult addOrganization(String orgName, String orgType, String parentOrgId, String orgManagerId, UserSession userSession) {
        //根据父级机构id查询一些基础信息，构建Organization对象
        OrganizationVO orgBean = initOrganization(Integer.parseInt(parentOrgId));
        String full_name;
        if (orgBean.getOrgFullName() != null) {
            full_name = orgBean.getOrgFullName() + "/" + orgName;
        } else {
            full_name = orgName;
        }
        orgBean.setOrgParentId(Integer.parseInt(parentOrgId));
        orgBean.setOrgType(orgType);
        orgBean.setOrgName(orgName);
        if (orgManagerId != null && !orgManagerId.equals("")) {
            orgBean.setOrgManagerId(Integer.parseInt(orgManagerId));
        }
        orgBean.setOrgFullName(full_name);
        orgBean.setCreateTime(new Date());
        orgBean.setOperatorId(userSession.getArchiveId());
        orgBean.setIsEnable((short) 1);
        //设置企业id
        orgBean.setCompanyId(userSession.getCompanyId());
        int i = organizationDao.insertSelective(orgBean);

        //维护机构与角色
        apiAuthService.addOrg(orgBean.getOrgId(), Integer.parseInt(parentOrgId), userSession.getArchiveId());
        //TODO 是否需要 维护机构与用户的关系

        ResponseResult responseResult;
        if (i != 0) {
            responseResult = new ResponseResult(CommonCode.SUCCESS);
        } else {
            responseResult = new ResponseResult(CommonCode.FAIL);
        }
        responseResult.setResult(orgBean);
        return responseResult;
    }

    //=====================================================================
    private OrganizationVO initOrganization(Integer orgParentId) {
        OrganizationVO orgBean = new OrganizationVO();
        if (orgParentId > 0) {
            //查询父级机构
            OrganizationVO parentOrg = organizationDao.selectByPrimaryKey(orgParentId);
            //查询最新的同级机构
            List<OrganizationVO> brotherOrgList = organizationDao.getOrganizationListByParentOrgId(orgParentId);
            //如果没有同级机构，则当前机构为parentOrg下第一个子机构
            String orgCode;
            Integer sortId;
            if (CollectionUtils.isEmpty(brotherOrgList)) {
                orgCode = parentOrg.getOrgCode() + "01";
                sortId = 1000;
            } else {
                sortId = brotherOrgList.get(0).getSortId() + 100;
                orgCode = culOrgCode(brotherOrgList.get(0).getOrgCode());
            }
            orgBean.setSortId(sortId);
            orgBean.setOrgCode(orgCode);
            orgBean.setOrgFullName(parentOrg.getOrgFullName());
            return orgBean;

            //如果是0，就是顶级机构
        } else {
            orgBean.setSortId(1000);
            orgBean.setOrgCode("1");
            return orgBean;
        }
    }
//=====================================================================

    /**
     * 获取新编码和sortId的机构
     *
     * @param orgParentId
     * @return
     */
    private OrganizationVO getNewOrgCode(Integer orgParentId) {
        OrganizationVO orgBean = new OrganizationVO();
        if (orgParentId > 0) {
            //查询父级机构
            OrganizationVO parentOrg = organizationDao.selectByPrimaryKey(orgParentId);
            //查询最新的同级机构
            List<OrganizationVO> brotherOrgList = organizationDao.getOrganizationListByParentOrgId(orgParentId);
            //如果没有同级机构，则当前机构为parentOrg下第一个子机构
            String orgCode;
            Integer sortId;
            if (CollectionUtils.isEmpty(brotherOrgList)) {
                orgCode = parentOrg.getOrgCode() + "01";
                sortId = 1000;
            } else {
                sortId = brotherOrgList.get(0).getSortId() + 100;
                orgCode = culOrgCode(brotherOrgList.get(0).getOrgCode());
            }
            BeanUtils.copyProperties(parentOrg, orgBean);
            orgBean.setOrgParentId(parentOrg.getOrgId());
            orgBean.setSortId(sortId);
            orgBean.setOrgCode(orgCode);
            return orgBean;
        } else {
            orgBean.setSortId(1000);
            orgBean.setOrgCode("1");
            return orgBean;
        }
    }

//=====================================================================

    /**
     * 获取新orgCode
     *
     * @param orgCode
     * @return
     */
    private String culOrgCode(String orgCode) {
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
    public ResponseResult editOrganization(String orgCode, String orgId, String orgName, String orgType, String parentOrgId, String orgManagerId, UserSession userSession) {
        ResponseResult responseResult;
        //反查organizationVO
        OrganizationVO organization = organizationDao.selectByPrimaryKey(Integer.parseInt(orgId));
        OrganizationVO parentOrganization = organizationDao.selectByPrimaryKey(Integer.parseInt(parentOrgId));

        //判断机构编码是否唯一
        if (!organization.getOrgCode().equals(orgCode)) {
            OrganizationVO orgBean = organizationDao.getOrganizationByOrgCodeAndCompanyId(orgCode, userSession.getCompanyId());
            if (orgBean != null && !orgId.equals(orgBean.getOrgId())) {
                //机构编码在同一企业下不唯一
                responseResult = new ResponseResult(CommonCode.FAIL);
                responseResult.setMessage("机构编码不唯一，更新失败");
                return responseResult;
            }
        }

        //修改子机构编码


        String newOrgFullName;
        if (Objects.nonNull(parentOrganization)) {
            newOrgFullName = parentOrganization.getOrgFullName() + "/" + orgName;
        } else {
            newOrgFullName = orgName;
        }
        //TODO 是否可以修改父机构id，如果修改则 机构 子机构编码 排序id都需要改变
        organization.setOrgParentId(Integer.parseInt(parentOrgId));
        organization.setOrgManagerId(Integer.parseInt(orgManagerId));
        organization.setOrgType(orgType);
        organization.setOrgName(orgName);
        organization.setOrgFullName(newOrgFullName);
        int result = organizationDao.updateByPrimaryKey(organization);

        //递归修改子机构的全称
        recursiveUpdateOrgNameByParentOrgId(newOrgFullName, orgId);

        return result == 1 ? new ResponseResult() : new ResponseResult(CommonCode.FAIL);
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
        List<Integer> orgidList = new ArrayList<>();
        //拿到关联的所有机构id
        List<Integer> orgIdList = null;
        if (layer < 1) {
            layer = 2;
        }
        orgIdList = getOrgIdList(userSession, orgId, (layer - 1), isEnable);
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
    public List<OrganizationVO> exportOrganization(Integer orgId, List<Integer> orgIds, UserSession userSession) {
        List<OrganizationVO> orgList = null;
        if (CollectionUtils.isEmpty(orgIds)) {
            List<Integer> orgIdList = getOrgIdList(userSession, orgId, null, Short.parseShort("1"));
            orgList = organizationDao.getAllOrganizationByArchiveIdAndOrgId(orgIdList, userSession.getArchiveId(), Short.parseShort("0"), new Date());
        } else {
            orgList = organizationDao.getOrganizationsByOrgIds(orgIds);
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
        //拿到关联的所有机构id
        List<Integer> orgIdList = null;
        Short isEnable = organizationPageVo.getIsEnable();
        Integer orgId = organizationPageVo.getOrgParentId();
        orgIdList = getOrgIdList(userSession, orgId, null, isEnable);
        if (organizationPageVo.getCurrentPage() != null && organizationPageVo.getPageSize() != null) {
            PageHelper.startPage(organizationPageVo.getCurrentPage(), organizationPageVo.getPageSize());
        }
        List<OrganizationVO> organizationVOList = organizationDao.getOrganizationsByOrgIds(orgIdList);
        PageInfo<OrganizationVO> pageInfo = new PageInfo<>(organizationVOList);
        PageResult<OrganizationVO> pageResult = new PageResult<>(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    /**
     * 入库
     * @param redisKey
     * @param userSession
     * @return
     */
    @Override
    public ResponseResult importToDatabase(String orgExcelRedisKey, UserSession userSession) {
       String data = redisService.get(orgExcelRedisKey);
       //将其转为对象集合
        List<OrganizationVO> list= JSONArray.parseArray(data,OrganizationVO.class);

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
                    return Long.compare(Long.parseLong(org1.getOrgCode()), Long.parseLong(org2.getOrgCode()));
                }
            });
            int sortId = 1000;
            for (OrganizationVO vo : orgLost) {
                OrganizationVO ifExistVo = organizationDao.getOrganizationByOrgCodeAndCompanyId(vo.getOrgCode(), userSession.getCompanyId());
                OrganizationVO parentOrg = organizationDao.getOrganizationByOrgCodeAndCompanyId(vo.getOrgParentCode(), userSession.getCompanyId());
                //已存在 则更新
                if (Objects.nonNull(ifExistVo)) {
                    organizationDao.updateByPrimaryKeySelective(vo);
                } else {
                    vo.setOperatorId(userSession.getArchiveId());
                    vo.setCompanyId(userSession.getCompanyId());
                    vo.setSortId(sortId);
                    sortId += 1000;
                    //TODO 根据部门负责人编号  查询档案id
                    vo.setOrgManagerId(userSession.getArchiveId());
                    //查询父机构的全称  设置全称
                    if (Objects.nonNull(parentOrg)) {
                        vo.setOrgParentId(parentOrg.getOrgId());
                        vo.setOrgFullName(parentOrg.getOrgFullName() + "/" + vo.getOrgName());
                    } else {
                        vo.setOrgParentId(0);
                        vo.setOrgFullName(vo.getOrgName());
                    }
                    int i = organizationDao.insertSelective(vo);
                    //维护机构与角色
                    //TODO
                    apiAuthService.addOrg(vo.getOrgId(), vo.getOrgParentId(), userSession.getArchiveId());
                }
            }
        }
        return new ResponseResult();
    }


    /**
     * 导入并校验
    * @param multfile
     * @param userSession
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession, HttpServletResponse response) throws Exception {
        ResponseResult responseResult = new ResponseResult(CommonCode.FAIL);
        //判断文件名
        String filename = multfile.getOriginalFilename();
        if(!(filename.endsWith(".xls")||filename.endsWith(".xlsx"))){
            responseResult.setResultCode(CommonCode.FILE_FORMAT_ERROR);
            return responseResult;
        }
        List<Object> objects = ExcelImportUtil.importExcel(multfile.getInputStream(), OrganizationVO.class);
        List<OrganizationVO> orgList = new ArrayList<>();
        //记录行号
        Map<String,Integer> lineNumber=new HashMap<>();
        Integer number=1;
        for (Object object : objects) {
            OrganizationVO vo = (OrganizationVO) object;
            lineNumber.put(vo.getOrgCode(),number++);
            orgList.add(vo);
        }
        orgList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                OrganizationVO org1 = (OrganizationVO) o1;
                OrganizationVO org2 = (OrganizationVO) o2;
                return Long.compare(Long.parseLong(org1.getOrgCode()), Long.parseLong(org2.getOrgCode()));
            }
        });
        //校验
        List<OrganizationCheckVo> checkResultList = checkExcel(lineNumber,orgList);
        //拿到错误校验列表
        List<OrganizationCheckVo> failCheckList = checkResultList.stream().filter(check -> {
            if (!check.getCheckResult()) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //如果为空则校验成功
        if (CollectionUtils.isEmpty(failCheckList)) {
            responseResult.setMessage("文件校验成功");
            responseResult.setResultCode(CommonCode.SUCCESS);
        } else {
            responseResult.setResultCode( CommonCode.FILE_PARSING_EXCEPTION);
        }
        //将orgList存入redis
        String orgExcelRedisKey = "orgExcel" + userSession.getArchiveId();
        redisService.del(orgExcelRedisKey);
        String json = JSON.toJSONString(orgList);
        redisService.setex(orgExcelRedisKey,60*60*2, json);
        //将校验结果与原表格信息返回
        HashMap<Object, Object> resultMap = new HashMap<>();
        //TODO
        List<String> headerList=new ArrayList<>();
        headerList.add("机构编码");
        headerList.add("机构名称");
        headerList.add("机构类型");
        headerList.add("上级机构编码");
        headerList.add("上级机构");
        headerList.add("部门复杂人编号");
        headerList.add("部门复杂人");
        resultMap.put("failCheckList",failCheckList);
        resultMap.put("excelList",orgList);
        resultMap.put("excelHeaderList",headerList);
        responseResult.setResult(resultMap);
        //将redis的key返回
        response.setCharacterEncoding("UTF-8");
        //TODO  加密
        response.setHeader("orgExcelRedisKey", orgExcelRedisKey);
        return responseResult;
    }


    //=====================================================================

    /**
     * 搜集机构下所有子机构的id
     *
     * @param userSession
     * @param orgId
     * @param layer       递归层级
     * @return
     */
    @Override
    public List<Integer> getOrgIdList(UserSession userSession, Integer orgId, Integer layer, Short isEnable) {
        List<Integer> idsList = new ArrayList<>();
        //先查询到所有机构
        List<OrganizationVO> allOrgs = organizationDao.getAllOrganizationByArchiveId(userSession.getArchiveId(), isEnable, new Date());
        //将机构的id和父id存入MultiMap,父id作为key，子id作为value，一对多
        MultiValuedMap<Integer, Integer> multiValuedMap = new HashSetValuedHashMap<>();
        for (OrganizationVO org : allOrgs) {
            multiValuedMap.put(org.getOrgParentId(), org.getOrgId());
        }
        for (Map.Entry<Integer, Integer> entry : multiValuedMap.entries()) {

            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        //根据机构id递归，取出该机构下的所有子机构
        collectOrgIds(multiValuedMap, orgId, idsList, layer);
        return idsList;
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
    private void recursiveUpdateOrgNameByParentOrgId(String parentOrgFullName, String orgId) {

        List<OrganizationVO> childOrgList = organizationDao.getOrganizationListByParentOrgId(Integer.parseInt(orgId));
        for (OrganizationVO org : childOrgList) {
            if (!"".equals(parentOrgFullName)) {
                org.setOrgFullName(parentOrgFullName + "/" + org.getOrgName());
            }
            organizationDao.updateByPrimaryKey(org);
            List<OrganizationVO> childOrgList2 = organizationDao.getOrganizationListByParentOrgId(org.getOrgId());
            if (!CollectionUtils.isEmpty(childOrgList2)) {
                recursiveUpdateOrgNameByParentOrgId(org.getOrgFullName(), String.valueOf(org.getOrgId()));
            }
        }
    }


    //=====================================================================
    @Transactional
    @Override
    @OrganizationDeleteAnno
    public ResponseResult deleteOrganizationById(List<Integer> orgIds, UserSession userSession) {
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
                OrganizationVO organizationVO = new OrganizationVO();
                organizationVO.setOrgId(orgId);
                organizationVO.setOperatorId(userSession.getArchiveId());
                organizationVO.setIsEnable(Short.parseShort("0"));
                organizationDao.updateByPrimaryKey(organizationVO);
                //删除岗位,逻辑删除
                postDao.deleteByOrgId(orgId);
            }

        }
        // 回收机构权限
        apiAuthService.deleteOrg(idList, userSession.getArchiveId());

        return new ResponseResult(CommonCode.SUCCESS);
    }

    //=====================================================================

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

    //=====================================================================
    @Transactional
    @Override
    public ResponseResult sealOrganizationByIds(List<Integer> orgIds, Short isEnable) {
        if (!CollectionUtils.isEmpty(orgIds)) {
            organizationDao.UpdateIsEnableByOrgIds(orgIds, isEnable);
        }
        return new ResponseResult();
    }


    //=====================================================================
    @Transactional
    @Override
    public ResponseResult mergeOrganization(String newOrgName, Integer parentOrgId, List<Integer> orgIds, UserSession userSession) {
        List<OrganizationVO> organizationVOList = null;
        if (!CollectionUtils.isEmpty(orgIds)) {
            //查询机构列表
            organizationVOList = organizationDao.getSingleOrganizationListByOrgIds(orgIds);
        }
        //判断是否是同一个父级下的
        if (!CollectionUtils.isEmpty(organizationVOList)) {
            Set<Integer> OrgParentIds = organizationVOList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (OrgParentIds.size() != 1) {
                //不是
                ResponseResult result = new ResponseResult(CommonCode.FAIL);
                result.setMessage("待合并机构不是同一个父级下的，合并失败");
                return result;
            }
            //根据归属机构构建新的机构实体
            OrganizationVO newOrgVO = getNewOrgCode(parentOrgId);
            newOrgVO.setOrgName(newOrgName);
            newOrgVO.setOrgType(organizationVOList.get(0).getOrgType());
            newOrgVO.setOrgFullName(newOrgVO.getOrgFullName() + "/" + newOrgVO.getOrgName());
            organizationDao.insert(newOrgVO);

            //TODO 调用人员接口，将老机构下的人员迁移至新机构

            //TODO 调用角色接口
            apiAuthService.mergeOrg(orgIds, newOrgVO.getOrgId(), userSession.getArchiveId());


            //refactorOrganization(organizationVOList, newOrganizationVO);
        } else {
            return new ResponseResult(CommonCode.BUSINESS_EXCEPTION);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //=====================================================================
    @Override
    public ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(String userName) {
        //TODO 调用人员的接口

        return null;
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
    public ResponseResult sortOrganization(LinkedList<Integer> orgIds) {
        ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
        List<OrganizationVO> organizationList = organizationDao.getSingleOrganizationListByOrgIds(orgIds);
        Set<Integer> parentOrgSet = new HashSet<>();
        for (OrganizationVO organizationVO : organizationList) {
            parentOrgSet.add(organizationVO.getOrgParentId());
        }
        //判断是否在同一级机构下
        if (parentOrgSet.size() > 1) {
            responseResult.setResultCode(CommonCode.FAIL);
            responseResult.setMessage("机构不在同级下，排序失败");
            return responseResult;
        }
        Integer i = organizationDao.sortOrganization(orgIds);
        return responseResult;
    }

    //=====================================================================
    @Override
    @Transactional
    @OrganizationTransferAnno
    public ResponseResult transferOrganization(List<Integer> orgIds, Integer targetOrgId, UserSession userSession) {
        ResponseResult responseResult = new ResponseResult(CommonCode.FAIL);
        List<OrganizationVO> organizationVOList = null;
        if (!CollectionUtils.isEmpty(orgIds)) {
            organizationVOList = organizationDao.getSingleOrganizationListByOrgIds(orgIds);
        } else {
            responseResult.setMessage("划转失败，待划转机构id为空");
            return responseResult;
        }
        //如果待划转机构已经在目标机构下，不允许重复划转
        for (OrganizationVO org : organizationVOList) {
            if (org.getOrgParentId().equals(targetOrgId)) {
                responseResult.setMessage("划转失败，待划转机构已经在目标机构下，不允许重复划转");
                return responseResult;
            }
        }
        //判断是否是同一个父级下的
        if (!CollectionUtils.isEmpty(organizationVOList)) {
            Set<Integer> OrgParentIds = organizationVOList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if (OrgParentIds.size() != 1) {
                //不是
                responseResult.setMessage("划转失败，不是同一个父级下的");
                return responseResult;
            }
            OrganizationVO parentOrganizationVO = organizationDao.selectByPrimaryKey(targetOrgId);
            if (Objects.isNull(parentOrganizationVO)) {
                responseResult.setMessage("目标机构为空");
                return responseResult;
            }
            refactorOrganization(organizationVOList, parentOrganizationVO);
        }
        apiAuthService.transferOrg(orgIds, targetOrgId, userSession.getArchiveId());

        responseResult.setResultCode(CommonCode.SUCCESS);
        responseResult.setMessage("划转成功");
        return responseResult;
    }

    //=====================================================================
    @Override
    public ResponseResult<List<OrganizationVO>> getOrganizationPostTree(UserSession userSession, Short isEnable) {
        //TODO 只显示未封存的机构
        List<OrganizationVO> organizationVOTreeList = getAllOrganizationTree(userSession, Short.parseShort("1"));
        //递归设置机构下的岗位
        //TODO 暂时不设置岗位下的子岗位
        handlerOrganizationPost(organizationVOTreeList, isEnable);
        return new ResponseResult<>(organizationVOTreeList);
    }

    //=====================================================================
    public void handlerOrganizationPost(List<OrganizationVO> orgList, Short isEnable) {
        for (OrganizationVO organizationVO : orgList) {
            if (organizationVO.getOrgType().equals("DEPT")) {
                List<Post> postList = postDao.getPostListByOrgId(organizationVO.getOrgId(), isEnable);
                organizationVO.setPostList(postList);
            }
            if (organizationVO.getChildList() != null && organizationVO.getChildList().size() > 0) {
                handlerOrganizationPost(organizationVO.getChildList(), isEnable);
            }
        }
    }

    //=====================================================================
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

    //=====================================================================
    @Override
    public ResponseResult downloadOrganizationToExcelByOrgId(String filePath, List<Integer> orgIds, UserSession userSession) {
        ResponseResult responseResult;

        List<OrganizationVO> organizationVOList = organizationDao.getOrganizationsByOrgIds(orgIds);
        System.out.println("organizationVOList:" + organizationVOList);
        if (CollectionUtils.isEmpty(organizationVOList)) {
            responseResult = new ResponseResult(CommonCode.FAIL);
            responseResult.setMessage("机构列表为空，导出失败");
            return responseResult;
        }
        responseResult = new ResponseResult(CommonCode.SUCCESS);
        responseResult.setMessage("导出成功");
        return responseResult;
    }


    //=====================================================================

    /**
     * @param userSession
     * @Description: 导出用户所有机构到excel
     * @Param:
     * @return:
     * @Author: penghs
     * @Date: 2019/11/20 0020
     */
    @Override
    public ResponseResult downloadAllOrganizationToExcel(String filePath, UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        List<OrganizationVO> organizationVOList = organizationDao.getOrganizationListByUserArchiveId(archiveId, new Date());
        if (Objects.isNull(organizationVOList) || organizationVOList.size() <= 0) {
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //=====================================================================
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

//=====================================================================

    /**
     * 获取所有的机构树
     *
     * @param userSession
     * @param isEnable
     * @return
     */
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

    //=====================================================================
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

    //=====================================================================

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

    //=====================================================================

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


//=====================================================================

    /**
     * 重构并更新机构：编码、机构全称、排序id、机构类型、机构父id
     *
     * @param targetOrg
     */
    private void refactorOrganization(List<OrganizationVO> originOrgList, OrganizationVO targetOrg) {
        //判断目标机构是否有子机构，如果有则返回最后一个子机构编码，否则返回自身编码
        List<OrganizationVO> targetChildOrgList = organizationDao.getOrganizationListByParentOrgId(targetOrg.getOrgId());
        String targetOrgCode = "";
        //目标子机构为空或者包含原有子机构，当作初始化处理
        if (CollectionUtils.isEmpty(targetChildOrgList) || targetChildOrgList.containsAll(originOrgList)) {
            targetOrgCode = targetOrg.getOrgCode() + "00";
        } else {
            String ChildOrgCode = targetChildOrgList.get(0).getOrgCode();
            targetOrgCode = targetOrg.getOrgCode() + "00" + ChildOrgCode.substring(ChildOrgCode.length() - 1, ChildOrgCode.length());
        }
        //获取目标机构全称
        String parentFullName = targetOrg.getOrgFullName();
        //待划转的机构类型
        String orgType = "DEPT";
        if (targetOrg.getOrgType().equalsIgnoreCase("GROUP")) {
            orgType = "UNIT";
        } else if (targetOrg.getOrgType().equalsIgnoreCase("UNIT")) {
            orgType = "DEPT";
        } else {
            orgType = "DEPT";
        }
        //截取目标机构编码或其最新子机构编码的最后一位数  加1
        String prefixOrgCode = targetOrgCode.substring(0, targetOrgCode.length() - 1);
        Integer subfixOrgCode = Integer.parseInt(targetOrgCode.substring(targetOrgCode.length() - 1, targetOrgCode.length()));
        //遍历设置划转机构的编码、机构类型、机构全称、父级机构id
        for (OrganizationVO originOrg : originOrgList) {
            String transOrgCode = prefixOrgCode + (++subfixOrgCode);
            originOrg.setOrgCode(transOrgCode);
            originOrg.setOrgType(orgType);
            originOrg.setOrgParentId(targetOrg.getOrgId());
            originOrg.setOrgFullName(parentFullName + "/" + originOrg.getOrgName());
            //sortId
            originOrg.setSortId(subfixOrgCode * 1000);
            organizationDao.updateByPrimaryKeySelective(originOrg);
            List<OrganizationVO> secondOriginOrgList = organizationDao.getOrganizationListByParentOrgId(originOrg.getOrgId());
            //递归
            if (!CollectionUtils.isEmpty(secondOriginOrgList)) {
                refactorOrganization(secondOriginOrgList, originOrg);
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
                handlerOrganizationToGraphics(allOrg, childList,isContainsCompiler,isContainsActualMembers);
            }
        }
    }

    //=====================================================================
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


//=====================================================================

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

    public List<OrganizationCheckVo> checkExcel(Map<String,Integer>lineNumber,List<OrganizationVO> voList) {
        List<OrganizationCheckVo> checkVos = new ArrayList<>();
        int line = 0;
        int groupCount = 0;
        for (OrganizationVO organizationVO : voList) {
            OrganizationCheckVo checkVo = new OrganizationCheckVo();
            checkVo.setCheckResult(true);
            checkVo.setLineNumer(lineNumber.get(organizationVO.getOrgCode()));
            BeanUtils.copyProperties(organizationVO, checkVo);
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
            if (Objects.isNull(organizationVO.getOrgParentCode()) && Objects.nonNull(organizationVO.getOrgType()) && !organizationVO.getOrgType().equals("GROUP")) {
                checkVo.setCheckResult(false);
                resultMsg.append("非集团类型机构的上级机构编码不能为空|");
            }
            if (Objects.isNull(organizationVO.getOrgParentName()) && Objects.nonNull(organizationVO.getOrgType()) && !organizationVO.getOrgType().equals("GROUP")) {
                checkVo.setCheckResult(false);
                resultMsg.append("非集团类型机构的上级机构名称不能为空|");
            }
            if (Objects.nonNull(organizationVO.getOrgType()) && (!(organizationVO.getOrgType().equals("GROUP") || organizationVO.getOrgType().equals("UNIT") || organizationVO.getOrgType().equals("DEPT")))) {
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

            if (organizationVO.getOrgManagerId() != null) {
                //查询部门负责人是否存在,
                //TODO
                if (0 > 1) {
                    checkVo.setCheckResult(false);
                    resultMsg.append("部门负责人不存在|");
                } else {
                    //如果存在则判断部门负责人名字是否一致
                    if (0 > 1) {
                        checkVo.setCheckResult(false);
                        resultMsg.append("部门负责人名字不一致|");
                    }
                }
            }
            //根据上级机构编码查询数据库 判断上级机构是否存在
            //TODO
            if (0 > 1) {
                resultMsg.append("编码为" + organizationVO.getOrgParentCode() + "的上级机构不存在|");
            } else {
                if (0 > 1) {
                    resultMsg.append("上级机构名称不一致|");
                }
            }
            checkVo.setResultMsg(resultMsg);
            checkVos.add(checkVo);
        }
        return checkVos;
    }


}


