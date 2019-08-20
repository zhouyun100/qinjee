package com.qinjee.model.request;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class RequestData {
    Integer pageCurrent;
    Integer pageSize;
}
