package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.service.staff.IStaffPreEmploymentService;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class StaffPreEmploymentServiceImpl implements IStaffPreEmploymentService {
    @Autowired
    private PreEmploymentDao preEmploymentDao;

    @Override
    public ResponseResult sendMessage(List<Integer> list, Integer templateId, String[] params) {
        Integer max=preEmploymentDao.selectMaxId();
        return null;
    }
}
