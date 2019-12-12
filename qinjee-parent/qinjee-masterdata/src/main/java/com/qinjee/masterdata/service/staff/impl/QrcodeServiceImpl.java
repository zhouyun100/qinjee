package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.consts.AesKeyConsts;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.sys.SysDictDao;
import com.qinjee.masterdata.redis.RedisClusterConfig;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.masterdata.service.staff.IQrcodeService;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.masterdata.utils.GetDayUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.utils.AesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class QrcodeServiceImpl implements IQrcodeService {
    @Autowired
    private SmsRecordService smsRecordService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private RedisClusterService redisClusterService;
    @Autowired
    private PreEmploymentDao preEmploymentDao;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendPreCheckCode(String phone) {
        smsRecordService.sendSmsLoginCode(phone);
    }

    @Override
    public void checkPreCodeAndRedirect(String phone, String code, HttpServletResponse response, Integer templateId,Integer companyId) throws Exception {
        //验证验证码结果
        boolean b = smsRecordService.checkPreLoginCodeByPhoneAndCode(phone, code);
        //根据电话号码与查询预入职人信息
        Integer integer = preEmploymentDao.selectIdByNumber(phone);
        if(b){
            //将短链接作为key，带参数的链接为value存到redis中，有效期为2小时
            String baseShortUrl = sysDictService.searchSysDictByTypeAndCode ( "SHORT_URL", "EMPLOYMENT_REGISTER" ).getDictValue ();
            redisClusterService.setex ( AesUtils.aesEncrypt ( baseShortUrl + "?" + integer +"?"+ templateId, AesKeyConsts.PRE_SMS_AES_KEY ),
                    2*60*60,baseShortUrl + "?" +integer +"?"+ templateId);
            //拼接连接进行重定向
            response.sendRedirect(baseShortUrl + "?" +integer +"?"+ templateId);
        }
    }

    @Override
    public void dealQrcodeRequest(Date date, Integer templateId, Integer companyId) {
        if(GetDayUtil.getDay(date,new Date())>1){
            ExceptionCast.cast(CommonCode.DATE_SO_LONG);
        }
    }
}
