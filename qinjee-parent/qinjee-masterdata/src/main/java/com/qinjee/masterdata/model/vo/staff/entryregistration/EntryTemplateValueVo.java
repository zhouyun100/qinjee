package com.qinjee.masterdata.model.vo.staff.entryregistration;

import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EntryTemplateValueVo implements Serializable {

    /**
     * 表数据
     */
    List < CustomFieldVO > list;
}
