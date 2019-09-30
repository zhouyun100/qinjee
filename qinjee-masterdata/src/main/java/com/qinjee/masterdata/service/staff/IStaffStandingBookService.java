package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.vo.staff.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author Administrator
 */

public interface IStaffStandingBookService {


    /**新增黑名单
     * @param blacklists
     * @param dataSource
     * @param archiveId
     * @param companyId
     * @return
     */
    ResponseResult insertBlackList(List<BlackListVo> blacklists, String dataSource, Integer archiveId, Integer companyId);

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
     * @param archiveId
     * @param companyId
     * @param standingBookInfo
     * @return
     */
    ResponseResult saveStandingBook(Integer archiveId,Integer companyId,StandingBookInfo standingBookInfo);

    /**
     * 展示台账表
     * @param id
     * @return
     */
    ResponseResult selectStandingBook(Integer id);

    /**
     * 查看我的台账，不含是否共享
     * @param archiveId
     * @return
     */
    ResponseResult selectMyStandingBook(Integer archiveId);

    /**
     * 查看我的台账,含是否共享
     * @param archiveId
     * @param companyId
     * @return
     */
    ResponseResult selectMyStandingBookShare(Integer archiveId, Integer companyId);

    /**
     * 人员查询操作
     * @param stangdingBookId
     * @param archiveType
     * @param id
     * @param type
     * @return
     */
    ResponseResult selectStaff(Integer stangdingBookId, String archiveType, Integer id, String type);
}
