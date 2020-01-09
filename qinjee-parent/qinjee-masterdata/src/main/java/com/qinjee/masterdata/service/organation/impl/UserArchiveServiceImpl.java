package com.qinjee.masterdata.service.organation.impl;

import com.alibaba.fastjson.JSON;
import com.github.liaochong.myexcel.core.DefaultExcelReader;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.organization.OrganizationController;
import com.qinjee.masterdata.dao.UserCompanyDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.ContractParamDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.dao.userinfo.UserInfoDao;
import com.qinjee.masterdata.dao.userinfo.UserLoginDao;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.entity.UserCompany;
import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.page.UserArchivePageVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.employeenumberrule.IEmployeeNumberRuleService;
import com.qinjee.masterdata.service.organation.UserArchiveService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.MD5Utils;
import com.qinjee.utils.MyCollectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserArchiveServiceImpl extends OrganizationHelper<UserArchiveVo>  implements UserArchiveService {
    private static Logger logger = LogManager.getLogger(UserArchiveServiceImpl.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private RedisClusterService redisService;
    @Autowired
    private UserLoginDao userLoginDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserCompanyDao userCompanyDao;

    @Autowired
    private OrganizationDao organizationDao;


    @Autowired
    private IEmployeeNumberRuleService employeeNumberRuleService;
    @Autowired
    private ContractParamDao contractParamDao;

    private List<Integer> getOrgIdList(Integer archiveId, Integer orgId, Short isEnable) {
        List<Integer> idsList = new ArrayList<>();
        //先查询到所有机构
        List<OrganizationVO> allOrgs = organizationDao.listAllOrganizationByArchiveId(archiveId, isEnable, new Date());
        //将机构的id和父id存入MultiMap,父id作为key，子id作为value，一对多
        MultiValuedMap<Integer, Integer> multiValuedMap = new HashSetValuedHashMap<>();
        for (OrganizationVO org : allOrgs) {
            multiValuedMap.put(org.getOrgParentId(), org.getOrgId());
        }
        //根据机构id递归，取出该机构下的所有子机构
        collectOrgIds(multiValuedMap, orgId, idsList);
        return MyCollectionUtil.removeDuplicate(idsList);
    }

    private void collectOrgIds(MultiValuedMap<Integer, Integer> multiValuedMap, Integer orgId, List<Integer> idsList) {
        idsList.add(orgId);
        Collection<Integer> sonOrgIds = multiValuedMap.get(orgId);
        for (Integer sonOrgId : sonOrgIds) {
            idsList.add(sonOrgId);
            if (multiValuedMap.get(sonOrgId).size() > 0) {
                collectOrgIds(multiValuedMap, sonOrgId, idsList);
            }

        }
    }

    @Override
    public ResponseResult<PageResult<UserArchiveVo>> getUserArchiveList(UserArchivePageVo pageQueryVo, UserSession userSession) {
        List<Integer> orgIdList = getOrgIdList(userSession.getArchiveId(), pageQueryVo.getOrgId(), null);
        if (pageQueryVo.getCurrentPage() != null && pageQueryVo.getPageSize() != null) {
            PageHelper.startPage(pageQueryVo.getCurrentPage(), pageQueryVo.getPageSize());
        }
        List<UserArchiveVo> userArchiveList = userArchiveDao.getUserArchiveList(orgIdList);
        PageInfo<UserArchiveVo> pageInfo = new PageInfo<>(userArchiveList);
        PageResult<UserArchiveVo> pageResult = new PageResult<>(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return new ResponseResult<>(pageResult);
    }


    @Override
    @Transactional
    /**
     * 1.添加账号（如果已存在则不更新）
     * 2.关联企业
     * 3.新增档案（用户在一家企业下只有一份档案）
     */
    public ResponseResult<Integer> addUserArchive(UserArchiveVo userArchiveVo, UserSession userSession) {
        //根据手机号查找账号表，如果存在则不对账号表进行处理
        UserInfo userInfo = userInfoDao.getUserByPhone(userArchiveVo.getPhone());
        if (Objects.isNull(userInfo)) {
            userInfo = new UserInfo();
            userInfo.setPhone(userArchiveVo.getPhone());
            userInfo.setEmail(userArchiveVo.getEmail());
            userInfo.setCreateTime(new Date());
            //密码默认手机号后6位
            String p = StringUtils.substring(userArchiveVo.getPhone(), userArchiveVo.getPhone().length() - 6, userArchiveVo.getPhone().length());
            userInfo.setPassword(MD5Utils.getMd5(p));
            userLoginDao.addUserInfo(userInfo);
        } else {
            int count = userInfoDao.getUserByPhoneAndCompanyId(userArchiveVo.getPhone(), userSession.getCompanyId());
            if (count > 0) {
                ExceptionCast.cast(CommonCode.PHONE_ALREADY_EXIST);
            }
        }

        //维护员工企业关系表
        UserCompany userCompany = new UserCompany();
        userCompany.setCreateTime(new Date());
        userCompany.setUserId(userInfo.getUserId());
        userCompany.setCompanyId(userSession.getCompanyId());
        userCompany.setIsEnable((short) 1);
        userCompanyDao.insertSelective(userCompany);
        UserArchive userArchive = new UserArchive();
        BeanUtils.copyProperties(userArchiveVo, userArchive);

        userArchive.setOperatorId(userSession.getArchiveId());
        userArchive.setCompanyId(userSession.getCompanyId());
        userArchive.setUserId(userInfo.getUserId());
        userArchive.setIsDelete((short) 0);
        userArchiveDao.insertSelective(userArchive);
        try {
            checkEmployeeNumber(userSession,userArchive);
            userArchiveDao.updateByPrimaryKeySelective(userArchive);
        } catch (Exception e) {
            e.printStackTrace();
            //ExceptionCast.cast(CommonCode.FAIL);
        }
        return new ResponseResult(userArchive.getArchiveId());
    }


    /**
     * 删除时不要删除账号信息，只将用户当前企业下的档案信息，以及用户企业关系表的信息
     * @param idsMap
     * @return
     */
    @Override
    public void deleteUserArchive(Map<Integer,Integer> idsMap,Integer companyId) {

        //entry中key为userId，value为archiveId
        for (Map.Entry<Integer, Integer> entry : idsMap.entrySet()) {
            //清除企业关联
            userInfoDao.clearUserCompany(entry.getKey(),companyId,new Date());
            //删除档案
            UserArchive userArchive = new UserArchive();
            userArchive.setIsDelete((short) 1);
            userArchive.setArchiveId(entry.getValue());
            userArchive.setUpdateTime(new Date());
            userArchiveDao.updateByPrimaryKeySelective(userArchive);
        }
    }

    @Override
    public void editUserArchive(UserArchiveVo userArchiveVo, UserSession userSession) {
        UserInfoVO userInfoVO = userLoginDao.searchUserCompanyByUserIdAndCompanyId(userSession.getCompanyId(), userArchiveVo.getUserId());
        //userInfoVO.setUserName(userArchiveVo.getUserName());
        userInfoVO.setPhone(userArchiveVo.getPhone());
        userInfoVO.setEmail(userArchiveVo.getEmail());

        userInfoDao.editUserInfo(userInfoVO);
        UserArchiveVo userArchiveVo1 = userArchiveDao.selectByPrimaryKey(userArchiveVo.getArchiveId());

        UserArchive userArchive = new UserArchive();
        BeanUtils.copyProperties(userArchiveVo, userArchiveVo1);
        BeanUtils.copyProperties(userArchiveVo1, userArchive);
        userArchiveDao.updateByPrimaryKeySelective(userArchive);
    }

    @Override
    public ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession, HttpServletResponse response) throws Exception {
        HashMap<Object, Object> resultMap = new HashMap<>();
        //excel文件基本校验
        basicCheck(multfile);
        //excel文件导入内存
        List<UserArchiveVo> excelList = importFromExcel(multfile,UserArchiveVo.class);

        List<UserArchiveVo> userArchiveList = new ArrayList<>(excelList.size());
        //记录行号
        Integer number = 1;
        for (UserArchiveVo vo : excelList) {
            vo.setLineNumber(++number);
            //排序前记录行号
            userArchiveList.add(vo);
        }
        //TODO 进行排序，根据工号？

        //将排序后的集合以及原excel数据集合存入redis
        String redisKey = "tempOrgData" + multfile.getOriginalFilename().hashCode();
        redisService.del(redisKey);
        String json = JSON.toJSONString(userArchiveList);
        redisService.setex(redisKey, 30 * 60, json);
        //将原表信息及redis key置入返回对象
        resultMap.put("excelList", userArchiveList);
        resultMap.put("redisKey", redisKey);

        //导入用户信息不用进行排序，只需按照原表顺序即可
        checkExcel(userArchiveList,userSession);

        //TODO

        return new ResponseResult();
    }

    private void checkExcel(List<UserArchiveVo> userArchiveList, UserSession userSession) {
        List<UserArchiveVo> checkVos = new ArrayList<>(userArchiveList.size());
        for (UserArchiveVo vo : userArchiveList) {


        }


    }


    private void checkEmployeeNumber(UserSession userSession, UserArchive userArchive) throws Exception {
        List < Integer > integers = contractParamDao.selectRuleIdByCompanyId ( userSession.getCompanyId () );
        String empNumber = employeeNumberRuleService.createEmpNumber ( integers.get ( 0 ), userArchive.getArchiveId () );
        List <String> employnumberList=userArchiveDao.selectEmployNumberByCompanyId(userSession.getCompanyId (),userArchive.getEmployeeNumber ());
        if(CollectionUtils.isEmpty ( employnumberList )){
            userArchive.setEmployeeNumber ( empNumber );
        }else{
            ExceptionCast.cast ( CommonCode.EMPLOYEENUMBER_IS_EXIST );
        }
    }
}
