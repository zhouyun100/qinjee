package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;


public interface OrganizationService {

    /**
     * 根据是否封存查询用户下所有的机构,树形结构展示
     * @param userSession
     * @param isEnable
     * @return
     */
    PageResult<OrganizationVO> getOrganizationPageTree(UserSession userSession, Short isEnable);



    /**
     * 根据条件分页查询用户下所有的机构
     * @param organizationPageVo
     * @return
     */
    PageResult<OrganizationVO> getOrganizationPageList(OrganizationPageVo organizationPageVo, UserSession userSession);

    /**
     * 根据是否封存查询用户下所有的机构,图形化展示
     * @param userSession
     * @param isEnable
     * @return
     */
    PageResult<OrganizationVO> getOrganizationGraphics(UserSession userSession, Short isEnable, Integer orgId);


    /**
     * 删除机构
     * @param orgIds
     * @return
     */
    ResponseResult deleteOrganizationById(List<Integer> orgIds,UserSession userSession);

    /**
     * 封存/封存机构
     * @param orgIds
     * @param isEnable
     * @return
     */
    ResponseResult sealOrganizationByIds(List<Integer> orgIds, Short isEnable);

    /**
     * 合并机构
     * @param newOrgName
     * @param targetOrgId
     * @param orgIds
     * @return
     */
    ResponseResult mergeOrganization(String newOrgName, Integer targetOrgId,  List<Integer> orgIds, UserSession userSession);

    /**
     * 机构负责人查询
     * @param userName
     * @return
     */
    ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(String userName);

    /**
     * 机构排序
     *
     * @return
     */
    ResponseResult sortOrganization(LinkedList<Integer> linkMap);

    /**
     * 划转机构
     * @param orgIds
     * @param targetOrgId
     * @return
     */
    ResponseResult transferOrganization(List<Integer> orgIds, Integer targetOrgId,UserSession userSession);

    /**
     * 机构职位树状图展示
     * @param userSession
     * @return
     */
    ResponseResult<List<OrganizationVO>> getOrganizationPositionTree(UserSession userSession, Short isEnable);

    /**
     * 下载模板
     * @param response
     * @return
     */
    ResponseResult downloadTemplate(HttpServletResponse response);



    /**
     * 导入机构Excel
     * @param file
     * @return
     */
    ResponseResult uploadExcel(MultipartFile file, UserSession userSession);

    /**
    * @Description:
    * @Param:
    * @return:
    * @Author: penghs
    * @Date: 2019/11/20 0020
    */
    OrganizationVO selectByPrimaryKey(Integer orgId);

    /**
    * @Description:
    * @Param:
    * @return:
    * @Author: penghs
    * @Date: 2019/11/20 0020
    */
    List<OrganizationVO> getOrganizationListByParentOrgId(Integer orgId);

    /**
    * @Description:
    * @Param:
    * @return:
    * @Author: penghs
    * @Date: 2019/11/20 0020
    */
    List<OrganizationVO> getAllOrganizationTree(UserSession userSession, Short isEnable);

    /** 
    * @Description:  导出所有机构到excel
    * @Param:  
    * @return:  
    * @Author: penghs 
    * @Date: 2019/11/20 0020 
    */
    ResponseResult downloadAllOrganizationToExcel(String filePath, UserSession userSession);

    /**
     * @Description: 根据选择的机构id导出Excel
     * @Param:
     * @return:
     * @Author: penghs
     * @Date: 2019/11/20 0020
     */
    ResponseResult downloadOrganizationToExcelByOrgId(String filePath,List<Integer> orgIds, UserSession userSession);


    ResponseResult addOrganization(String orgName, String orgType, String parentOrgId, String orgManagerId, UserSession userSession);


    ResponseResult editOrganization(String orgCode,String orgId,String orgName, String orgType, String parentOrgId, String orgManagerId, UserSession userSession);
}
