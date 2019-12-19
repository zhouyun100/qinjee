package com.qinjee.masterdata.service.file.impl;

import com.qcloud.cos.model.COSObjectInputStream;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.AttachmentRecordDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.service.file.IFileOperateService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.utils.FileUploadUtils;
import com.qinjee.utils.UpAndDownUtil;
import org.apache.commons.io.IOUtils;
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
    private PreEmploymentDao preEmploymentDao;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void putFile(MultipartFile multipartFile, UserSession userSession) throws Exception {
        String pathUrl=multipartFile.getOriginalFilename ();
        File file = FileUploadUtils.multipartFileToFile ( multipartFile );
        insertAttachment(multipartFile, userSession, pathUrl);
        UpAndDownUtil.putFile(file, userSession.getCompanyId ()+pathUrl);
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
    public List<URL> getFilePath(UserSession userSession,String groupName) {
        List<URL> stringList=new ArrayList<>();
        List<String> list = attachmentRecordDao.selectFilePath(groupName,userSession.getArchiveId (),userSession.getCompanyId());
        for (String s : list) {
            stringList.add(UpAndDownUtil.getPath(s));
        }
        return stringList;
    }
    @Transactional(rollbackFor = Exception.class)
    public void insertAttachment(MultipartFile multipartFile, UserSession userSession,String pathUrl) {
        int i = pathUrl.lastIndexOf ( "/" );
        String substring = pathUrl.substring ( i, pathUrl.length () + 1 );
        List < URL > filePath = getFilePath (userSession,substring );
        //找到应该上传文件的个数
        Integer fileSize=attachmentRecordDao.selectFileSize(substring);
        if(filePath.size ()>=fileSize){
            ExceptionCast.cast ( CommonCode.File_NUMBER_WRONG );
        }
        AttachmentRecord attachmentRecord=new AttachmentRecord();
        //通过groupName找到id
        Integer groupId=attachmentRecordDao.selectGroupId(substring);
        attachmentRecord.setGroupId ( groupId );
        attachmentRecord.setCompanyId(userSession.getCompanyId());
        attachmentRecord.setAttachmentName(multipartFile.getOriginalFilename());
        attachmentRecord.setIsDelete((short) 0);
        attachmentRecord.setAttachmentUrl(pathUrl);
        attachmentRecord.setAttachmentSize((int)(multipartFile.getSize())/1024);
        attachmentRecord.setOperatorId(userSession.getArchiveId());
        attachmentRecordDao.insertSelective(attachmentRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Integer id ,UserSession userSession) {
        attachmentRecordDao.deleteFile (id,userSession.getCompanyId ());
    }

    @Override
    public List<AttachmentRecord> selectAttach(Integer archiveId,UserSession userSession) {
      return attachmentRecordDao.selectAttach(archiveId,userSession.getCompanyId ());
    }

    @Override
    public Boolean checkFielName(String fileName, UserSession userSession) {
        Boolean flag=false;
        String s = fileName.split ( "/" )[0];
        PreEmployment preEmployment=preEmploymentDao.selectByEmployNumber(s);
        List<String> list=attachmentRecordDao.selectGroup();
        for (String s1 : list) {
            if(fileName.contains ( s1 )){
                flag=true;
                break;
            }
        }
        if(preEmployment!=null && flag==true){
            return true;
        }else {
            return false;
        }
    }

}
