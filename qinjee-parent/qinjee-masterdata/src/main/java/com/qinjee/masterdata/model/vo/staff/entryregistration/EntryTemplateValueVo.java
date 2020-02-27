package com.qinjee.masterdata.model.vo.staff.entryregistration;

import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EntryTemplateValueVo implements Serializable {
    /**
     * 数据内容id
     */
    private Integer bigDataId;

    /**
     * 表数据
     */
    List < CustomFieldVO > list;
}
