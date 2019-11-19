package com.qinjee.masterdata.dao.staffdao.preemploymentdao;

import com.qinjee.masterdata.model.entity.PreEmployment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface PreEmploymentDao {
    int deleteByPrimaryKey(Integer employmentId);

    int insert(PreEmployment record);

    PreEmployment selectByPrimaryKey(Integer employmentId);

    int updateByPrimaryKey(@Param("record") PreEmployment record);

    /**
     * 查询预入职id的最大值
     * @return
     */
    Integer selectMaxId();

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
    List<PreEmployment> selectPreEmployment(@Param("companyId") Integer companyId);

    void deletePreEmploymentList(@Param("list") List<Integer> list);

    void deletePreEmployment(@Param("integer") Integer integer);

    Integer selectIdByNumber(@Param("phoneNumber") String phoneNumber);

    List<Integer> selectIdByComId(@Param("companyId") Integer companyId);

    List<PreEmployment> selectByPrimaryKeyList(@Param("list") List<Integer> list);
}