package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.model.vo.staff.export.ExportArcVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Repository
public interface UserArchiveDao {

    int deleteByPrimaryKey(Integer archiveId);

    int insert(UserArchive record);

    int insertSelective(UserArchive record);

    UserArchiveVo selectByPrimaryKey(Integer archiveId);

    int updateByPrimaryKeySelective(UserArchive record);

    List<UserArchiveVo> listUserArchiveByCompanyId(Integer companyId);
    int updateByPrimaryKey(UserArchive record);

   Integer deleteArchiveByIdList(@Param("archiveid") List<Integer> archiveid);

    Integer resumeDeleteArchiveById(@Param("archiveid")Integer archiveid);


    /**
     * 查询机构id下员工信息列表
     *
     * @return
     */
    List<UserArchiveVo> getUserArchiveList(@Param("orgIdList") List<Integer> orgIdList, boolean isContains);

    Map<String,String> selectNameAndNumber(@Param("id") Integer id);


    Integer selectArchiveIdByNumber(String employeeNumber, Integer companyId);

    List<Integer> selectByOrgId(Integer orgId);

    List<UserArchiveVo> selectByPrimaryKeyList(@Param("archiveList") List<Integer> archiveList, @Param("companyId") Integer companyId);

    Integer selectArchiveIdByTel(@Param("phoneNumber") String phoneNumber);

    List<Integer> selectIdByComId(@Param("companyId") Integer companyId);

    List<Integer> selectStaff(@Param("sql") String sql, @Param("archiveType") List<String> archiveType, @Param("list") List<Integer> list);

     @MapKey("archive_id")
     Map<Integer,Map<String,Object>> getUserArchiveListCustom(@Param("baseSql") String baseSql, @Param("order") String order);

    List<Integer> selectArchiveIdByOrgId(@Param("companyId") Integer companyId);

    List<ExportArcVo> selectDownLoadVoList(@Param("archiveIdList") List<Integer> archiveIdList);

    String selectEmployNumber(@Param("businessId") Integer businessId);

    Date selectDateByStatus(@Param("string") String string);

    List< UserArchiveVo > selectByOrgAndAuth(@Param("orgId") List<Integer> orgId, @Param("archiveId") Integer archiveId, @Param("companyId") Integer companyId
    , @Param("message") String message);

    Integer countUserArchiveByOrgId(@Param("orgId") Integer orgId);

   List<Integer> selectIdByNumberAndEmploy(@Param("number") String number, @Param("employ") String employ, @Param("companyId") Integer companyId);

    void insertBatch(@Param("userArchives") List< UserArchive> userArchives);

    Integer countUserArchiveByPostId(Integer postId);

    void deleteArchiveById(Integer businessId);

    List< UserArchiveVo> selectBeforeFilter(@Param("archiveType") String archiveType, @Param("orgId") Integer orgId, @Param("type") String type);

    List< UserArchiveVo> selectUserArchiveByName(@Param("name") String name, @Param("companyId") Integer companyId);

    List<UserArchiveVo> listUserArchiveByPostIds(@Param("postIds")List<Integer> postIds);

    List< UserArchiveVo> selectArcByNotCon(@Param("orgId") List<Integer> orgId, @Param("companyId") Integer companyId);

    Map< String, Object> selectTransMessage(@Param("key") Integer key);

    List<UserArchiveVo> selectByIdNumber(@Param("s") String s, @Param("companyId") Integer companyId);

    List< UserArchiveVo> selectArchiveDelete(@Param("orgId") List<Integer> orgId);

    List<Integer> selectByPhoneAndCompanyId(@Param("phone") String phone, @Param("companyId") Integer companyId);

    List< UserArchiveVo> selectUserArchiveVo(@Param("list") List<Integer> list, @Param("companyId") Integer companyId);
    Integer selectByIDNumberAndCompanyId(@Param("idType") String idType, @Param("idNumber") String idNumber, @Param("companyId") Integer companyId);

    List< Integer> selectEmployNumberByCompanyId(@Param("companyId") Integer companyId, @Param("employeeNumber") String employeeNumber);

    List<UserArchiveVo> getByCompanyId(Integer employeeNumber);

    UserArchiveVo selectByUserId(Integer userId,Integer companyId);



    List< UserArchiveVo> selectPartTimeArchive(@Param("integerList") List< Integer> integerList, @Param("companyId") Integer companyId);

   List<Integer> selectIdByNumber(@Param("idnumber") String idnumber, @Param("companyId") Integer companyId);

   Integer selectIsExist(@Param("idNumber") String idNumber, @Param("userId") Integer userId);

    int updateBatch(@Param("list1") List<UserArchive> list1);

    List<UserArchiveVo> selectByIdNumberOrEmploy(@Param("idnumber") String idnumber, @Param("companyId") Integer companyId);
}
