package com.qinjee.masterdata.dao.staffdao.preemploymentdao;

import com.qinjee.masterdata.model.entity.PreEmployment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface PreEmploymentDao {
    int deleteByPrimaryKey(Integer employmentId);

    int insert(PreEmployment record);

    int insertSelective(PreEmployment record);

    PreEmployment selectByPrimaryKey(Integer employmentId);

    int updateByPrimaryKeySelective(PreEmployment record);

    int updateByPrimaryKey(PreEmployment record);

    /**
     * 查询预入职id的最大值
     * @return
     */
    Integer selectMaxId();

    /**
     * 根据id得到电话号码
     * @param integer
     * @return
     */
    String getPhoneNumber(Integer integer);

    /**
     * 根据id得到邮箱
     * @param integer
     * @return
     */
    String getMail(Integer integer);

    List<PreEmployment> selectPreEmployment(Integer companyId);

    void deletePreEmployment(Integer id);
}