package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@JsonInclude
@Data
public class EmailSendVo {
    private List<Integer> prelist;
    private List<Integer> conList;
    private String content;
    private String subject;
    private List <String> filepath;
}
