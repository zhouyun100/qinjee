package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationExportBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationMergeBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationPageBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationTransferBO;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;


public interface OrganizationService  {

  /**
   * 根据是否封存查询用户下所有的机构,树形结构展示
   *接口已过时，新接口getAllOrganizationTree(UserSession userSession, Short isEnable);
   * @param userSession
   * @param isEnable
   * @return
   */
  @Deprecated
  PageResult<OrganizationVO> getOrganizationPageTree(UserSession userSession, Short isEnable);


  /**
   * 按条件分页查询机构（直属下级）
   *
   * @param organizationPageBO
   * @param userSession
   * @return
   */
  PageResult<OrganizationVO> getDirectOrganizationPageList(OrganizationPageBO organizationPageBO, UserSession userSession);


  /**
   * 删除机构（逻辑删除）
   * 维护历史机构表
   * 当机构在别处存在关联时，不允许删除
   *
   * @param orgIds
   * @return
   */
  @Transactional
  void deleteOrganizationById(List<Integer> orgIds,boolean cascadeDeletePost, UserSession userSession);

  /**
   * 封存/封存机构 包括其下级机构
   *
   * @param orgIds
   * @param isEnable
   * @return
   */
  @Transactional
  void sealOrganization(Integer archiveId,List<Integer> orgIds, Short isEnable);

  /**
   * 合并机构（将多个老机构划转到一个新生成的机构，老机构逻辑删除）
   * 需要维护历史机构表
   * 需要维护相关联的权限、用户之间的关系
   *
   * @return
   */
  @Transactional
  void mergeOrganization(OrganizationMergeBO orgMergeBO, UserSession userSession);


  /**
   * 机构排序
   *
   * @param linkMap 顺序机构id数组
   * @return
   */
  @Transactional
  void sortOrganization(LinkedList<Integer> linkMap);

  /**
   * 划转机构（将多个机构（只能同级）划转到目标机构）
   * 需要维护历史机构表
   * 需要维护相关联的权限、用户之间的关系
   *
   * @param orgTransferBO
   * @param userSession
   * @return
   */
  @Transactional
  void transferOrganization(OrganizationTransferBO orgTransferBO, UserSession userSession);

  /**
   * 获取机构岗位树，默认查询未封存的机构
   *
   * @param userSession
   * @param isEnable    1表示只显示未封存的岗位，0 表示显示封存+未封存的岗位
   * @return
   */
  List<OrganizationVO> getOrganizationPostTree(UserSession userSession, Short isEnable);



  /**
   * 根据机构id查询机构
   *
   * @param orgId
   * @return
   */
  OrganizationVO selectByPrimaryKey(Integer orgId);

  /**
   * 根据父机构id查询子机构列表
   *
   * @param orgId
   * @return
   */
  List<OrganizationVO> getOrganizationListByParentOrgId(Integer orgId);

  /**
   * 获取用户下所有机构树
   *
   * @param archiveId
   * @param isEnable
   * @return
   */
  List<OrganizationVO> getAllOrganizationTree(Integer archiveId, Short isEnable);


  /**
   * 新增机构，需要维护相关权限
   *
   * @param orgName
   * @param orgType
   * @param parentOrgId
   * @param orgManagerId
   * @param userSession
   * @return
   */
  @Transactional
  OrganizationVO addOrganization(String orgName, String orgCode,String orgType, String parentOrgId, String orgManagerId, UserSession userSession);


  /**
   * 编辑机构，当修改父级机构id时，注意机构（包含子机构）编码的更新，sortid也要注意
   * 同步维护历时机构表
   *
   * @param orgCode
   * @param orgId
   * @param orgName
   * @param orgType
   * @param parentOrgId
   * @param orgManagerId
   * @param userSession
   * @return ResponseResult
   */
  @Transactional
  void editOrganization(String orgCode, String orgId, String orgName, String orgType, String parentOrgId, String orgManagerId, UserSession userSession);

  List<OrganizationVO> getOrganizationGraphics(UserSession userSession, Integer layer, boolean isContainsCompiler, boolean isContainsActualMembers, Integer orgId, Short isEnable);

  List<OrganizationVO> exportOrganization(OrganizationExportBO orgExportBO, Integer archiveId);

  PageResult<OrganizationVO> getAllOrganizationPageList(OrganizationPageBO organizationPageBO, UserSession userSession);

  @Transactional
  void importToDatabase(String orgExcelRedisKey, UserSession userSession);

  ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession, HttpServletResponse response) throws Exception;


  void cancelImport(String redisKey, String errorInfoKey);


    String generateOrgCode(Integer orgId);

  Integer getBusunessUnitIdByOrgId(Integer orgId);
}


