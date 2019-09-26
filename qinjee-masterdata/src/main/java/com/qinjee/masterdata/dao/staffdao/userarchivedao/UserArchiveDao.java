package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.PageQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface UserArchiveDao {
    int deleteByPrimaryKey(Integer archiveId);

    int insert(UserArchive record);

    int insertSelective(UserArchive record);

    UserArchive selectByPrimaryKey(Integer archiveId);

    int updateByPrimaryKeySelective(UserArchive record);

    int updateByPrimaryKey(UserArchive record);

    Integer selectMaxId();

    String selectMail(Integer integer);

    List<UserArchive> selectNotInList(@Param("readyIdList") List<Integer> readyIdList);

    void deleteArchiveById(@Param("archiveid") Integer archiveid);

    void resumeDeleteArchiveById(@Param("archiveid") Integer archiveid);

    /**
     * 根据机构id查询人员档案
     *
     * @param orgId
     * @return
     */
    List<UserArchive> getUserArchiveListByOrgId(Integer orgId);

    /**
     * 查询机构id下员工信息列表
     *
     * @param pageQueryVo
     * @param sortFieldStr
     * @return
     */
    List<UserArchive> getUserArchiveList(@Param("pageQueryVo") PageQueryVo pageQueryVo, @Param("sortFieldStr") String sortFieldStr);

    String selectName(Integer id);

    String selectNumber(Integer id);

    Integer selectArchiveIdByNumber(String employeeNumber);

    List<Integer> selectOrgIdByArchiveId(Integer archiveId);

    Integer selectArchiveIdByOrgId(Integer integer);

    List<UserArchive> selectNoLaborContract(List<Integer> arichiveIds);
}
