package com.qinjee.masterdata.service.staff.impl;

import com.alibaba.fastjson.JSON;
import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.dao.staffdao.entryregistration.EntryRegistrationDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.model.vo.staff.PreRegistVo;
import com.qinjee.masterdata.service.custom.TemplateCustomTableFieldService;
import com.qinjee.masterdata.service.email.EmailRecordService;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.masterdata.service.staff.IPreTemplateService;
import com.qinjee.model.request.UserSession;
import com.qinjee.utils.AesUtils;
import com.qinjee.utils.FileUploadUtils;
import com.qinjee.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
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
    private EmailRecordService emailRecordService;


//    private static final String CHANGSTATUS_READY = "已入职";
//    private static final String CHANGSTATUS_BLACKLIST = "黑名单";
//    private static final String CHANGSTATUS_GIVEUP = "放弃入职";
    private static  final String AESENCRYPT="QINJEE_EHR";
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
    public void createPreRegistQrcode(Integer templateId, HttpServletResponse response,UserSession userSession) throws Exception {
        //给相应添加头部信息，主要告诉浏览器返回的是图片流
        response.setHeader("Cache-Control", "no-store");
        // 不设置缓存
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        //TODO  前端登陆页面url由前端提供
        String s = AesUtils.aesEncrypt (  templateId  +"@@"+ userSession.getCompanyId () , AESENCRYPT );
        String baseUrl="www.baidu.com"+"?"+s;
        //数据库查询logurl
        String url=entryRegistrationDao.searchLogurlByComanyIdAnadTemplateId(templateId,userSession.getCompanyId ());
        QRCodeUtil.outputQRCodeImageByLogoUrl (url, IMAGE_WIDTH, IMAGE_HEIGHT, baseUrl, response.getOutputStream () );
    }




    @Override
    public String toCompleteMessage(String phone,String s, String code) throws Exception {
        String s1 = AesUtils.aesDecode ( s, AESENCRYPT );
        String[] split = s1.split ( "@@" );
        boolean b = smsRecordService.checkPreLoginCodeByPhoneAndCode ( phone, code );
        if(b){
            Integer preId= preEmploymentDao.selectIdByNumber ( phone,Integer.parseInt (split[1]));
            Map<String,Integer> map=new HashMap <> (  );
            map.put ( "preId",preId );
            map.put ( "templateId",Integer.parseInt ( split[0] ) );
            return JSON.toJSONString ( map );
        }else{
            return "验证不通过！";
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendRegisterMessage(PreRegistVo preRegistVo,UserSession userSession) throws Exception {
        List < Integer > list = preRegistVo.getList();
            for (Integer sendWay : preRegistVo.getSendWay ()) {
                if (sendWay.equals ( 1 )) {
                    //短信发送
                    smsRecordService.sendMessageSms ( list, preRegistVo.getTemplateId (), userSession );
                } else if (sendWay.equals ( 2 )) {
                    //邮件发送
                    emailRecordService.SendMailForPreRegist ( userSession, list, preRegistVo.getTemplateId () );
                }
        }

        for (Integer integer : list) {
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
        List < Integer > integers = new ArrayList <> ();
        //根据模板id获取模板数据
        List < EntryRegistrationTableVO > entryRegistrationTableVOList =
                templateCustomTableFieldService.searchCustomTableListByTemplateId ( templateId );
        //筛选出tableId
        for (EntryRegistrationTableVO entryRegistrationTableVO : entryRegistrationTableVOList) {
            integers.add ( entryRegistrationTableVO.getTableId () );
        }
       //找到对应的值
        for (Integer integer : integers) {
            List < Map < Integer, String > > mapList = staffCommonService.selectValue ( integer, preId );
            for (Map < Integer, String > integerStringMap : mapList) {
                for (Map.Entry < Integer, String > integerStringEntry : integerStringMap.entrySet ()) {
                    map.put ( integerStringEntry.getKey (),integerStringEntry.getValue () );
                }
            }
            for (EntryRegistrationTableVO entryRegistrationTableVO : entryRegistrationTableVOList) {
                if(entryRegistrationTableVO.getTableId ().equals ( integer )){
                    entryRegistrationTableVOList =
                            templateCustomTableFieldService.handlerCustomTableGroupFieldList ( entryRegistrationTableVOList, map );
                }
            }
        }
        return entryRegistrationTableVOList;
    }
}
