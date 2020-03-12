package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.CompanyInfoDao;
import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.BlacklistDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.*;
import com.qinjee.masterdata.dao.sys.SysDictDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CustomTableVO;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.*;
import com.qinjee.masterdata.model.vo.staff.export.ExportArcVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.service.custom.CustomTableFieldService;
import com.qinjee.masterdata.service.employeenumberrule.IEmployeeNumberRuleService;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.masterdata.service.userinfo.UserLoginService;
import com.qinjee.masterdata.utils.DealHeadParamUtil;
import com.qinjee.masterdata.utils.FieldToProperty;
import com.qinjee.masterdata.utils.SqlUtil;
import com.qinjee.masterdata.utils.export.HeadMapUtil;
import com.qinjee.masterdata.utils.export.TransCustomFieldMapHelper;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 */
@Service
public class StaffArchiveServiceImpl implements IStaffArchiveService {
    /**
     * 档案状态：实习，试用，转正，离职，挂编，兼职
     * 岗位，部门，
     */
//        private static final Logger logger = LoggerFactory.getLogger(StaffArchiveServiceImpl.class);
    private static final String ASC = "升序";
    private static final String DESC = "降序";
    private static final String ARCHIVE = "ARC";
    @Autowired
    private UserArchivePostRelationDao userArchivePostRelationDao;
    @Autowired
    private QuerySchemeDao querySchemeDao;
    @Autowired
    private QuerySchemeFieldDao querySchemeFieldDao;
    @Autowired
    private QuerySchemeSortDao querySchemeSortDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private CustomTableFieldDao customTableFieldDao;
    @Autowired
    private ArchiveCareerTrackDao archiveCareerTrackdao;
    @Autowired
    private CustomTableFieldService customTableFieldService;
    @Autowired
    private BlacklistDao blacklistDao;
    @Autowired
    private CompanyInfoDao companyInfoDao;

    @Autowired
    private IEmployeeNumberRuleService employeeNumberRuleService;
    @Autowired
    private SysDictDao sysDictDao;
    @Autowired
    private IStaffCommonService staffCommonService;

