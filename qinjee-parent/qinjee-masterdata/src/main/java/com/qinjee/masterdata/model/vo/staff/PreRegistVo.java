package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 黄开天
 * @date 2019/12/31
 */
@Data
public class PreRegistVo implements Serializable {

    private List <Integer> preIdList;
    private Integer templateId;
    private List<Integer> sendWayList;

}
