package com.qinjee.masterdata.dao.staffdao.preemploymentdao;

import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Repository
public interface PreEmploymentDao {
    int deleteByPrimaryKey(Integer employmentId);

    int insert(PreEmployment preEmployment);

    PreEmployment selectByPrimaryKey(Integer employmentId);

    int updateByPrimaryKey( PreEmployment record);

    /**
     * 查询预入职id的最大值
     * @return
     */
    Integer updateBatch(List<PreEmployment> list);


    /**
     * 根据id得到电话号码
     * @return
     */
    List<String> getPhoneNumber(@Param("list") List<Integer> list);

    /**
     * 根据id得到邮箱
     * @param list
     * @return
     */
    List<String> getMail(@Param("list") List<Integer> list);

    /**
     * @param companyId
     * @return
     */
    List< PreEmployment> selectPreEmployment(@Param("companyId") Integer companyId);

    void deletePreEmploymentList(@Param("list") List<Integer> list);

    void deletePreEmployment(@Param("integer") Integer integer);

    List<Integer> selectIdByNumber(@Param("phoneNumber") String phoneNumber, @Param("companyId") Integer companyId);

    List<Integer> selectIdByComId(@Param("companyId") Integer companyId, @Param("whereSql") String whereSql, @Param("orderSql") String orderSql);

    List<PreEmployment> selectByPrimaryKeyList(@Param("list") List<Integer> list);
    @MapKey("employment_id")
    Map<Integer,Map<String,Object>> selectExportPreList(@Param("list") List<Integer> list, @Param("companyId") Integer companyId);

    int insertBatch(@Param ( "list" ) List<PreEmployment> list);

    List< PreEmploymentVo> selectPreEmploymentVo(@Param("companyId") Integer companyId, @Param("orgId") List<Integer> orgId, @Param("whereSql") String whereSql, @Param("orderSql") String orderSql);
    @MapKey ( "employment_id" )
    Map< Integer,Map< String, String>> selectNameAndOrg(@Param("list") List< Integer> list);

    PreEmploymentVo selectPreEmploymentVoById(@Param("businessId") Integer businessId);
   List<Integer> selectPreByIdtypeAndIdnumber(@Param("phone") String phone, @Param("idnumber") String idnumber, @Param("companyId") Integer companyId);

    PreEmployment selectByEmployNumber(@Param("s") String s);

    List<Integer> selectPreByPhone(@Param("s3") String s3, @Param("s4") String s4);

    PreEmployment selectemploymentRegisterByPreId(@Param("preId") Integer preId, @Param("companyId") Integer companyId);

    Integer selectReadyCount(@Param("archiveId") String archiveId, @Param("companyId") Integer companyId);

    List<PreEmploymentVo> searchByHead(@Param("whereSql") String whereSql, @Param("companyId") Integer companyId);

    List<PreEmployment> selectByPostIds(@Param("postIds")List<Integer> postIds);

    List<PreEmploymentVo> selectReadyPre(@Param("archiveId") Integer archiveId, @Param("companyId") Integer companyId);
}