    @Override
    public List<ArchiveRegistVo> getArchiveRegisterInfo(List<Integer> archiveIds, UserSession userSession) throws Exception {
        ArrayList<ArchiveRegistVo> archiveRegistList = new ArrayList<>();
        //TODO 缓存字典
        List<SysDict> sysDicts = sysDictDao.searchSomeSysDictList();
        for (Integer archiveId : archiveIds) {
            //组装成一个大的对象用来封装所有页面需要的信息
            ArchiveRegistVo archiveRegistVo = new ArchiveRegistVo();
            //1、查询预入职档案基本信息
            UserArchiveVo userArchiveVo = userArchiveDao.selectFullById(archiveId);
            userArchiveVo.setBusinessUnitName(userArchiveVo.getOrgFullName().replace("/", "|"));
            //转换字典
            new TransCustomFieldMapHelper<UserArchiveVo>().transToDict(userArchiveVo, sysDicts);
            archiveRegistVo.setUserArchiveVo(userArchiveVo);

            //2、查询人事变动信息
            List<ArchiveCareerTrackVo> archiveCareerTrackVoList = archiveCareerTrackdao.selectCareerTrack(archiveId);
            archiveRegistVo.setArchiveCareerTrackVoList(archiveCareerTrackVoList);
            //--------------
            //3、查询自定义相关数据  （教育经历、工作经历、家庭成员）
            ArrayList<String> tableNames = Lists.newArrayList("教育经历", "工作经历", "家庭成员", "职称信息");

            tableNames.stream().forEach(tableName -> {
                //1.获取表id
                Integer tableId = customTableFieldDao.selectTableIdByTableNameAndCompanyId(tableName, userSession.getCompanyId(), "ARC");
                //2.根据表id拿到 自定义集合  List<Map<String,String>>，key为fieldName，value为字段值
                try {
                    List<Map<String, String>> customDataList = staffCommonService.getCustomDataByTableIdAndEmploymentId(archiveId, tableId);
                    //3.将resMapList填充到preRegistVo中对应得属性中
                    if ("教育经历".equals(tableName)) {
                        //转化为对象vo
                        List<EducationExperienceVo> educationExperienceList = new TransCustomFieldMapHelper<EducationExperienceVo>().transToObeject(customDataList, EducationExperienceVo.class, sysDicts);
                        archiveRegistVo.setEducationExperienceList(educationExperienceList);
                    }
                    if ("工作经历".equals(tableName)) {
                        List<WorkExperienceVo> workExperienceList = new TransCustomFieldMapHelper<WorkExperienceVo>().transToObeject(customDataList, WorkExperienceVo.class, sysDicts);
                        archiveRegistVo.setWorkExperienceList(workExperienceList);
                    }
                    if ("家庭成员".equals(tableName)) {
                        List<FamilyMemberAndSocialRelationsVo> FamilyMemberAndSocialRelationsList = new TransCustomFieldMapHelper<FamilyMemberAndSocialRelationsVo>().transToObeject(customDataList, FamilyMemberAndSocialRelationsVo.class, sysDicts);
                        archiveRegistVo.setFamilyMemberAndSocialRelationsList(FamilyMemberAndSocialRelationsList);
                    }
                    if ("职称信息".equals(tableName)) {
                        List<TitleInformationVo> titleInformationList = new TransCustomFieldMapHelper<TitleInformationVo>().transToObeject(customDataList, TitleInformationVo.class, sysDicts);
                        archiveRegistVo.setTitleInformationList(titleInformationList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ExceptionCast.cast(CommonCode.SERVER_ERROR);
                }
            });
            archiveRegistList.add(archiveRegistVo);
        }
        return archiveRegistList;
    }

    /**
     * 离职证明信息
     *
     * @param archiveIds
     * @param userSession
     * @return
     */
    @Override
    public List<DimissionCertificateVo> listDimissionCertificate(List<Integer> archiveIds, UserSession userSession) {
        List<DimissionCertificateVo> dimissionCertificateList = new ArrayList<>();
        archiveIds.stream().forEach(id -> {
            UserArchiveVo userArchiveVo = userArchiveDao.selectFullById(id);
            if(Objects.nonNull(userArchiveVo)){
                DimissionCertificateVo dimissionCertificateVo = DimissionCertificateVo.builder()
                        .gender(userArchiveVo.getGender())
                        .hireDate(userArchiveVo.getHireDate())
                        .idNumber(userArchiveVo.getIdNumber())
                        .postName(userArchiveVo.getPostName())
                        .userName(userArchiveVo.getUserName())
                        .attritionDate(userArchiveVo.getAttritionDate()).build();
                dimissionCertificateList.add(dimissionCertificateVo);
            }
        });
        return dimissionCertificateList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArchiveById(List<Integer> archiveid) {
        userArchiveDao.deleteArchiveByIdList(archiveid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumeDeleteArchiveById(List<Integer> archiveid) {
        for (Integer integer : archiveid) {
            UserArchiveVo userArchiveVo = userArchiveDao.selectBasicById(integer);
            Integer isExistId = userArchiveDao.selectIsExist(userArchiveVo.getIdNumber(), userArchiveVo.getUserId());
            if (isExistId == null || isExistId == 0) {
                userArchiveDao.resumeDeleteArchiveById(integer);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArchive(UserArchiveVo userArchiveVo, UserSession userSession) {
        UserArchive userArchive = new UserArchive();
        BeanUtils.copyProperties(userArchiveVo, userArchive);
        userArchive.setOperatorId(userSession.getArchiveId());
        userArchive.setIsDelete((short) 0);
        checkEmployeeNumber(userSession, userArchive);
        userArchiveDao.updateByPrimaryKeySelective(userArchive);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<UserArchiveVo> selectArchive(UserSession userSession) {
        UserArchiveVo userArchiveVo = userArchiveDao.selectBasicById(userSession.getArchiveId());
        List<UserArchiveVo> userArchiveVos = new ArrayList<>();
        userArchiveVos.add(userArchiveVo);
        return new PageResult<>(userArchiveVos);
    }

    private List<TableHead> getHeadList(UserSession userSession, List<QueryScheme> list1) {
        List<TableHead> headList = new ArrayList<>();
        //非默认显示方案表头
        for (QueryScheme queryScheme : list1) {
            if (queryScheme.getIsDefault().equals(1)) {
                //找到默认的显示方案然后设值
                headList = setDefaultHead(userSession, queryScheme.getQuerySchemeId());
                break;
            }
        }
        if (CollectionUtils.isEmpty(headList)) {
            headList = getDefaultArcHead();
        }
        return headList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> insertArchive(UserArchiveVo userArchiveVo, UserSession userSession) throws Exception {
        UserArchive userArchive = new UserArchive();
        BeanUtils.copyProperties(userArchiveVo, userArchive);
        //黑名单校验
        List<Blacklist> blacklistList = blacklistDao.selectByPage(userSession.getCompanyId());

        boolean bool = blacklistList.stream().anyMatch(a -> StringUtils.isNotBlank(userArchive.getPhone()) && userArchive.getPhone().equals(a.getPhone()) ||
                StringUtils.isNotBlank(userArchive.getIdNumber()) && userArchive.getIdNumber().equals(a.getIdNumber()));
        if (bool) {
            CompanyInfo companyInfo = companyInfoDao.selectByPrimaryKey(userSession.getCompanyId());
            CommonCode isExistBlacklistCommonCode = CommonCode.IS_EXIST_BLACKLIST;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            String reason = "";
            if (null != blacklistList.get(0).getBlockReason()) {
                reason = blacklistList.get(0).getBlockReason();
            }
            String msg = blacklistList.get(0).getUserName() + "曾于" + sdf.format(blacklistList.get(0).getBlockTime()) + "被" + companyInfo.getCompanyName() + "因[ " + reason + " ]原因列入黑名单，不允许入职/投递简历，请联系该公司处理!";
            isExistBlacklistCommonCode.setMessage(msg);
            ExceptionCast.cast(isExistBlacklistCommonCode);
        }
        if (StringUtils.isNoneBlank(userArchive.getIdType()) && StringUtils.isNoneBlank(userArchive.getIdNumber())) {
            Integer id = userArchiveDao.selectByIDNumberAndCompanyId(userArchive.getIdType(), userArchive.getIdNumber(), userSession.getCompanyId());
            if (id != null) {
                ExceptionCast.cast(CommonCode.IDNUMBER_ALREADY_EXIST);
            }
        }
        if (StringUtils.isNoneBlank(userArchive.getPhone())) {
            List<Integer> id = userArchiveDao.selectByPhoneAndCompanyId(userArchive.getPhone(), userSession.getCompanyId());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(id)) {
                ExceptionCast.cast(CommonCode.PHONE_ALREADY_EXIST);
            }
        }
        if (StringUtils.isNotBlank(userArchive.getPhone())) {
            int userId = userLoginService.getUserIdByPhone(userArchive.getPhone(), userSession.getCompanyId());
            userArchive.setUserId(userId);
        }
        userArchive.setArchiveStatus("SERVICE");
        userArchive.setOperatorId(userSession.getArchiveId());
        userArchive.setIsDelete((short) 0);
        userArchive.setCompanyId(userSession.getCompanyId());
        checkEmployeeNumber(userSession, userArchive);
        userArchiveDao.insertSelective(userArchive);
        List<Integer> integers = new ArrayList<>();
        integers.add(userArchive.getArchiveId());
        return integers;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArchiveField(Map<Integer, String> map) {
        customTableFieldDao.updatePreEmploymentField(map);
    }

    @Override
    public PageResult<UserArchiveVo> selectArchivebatch(UserSession userSession, RequestUserarchiveVo requestUserarchiveVo) {
        String message = null;
        if (CollectionUtils.isEmpty(requestUserarchiveVo.getOrgId())) {
            message = "contain";
        } else {
            List<OrganizationVO> list = organizationDao.selectByOrgId(requestUserarchiveVo.getOrgId());
            for (OrganizationVO organizationVO : list) {
                if (null == organizationVO.getOrgParentId()) {
                    message = "contain";
                    break;
                }
            }
        }
        List<SysDict> sysDicts = sysDictDao.searchSomeSysDictList();
        PageHelper.startPage(requestUserarchiveVo.getCurrentPage(), requestUserarchiveVo.getPageSize());
        String whereSql = DealHeadParamUtil.getWhereSql(requestUserarchiveVo.getList(), "t.");
        String orderSql = DealHeadParamUtil.getOrderSql(requestUserarchiveVo.getList(), "t..");
        List<UserArchiveVo> list2 = userArchiveDao.selectByOrgAndAuth(requestUserarchiveVo.getOrgId(), userSession.getArchiveId(), userSession.getCompanyId(), message, whereSql, orderSql);
        new TransCustomFieldMapHelper<UserArchiveVo>().transBatchToDict(list2, sysDicts);
        return new PageResult<>(list2);
    }

    public List<TableHead> setDefaultHead(UserSession userSession, Integer querySchemaId) {
        List<TableHead> headList = new ArrayList<>();
        if (null == querySchemaId || 0 == querySchemaId) {
            return getDefaultArcHead();
        }
        List<QuerySchemeField> querySchemeFieldList = querySchemeFieldDao.selectByQuerySchemeId(querySchemaId);
        if (CollectionUtils.isEmpty(querySchemeFieldList)) {
            ExceptionCast.cast(CommonCode.PLAN_IS_NULL);
        }
        try {
            for (QuerySchemeField querySchemeField : querySchemeFieldList) {
                TableHead arcHead = new TableHead();
                arcHead.setIndex(querySchemeField.getSort());
                CustomFieldVO customFieldVO = customTableFieldDao.selectFieldById(querySchemeField.getFieldId(), userSession.getCompanyId(),
                        "ARC");
                arcHead.setName(customFieldVO.getFieldName());
                if ("姓名，性别，联系电话，年龄，出生日期，证件号码，最高学历".contains(customFieldVO.getFieldName())) {
                    arcHead.setWidth("180px");
                }
                arcHead.setKey(FieldToProperty.fieldToProperty(customFieldVO.getFieldCode()));
                if ("org_id".equals(customFieldVO.getFieldCode())) {
                    arcHead.setKey("orgName");
                }
                if ("post_id".equals(customFieldVO.getFieldCode())) {
                    arcHead.setKey("postName");
                }
                if ("business_unit_id".equals(customFieldVO.getFieldCode())) {
                    arcHead.setKey("businessUnitName");
                }
                if ("supervisor_id".equals(customFieldVO.getFieldCode())) {
                    arcHead.setKey("supervisorUserName");
                }
                arcHead.setIsShow(1);
                headList.add(arcHead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(CommonCode.PLAN_IS_MISTAKE);
        }
        return headList;
    }

    @Override
    public void insertUserArchivePostRelation(UserArchivePostRelationVo userArchivePostRelationVo, UserSession userSession) throws ParseException {
        UserArchivePostRelation userArchivePostRelation = getUserArchivePostRelation(userArchivePostRelationVo, userSession);
        userArchivePostRelationDao.insertSelective(userArchivePostRelation);
    }

    @Override
    public Map<String, String> selectNameAndNumber(Integer id) {
        return userArchiveDao.selectNameAndNumber(id);
    }

    @Override
    public String selectOrgName(Integer id, UserSession userSession) {
        return organizationDao.selectOrgName(id, userSession.getCompanyId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExportFile selectArchiveByQueryScheme(UserSession userSession, List<Integer> archiveIdList, Integer querySchemaId) throws IllegalAccessException {
        ExportFile exportFile = new ExportFile();
        List<CustomFieldVO> orderNotIn = new ArrayList<>();
        Map<Integer, Map<String, Object>> userArchiveListCustom;
        if (querySchemaId != null && querySchemaId != 0) {
            return getExportFile(userSession, archiveIdList, exportFile, orderNotIn, querySchemaId);
        } else {
            List<ExportArcVo> exportArcVoList = userArchiveDao.selectDownLoadVoList(archiveIdList);
            userArchiveListCustom = getMap(archiveIdList, exportArcVoList);
            exportFile.setMap(userArchiveListCustom);
            exportFile.setTittle("ARC");
            return exportFile;
        }
    }

    private ExportFile getExportFile(UserSession userSession, List<Integer> archiveIdList, ExportFile exportFile, List<CustomFieldVO> orderNotIn, Integer schemaId) {
        Map<Integer, Map<String, Object>> userArchiveListCustom = new HashMap<>();
        StringBuilder stringBuffer = new StringBuilder();
        String order = null;
        //根据查询方案id找到需要展示字段的id以及按顺序排序
        List<Integer> integers1 = querySchemeFieldDao.selectFieldIdWithSort(schemaId);
        List<Integer> integers2 = querySchemeSortDao.selectSortId(schemaId);

        CustomTableVO customTableVO = new CustomTableVO();
        customTableVO.setCompanyId(userSession.getCompanyId());
        customTableVO.setFuncCode("ARC");
        List<CustomTableVO> customTableVOS = customTableFieldService.searchCustomTableListByCompanyIdAndFuncCode(customTableVO);
        //找到tableId

        //拼接order
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(integers2)) {
            List<CustomFieldVO> customFields = customTableFieldDao.selectFieldByIdList(integers2, userSession.getCompanyId(), "ARC");
            for (CustomFieldVO customFieldVO : customFields) {
                if (customFieldVO.getIsSystemDefine() == 1) {
                    stringBuffer.append("t.").append(customTableFieldDao.selectFieldCodeById(customFieldVO.getFieldId()) + "\t")
                            .append(getSort(querySchemeSortDao.selectSortById(customFieldVO.getFieldId(), schemaId))).append(",");
                } else {
                    orderNotIn.add(customFieldVO);
                    stringBuffer.append("t.").append(customFieldVO.getFieldName() + "\t").
                            append(getSort(querySchemeSortDao.selectSortById(customFieldVO.getFieldId(), schemaId))).append(",");
                }
            }
            assert order != null;
            int i = stringBuffer.toString().lastIndexOf(",");
            order = stringBuffer.toString().substring(0, i);
        }
        //调用接口查询机构权限范围内的档案集合
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(integers1)) {
            userArchiveListCustom = userArchiveDao.getUserArchiveListCustom(getBaseSql(orderNotIn, integers1, userSession.getCompanyId(), customTableVOS), order);
        } else {
            ExceptionCast.cast(CommonCode.PLAN_IS_MISTAKE);
        }
        //进行sql查询，返回数据，档案在机构范围内
        List<Integer> integers = new ArrayList<>(userArchiveListCustom.keySet());
        integers.removeAll(archiveIdList);
        for (Integer integer : integers) {
            userArchiveListCustom.remove(integer);
        }
        exportFile.setMap(userArchiveListCustom);
        exportFile.setTittle("ARC");
        return exportFile;
    }


    @Override
    public List<String> selectFieldByTableIdAndAuth(Integer tableId, UserSession userSession) {
        return customTableFieldDao.selectFieldByTableIdAndAuth(tableId, userSession.getArchiveId());
    }

    @Override
    public List<String> selectFieldByArcAndAuth(UserSession userSession) {
        return customTableFieldDao.selectFieldByArcAndAuth(userSession.getArchiveId(), userSession.getCompanyId());
    }

    @Override
    public List<ArchiveCareerTrackVo> selectCareerTrack(Integer id) {
        return archiveCareerTrackdao.selectCareerTrack(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertCareerTrack(ArchiveCareerTrackVo archiveCareerTrackVo, UserSession userSession) {
        ArchiveCareerTrack archiveCareerTrack = new ArchiveCareerTrack();
        BeanUtils.copyProperties(archiveCareerTrackVo, archiveCareerTrack);
        archiveCareerTrack.setOperatorId(userSession.getArchiveId());
        archiveCareerTrack.setAfterBusinessUnitId(archiveCareerTrackVo.getBusinessUnitId());
        archiveCareerTrack.setAfterOrgId(archiveCareerTrackVo.getOrgId());
        archiveCareerTrack.setAfterPostId(archiveCareerTrackVo.getPostId());
        archiveCareerTrack.setAfterPositionId(archiveCareerTrackVo.getPositionId());
        archiveCareerTrackdao.insertArchiveCareerTrack(archiveCareerTrack);
    }

    @Override
    public PageResult<UserArchiveVo> selectArchiveSingle(Integer id, UserSession userSession) {
        UserArchiveVo userArchiveVo = userArchiveDao.selectBasicById(id);
        List<UserArchiveVo> userArchiveVos = new ArrayList<>();
        userArchiveVos.add(userArchiveVo);
        return new PageResult<>(userArchiveVos);
    }

    @Override
    public List<UserArchiveVo> selectUserArchiveByName(String name, UserSession userSession) {
        return userArchiveDao.selectUserArchiveByName(name, userSession.getCompanyId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultQuerySchme(Integer querySchmeId, UserSession userSession) {
        List<QueryScheme> list = querySchemeDao.selectQueryByArchiveId(userSession.getArchiveId());
        for (QueryScheme queryScheme : list) {
            if (querySchmeId.equals(queryScheme.getQuerySchemeId())) {
                queryScheme.setIsDefault(1);
                querySchemeDao.updateByPrimaryKeySelective(queryScheme);
            } else {
                if (queryScheme.getIsDefault() == 1) {
                    queryScheme.setIsDefault(0);
                    querySchemeDao.updateByPrimaryKeySelective(queryScheme);
                }
            }
        }
    }

    @Override
    public List<TableHead> getHeadList(UserSession userSession) {
        List<QueryScheme> list1 = querySchemeDao.selectQueryByArchiveId(userSession.getArchiveId());
        //组装默认显示方案表头
        if (CollectionUtils.isEmpty(list1)) {
            return getDefaultArcHead();
        } else {
            return getHeadList(userSession, list1);
        }
    }

    private Map<Integer, Map<String, Object>> getMap(List<Integer> archiveIdList, List<ExportArcVo> exportArcVoList) throws IllegalAccessException {
        Map<Integer, Map<String, Object>> userArchiveListCustom = new HashMap<>();
        for (int i = 0; i < archiveIdList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            //获得类
            Class clazz = exportArcVoList.get(i).getClass();
            // 获取实体类的所有属性信息，返回Field数组
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), String.valueOf(field.get(exportArcVoList.get(i))));
            }
            userArchiveListCustom.put(archiveIdList.get(i), map);
        }
        return userArchiveListCustom;
    }


    /**
     * list是所查询项的集合
     * 组装sql
     */
    private String getBaseSql(List<CustomFieldVO> notIn, List<Integer> integers, Integer companyId, List<CustomTableVO> tableVOS) {
        List<CustomFieldVO> inList = new ArrayList<>();
        List<CustomFieldVO> outList = new ArrayList<>();
        List<CustomFieldVO> list = customTableFieldDao.selectFieldByIdList(integers, companyId, "ARC");
        list.addAll(notIn);

//        for (CustomFieldVO customFieldVO : list) {
//            if (customFieldVO.getIsSystemDefine () == 0) {
//                outList.add ( customFieldVO );
//            } else {
//                inList.add ( customFieldVO );
//            }
//        }
//        outList.addAll ( notIn );
        StringBuilder stringBuffer = new StringBuilder();
        String a = "";
        String b = "select  t.archive_id, ";
        String c = null;
        String d = "FROM ( select t0.*";
        for (CustomFieldVO customFieldVO : list) {
            if (customFieldVO.getIsSystemDefine() == 0) {
                stringBuffer.append("t.").append(customFieldVO.getFieldName()).append(",");
            } else {
                stringBuffer.append("t.").append(customFieldVO.getFieldCode()).append(",");
            }
        }

//        for (CustomFieldVO customFieldVO : inList) {
//            stringBuffer.append ( "t." ).append ( customFieldVO.getFieldCode () ).append ( "," );
//        }
//        if (!CollectionUtils.isEmpty ( outList )) {
//            for (CustomFieldVO customFieldVO : outList) {
//                stringBuffer.append ( "t." ).append ( customFieldVO.getFieldName () ).append ( "," );
//            }
//        }
        int i1 = stringBuffer.toString().lastIndexOf(",");
        a = stringBuffer.toString().substring(0, i1);
        if (CollectionUtils.isEmpty(outList)) {
            c = b + a + "\t" + d;
        } else {
            c = b + a + "\t" + d + ",";
        }
        return c + SqlUtil.getsql(companyId, outList, tableVOS);
    }

    private String getSort(String sort) {
        if (sort != null) {
            if (sort.equals(ASC)) {
                return "ASC";
            }
            if (sort.equals(DESC)) {
                return "DESC";
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserArchivePostRelation(List<Integer> list) {
        userArchivePostRelationDao.deleteUserArchivePostRelation(list);
    }

    @Override
    public void updateUserArchivePostRelation(UserArchivePostRelationVo userArchivePostRelationVo, UserSession userSession) throws ParseException {
        UserArchivePostRelation userArchivePostRelation = getUserArchivePostRelation(userArchivePostRelationVo, userSession);
        userArchivePostRelationDao.updateByPrimaryKeySelective(userArchivePostRelation);
    }

    private UserArchivePostRelation getUserArchivePostRelation(UserArchivePostRelationVo userArchivePostRelationVo, UserSession userSession) throws ParseException {
        UserArchivePostRelation userArchivePostRelation = new UserArchivePostRelation();
        BeanUtils.copyProperties(userArchivePostRelationVo, userArchivePostRelation);
        Date employmentEndDate = userArchivePostRelationVo.getEmploymentEndDate();
        if (employmentEndDate != null) {
            userArchivePostRelation.setEmploymentEndDate(employmentEndDate);
        }
        //通过工号查询档案id
//        Integer id = userArchiveDao.selectArchiveIdByNumber ( userArchivePostRelationVo.getEmployeeNumber () );
        userArchivePostRelation.setArchiveId(userArchivePostRelation.getArchiveId());
        userArchivePostRelation.setOperatorId(userSession.getArchiveId());
        userArchivePostRelation.setIsDelete((short) 0);
        return userArchivePostRelation;
    }

    @Override
    public List<UserArchivePostRelationVo> selectUserArchivePostRelation(Integer archiveId) {
        return userArchivePostRelationDao.selectByPrimaryKeyList(archiveId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQueryScheme(List<Integer> list) {
        querySchemeDao.deleteQuerySchemeList(list);
        //删除查询字段
        for (Integer integer : list) {
            deleteQuery(integer);
        }
    }

    private void deleteQuery(Integer integer) {
        List<Integer> fieldIdList = new ArrayList<>();
        List<Integer> sortIdList = new ArrayList<>();
        querySchemeDao.deleteByPrimaryKey(integer);
        List<QuerySchemeField> querySchemeFieldList = querySchemeFieldDao.selectByQuerySchemeId(integer);
        for (QuerySchemeField querySchemeField : querySchemeFieldList) {
            fieldIdList.add(querySchemeField.getQuerySchemeFieldId());
        }
        List<QuerySchemeSort> querySchemeSortList = querySchemeSortDao.selectByQuerySchemeId(integer);
        for (QuerySchemeSort querySchemeSort : querySchemeSortList) {
            sortIdList.add(querySchemeSort.getQuerySchemeSortId());
        }
        //进行删除
        if (!CollectionUtils.isEmpty(fieldIdList)) {

            querySchemeFieldDao.deleteBySchemeIdList(fieldIdList);
        }
        if (!CollectionUtils.isEmpty(sortIdList)) {
            querySchemeSortDao.deleteBySchemeIdList(sortIdList);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<QueryScheme> selectQueryScheme(UserSession userSession) {
        return querySchemeDao.selectByArchiveId(userSession.getArchiveId());

    }

    @Override
    public QuerySchemeList selectQuerySchemeMessage(Integer id) {
        QueryScheme queryScheme = querySchemeDao.selectByPrimaryKey(id);
        QuerySchemeList querySchemeList = new QuerySchemeList();
        //通过查询方案id找到显示字段
        querySchemeList.setQuerySchemeId(id);
        querySchemeList.setQuerySchemeName(queryScheme.getQuerySchemeName());
        querySchemeList.setSort(queryScheme.getSort());
        List<QuerySchemeField> querySchemeFields = querySchemeFieldDao.selectByQuerySchemeId(id);
        //通过查询方案id找到排序字段
        List<QuerySchemeSort> querySchemeSorts = querySchemeSortDao.selectByQuerySchemeId(id);
        querySchemeList.setQuerySchemeFieldList(querySchemeFields);
        querySchemeList.setQuerySchemeSortList(querySchemeSorts);

        return querySchemeList;
    }

    @Override
    public PageResult<UserArchiveVo> selectArchiveDelete(RequestUserarchiveVo requestUserarchiveVo) {
        List<SysDict> sysDicts = sysDictDao.searchSomeSysDictList();
        PageHelper.startPage(requestUserarchiveVo.getCurrentPage(), requestUserarchiveVo.getPageSize());
        String whereSql = DealHeadParamUtil.getWhereSql(requestUserarchiveVo.getList(), "arc.");
        String orderSql = DealHeadParamUtil.getOrderSql(requestUserarchiveVo.getList(), "arc.");
        List<UserArchiveVo> list = userArchiveDao.selectArchiveDelete(requestUserarchiveVo.getOrgId(), whereSql, orderSql);
        new TransCustomFieldMapHelper<UserArchiveVo>().transBatchToDict(list, sysDicts);
        return new PageResult<>(list);
    }

    @Override
    public List<UserArchiveVo> selectByOrgList(ExportArcParamVo exportArcParamVo, UserSession userSession) {
        String message = null;
        if (CollectionUtils.isEmpty(exportArcParamVo.getList())) {
            message = "contain";
        } else {
            List<OrganizationVO> list1 = organizationDao.selectByOrgId(exportArcParamVo.getList());
            for (OrganizationVO organizationVO : list1) {
                if (null == organizationVO.getOrgParentId()) {
                    message = "contain";
                    break;
                }
            }
        }
        String orderSql = DealHeadParamUtil.getOrderSql(exportArcParamVo.getSearchList(), "arc.");
        String whereSql = DealHeadParamUtil.getWhereSql(exportArcParamVo.getSearchList(), "arc.");
        return userArchiveDao.selectByOrgAndAuth(exportArcParamVo.getList(), userSession.getArchiveId(), userSession.getCompanyId(), message, whereSql, orderSql);
    }

    @Override
    public UserArchiveVo selectById(Integer id) {
        return userArchiveDao.selectBasicById(id);
    }

    @Override
    public List<CustomFieldForHead> selectFieldListByqueryId(Integer queryschemaId, UserSession userSession) {
        List<CustomFieldForHead> customFieldForHeads = null;
        if (queryschemaId != null && !queryschemaId.equals(0)) {
            List<Integer> integers = querySchemeFieldDao.selectFieldIdWithSort(queryschemaId);
            if (!CollectionUtils.isEmpty(integers)) {
                customFieldForHeads = customTableFieldDao.searchCustomFieldForHeadListByFieldIdList(integers);
            } else {
                ExceptionCast.cast(CommonCode.PLAN_IS_MISTAKE);
            }
        } else {
            String[] codeList = {"user_name", "employee_number", "business_unit_id", "org_id", "hireDate",
                    "probation_due_date", "supervisor_id", "phone"};
            List<String> strings = Arrays.asList(codeList);
            customFieldForHeads = customTableFieldDao.selectFieldIdListByCodeListAndIsDefine (strings, userSession.getCompanyId(), "ARC",1);
        }
        for (CustomFieldForHead customFieldVO : customFieldForHeads) {
            String key =customFieldVO.getKey();
            if (StringUtils.isNotBlank(key)) {
                transCustomTableHead(customFieldVO, key);
            }
            customFieldVO.setKey (  FieldToProperty.fieldToProperty ( customFieldVO.getKey () ));
            if ("code".equals(customFieldVO.getTextType())) {
                customFieldVO.setDictList(sysDictDao.selectMoreDict(customFieldVO.getCode()));
            }
        }
        return customFieldForHeads;
    }

    private void transCustomTableHead(CustomFieldForHead customFieldVO, String key) {
        if("org_id".equalsIgnoreCase(key)){
            customFieldVO.setKey("org_name");
            customFieldVO.setTextType ( "code" );
        }
        if("post_id".equalsIgnoreCase(key)){
            customFieldVO.setKey("post_name");
            setOthersField(customFieldVO);
        }
        if("business_unit_id".equalsIgnoreCase(key)){
            customFieldVO.setKey("business_unit_name");
            setOthersField(customFieldVO);
        }
        if("supervisor_id".equalsIgnoreCase(key)){
            customFieldVO.setKey("supervisor_user_name");
            setOthersField(customFieldVO);
        }
        if("position_level_id".equalsIgnoreCase(key)){
            customFieldVO.setKey("position_level_name");
            setOthersField(customFieldVO);
        }
        if("position_grade_id".equalsIgnoreCase(key)){
            customFieldVO.setKey("position_grade_name");
            setOthersField(customFieldVO);
        }
    }

    private void setOthersField(CustomFieldForHead customFieldVO) {
        customFieldVO.setInputType("text");
        customFieldVO.setType("input");
        customFieldVO.setTextType("text");
    }

    @Override
    public List<CustomFieldForHead> selectFieldListForPre(UserSession userSession) {
        List<String> headsForPre = HeadMapUtil.getHeadsForPre();
        List<CustomFieldForHead> customFieldVOS = customTableFieldDao.selectFieldCodeByNameListAndFuncCodeAndCompanyId(headsForPre, "PRE", userSession.getCompanyId());
        for (CustomFieldForHead customFieldVO : customFieldVOS) {
            String key = customFieldVO.getKey();
            if (StringUtils.isNotBlank(key)) {
                transCustomTableHead(customFieldVO, key);
            }
            if ("code".equals(customFieldVO.getTextType())) {
                customFieldVO.setDictList(sysDictDao.selectMoreDict(customFieldVO.getCode()));
            }
        }
        return customFieldVOS;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer insertQueryScheme(QuerySchemaVo querySchemaVo) {
        if (querySchemaVo.getQuerySchemeId() == null || querySchemaVo.getQuerySchemeId() == 0) {
            QueryScheme queryScheme = new QueryScheme();
            BeanUtils.copyProperties(querySchemaVo, queryScheme);
            queryScheme.setSort(querySchemaVo.getSort());
            queryScheme.setIsDefault(0);
            querySchemeDao.insertSelective(queryScheme);
            return queryScheme.getQuerySchemeId();
        } else {
            QueryScheme queryScheme = new QueryScheme();
            BeanUtils.copyProperties(querySchemaVo, queryScheme);
            queryScheme.setIsDefault(0);
            queryScheme.setSort(querySchemaVo.getSort());
            queryScheme.setQuerySchemeId(querySchemaVo.getQuerySchemeId());
            querySchemeDao.insertSelective(queryScheme);
            return queryScheme.getQuerySchemeId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQueryScheme(QuerySchemaVo querySchemaVo) {
        if (querySchemaVo.getQuerySchemeId() != null) {
            deleteQuery(querySchemaVo.getQuerySchemeId());
        }
        Integer integer = insertQueryScheme(querySchemaVo);
        List<QuerySchemeField> fieldList = new ArrayList<>();
        List<QuerySchemeSort> sortList = new ArrayList<>();
        operateFieldAndSort(querySchemaVo, fieldList, sortList, integer);
        if (fieldList.size() > 0) {
            querySchemeFieldDao.insertBatch(fieldList);
        }
        if (sortList.size() > 0) {
            querySchemeSortDao.insertBatch(sortList);
        }
    }

    private void operateFieldAndSort(QuerySchemaVo querySchemaVo, List<QuerySchemeField> fieldList, List<QuerySchemeSort> sortList, Integer integer) {
        if (querySchemaVo.getFieldId().size() > 0) {
            for (int i = 0; i < querySchemaVo.getFieldId().size(); i++) {
                QuerySchemeField querySchemeField = new QuerySchemeField();
                querySchemeField.setQuerySchemeId(integer);
                querySchemeField.setFieldId(querySchemaVo.getFieldId().get(i));
                querySchemeField.setSort(i);
                querySchemeField.setCreateTime(new Date());
                fieldList.add(querySchemeField);
            }
        }
        if (querySchemaVo.getSorts().size() > 0) {
            for (int i = 0; i < querySchemaVo.getSorts().size(); i++) {
                QuerySchemeSort querySchemeSort = new QuerySchemeSort();
                querySchemeSort.setFieldId(querySchemaVo.getSorts().get(i).getFieldId());
                querySchemeSort.setOrderByRule(querySchemaVo.getSorts().get(i).getOrderByRule());
                querySchemeSort.setSort(i);
                querySchemeSort.setCreateTime(new Date());
                querySchemeSort.setQuerySchemeId(integer);
                sortList.add(querySchemeSort);
            }
        }
    }

    @Override
    public PageResult<UserArchiveVo> selectArchiveByHead(List<FieldValueForSearch> fieldValueForSearch, Integer pggeSize, Integer currentPage, UserSession userSession) {
        String whereSql = DealHeadParamUtil.getWhereSql(fieldValueForSearch, "arc.");
        PageHelper.startPage(currentPage, pggeSize);
        List<UserArchiveVo> list = userArchiveDao.selectArchiveByHead(whereSql, userSession.getCompanyId());
        return new PageResult<>(list);
    }

    @Override
    public void deleteCareerTrack(Integer id) {
        archiveCareerTrackdao.deleteCareerTrack(id);
    }


    public List<TableHead> getDefaultArcHead() {
        List<TableHead> headList = new ArrayList<>();
        String[] strings = {"姓名", "工号", "单位", "部门", "岗位", "入职日期", "试用期到期时间", "直接上级", "联系电话", "任职类型"};
        String[] codeList = {"userName", "employeeNumber", "businessUnitName", "orgName", "postName", "hireDate",
                "probationDueDate", "supervisorUserName", "phone", "emplymentType"};
        for (int i = 0; i < strings.length; i++) {
            TableHead arcHead = new TableHead();
            if ("姓名，性别，联系电话，年龄，出生日期，证件号码，最高学历,电子邮箱".contains(strings[i])) {
                arcHead.setWidth("180px");
            }
            arcHead.setName(strings[i]);
            arcHead.setKey(codeList[i]);
            arcHead.setIndex(i);
            arcHead.setIsShow(1);
            headList.add(arcHead);
        }
        return headList;
    }

//    private Integer getQuerySchemaId(Integer archiveId){
//        Integer querySchemaId=querySchemeDao.selectDefaultQuerySchemaIdByArchiveId(archiveId);
//        if(null!=querySchemaId && 0!=querySchemaId){
//            return null;
//        }else {
//            return querySchemaId;
//        }
//    }

    private void checkEmployeeNumber(UserSession userSession, UserArchive userArchive) {
        String empNumber = employeeNumberRuleService.createEmpNumber(userSession.getCompanyId());
        List<Integer> employnumberList = userArchiveDao.selectEmployNumberByCompanyId(userSession.getCompanyId(), userArchive.getEmployeeNumber());
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(employnumberList) || (employnumberList.size() == 1 && employnumberList.get(0).equals(userArchive.getArchiveId()))) {
            userArchive.setEmployeeNumber(empNumber);
        } else {
            ExceptionCast.cast(CommonCode.EMPLOYEENUMBER_IS_EXIST);
        }
    }
}







