package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinjee.masterdata.dao.ArchiveCareerTrackDao;
import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.*;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CustomTableVO;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.model.vo.staff.export.ExportArcVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.service.custom.CustomTableFieldService;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.masterdata.utils.SqlUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
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
    private CustomTableFieldDao customTableFieldDao;
    @Autowired
    private ArchiveCareerTrackDao archiveCareerTrackdao;
    @Autowired
    private CustomTableFieldService customTableFieldService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArchiveById(List < Integer > archiveid) {
        userArchiveDao.deleteArchiveByIdList ( archiveid );
    }

    @Override
    public void resumeDeleteArchiveById(Integer archiveid) {
        userArchiveDao.resumeDeleteArchiveById ( archiveid );
    }

    @Override
    public void updateArchive(UserArchiveVo userArchiveVo, UserSession userSession) {
        UserArchive userArchive = new UserArchive ();
        BeanUtils.copyProperties ( userArchiveVo, userArchive );
        userArchive.setOperatorId ( userSession.getArchiveId () );
        userArchive.setIsDelete ( ( short ) 0 );
        userArchiveDao.updateByPrimaryKeySelective ( userArchive );
    }

    @Override
    public UserArchiveVoAndHeader selectArchive(UserSession userSession) {
        UserArchiveVo userArchiveVo = userArchiveDao.selectByPrimaryKey ( userSession.getArchiveId () );
        List < QueryScheme > list1 = querySchemeDao.selectQueryByArchiveId ( userSession.getArchiveId () );
        List < ArcHead > headList = getHeadList ( userSession, list1 );
        List<UserArchiveVo> userArchiveVos=new ArrayList <> (  );
        userArchiveVos.add ( userArchiveVo );
        UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
        PageInfo < UserArchiveVo > userArchiveVoPageInfo = new PageInfo <> ( userArchiveVos );
        PageResult < UserArchiveVo > userArchiveVoPageResult = new PageResult <> (  userArchiveVoPageInfo.getList () );
        userArchiveVoAndHeader.setPageResult (userArchiveVoPageResult );
        userArchiveVoAndHeader.setHeads ( headList );
        return userArchiveVoAndHeader;

    }

    private  List < ArcHead >  getHeadList(UserSession userSession, List < QueryScheme > list1) {
        List < ArcHead > headList = new ArrayList <> ();
        if (CollectionUtils.isEmpty ( list1 )) {
            headList = setDefaultHead ( userSession, headList, 0 );
        }
        //非默认显示方案表头
        for (QueryScheme queryScheme : list1) {
            if (queryScheme.getIsDefault ().equals ( 1 )) {
                //找到默认的显示方案然后设值
                headList = setDefaultHead ( userSession, headList, queryScheme.getQuerySchemeId () );
                break;
            } else {
                headList = setDefaultHead ( userSession, headList, 0 );
            }
        }
        return headList;
    }

    @Override
    public void insertArchive(UserArchiveVo userArchiveVo, UserSession userSession) {
        UserArchive userArchive = new UserArchive ();
        BeanUtils.copyProperties ( userArchiveVo, userArchive );
        userArchive.setOperatorId ( userSession.getArchiveId () );
        userArchive.setIsDelete ( ( short ) 0 );
        userArchiveDao.insertSelective ( userArchive );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArchiveField(Map < Integer, String > map) {
        customTableFieldDao.updatePreEmploymentField ( map );
    }

    @Override
    public UserArchiveVoAndHeader  selectArchivebatch(UserSession userSession, Integer orgId, Integer pageSize, Integer currentPage) {
        PageHelper.startPage ( currentPage, pageSize );
        List < UserArchiveVo > list = userArchiveDao.selectByOrgAndAuth ( orgId, userSession.getArchiveId (), userSession.getCompanyId () );
        List < QueryScheme > list1 = querySchemeDao.selectQueryByArchiveId ( userSession.getArchiveId () );
        //组装默认显示方案表头
        List < ArcHead > headList = getHeadList ( userSession, list1 );
        UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
        userArchiveVoAndHeader.setHeads ( headList );
        PageInfo < UserArchiveVo > userArchiveVoPageInfo = new PageInfo <> ( list );
        PageResult < UserArchiveVo > userArchiveVoPageResult = new PageResult <> ( userArchiveVoPageInfo.getList () );
        userArchiveVoAndHeader.setPageResult ( userArchiveVoPageResult );
        return userArchiveVoAndHeader;

    }

    private List < ArcHead > setDefaultHead(UserSession userSession, List < ArcHead > headList, Integer querySchemaId) {
        List < QuerySchemeField > querySchemeFieldList = querySchemeFieldDao.selectByQuerySchemeId ( querySchemaId );
        for (QuerySchemeField querySchemeField : querySchemeFieldList) {
            ArcHead arcHead = new ArcHead ();
            arcHead.setIndex ( querySchemeField.getSort () );

            CustomFieldVO customFieldVO = customTableFieldDao.selectFieldById ( querySchemeField.getFieldId (), userSession.getCompanyId (),
                    "ARC" );
            arcHead.setName ( customFieldVO.getFieldName () );
            arcHead.setKey ( customFieldVO.getFieldCode () );
            if ("org_id".equals ( customFieldVO.getFieldCode () )) {
                arcHead.setKey ( "orgName" );
            }
            if ("post_id".equals ( customFieldVO.getFieldCode () )) {
                arcHead.setKey ( "postName" );
            }
            if ("bussiness_unit_id".equals ( customFieldVO.getFieldCode () )) {
                arcHead.setKey ( "businessUnitName" );
            }
            if ("supervisor_id".equals ( customFieldVO.getFieldCode () )) {
                arcHead.setKey ( "supervisorUserName" );
            }
            arcHead.setIsShow ( 1 );
            headList.add ( arcHead );
            }
            if(querySchemaId.equals ( 0 )) {
                ArcHead arcHead = new ArcHead ();
                arcHead.setName ( "任职类型" );
                arcHead.setKey ( "employment_type" );
                arcHead.setIndex ( 10 );
                arcHead.setIsShow ( 1 );
                headList.add ( arcHead );
            }
        return headList;
    }

    public PageResult < UserArchiveVo > selectArchivebatchAndOrgList(UserSession userSession, List < Integer > orgList, Integer pageSize, Integer currentPage) {
        PageHelper.startPage ( currentPage, pageSize );
        List < UserArchiveVo > list = userArchiveDao.selectByOrgListAndAuth ( orgList, userSession.getArchiveId (), userSession.getCompanyId () );
        return new PageResult <> ( list );
    }

    @Override
    public void insertUserArchivePostRelation(UserArchivePostRelationVo userArchivePostRelationVo, UserSession userSession) {
        UserArchivePostRelation userArchivePostRelation = new UserArchivePostRelation ();
        BeanUtils.copyProperties ( userArchivePostRelationVo, userArchivePostRelation );
        //通过工号查询档案id
        Integer id = userArchiveDao.selectArchiveIdByNumber ( userArchivePostRelationVo.getEmployeeNumber () );
        userArchivePostRelation.setArchiveId ( id );
        userArchivePostRelation.setOperatorId ( userSession.getArchiveId () );
        userArchivePostRelation.setIsDelete ( ( short ) 1 );
        userArchivePostRelationDao.insertSelective ( userArchivePostRelation );
    }

    @Override
    public Map < String, String > selectNameAndNumber(Integer id) {
        return userArchiveDao.selectNameAndNumber ( id );
    }

    @Override
    public String selectOrgName(Integer id) {
        return organizationDao.selectOrgName ( id );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExportFile selectArchiveByQueryScheme( UserSession userSession, List < Integer > archiveIdList) throws IllegalAccessException {
        ExportFile exportFile = new ExportFile ();
        Integer j=0;
        List < CustomFieldVO > orderNotIn = new ArrayList <> ();
        Map < Integer, Map < String, Object > > userArchiveListCustom;
        List < QueryScheme > list = querySchemeDao.selectQueryByArchiveId ( userSession.getArchiveId () );
        for (QueryScheme queryScheme : list) {
            if (queryScheme.getIsDefault () == 1) {
                j++;
                StringBuilder stringBuffer = new StringBuilder ();
                String order = null;
                //根据查询方案id找到需要展示字段的id以及按顺序排序
                List < Integer > integers1 = querySchemeFieldDao.selectFieldIdWithSort ( queryScheme.getQuerySchemeId (  ) );
                List < Integer > integers2 = querySchemeSortDao.selectSortId ( queryScheme.getQuerySchemeId (  ) );

                CustomTableVO customTableVO = new CustomTableVO ();
                customTableVO.setCompanyId ( userSession.getCompanyId () );
                customTableVO.setFuncCode ( "ARC" );
                List < CustomTableVO > customTableVOS = customTableFieldService.searchCustomTableListByCompanyIdAndFuncCode ( customTableVO );
                //找到tableId

                //拼接order
                List < CustomFieldVO > customFields = customTableFieldDao.selectFieldByIdList ( integers2,userSession.getCompanyId (),"ARC" );
                for (CustomFieldVO customFieldVO : customFields) {
                    if (customFieldVO.getIsSystemDefine () == 1) {
                        stringBuffer.append ( "t." ).append ( customTableFieldDao.selectFieldCodeById ( customFieldVO.getFieldId () ) + "\t" )
                                .append ( getSort ( querySchemeSortDao.selectSortById ( customFieldVO.getFieldId () ) ) ).append ( "," );
                    } else {
                        orderNotIn.add ( customFieldVO );
                        stringBuffer.append ( "t." ).append ( "t" + customFieldVO.getFieldId () + "\t" ).
                                append ( getSort ( querySchemeSortDao.selectSortById ( customFieldVO.getFieldId () ) ) ).append ( "," );
                    }
                }
                assert order != null;
                int i = stringBuffer.toString ().lastIndexOf ( "," );
                order = stringBuffer.toString ().substring ( 0, i );
                //调用接口查询机构权限范围内的档案集合
                //进行sql查询，返回数据，档案在机构范围内
                userArchiveListCustom = userArchiveDao.getUserArchiveListCustom ( getBaseSql ( orderNotIn, integers1, userSession.getCompanyId (), customTableVOS ), order );
                List < Integer > integers = new ArrayList <> ( userArchiveListCustom.keySet () );
                integers.removeAll ( archiveIdList );
                for (Integer integer : integers) {
                    userArchiveListCustom.remove ( integer );
                }
                exportFile.setMap ( userArchiveListCustom );
                exportFile.setTittle ( "ARC" );
                return exportFile;
            }
        }
        if(j==0){
                List < ExportArcVo > exportArcVoList;
                exportArcVoList = userArchiveDao.selectDownLoadVoList ( archiveIdList );
                userArchiveListCustom = getMap ( archiveIdList, exportArcVoList );
                exportFile.setMap ( userArchiveListCustom );
                exportFile.setTittle ( "ARC" );
                return exportFile;
        }
        return null;
    }


    @Override
    public List < String > selectFieldByTableIdAndAuth(Integer tableId, UserSession userSession) {
        return customTableFieldDao.selectFieldByTableIdAndAuth ( tableId, userSession.getArchiveId () );
    }

    @Override
    public List < String > selectFieldByArcAndAuth(UserSession userSession) {
        return customTableFieldDao.selectFieldByArcAndAuth ( userSession.getArchiveId (), userSession.getCompanyId () );
    }

    @Override
    public List < ArchiveCareerTrack > selectCareerTrack(Integer id) {
        return archiveCareerTrackdao.selectCareerTrack ( id );
    }

    @Override
    public void updateCareerTrack(ArchiveCareerTrackVo archiveCareerTrackVo, UserSession userSession) throws IllegalAccessException {
        Map < String, Object > map = getParamMap ( archiveCareerTrackVo, userSession );
        archiveCareerTrackdao.updateArchiveCareerTrack ( map );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertCareerTrack(ArchiveCareerTrackVo archiveCareerTrackVo, UserSession userSession) throws IllegalAccessException {
        Map < String, Object > map = getParamMap ( archiveCareerTrackVo, userSession );
        archiveCareerTrackdao.insertArchiveCareerTrack ( map );
    }

    @Override
    public void deleteCareerTrack(Integer id) {
        archiveCareerTrackdao.deleteCareerTrack ( id );
    }

    @Override
    public UserArchiveVoAndHeader selectArchiveSingle(Integer id,UserSession userSession) {

        UserArchiveVo userArchiveVo = userArchiveDao.selectByPrimaryKey ( id );
        List < QueryScheme > list1 = querySchemeDao.selectQueryByArchiveId ( id );
        List < ArcHead > headList = getHeadList ( userSession, list1 );
        List<UserArchiveVo> userArchiveVos=new ArrayList <> (  );
        userArchiveVos.add ( userArchiveVo );
        PageInfo < UserArchiveVo > userArchiveVoPageInfo = new PageInfo <> ( userArchiveVos );
        UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
        PageResult < UserArchiveVo > userArchiveVoPageResult = new PageResult <> ( userArchiveVoPageInfo.getList () );
        userArchiveVoAndHeader.setPageResult ( userArchiveVoPageResult );
        userArchiveVoAndHeader.setHeads ( headList );
        return userArchiveVoAndHeader;
    }

    @Override
    public List < UserArchiveVo > selectUserArchiveByName(String name) {
        return userArchiveDao.selectUserArchiveByName ( name );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultQuerySchme(Integer querySchmeId, UserSession userSession) {
        List < QueryScheme > list = querySchemeDao.selectQueryByArchiveId ( userSession.getArchiveId () );
        for (QueryScheme queryScheme : list) {
            if (querySchmeId.equals ( queryScheme.getQuerySchemeId () )) {
                queryScheme.setIsDefault ( 1 );
                querySchemeDao.updateByPrimaryKeySelective ( queryScheme );
            } else {
                if (queryScheme.getIsDelete () == 1) {
                    queryScheme.setIsDefault ( 0 );
                    querySchemeDao.updateByPrimaryKeySelective ( queryScheme );
                }
            }
        }
    }

    private Map < String, Object > getParamMap(ArchiveCareerTrackVo archiveCareerTrackVo, UserSession userSession) throws IllegalAccessException {
        ArchiveCareerTrack archiveCareerTrack = new ArchiveCareerTrack ();
        BeanUtils.copyProperties ( archiveCareerTrackVo, archiveCareerTrack );
        archiveCareerTrack.setOperatorId ( userSession.getArchiveId () );
        BusinessOrgPostPos businessOrgPostPos = organizationDao.selectManyId (
                archiveCareerTrackVo.getBusinessUnitName (), archiveCareerTrackVo.getOrgName (),
                archiveCareerTrackVo.getPostName (), archiveCareerTrackVo.getPositionName () );
        BeanUtils.copyProperties ( businessOrgPostPos, archiveCareerTrack );
        archiveCareerTrack.setCreateTime ( new Date () );
        Class clazz = archiveCareerTrack.getClass ();
        Map < String, Object > map = new HashMap <> ();
        Field[] fields = clazz.getDeclaredFields ();
        for (Field field : fields) {
            field.setAccessible ( true );
            map.put ( field.getName (), field.get ( archiveCareerTrack ) );
        }
        return map;
    }


    private Map < Integer, Map < String, Object > > getMap(List < Integer > archiveIdList, List < ExportArcVo > exportArcVoList) throws IllegalAccessException {
        Map < Integer, Map < String, Object > > userArchiveListCustom = new HashMap <> ();
        for (int i = 0; i < archiveIdList.size (); i++) {
            Map < String, Object > map = new HashMap <> ();
            //获得类
            Class clazz = exportArcVoList.get ( i ).getClass ();
            // 获取实体类的所有属性信息，返回Field数组
            Field[] fields = clazz.getDeclaredFields ();
            for (Field field : fields) {
                field.setAccessible ( true );
                map.put ( field.getName (), String.valueOf ( field.get ( exportArcVoList.get ( i ) ) ) );
            }
            userArchiveListCustom.put ( archiveIdList.get ( i ), map );
        }
        return userArchiveListCustom;
    }


    /**
     * list是所查询项的集合
     */
    private String getBaseSql(List < CustomFieldVO > notIn, List < Integer > integers, Integer companyId, List < CustomTableVO > tableVOS) {
        List < CustomFieldVO > inList = new ArrayList <> ();
        List < CustomFieldVO > outList = new ArrayList <> ();
        List < CustomFieldVO > list = customTableFieldDao.selectFieldByIdList ( integers,companyId,"ARC" );
        for (CustomFieldVO customFieldVO : list) {
            if (customFieldVO.getIsSystemDefine () == 0) {
                outList.add ( customFieldVO );
            } else {
                inList.add ( customFieldVO );
            }
        }
        outList.addAll ( notIn );
        StringBuilder stringBuffer = new StringBuilder ();
        String a = "";
        String b = "select distinct t.archive_id, ";
        String c = null;
        String d = "FROM ( select t0.*";
        for (CustomFieldVO customFieldVO : inList) {
            stringBuffer.append ( "t." ).append ( customFieldVO.getFieldCode () ).append ( "," );
        }
        if (!CollectionUtils.isEmpty ( outList )) {
            for (CustomFieldVO customFieldVO : outList) {
                stringBuffer.append ( "t.t" ).append ( customFieldVO.getFieldId () ).append ( "," );
            }
        }
        int i1 = stringBuffer.toString ().lastIndexOf ( "," );
        a = stringBuffer.toString ().substring ( 0, i1 );
        if (CollectionUtils.isEmpty ( outList )) {
            c = b + a + "\t" + d;
        } else {
            c = b + a + "\t" + d + ",";
        }
        return c + SqlUtil.getsql ( companyId, outList, tableVOS );
    }

    private String getSort(String sort) {
        if (sort != null) {
            if (sort.equals ( ASC )) {
                return "ASC";
            }
            if (sort.equals ( DESC )) {
                return "DESC";
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserArchivePostRelation(List < Integer > list) {
        userArchivePostRelationDao.deleteUserArchivePostRelation ( list );
    }

    @Override
    public void updateUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        userArchivePostRelationDao.updateByPrimaryKeySelective ( userArchivePostRelation );
    }

    @Override
    public PageResult < UserArchivePostRelation > selectUserArchivePostRelation(Integer currentPage, Integer pageSize,
                                                                                List < Integer > list) {
        PageHelper.startPage ( currentPage, pageSize );
        List < UserArchivePostRelation > relationList = userArchivePostRelationDao.selectByPrimaryKeyList ( list );
        return new PageResult <> ( relationList );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQueryScheme(List < Integer > list) {
        querySchemeDao.deleteQuerySchemeList ( list );
        //删除查询字段
        querySchemeFieldDao.deleteBySchemeIdList ( list );
        //删除排序字段
        querySchemeSortDao.deleteBySchemeIdList ( list );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List < QuerySchemeList > selectQueryScheme(UserSession userSession) {
        List < QuerySchemeList > lists = new ArrayList <> ();
        List < Integer > list = querySchemeDao.selectIdByArchiveId ( userSession.getArchiveId () );
        for (Integer integer : list) {
            QuerySchemeList querySchemeList = new QuerySchemeList ();
            //通过查询方案id找到显示字段
            List < QuerySchemeField > querySchemeFields = querySchemeFieldDao.selectByQuerySchemeId ( integer );
            //通过查询方案id找到排序字段
            List < QuerySchemeSort > querySchemeSorts = querySchemeSortDao.selectByQuerySchemeId ( integer );
            querySchemeList.setQuerySchemeFieldList ( querySchemeFields );
            querySchemeList.setQuerySchemeSortList ( querySchemeSorts );
            lists.add ( querySchemeList );
        }
        return lists;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQueryScheme(QueryArcVo queryArcVo) {
        QueryScheme queryScheme = queryArcVo.getQueryScheme ();
        List < QuerySchemeField > querySchemeFieldlist = queryArcVo.getQuerySchemeFieldlist ();
        List < QuerySchemeSort > querySchemeSortlist = queryArcVo.getQuerySchemeSortlist ();
        if (queryScheme.getQuerySchemeId () == null || queryScheme.getQuerySchemeId () == 0) {
            //说明是新增操作，新增查询方案
            querySchemeDao.insertSelective ( queryScheme );
            //新增查询字段与排序
            for (QuerySchemeField querySchemeField : querySchemeFieldlist) {
                querySchemeField.setQuerySchemeId ( queryScheme.getQuerySchemeId () );
            }
            for (QuerySchemeSort querySchemeSort : querySchemeSortlist) {
                querySchemeSort.setQuerySchemeId ( queryScheme.getQuerySchemeId () );
            }

            querySchemeFieldDao.insertBatch ( querySchemeFieldlist );
            querySchemeSortDao.insertBatch ( querySchemeSortlist );
        }
        //说明是更新操作
        //将list里面的queryschemeId设置为传过来的id
        for (QuerySchemeField querySchemeField : querySchemeFieldlist) {
            querySchemeField.setQuerySchemeId ( queryScheme.getQuerySchemeId () );
        }
        for (QuerySchemeSort querySchemeSort : querySchemeSortlist) {
            querySchemeSort.setQuerySchemeId ( queryScheme.getQuerySchemeId () );
        }
        querySchemeDao.updateByPrimaryKeySelective ( queryScheme );
        for (QuerySchemeField querySchemeField : querySchemeFieldlist) {
            querySchemeFieldDao.updateByPrimaryKey ( querySchemeField );
        }
        for (QuerySchemeSort querySchemeSort : querySchemeSortlist) {
            querySchemeSortDao.updateByPrimaryKey ( querySchemeSort );
        }
    }


}







