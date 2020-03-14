package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.PreRegistVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface IStaffPreEmploymentService {


    /**
     * 新增预入职变更表
     * @param statusChangeVo
     * @return
     */

    void insertStatusChange(UserSession userSession, StatusChangeVo statusChangeVo) throws Exception;

    /**
     * 根据机构展示预入职表
     * @return
     */
    PageResult<PreEmploymentVo> selectPreEmployment(UserSession userSession, RequestUserarchiveVo requestUserarchiveVo);

    /**
     * 逻辑删除预入职表
     * @param list
     * @return
     */
    void deletePreEmployment(List<Integer> list) throws Exception;

    /**
     * 更新预入职表（物理表信息）
     * @param preEmploymentVo
     * @return
     */
    void updatePreEmployment(PreEmploymentVo preEmploymentVo);

    /**
     * 新增预入职表
     * @param preEmploymentVo
     * @return
     */
    void insertPreEmployment(PreEmploymentVo preEmploymentVo,UserSession userSession) throws Exception;
    /**
     * 修改预入职信息(显示字段的信息)
     */
    void updatePreEmploymentField(Map<Integer, String> map) throws Exception;
    /**
     * 查看预入职信息(显示字段的信息)
     */
    Map<String,String> selectPreEmploymentField(UserSession userSession);

    PreEmployment selectPreEmploymentSingle(Integer employeeId);

    void confirmEmployment(List<Integer> list,UserSession userSession) throws Exception;

    DetailCount getReadyCount(UserSession userSession);

    PageResult<PreEmploymentVo> searchByHead(UserSession userSession, Integer currentPage, Integer pageSize, List<FieldValueForSearch> list);

    List<PreRegistVo> getEmploymentRegisterInfo(List<Integer> employmentIds,UserSession userSession) ;

    PageResult<PreEmploymentVo> getReadyPreEmployment(UserSession userSession, Integer pageSzie, Integer currentPage);
}
