package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.PageQueryVo;
import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

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
     * @param pageQueryVo
     * @param userSession
     * @return
     */
    ResponseResult<PageResult<UserArchive>> getUserArchiveList(PageQueryVo pageQueryVo, UserSession userSession);

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

}
