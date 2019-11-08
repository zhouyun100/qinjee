package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.AttachmentVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportArc;
import com.qinjee.masterdata.model.vo.staff.export.ExportBusiness;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Administrator
 */
public interface IStaffCommonService {

    /**
     * 新增自定义表
     * @param customArchiveTable
     * @return
     */
   void  insertCustomArichiveTable(CustomArchiveTable customArchiveTable,UserSession userSession);

    /**
     * 逻辑删除自定义表
     * @param list
     * @return
     */
     void deleteCustomArchiveTable(List<Integer> list) throws Exception;

    /**
     * 自定义表修改
     * @param customArchiveTable
     * @return
     */
   void updateCustomArchiveTable(CustomArchiveTable customArchiveTable);


    /**
     * 分页展示自定义表
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageResult<CustomArchiveTable> selectCustomArchiveTable(Integer currentPage, Integer pageSize,UserSession userSession);

    /**
     * 新增自定义组
     * @param customArchiveGroup
     * @return
     */

    void insertCustomArchiveGroup(CustomArchiveGroup customArchiveGroup,UserSession userSession);

    /**
     * 删除自定义组
     * @param list
     * @return
     */
    void deleteCustomArchiveGroup(List<Integer> list) throws Exception;

    /**
     * 自定义组修改
     * @param customArchiveGroup
     * @return
     */
    void updateCustomArchiveGroup(CustomArchiveGroup customArchiveGroup);

    /**
     *分页展示自定义组中的表
     * @param currentPage
     * @param pageSize
     * @param customArchiveGroupId
     * @return
     */
  PageResult<CustomArchiveField>selectArchiveFieldFromGroup(Integer currentPage, Integer pageSize, Integer customArchiveGroupId);

    /**
     * 新增自定义字段
     * @param customArchiveField
     * @return
     */
  void insertCustomArchiveField(CustomArchiveField customArchiveField,UserSession userSession);

    /**
     * 逻辑删除自定义字段
     * @param list
     * @return
     */
     void deleteCustomArchiveField(List<Integer> list) throws Exception;

    /**
     * 修改自定义字段类型
     * @param customArchiveField
     * @returnvoid
     */
    void updateCustomArchiveField(CustomArchiveField customArchiveField);

    /**
     *分页展示指定自定义表下的自定义字段
     * @param currentPage
     * @param pageSize
     * @param customArchiveTableId
     * @return
     */
    PageResult<CustomArchiveField> selectCustomArchiveField(Integer currentPage, Integer pageSize, Integer customArchiveTableId);
    /**
     * 通过字段id找到自定义字段信息
     * @param customArchiveFieldId
     * @return
     */
    CustomArchiveField selectCustomArchiveFieldById(Integer customArchiveFieldId);
    /**
     * 将数据插入自定义数据表
     * @param customArchiveTableData
     * @return
     */
    void insertCustomArchiveTableData(CustomArchiveTableData customArchiveTableData, UserSession userSession);

    /**
     * 修改自定义字段表中的数据
     * @param  customArchiveTableData
     * @return
     */
    void updateCustomArchiveTableData(CustomArchiveTableData customArchiveTableData);

    /**
     * 展示自定义表数据内容,返回自定义表数据
     * @param currentPage
     * @param pageSize
     * @param customArchiveTableId
     * @return
     */
   PageResult<CustomArchiveTableData> selectCustomArchiveTableData(Integer currentPage, Integer pageSize, Integer customArchiveTableId);

    /**
     * 获取字段校验类型
     * @param fieldId
     * @return
     */
    List<String> checkField(Integer fieldId);

    /**
     * 模板导入
     * @param path
     * @param userSession
     * @return
     */
    void importFile(String path,UserSession userSession ) throws IOException, NoSuchFieldException, IllegalAccessException;

    /**
     * 模板导出档案
     * @return
     */
    void exportArcFile(ExportArc exportArcVo, HttpServletResponse response, UserSession userSession) ;

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    void putFile(MultipartFile multipartFile, AttachmentVo attachmentVo, UserSession userSession) throws Exception;
    /**
     * 据档案显示对应权限下的单位
     * @param userSession
     * @return
     */
    List<Integer> getCompanyId(UserSession userSession);

    /**
     * 根据档案id显示对应权限下的子集部门
     * @param orgId
     * @return
     */
    List<Integer> getOrgIdByCompanyId(Integer orgId);

    /**
     * 显示部门下的岗位
     * @param orgId
     * @return
     */
    List<Post> getPostByOrgId(@Param("orgId") Integer orgId);

    /**
     * 展示部门的自定义表
     * @param userSession
     * @return
     */
    List<String> selectTableFromOrg(UserSession userSession);

    /**
     * 展示自定义字段的值
     * @param customArchiveFieldId
     * @return
     */
    List<String> selectFieldValueById(Integer customArchiveFieldId);

    /**
     * @param userSession
     */
    void exportPreFile(ExportArc exportArcVo, HttpServletResponse response, UserSession userSession) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 下载文件
     * @param path
     */
    void downLoadFile(String path) throws Exception;

    /**
     * 导出业务类
     * @param exportBusiness
     * @param response
     * @param userSession
     */
    void exportBusiness(ExportBusiness exportBusiness, HttpServletResponse response, UserSession userSession);

}
