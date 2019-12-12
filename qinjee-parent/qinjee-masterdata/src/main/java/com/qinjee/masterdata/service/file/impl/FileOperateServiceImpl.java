package com.qinjee.masterdata.service.file.impl;

import com.qcloud.cos.model.COSObjectInputStream;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.AttachmentRecordDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.vo.staff.AttachmentVo;
import com.qinjee.masterdata.service.file.IFileOperateService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.utils.UpAndDownUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class FileOperateServiceImpl implements IFileOperateService {
    @Autowired
    private AttachmentRecordDao attachmentRecordDao;
    @Autowired
    private UserArchiveDao userArchiveDao;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void putFile(MultipartFile multipartFile, AttachmentVo attachmentVo, UserSession userSession) throws Exception {
        File file = new File(multipartFile.getOriginalFilename());
        String pathUrl = getPathUrl(attachmentVo, userSession, multipartFile);
        insertAttachment(multipartFile, attachmentVo, userSession, pathUrl);
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        UpAndDownUtil.putFile(file, pathUrl);
        file.delete();
    }


    @Override
    public void downLoadFile(HttpServletResponse response, String path) throws Exception {
        try {
            COSObjectInputStream cosObjectInputStream = UpAndDownUtil.downFile(path);
            int i = path.lastIndexOf("/");
            String fileName=path.substring(i+1);
            // 将文件输入流写入response的输出流中
            response.setHeader("content-disposition", "attachment;fileName="+fileName);
            response.setHeader("content-disposition", "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(cosObjectInputStream,response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("下载失败!");
        }
    }

    @Override
    public List<URL> getFilePath(AttachmentVo attachmentVo, UserSession userSession) {
        List<URL> stringList=new ArrayList<>();
        List<String> list = attachmentRecordDao.selectFilePath(attachmentVo, userSession.getCompanyId());
        for (String s : list) {
            stringList.add(UpAndDownUtil.getPath(s));
        }
        return stringList;
    }
    @Transactional(rollbackFor = Exception.class)
    public void insertAttachment(MultipartFile multipartFile, AttachmentVo attachmentVo, UserSession userSession,String pathUrl) {
        List < URL > filePath = getFilePath ( attachmentVo, userSession );
        //找到应该上传文件的个数
        Integer fileSize=attachmentRecordDao.selectFileSize(attachmentVo.getGroupName ());
        if(filePath.size ()>=fileSize){
            ExceptionCast.cast ( CommonCode.File_NUMBER_WRONG );
        }
        AttachmentRecord attachmentRecord=new AttachmentRecord();
        BeanUtils.copyProperties(attachmentVo,attachmentRecord);
        //通过groupName找到id
        Integer groupId=attachmentRecordDao.selectGroupId(attachmentVo.getGroupName ());
        attachmentRecord.setGroupId ( groupId );
        attachmentRecord.setCompanyId(userSession.getCompanyId());
        attachmentRecord.setAttachmentName(multipartFile.getOriginalFilename());
        attachmentRecord.setIsDelete((short) 0);
        attachmentRecord.setAttachmentUrl(pathUrl);
        attachmentRecord.setAttachmentSize((int)(multipartFile.getSize())/1024);
        attachmentRecord.setOperatorId(userSession.getArchiveId());
        attachmentRecordDao.insertSelective(attachmentRecord);
    }

    private String getPathUrl(AttachmentVo attachmentVo,UserSession userSession,MultipartFile multipartFile) {
        String businessModule = attachmentVo.getBusinessModule();
        Integer companyId = userSession.getCompanyId();
        Integer businessId = attachmentVo.getBusinessId();
        String businessType = attachmentVo.getBusinessType();
        String groupName=attachmentVo.getGroupName ();
        String employNumber=userArchiveDao.selectEmployNumber(attachmentVo.getBusinessId());
        String originalFilename = multipartFile.getOriginalFilename();
        String attachmentName = employNumber+originalFilename;
        return businessModule+"/"+companyId+"/"+businessId+"/"+businessType+"/"+groupName+"/"+attachmentName;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(AttachmentVo attachmentVo,UserSession userSession) {
        attachmentRecordDao.deleteFile (attachmentVo,userSession.getCompanyId ());
    }

    @Override
    public List<AttachmentRecord> selectAttach(AttachmentVo attachmentVo,UserSession userSession) {
      return attachmentRecordDao.
              selectAttach(attachmentVo,userSession.getCompanyId ());

    }
}
