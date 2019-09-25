package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.staff.StatusChange;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface IStaffPreEmploymentService {
    /**
     * 发送短信
     * @param list
     * @param templateId
     * @param params
     * @return
     */
    ResponseResult sendMessage(List<Integer> list, Integer templateId, String[] params);

    /**
     * 发送邮件
     * @param prelist
     * @param conList
     * @param content
     * @param subject
     * @param filepath
     * @return
     */
    ResponseResult sendManyMail(List<Integer> prelist, List<Integer> conList, String content, String subject, String[] filepath);

    /**
     * 验证手机号
     * @param phoneNumber
     * @return
     */
    ResponseResult checkPhone(String phoneNumber);

    /**
     * 验证邮箱
     * @param mail
     * @return
     */
    ResponseResult checkMail(String mail);

    /**
     * 新增预入职变更表
     * @param preEmployment,statusChange
     * @return
     */

    ResponseResult insertStatusChange(PreEmployment preEmployment, StatusChange statusChange,String reason);

    /**
     * 根据机构展示预入职表
     * @param companyId
     * @param currentPage
     * @param pageSize
     * @return
     */
    ResponseResult<PageResult<PreEmployment>> selectPreEmployment(Integer companyId,Integer currentPage,Integer pageSize);

    /**
     * 逻辑删除预入职表
     * @param list
     * @return
     */
    ResponseResult deletePreEmployment(List<Integer> list);

    /**
     * 更新预入职表（物理表信息）
     * @param preEmployment
     * @return
     */
    ResponseResult updatePreEmployment(PreEmployment preEmployment);

    /**
     * 新增预入职表
     * @param preEmployment
     * @return
     */
    ResponseResult insertPreEmployment(PreEmployment preEmployment);
    /**
     * 修改预入职信息(显示字段的信息)
     */
    ResponseResult updatePreEmploymentField(Map<Integer, String> map);
    /**
     * 查看预入职信息(显示字段的信息)
     */
    ResponseResult<Map<String,String>> selectPreEmploymentField(Integer companyId);
}
