package com.qinjee.masterdata.service.organation.impl;

import com.qinjee.masterdata.dao.organation.UserRoleDao;
import com.qinjee.masterdata.model.entity.UserRole;
import com.qinjee.masterdata.service.organation.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月17日 17:53:00
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public List<UserRole> getUserRoleList(Integer archiveId) {
        List<UserRole> userRoleList = userRoleDao.getUserRoleList(archiveId);
        List<UserRole> userRoles = userRoleList.stream().filter(userRole -> {
            Date trusteeshipEndTime = userRole.getTrusteeshipEndTime();
            if (trusteeshipEndTime != null) {
                long currentTimeMillis = System.currentTimeMillis();
                long endTime = trusteeshipEndTime.getTime();
                if (endTime < currentTimeMillis) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        return userRoles;
    }
}
