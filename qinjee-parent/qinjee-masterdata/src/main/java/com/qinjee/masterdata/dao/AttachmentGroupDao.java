package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.vo.ShowAttatchementVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentGroupDao  {

    List < ShowAttatchementVo > selectGroupTop();

    String selectGroupName(Integer groupId);

//    List< AttachmentGroup> selectGroup(Integer groupId);
}
