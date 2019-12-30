package com.qinjee.masterdata.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentGroupDao  {
    List< String> selectGroupTop() ;

    List< String> selectGroup(String string);
}
