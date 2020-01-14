package com.qinjee.masterdata.service.organation.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.CompanyInfoDao;
import com.qinjee.masterdata.dao.UserCompanyDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.ContractParamDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.dao.sys.SysDictDao;
import com.qinjee.masterdata.dao.userinfo.UserInfoDao;
import com.qinjee.masterdata.dao.userinfo.UserLoginDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.page.UserArchivePageVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.employeenumberrule.IEmployeeNumberRuleService;
import com.qinjee.masterdata.service.organation.AbstractOrganizationHelper;
import com.qinjee.masterdata.service.organation.UserArchiveService;
import com.qinjee.masterdata.utils.BeanUtilsExtension;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserArchiveServiceImpl extends AbstractOrganizationHelper<UserArchiveVo> implements UserArchiveService {
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
    private SysDictDao sysDictDao;

    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private BlacklistDao blacklistDao;
    @Autowired
    private CompanyInfoDao companyInfoDao;

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
        logger.info("查询机构下用户，机构id：" + orgIdList);
        if (pageQueryVo.getCurrentPage() != null && pageQueryVo.getPageSize() != null) {
            PageHelper.startPage(pageQueryVo.getCurrentPage(), pageQueryVo.getPageSize());
        }
        List<UserArchiveVo> userArchiveList = userArchiveDao.getUserArchiveList(orgIdList);
        PageInfo<UserArchiveVo> pageInfo = new PageInfo<>(userArchiveList);
        PageResult<UserArchiveVo> pageResult = new PageResult<>(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return new ResponseResult<>(pageResult);
    }


    /**
     * 删除时不要删除账号信息，只将用户当前企业下的档案信息，以及用户企业关系表的信息
     *
     * @param idsMap
     * @return
     */
    @Override
    public void deleteUserArchive(Map<Integer, Integer> idsMap, Integer companyId) {

        //entry中key为userId，value为archiveId
        for (Map.Entry<Integer, Integer> entry : idsMap.entrySet()) {
            //清除企业关联
            userInfoDao.clearUserCompany(entry.getKey(), companyId, new Date());
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

        logger.info("编辑用户信息：userArchiveVo：" + userArchiveVo);
        UserInfoVO userInfoVO = userLoginDao.searchUserCompanyByUserIdAndCompanyId(userSession.getCompanyId(), userArchiveVo.getUserId());
        UserInfo userByPhone = userInfoDao.getUserByPhone(userArchiveVo.getPhone());
        logger.info("根据手机号查到的用户：" + userByPhone);
        if (Objects.nonNull(userByPhone) && userByPhone.getUserId() != userInfoVO.getUserId()) {

            ExceptionCast.cast(CommonCode.PHONE_ALREADY_EXIST);
        }
        //userInfoVO.setUserName(userArchiveVo.getUserName());
        userInfoVO.setPhone(userArchiveVo.getPhone());
        userInfoVO.setEmail(userArchiveVo.getEmail());

        userInfoDao.editUserInfo(userInfoVO);
        UserArchiveVo userArchiveVo1 = userArchiveDao.selectByPrimaryKey(userArchiveVo.getArchiveId());

        UserArchive userArchive = new UserArchive();
        BeanUtils.copyProperties(userArchiveVo, userArchiveVo1);
        BeanUtils.copyProperties(userArchiveVo1, userArchive);
        logger.info("编辑的userArchive：" + userArchive);
        userArchiveDao.updateByPrimaryKeySelective(userArchive);
    }

    @Override
    public ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession, HttpServletResponse response) throws Exception {
        return doUploadAndCheck(multfile, UserArchiveVo.class, userSession, response);
    }

    @Override
    @Transactional
    /**
     * 1.添加账号（如果已存在则不更新）
     * 2.关联企业
     * 3.新增档案（用户在一家企业下只有一份档案）
     */
    public ResponseResult<Integer> addUserArchive(UserArchiveVo userArchiveVo, UserSession userSession) {

        //黑名单校验
        List<Blacklist> blacklistsMem = blacklistDao.selectByPage(userSession.getCompanyId());

        if(CollectionUtils.isNotEmpty(blacklistsMem)){
            //”XX曾于XX年月日被XX公司因XX原因被列入黑名单，不允许入职/投递简历，请联系该公司处理!"
            //查询公司名称
            CompanyInfo companyInfo = companyInfoDao.selectByPrimaryKey(userSession.getCompanyId());
            //如果身份证在黑名单表中存在  或者（手机号+姓名）在身份证中存在
            List<Blacklist> blacklist = blacklistsMem.stream().filter(a -> (userArchiveVo.getPhone().equals(a.getPhone()) && userArchiveVo.getUserName().equals(a.getUserName()))||(null!= userArchiveVo.getIdNumber()&&userArchiveVo.getIdNumber().equals(a.getIdNumber()))).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(blacklist)){
                CommonCode isExistBlacklist = CommonCode.IS_EXIST_BLACKLIST;
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
                String msg=blacklist.get(0).getUserName()+"曾于"+sdf.format(blacklist.get(0).getBlockTime())+"被"+companyInfo.getCompanyName()+"因"+blacklist.get(0).getBlockReason()+"原因列入黑名单，不允许入职/投递简历，请联系该公司处理!";
                isExistBlacklist.setMessage(msg);
                ExceptionCast.cast(isExistBlacklist);
            }
        }

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
            UserInfo user = userInfoDao.getUserByPhoneAndCompanyId(userArchiveVo.getPhone(), userSession.getCompanyId());
            if (Objects.nonNull(user)) {
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
            checkEmployeeNumber(userSession, userArchive);
            userArchiveDao.updateByPrimaryKeySelective(userArchive);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO 记录异常 ExceptionCast.cast(CommonCode.FAIL);
        }
        return new ResponseResult(userArchive.getArchiveId());
    }


    @Override
    public void importToDatabase(String orgExcelRedisKey, UserSession userSession) {
        String data = redisService.get(orgExcelRedisKey.trim());
        //将其转为对象集合
        List<UserArchiveVo> list = JSONArray.parseArray(data, UserArchiveVo.class);

        if(CollectionUtils.isNotEmpty(list)){
            //直接遍历入库  账户信息表  账户企业关联表 用户档案表
            for (UserArchiveVo vo : list) {

                //1.根据手机号+企业查询用户 如果存在则更新  如果不存在则单独以手机号查询用户  如果存在则不新增账户 只需进行企业关联 如果两次查询都不存在则新增一条账户信息
                UserInfo user1 = userInfoDao.getUserByPhoneAndCompanyId(vo.getPhone(), userSession.getCompanyId());
                if (Objects.nonNull(user1)) {
                    //2.进行账户更新操作 账户企业关联表不用动
                    user1.setEmail(vo.getEmail());
                    //TODO 分析还有哪些列需要更新
                    userInfoDao.update(user1);
                    //3.进行档案维护 根据userId查询企业下关联的档案的维护，如果存在档案则更新，不存在则插入
                    UserArchiveVo archiveVo = userArchiveDao.selectByUserId(user1.getUserId(), userSession.getCompanyId());
                    if (Objects.nonNull(archiveVo)) {
                        UserArchive userArchive = new UserArchive();
                        //进行一些属性设置，注意 有些需要进行字典转换
                        userArchive.setUserId(user1.getUserId());
                        userArchive.setArchiveId(archiveVo.getArchiveId());
                        userArchive.setEmail(vo.getEmail());
                        userArchiveDao.updateByPrimaryKeySelective(userArchive);
                    } else {
                        UserArchive userArchive = new UserArchive();
                        //进行一些属性设置，注意 有些需要进行字典转换
                        BeanUtils.copyProperties(vo,userArchive);
                        userArchive.setCompanyId(userSession.getCompanyId());
                        userArchive.setUserId(user1.getUserId());
                        userArchive.setOperatorId(userSession.getArchiveId());
                        userArchiveDao.insertSelective(userArchive);
                    }
                } else {
                    UserInfo user2 = userInfoDao.getUserByPhone(vo.getPhone());
                    if (Objects.nonNull(user2)) {
                        //4.进行企业绑定
                        //维护员工企业关系表
                        UserCompany userCompany = new UserCompany();
                        userCompany.setCreateTime(new Date());
                        userCompany.setUserId(user2.getUserId());
                        userCompany.setCompanyId(userSession.getCompanyId());
                        userCompany.setIsEnable((short) 1);
                        userCompanyDao.insertSelective(userCompany);

                    } else {
                        //5.如果都为空 新增账户并绑定企业
                        user2 = new UserInfo();
                        user2.setPhone(vo.getPhone());
                        user2.setEmail(vo.getEmail());
                        user2.setCreateTime(new Date());
                        //密码默认手机号后6位
                        String p = StringUtils.substring(vo.getPhone(), vo.getPhone().length() - 6, vo.getPhone().length());
                        user2.setPassword(MD5Utils.getMd5(p));
                        userLoginDao.addUserInfo(user2);

                        UserCompany userCompany = new UserCompany();
                        userCompany.setCreateTime(new Date());
                        userCompany.setUserId(user2.getUserId());
                        userCompany.setCompanyId(userSession.getCompanyId());
                        userCompany.setIsEnable((short) 1);
                        userCompanyDao.insertSelective(userCompany);
                    }

                    //6.进行档案维护 新增档案
                    UserArchive userArchive = new UserArchive();
                    //进行一些属性设置，注意 有些需要进行字典转换
                    BeanUtils.copyProperties(vo,userArchive);
                    userArchive.setUserId(user2.getUserId());
                    userArchive.setCompanyId(userSession.getCompanyId());
                    userArchive.setOperatorId(userSession.getArchiveId());
                    userArchiveDao.insertSelective(userArchive);
                }
            }
        }


    }

    private void checkEmployeeNumber(UserSession userSession, UserArchive userArchive) {
        String empNumber = employeeNumberRuleService.createEmpNumber (userSession.getCompanyId () );
        List <Integer> employnumberList=userArchiveDao.selectEmployNumberByCompanyId(userSession.getCompanyId (),userArchive.getEmployeeNumber ());
        if(CollectionUtils.isEmpty ( employnumberList ) || (employnumberList.size ()==1 && employnumberList.get(0).equals ( userArchive.getArchiveId () ))){
            userArchive.setEmployeeNumber ( empNumber );
        }else{
            ExceptionCast.cast ( CommonCode.EMPLOYEENUMBER_IS_EXIST );
        }
    }




    @Override
    protected List<UserArchiveVo> checkExcel(List<UserArchiveVo> userArchiveList, UserSession userSession) {
        List<UserArchiveVo> checkList = new ArrayList<>(userArchiveList.size());
        List<UserArchiveVo> archiveVoByCompanyIdMem = userArchiveDao.getByCompanyId(userSession.getCompanyId());
        List<SysDict> sysDictsMem = sysDictDao.searchSomeSysDictList();
        List<Blacklist> blacklistsMem = blacklistDao.selectByPage(userSession.getCompanyId());

        for (UserArchiveVo vo : userArchiveList) {
            StringBuilder resultMsg = new StringBuilder(1024);
            //先校验必填项 手机号 姓名 证件号
            if (StringUtils.isBlank(vo.getUserName())) {
                vo.setCheckResult(false);
                resultMsg.append("用户姓名不能为空 | ");
            }
            if (StringUtils.isBlank(vo.getPhone())) {
                vo.setCheckResult(false);
                resultMsg.append("手机号不能为空 | ");
            }
            if (StringUtils.isBlank(vo.getIdNumber())) {
                vo.setCheckResult(false);
                resultMsg.append("证件号码不能为空 | ");
            }
            //黑名单校验
            if(blacklistsMem.stream().anyMatch(a -> (vo.getPhone().equals(a.getPhone()) && vo.getUserName().equals(a.getUserName()))|| vo.getIdNumber().equals(a.getIdNumber()))){
                vo.setCheckResult(false);
                resultMsg.append("用户已存在于黑名单 | ");
            }



            //如果同时存在证件号和工号 //则判断是否相等
            if (StringUtils.isNotBlank(vo.getEmployeeNumber()) && StringUtils.isNotBlank(vo.getIdNumber())) {
                Optional<UserArchiveVo> first = archiveVoByCompanyIdMem.stream().filter(a -> vo.getIdNumber().equals(a.getIdNumber())).findFirst();
                if (first.isPresent()) {
                    UserArchiveVo archiveVo = first.get();
                    if (Objects.nonNull(archiveVo) && Objects.nonNull(archiveVo.getEmployeeNumber()) && !archiveVo.getEmployeeNumber().equals(vo.getEmployeeNumber())) {
                        vo.setCheckResult(false);
                        resultMsg.append("证件号码与其对应的工号人员不一致 | ");
                    }
                }

            }
            //--------------下面的进行字典验证
            if (StringUtils.isNotBlank(vo.getGender())) {
                boolean bool = sysDictsMem.stream().anyMatch(a -> "SEX_TYPE".equals(a.getDictType()) &&vo.getGender() .equals(a.getDictValue()));
                if (!bool) {
                    vo.setCheckResult(false);

                    resultMsg.append("性别中没有[" + vo.getGender() + "]的选项 | ");
                }
            }
            if (StringUtils.isNotBlank(vo.getIdType())) {
                boolean bool = sysDictsMem.stream().anyMatch(a -> "CARD_TYPE".equals(a.getDictType()) &&vo.getIdType() .equals(a.getDictValue()));
                if (!bool) {
                    vo.setCheckResult(false);

                    resultMsg.append("证件类型中没有[" + vo.getIdType() + "]的选项 | ");
                }
            }

            if (StringUtils.isNotBlank(vo.getNationality())) {
                boolean bool = sysDictsMem.stream().anyMatch(a ->"NATIONALITY" .equals(a.getDictType()) &&vo.getNationality() .equals(a.getDictValue()));
                if (!bool) {
                    vo.setCheckResult(false);
                    resultMsg.append("民族中没有[" + vo.getNationality() + "]的选项 | ");
                }
            }
            if (StringUtils.isNotBlank(vo.getHighestDegree())) {
                boolean bool = sysDictsMem.stream().anyMatch(a -> "ACADEMIC_DEGREE".equals(a.getDictType()) && vo.getHighestDegree().equals(a.getDictValue()));
                if (!bool) {
                    vo.setCheckResult(false);
                    resultMsg.append("最高学历中没有[" + vo.getHighestDegree() + "]的选项 | ");
                }
            }
            if (StringUtils.isNotBlank(vo.getFirstDegree())) {
                boolean bool = sysDictsMem.stream().anyMatch(a ->"ACADEMIC_DEGREE" .equals(a.getDictType()) && vo.getFirstDegree().equals(a.getDictValue()));
                if (!bool) {
                    vo.setCheckResult(false);
                    resultMsg.append("第一学历中没有[" + vo.getFirstDegree() + "]的选项 | ");
                }
            }
            if (StringUtils.isNotBlank(vo.getMaritalStatus())) {
                boolean bool = sysDictsMem.stream().anyMatch(a -> "MARITAL_STATUS".equals(a.getDictType()) && vo.getMaritalStatus().equals(a.getDictValue()));
                if (!bool) {
                    vo.setCheckResult(false);
                    resultMsg.append("婚姻状况中没有[" + vo.getMaritalStatus() + "]的选项 | ");
                }
            }
            if (StringUtils.isNotBlank(vo.getPoliticalStatus())) {
                boolean bool = sysDictsMem.stream().anyMatch(a ->"POLITICAL_AFFILIATION" .equals(a.getDictType()) && vo.getPoliticalStatus().equals(a.getDictValue()));
                if (!bool) {
                    vo.setCheckResult(false);
                    resultMsg.append("政治面貌中没有[" + vo.getPoliticalStatus() + "]的选项 | ");
                }
            }
            // --------------下面是一些其他的匹配性校验

            //--------------下面的进行格式验证

            if (resultMsg.length() > 2) {
                resultMsg.deleteCharAt(resultMsg.length() - 2);
            }

            vo.setResultMsg(resultMsg.toString());
            if (!vo.isCheckResult()) {
                checkList.add(vo);
            }

        }
        return checkList;
    }
}
