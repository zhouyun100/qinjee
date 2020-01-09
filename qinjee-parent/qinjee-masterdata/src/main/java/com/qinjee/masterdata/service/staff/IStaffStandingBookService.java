package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.StandingBook;
import com.qinjee.masterdata.model.vo.staff.StandingBookReturnVo;
import com.qinjee.masterdata.model.vo.staff.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfoVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.model.request.UserSession;

import java.text.ParseException;
import java.util.List;

/**
 * @author Administrator
 */

public interface IStaffStandingBookService {


    /**新增黑名单
     * @param blacklists
     * @param userSession
     * @return
     */
    void insertBlackList(List<BlackListVo> blacklists,  UserSession userSession);

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
     * @return
     */
    List<Blacklist> selectBalckList(UserSession userSession);

    /**
     * 删除台账
     * @param standingBookId
     * @return
     */
    void deleteStandingBook(Integer standingBookId);

    /**
     * 新增与修改台账
     * @param userSession
     * @param standingBookInfoVo
     * @return
     */
    void saveStandingBook(UserSession userSession, StandingBookInfoVo standingBookInfoVo);

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
     * @return
     */
    List< UserArchiveVo > selectStaff(StandingBookReturnVo standingBookReturnVo, UserSession userSession) throws ParseException;

    void updateStandingBook(Integer standingBookId, String name);
}
