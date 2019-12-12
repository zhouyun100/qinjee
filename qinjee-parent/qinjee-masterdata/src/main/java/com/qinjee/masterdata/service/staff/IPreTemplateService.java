package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;

import java.util.List;

public interface IPreTemplateService {
    List< EntryRegistrationTableVO> handlerCustomTableGroupFieldList(Integer companyId, Integer preId) throws IllegalAccessException;
}
