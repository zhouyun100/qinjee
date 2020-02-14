package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.page.UserArchivePageVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserArchiveService {
    /**
     * 根据条件分页查询用户信息
     *
     * @param userArchivePageVo
     * @param userSession
     * @return
     */
    ResponseResult<PageResult<UserArchiveVo>> getUserArchiveList(UserArchivePageVo userArchivePageVo, UserSession userSession);

    /**
     * 新增人员档案信息
     *
     * @param userArchiveVo
     * @return
     */
    @Transactional
    ResponseResult<Integer> addUserArchive(UserArchiveVo userArchiveVo, UserSession userSession);

    /**
     * 删除员工档案信息
     *
     * @param idsMap
     * @return
     */
    @Transactional
    void deleteUserArchive(Map<Integer, Integer> idsMap, Integer companyId,Integer operatorId);

    @Transactional
    void editUserArchive(UserArchiveVo userArchiveVo, UserSession userSession);

    @Transactional
    ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession, HttpServletResponse response) throws Exception;
    @Transactional
    void importToDatabase(String orgExcelRedisKey, UserSession userSession);
}
