package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author Administrator
 */

public interface IStaffStandingBookService {
    /**
     * 新增黑名单
     * @param blacklists
     * @return
     */
    ResponseResult insertBlackList(List<Blacklist> blacklists);

    /**
     * 批量删除黑名单
     * @param list
     * @return
     */
    ResponseResult deleteBlackList(List<Integer> list);

    /**
     * 更新黑名单表
     * @param blacklist
     * @return
     */
    ResponseResult updateBalckList(Blacklist blacklist);

    /**
     * 分页展示黑名单
     * @param currentPage
     * @param pageSize
     * @return
     */
    ResponseResult<PageResult<Blacklist>> selectBalckList(Integer currentPage, Integer pageSize);

    /**
     * 删除台账
     * @param standingBookId
     * @return
     */
    ResponseResult deleteStandingBook(Integer standingBookId);

    /**
     * 新增与修改台账
     * @param standingBookInfo
     * @return
     */
    ResponseResult saveStandingBook(StandingBookInfo standingBookInfo);

    /**
     * 展示台账表
     * @param id
     * @return
     */
    ResponseResult selectStandingBook(Integer id);
}
