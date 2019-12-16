package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.dao.staffdao.entryregistration.EntryRegistrationDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.model.vo.staff.PreRegistVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.custom.TemplateCustomTableFieldService;
import com.qinjee.masterdata.service.email.EmailRecordService;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.masterdata.service.staff.IPreTemplateService;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.model.request.UserSession;
import com.qinjee.utils.FileUploadUtils;
import com.qinjee.utils.QRCodeUtil;
import com.qinjee.utils.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PreTemplateServiceImpl implements IPreTemplateService {

    private static final int IMAGE_WIDTH = 512;
    private static final int IMAGE_HEIGHT = 512;


    @Autowired
    private TemplateCustomTableFieldService templateCustomTableFieldService;
    @Autowired
    private CustomTableFieldDao customTableFieldDao;
    @Autowired
    private StaffCommonServiceImpl staffCommonService;
    @Autowired
    private PreEmploymentDao preEmploymentDao;
    @Autowired
    private SmsRecordService smsRecordService;
    @Autowired
    private EntryRegistrationDao entryRegistrationDao;
    @Autowired
    private RedisClusterService redisClusterService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private EmailRecordService emailRecordService;


//    private static final String CHANGSTATUS_READY = "已入职";
//    private static final String CHANGSTATUS_BLACKLIST = "黑名单";
//    private static final String CHANGSTATUS_GIVEUP = "放弃入职";
    @Override
    public String createBackGroundPhoto(MultipartFile file, UserSession userSession) throws Exception {
        File file1 = FileUploadUtils.multipartFileToFile ( file );
        return  FileUploadUtils.uploadFile ( file1, FileUploadUtils.COMPANY_LOGO_DIRECTORY, file.getOriginalFilename () );
    }
    /**
     * 生成预入职二维码
     * @param templateId
     * @param response
     */
    @Override
    public void createPreRegistQrcode(Integer templateId, HttpServletResponse response,UserSession userSession) throws IOException {
        //给相应添加头部信息，主要告诉浏览器返回的是图片流
        response.setHeader("Cache-Control", "no-store");
        // 不设置缓存
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        //TODO  前端登陆页面url由前端提供
        String baseUrl="www.baidu.com?"+templateId+"?"+userSession.getCompanyId ();
        //数据库查询logurl
        String url=entryRegistrationDao.searchLogurlByComanyIdAnadTemplateId(templateId,userSession.getCompanyId ());
        QRCodeUtil.outputQRCodeImageByLogoUrl (url, IMAGE_WIDTH, IMAGE_HEIGHT, baseUrl, response.getOutputStream () );
    }
    @Override
    public void ToCompleteMessage(String phone, UserSession userSession,Integer templateId,HttpServletResponse response) throws IOException {
        Integer preId= preEmploymentDao.selectIdByNumber ( phone,userSession.getCompanyId ());
        String baseShortUrl = sysDictService.searchSysDictByTypeAndCode ( "SHORT_URL", "EMPLOYMENT_REGISTER" ).getDictValue ();
        redisClusterService.setex ( ShortUrl.shortUrl ( baseShortUrl + "?" + preId +"?"+ templateId ),
                2*60*60,baseShortUrl + "?" + preId +"?"+ templateId);
        response.sendRedirect ( ShortUrl.shortUrl ( baseShortUrl + "?" + preId +"?"+ templateId ));
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendRegisterMessage(PreRegistVo preRegistVo,UserSession userSession) throws Exception {
        //判断是否可以发短信
//        for (Integer integer : preRegistVo.getList ()) {
//            PreEmployment preEmployment = preEmploymentDao.getOrganizationById ( integer );
//            if(CHANGSTATUS_BLACKLIST.equals (preEmployment.getEmploymentState ()) ||
//              CHANGSTATUS_GIVEUP.equals (preEmployment.getEmploymentState ()) || CHANGSTATUS_READY.equals ( preEmployment.getEmploymentState ()
//            )){
//                ExceptionCast.cast ( CommonCode.CAN_NOT_SEND_PREREGIST );
//            }
//        }
        if(1==preRegistVo.getSendWay ()){
            //短信发送
          smsRecordService.sendMessageSms ( preRegistVo.getList (),preRegistVo.getTemplateId () );
        }
        if(2==preRegistVo.getSendWay ()){
            //邮件发送
        emailRecordService.SendMailForPreRegist (userSession ,preRegistVo.getList (),preRegistVo.getTemplateId () );
        }
        for (Integer integer : preRegistVo.getList ()) {
            PreEmployment preEmployment = preEmploymentDao.selectByPrimaryKey ( integer );
            preEmployment.setEmploymentRegister ( "已发送" );
            preEmploymentDao.updateByPrimaryKey ( preEmployment );
        }
    }



    @Override
    public Integer selectPreIdByPhone(String phone, UserSession userSession) {
        return preEmploymentDao.selectIdByNumber ( phone,userSession.getCompanyId ());
    }




    @Override
    public List < EntryRegistrationTableVO > handlerCustomTableGroupFieldList(Integer companyId, Integer preId,Integer templateId)
            throws IllegalAccessException {
        Map < Integer, String > map =new HashMap <> (  );
        //根据模板id与预入职id获取模板数据
        List < EntryRegistrationTableVO > entryRegistrationTableVOS1 =
                templateCustomTableFieldService.searchCustomTableListByTemplateIdAndArchiveId ( templateId, preId );
        //根据企业id找到tableId
       List<Integer> list=customTableFieldDao.selectTableIdByCompanyIdAndFuncCode(companyId,"PRE");
       //找到对应的值
        for (Integer integer : list) {
            List < Map < Integer, String > > mapList = staffCommonService.selectValue ( integer, preId );
            for (Map < Integer, String > integerStringMap : mapList) {
                for (Map.Entry < Integer, String > integerStringEntry : integerStringMap.entrySet ()) {
                    map.put ( integerStringEntry.getKey (),integerStringEntry.getValue () );
                }
            }
        }
        List < EntryRegistrationTableVO > entryRegistrationTableVOS =
                templateCustomTableFieldService.handlerCustomTableGroupFieldList ( entryRegistrationTableVOS1, map );
        return entryRegistrationTableVOS;
    }
}
