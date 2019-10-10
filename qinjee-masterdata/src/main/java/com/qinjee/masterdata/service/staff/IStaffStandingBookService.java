package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.StandingBook;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;

import java.util.List;

/**
 * @author Administrator
 */

public interface IStaffStandingBookService {


    /**新增黑名单
     * @param blacklists
     * @param dataSource
     * @param userSession
     * @return
     */
    void insertBlackList(List<BlackListVo> blacklists, String dataSource, UserSession userSession);

    /**
     * 批量删除黑名单
     * @param list
     * @return
     */
    void deleteBlackList(List<Integer> list) throws Exception;

    /**
     * 更新黑名单表
     * @param blacklist
     * @return
     */
    void updateBalckList(Blacklist blacklist);

    /**
     * 分页展示黑名单
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageResult<Blacklist> selectBalckList(Integer currentPage, Integer pageSize);

    /**
     * 删除台账
     * @param standingBookId
     * @return
     */
    void deleteStandingBook(Integer standingBookId);

    /**
     * 新增与修改台账
     * @param userSession
     * @param standingBookInfo
     * @return
     */
    void saveStandingBook(UserSession userSession,StandingBookInfo standingBookInfo);

    /**
     * 展示台账表
     * @param id
     * @return
     */
   StandingBookInfo selectStandingBook(Integer id);

    /**
     * 查看我的台账，不含是否共享
     * @param userSession
     * @return
     */
    List<StandingBook> selectMyStandingBook(UserSession userSession);

    /**
     * 查看我的台账,含是否共享
     * @param userSession
     * @return
     */
    List<StandingBook> selectMyStandingBookShare(UserSession userSession);

    /**
     * 人员查询操作
     * @param stangdingBookId
     * @param archiveType
     * @param orgId
     * @param type
     * @return
     */
   List<UserArchive> selectStaff(Integer stangdingBookId, String archiveType, Integer orgId, String type);
}
