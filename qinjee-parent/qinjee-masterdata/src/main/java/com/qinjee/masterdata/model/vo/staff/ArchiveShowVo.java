package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
@Data
public class ArchiveShowVo implements Serializable {
    private Map<Integer,Map<String,Object>> map;
    private Map<String,String> fieldMap;
    private Map<String,String> entityMap;

}
