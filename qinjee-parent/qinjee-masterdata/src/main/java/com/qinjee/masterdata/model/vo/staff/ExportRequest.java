package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
@Data
@JsonInclude
public class ExportRequest {
    private  List <Integer> list;
    private String title;
    private HttpServletResponse response;
}
