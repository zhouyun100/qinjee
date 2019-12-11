package com.qinjee.masterdata.service.file.impl;

import com.qcloud.cos.model.COSObjectInputStream;
import com.qinjee.masterdata.dao.AttachmentRecordDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.vo.staff.AttachmentVo;
import com.qinjee.masterdata.model.vo.staff.GetFilePath;
import com.qinjee.masterdata.service.file.IFileOperateService;
import com.qinjee.model.request.UserSession;
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
    public List<URL> getFilePath(GetFilePath getFilePath, UserSession userSession) {
        List<URL> stringList=new ArrayList<>();
        List<String> list = attachmentRecordDao.selectFilePath(getFilePath, userSession.getCompanyId());
        for (String s : list) {
            stringList.add(UpAndDownUtil.getPath(s));
        }
        return stringList;
    }
    private void insertAttachment(MultipartFile multipartFile, AttachmentVo attachmentVo, UserSession userSession,String pathUrl) {
        AttachmentRecord attachmentRecord=new AttachmentRecord();
        BeanUtils.copyProperties(attachmentVo,attachmentRecord);
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
        Integer groupId = attachmentVo.getGroupId ();
        String employNumber=userArchiveDao.selectEmployNumber(attachmentVo.getBusinessId());
        String originalFilename = multipartFile.getOriginalFilename();
        String attachmentName = employNumber+originalFilename;
        return businessModule+"/"+companyId+"/"+businessId+"/"+businessType+"/"+groupId+"/"+attachmentName;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttachment(List<Integer> list) {
        attachmentRecordDao.deleteByIdList(list);
    }

    @Override
    public List<AttachmentRecord> selectAttach(String businessModule, String businessType,String groupName, UserSession userSession) {
        List<AttachmentRecord> list=attachmentRecordDao.selectAttach(businessModule,businessType,userSession.getArchiveId(),groupName,userSession.getCompanyId());
        return list;
    }

}
