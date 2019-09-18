package com.qinjee.masterdata.service.staff;

import com.qinjee.model.response.ResponseResult;

import java.util.List;

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

}
