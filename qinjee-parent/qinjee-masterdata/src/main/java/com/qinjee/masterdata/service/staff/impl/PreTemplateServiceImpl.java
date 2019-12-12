package com.qinjee.masterdata.service.staff.impl;

import com.google.zxing.common.BitMatrix;
import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.model.vo.staff.PreRegistVo;
import com.qinjee.masterdata.service.custom.TemplateCustomTableFieldService;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.masterdata.service.staff.EntryRegistrationService;
import com.qinjee.masterdata.service.staff.IPreTemplateService;
import com.qinjee.model.request.UserSession;
import com.qinjee.utils.FileUploadUtils;
import com.qinjee.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PreTemplateServiceImpl implements IPreTemplateService {
    @Autowired
    private EntryRegistrationService entryRegistrationService;
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
    private static final String CHANGSTATUS_READY = "已入职";
    private static final String CHANGSTATUS_BLACKLIST = "黑名单";
    private static final String CHANGSTATUS_GIVEUP = "放弃入职";
    @Override
    public String createBackGroundPhoto(MultipartFile file, UserSession userSession) throws Exception {
        File file1 = FileUploadUtils.multipartFileToFile ( file );
        return  FileUploadUtils.uploadFile ( file1, FileUploadUtils.COMPANY_LOGO_DIRECTORY, file.getOriginalFilename () );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendRegisterMessage(PreRegistVo preRegistVo) throws Exception {
        //判断是否可以发短信
//        for (Integer integer : preRegistVo.getList ()) {
//            PreEmployment preEmployment = preEmploymentDao.selectByPrimaryKey ( integer );
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
        }
        for (Integer integer : preRegistVo.getList ()) {
            PreEmployment preEmployment = preEmploymentDao.selectByPrimaryKey ( integer );
            preEmployment.setEmploymentRegister ( "已发送" );
            preEmploymentDao.updateByPrimaryKey ( preEmployment );
        }
    }

    /**
     * 生成预入职二维码
     * @param templateId
     * @param response
     */
    @Override
    public void createPreRegistQrcode(Integer templateId, HttpServletResponse response) {
        //给相应添加头部信息，主要告诉浏览器返回的是图片流
        response.setHeader("Cache-Control", "no-store");
        // 不设置缓存
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        String baseUrl="www.baidu.com?"+templateId;
//        BitMatrix bitMatrix = QRCodeUtil.generateQRCodeStream ( baseUrl, response );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        BufferedImage image = toBufferedImage(bitMatrix)
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
            Map < Integer, String > map1 = staffCommonService.selectValue ( integer, preId );
            for (Map.Entry < Integer, String > integerStringEntry : map1.entrySet ()) {
                map.put ( integerStringEntry.getKey (),integerStringEntry.getValue () );
            }
        }
        List < EntryRegistrationTableVO > entryRegistrationTableVOS =
                templateCustomTableFieldService.handlerCustomTableGroupFieldList ( entryRegistrationTableVOS1, map );
        return entryRegistrationTableVOS;
    }

  


}
