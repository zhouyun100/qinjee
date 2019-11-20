package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import com.qinjee.masterdata.model.vo.staff.StatusChangeVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface IStaffPreEmploymentService {

    /**
     * 发送邮件
     * @param prelist
     * @param conList
     * @param content
     * @param subject
     * @param filepath
     * @return
     */
    void sendManyMail(List<Integer> prelist, List<Integer> conList, String content, String subject, List<String> filepath) throws Exception;

    /**
     * 验证手机号
     * @param phoneNumber
     * @return
     */
    boolean checkPhone(String phoneNumber);

    /**
     * 验证邮箱
     * @param mail
     * @return
     */
    boolean checkMail(String mail);

    /**
     * 新增预入职变更表
     * @param preEmploymentId,statusChangeVo
     * @return
     */

    void insertStatusChange(UserSession userSession, Integer preEmploymentId,
                            StatusChangeVo statusChangeVo);

    /**
     * 根据机构展示预入职表
     * @param companyId
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageResult<PreEmployment> selectPreEmployment(Integer companyId, Integer currentPage, Integer pageSize);

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
    void updatePreEmployment(PreEmploymentVo preEmploymentVo,UserSession userSession);

    /**
     * 新增预入职表
     * @param preEmploymentVo
     * @return
     */
    void insertPreEmployment(PreEmploymentVo preEmploymentVo,UserSession userSession);
    /**
     * 修改预入职信息(显示字段的信息)
     */
    void updatePreEmploymentField(Map<Integer, String> map) throws Exception;
    /**
     * 查看预入职信息(显示字段的信息)
     */
    Map<String,String> selectPreEmploymentField(UserSession userSession);
}
