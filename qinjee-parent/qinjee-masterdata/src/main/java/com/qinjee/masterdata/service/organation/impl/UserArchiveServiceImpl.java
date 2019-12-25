package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;
import com.qinjee.masterdata.model.vo.organization.page.UserArchivePageVo;
import com.qinjee.masterdata.service.organation.UserArchiveService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月24日 15:25:00
 */
@Service
public class UserArchiveServiceImpl implements UserArchiveService {
    @Autowired
    private UserArchiveDao userArchiveDao;

    @Override
    public ResponseResult<PageResult<UserArchive>> getUserArchiveList(UserArchivePageVo pageQueryVo, UserSession userSession) {
        if(pageQueryVo.getCurrentPage() != null && pageQueryVo.getPageSize() != null){
            PageHelper.startPage(pageQueryVo.getCurrentPage(), pageQueryVo.getPageSize());
        }
        List<UserArchive> userArchiveList = userArchiveDao.getUserArchiveList(pageQueryVo);
        PageResult<UserArchive> pageResult = new PageResult<>(userArchiveList);
        return new ResponseResult<>(pageResult);
    }


    @Override
    public ResponseResult<Integer> addUserArchive(UserArchiveVo userArchiveVo, UserSession userSession) {
        UserArchive userArchive = new UserArchive();
        BeanUtils.copyProperties(userArchiveVo, userArchive);
        userArchive.setOperatorId(userSession.getArchiveId());
        userArchive.setIsDelete((short) 0);
        userArchiveDao.insertSelective(userArchive);
        return new ResponseResult(userArchive.getArchiveId());
    }

    @Override
    public ResponseResult deleteUserArchive(List<Integer> archiveIds) {
        if(!CollectionUtils.isEmpty(archiveIds)){
            for (Integer archiveId : archiveIds) {
                UserArchive userArchive = new UserArchive();
                userArchive.setIsDelete((short) 1);
                userArchive.setArchiveId(archiveId);
                userArchiveDao.updateByPrimaryKeySelective(userArchive);
            }
        }
        return new ResponseResult();
    }
}
