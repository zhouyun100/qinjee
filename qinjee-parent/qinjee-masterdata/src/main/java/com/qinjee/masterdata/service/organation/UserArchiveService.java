package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.page.UserArchivePageVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月24日 15:25:00
 */
public interface UserArchiveService {
    /**
     * 根据条件分页查询用户信息
     * @param userArchivePageVo
     * @param userSession
     * @return
     */
    ResponseResult<PageResult<UserArchiveVo>> getUserArchiveList(UserArchivePageVo userArchivePageVo, UserSession userSession);

    /**
     * 新增人员档案信息
     * @param userArchiveVo
     * @return
     */
    ResponseResult<Integer> addUserArchive(UserArchiveVo userArchiveVo, UserSession userSession);

    /**
     * 删除员工档案信息
     * @param archiveIds
     * @return
     */
    ResponseResult deleteUserArchive(List<Integer> archiveIds);

    @Transactional
    void editUserArchive(UserArchiveVo userArchiveVo, UserSession userSession);
}
