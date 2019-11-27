package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.CheckTypeDao;
import com.qinjee.masterdata.dao.CompanyCodeDao;
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.commondao.*;
import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.masterdata.model.entity.CustomArchiveGroup;
import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import com.qinjee.masterdata.model.entity.CustomArchiveTableData;
import com.qinjee.masterdata.model.vo.staff.BigDataVo;
import com.qinjee.masterdata.model.vo.staff.OrganzitionVo;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
public class StaffCommonServiceImpl implements IStaffCommonService {
    private static final Logger logger = LoggerFactory.getLogger ( StaffCommonServiceImpl.class );
    private static final String ARCHIVE = "档案";
    private static final String PREEMP = "预入职";
    private static final String IDTYPE = "证件类型";
    private static final String IDNUMBER = "证件号码";
    private static final String PHONE = "手机";
    private static final Integer MAXISM = 5242880;
    private static final String FILE = "FILE";
    private static final String PHOTO = "PHOTO";
    @Autowired
    private CustomArchiveTableDao customArchiveTableDao;
    @Autowired
    private CustomArchiveGroupDao customArchiveGroupDao;
    @Autowired
    private CustomArchiveFieldDao customArchiveFieldDao;
    @Autowired
    private CustomArchiveTableDataDao customArchiveTableDataDao;
    @Autowired
    private CustomArchiveFieldCheckDao customArchiveFieldCheckDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private CompanyCodeDao companyCodeDao;
    @Autowired
    private CheckTypeDao checkTypeDao;
    @Autowired
    private PostDao postDao;
    @Override
    public void insertCustomArichiveTable(CustomArchiveTable customArchiveTable, UserSession userSession) {
        customArchiveTable.setCompanyId ( userSession.getCompanyId () );
        customArchiveTable.setCreatorId ( userSession.getArchiveId () );
        customArchiveTable.setIsDelete ( ( short ) 0 );
        customArchiveTableDao.insertSelective ( customArchiveTable );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustomArchiveTable(List < Integer > list) throws Exception {
        Integer max = customArchiveTableDao.selectMaxPrimaryKey ();
        for (Integer integer : list) {
            if (max < integer) {
                throw new Exception ( "id有误" );
            }
        }
        customArchiveTableDao.deleteCustomTable ( list );
    }

    @Override
    public void updateCustomArchiveTable(CustomArchiveTable customArchiveTable) {

        customArchiveTableDao.updateByPrimaryKeySelective ( customArchiveTable );
    }

    @Override
    public PageResult < CustomArchiveTable > selectCustomArchiveTable(Integer currentPage, Integer pageSize, UserSession userSession) {
        PageHelper.startPage ( currentPage, pageSize );
        List < CustomArchiveTable > customArchiveTables = customArchiveTableDao.selectByPage ( userSession.getCompanyId () );
        for (CustomArchiveTable customArchiveTable : customArchiveTables) {
            logger.info ( "展示自定义表名{}", customArchiveTable.getTableName () );
        }
        return new PageResult <> ( customArchiveTables );
    }

    @Override
    public void insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup, UserSession userSession) {
        customArchiveGroup.setCreatorId ( userSession.getArchiveId () );
        customArchiveGroup.setIsDelete ( ( short ) 0 );

        customArchiveGroupDao.insertSelective ( customArchiveGroup );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomArchiveGroup(List < Integer > list) throws Exception {
        Integer max = customArchiveGroupDao.selectMaxPrimaryKey ();
        for (Integer integer : list) {
            if (max < integer) {
                throw new Exception ( "id有误" );
            }
        }
        customArchiveGroupDao.deleteCustomGroup ( list );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomArchiveGroup(CustomArchiveGroup customArchiveGroup) {
        customArchiveGroupDao.updateByPrimaryKeySelective ( customArchiveGroup );
    }

    @Override
    public PageResult < CustomArchiveField > selectArchiveFieldFromGroup(Integer currentPage, Integer pageSize, Integer customArchiveGroupId) {
        PageHelper.startPage ( currentPage, pageSize );
        //获得自定义组中自定义表id的集合
        List < CustomArchiveField > list = customArchiveFieldDao.selectCustomArchiveField ( customArchiveGroupId );
        return new PageResult <> ( list );
    }

    @Override
    public void insertCustomArchiveField(CustomArchiveField customArchiveField, UserSession userSession) {
        customArchiveField.setCreatorId ( userSession.getArchiveId () );
        customArchiveField.setIsDelete ( ( short ) 0 );
        customArchiveFieldDao.insertSelective ( customArchiveField );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomArchiveField(List < Integer > list) throws Exception {
        Integer max = customArchiveFieldDao.selectMaxPrimaryKey ();
        for (Integer integer : list) {
            if (max < integer) {
                throw new Exception ( "id有误" );
            }
        }
        customArchiveFieldDao.deleteCustomField ( list );
    }

    @Override
    public void updateCustomArchiveField(CustomArchiveField customArchiveField) {
        customArchiveFieldDao.updateByPrimaryKeySelective ( customArchiveField );
    }

    @Override
    public PageResult < CustomArchiveField > selectCustomArchiveField(Integer currentPage, Integer pageSize,
                                                                      Integer customArchiveTableId) {

        PageHelper.startPage ( currentPage, pageSize );
        //根据自定义表找自定义字段id
        List < CustomArchiveField > list = customArchiveFieldDao.selectFieldByTableId ( customArchiveTableId );
        return new PageResult <> ( list );
    }

    @Override
    public CustomArchiveField selectCustomArchiveFieldById(Integer customArchiveFieldId) {
        return customArchiveFieldDao.selectByPrimaryKey ( customArchiveFieldId );
    }

    @Override
    public Integer getCompanyId(UserSession userSession) {
        return userSession.getCompanyId ();
    }

    @Override
    public OrganzitionVo getOrgIdByCompanyId(Integer companyId, UserSession userSession) {
        return getOrganTree ( companyId, userSession.getArchiveId () );
    }

    private OrganzitionVo getOrganTree(Integer companyId, Integer archiveId) {
        OrganzitionVo organzitionVo = new OrganzitionVo ();
        organzitionVo.setOrg_id ( companyId );
        //获取该人员下的所有权限机构
        List < OrganzitionVo > list = organizationDao.selectorgBycomanyIdAndUserAuth ( companyId, archiveId );

        //取一级子机构
        List < OrganzitionVo > organzitionVoList = list.stream ().filter ( organzitionVo1 -> {
            if (organzitionVo1.getOrg_parent_id ().equals ( 0 )) {
                organzitionVo.setOrg_name ( organzitionVo1.getOrg_name () );
                return true;
            } else {
                return false;
            }
        } ).collect ( Collectors.toList () );

        list.removeAll ( organzitionVoList );

        handlerAllChildOrganizationTree ( organzitionVoList, list );

        organzitionVo.setList ( organzitionVoList );

        return organzitionVo;
    }

    private void handlerAllChildOrganizationTree(List < OrganzitionVo > organzitionVoList, List < OrganzitionVo > orgList) {

        for (OrganzitionVo organzitionVo : organzitionVoList) {
            List < OrganzitionVo > childOrgList = orgList.stream ().filter ( org -> {
                if (organzitionVo.getOrg_id ().equals ( org.getOrg_parent_id () )) {
                    return true;
                } else {
                    return false;
                }
            } ).collect ( Collectors.toList () );

            orgList.removeAll ( childOrgList );

            if (!CollectionUtils.isEmpty ( childOrgList )) {
                organzitionVo.setList ( childOrgList );
                handlerAllChildOrganizationTree ( organzitionVoList, orgList );
            }
        }
    }

    @Override
    public String getPostByOrgId(Integer orgId) {

        Map < Integer, String > postByOrgId = postDao.getPostByOrgId ( orgId );
        return JSON.toJSONString ( postByOrgId );
    }

    @Override
    public List < String > selectTableFromOrg(UserSession userSession) {
        return customArchiveTableDao.selectNameBycomId ( userSession.getCompanyId () );
    }
    @Override
    public List < String > selectFieldValueById(Integer customArchiveFieldId) {
        //找到企业代码id
        Integer id = customArchiveFieldDao.selectCodeId ( customArchiveFieldId );
        //找到自定义字段的值
        return companyCodeDao.selectValue ( id );
    }
    @Override
    public void insertCustomArchiveTableData(BigDataVo bigDataVo, UserSession userSession) {
        //将前端传过来的json串进行解析
        StringBuilder bigData = new StringBuilder ();
        JSONObject jsonObject = JSONObject.parseObject ( bigDataVo.getJoonString () );
        List < String > strings = new ArrayList <> ( jsonObject.keySet () );
        for (String string : strings) {
            bigData.append ( "@@" ).append ( string ).append ( "@@:" ).append ( jsonObject.get ( string ) );
        }
        CustomArchiveTableData customArchiveTableData = new CustomArchiveTableData ();
        customArchiveTableData.setBigData ( bigData.toString () );
        customArchiveTableData.setBusinessId ( bigDataVo.getBusinessId () );
        customArchiveTableData.setTableId
                ( customArchiveTableDao.selectTableIdByNameAndCompanyId ( bigDataVo.getTitle (), userSession.getCompanyId () ) );
        customArchiveTableData.setIsDelete ( 0 );
        customArchiveTableData.setOperatorId ( userSession.getArchiveId () );
        customArchiveTableDataDao.insertSelective ( customArchiveTableData );
    }

    @Override
    public void updateCustomArchiveTableData(CustomArchiveTableData customArchiveTableData) {
        customArchiveTableDataDao.updateByPrimaryKey ( customArchiveTableData );
    }

    @Override
    public PageResult < CustomArchiveTableData > selectCustomArchiveTableData(Integer currentPage, Integer pageSize, Integer customArchiveTableId) {
        PageHelper.startPage ( currentPage, pageSize );
        //通过自定义表id找到数据id集合
        List < Integer > integerList = customArchiveTableDataDao.selectCustomArchiveTableId ( customArchiveTableId );
        List < CustomArchiveTableData > list = customArchiveTableDataDao.selectByPrimaryKeyList ( integerList );
        return new PageResult <> ( list );
    }

}






