package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.service.staff.IQrcodeService;
import com.qinjee.masterdata.utils.GetDayUtil;
import com.qinjee.model.response.CommonCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class QrcodeServiceImpl implements IQrcodeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealQrcodeRequest(HttpServletResponse response , Date date, Integer templateId, Integer companyId) throws IOException {
        //检测是否过期
        if(GetDayUtil.getDay ( date,new Date (  ) )>0){
            ExceptionCast.cast ( CommonCode.DATE_SO_LONG );
        }
        //调service判断是否登陆
        //重定向到填写页面
        response.sendRedirect("");
    }
}
