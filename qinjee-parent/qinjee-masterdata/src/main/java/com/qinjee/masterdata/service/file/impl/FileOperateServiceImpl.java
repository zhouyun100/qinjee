package com.qinjee.masterdata.service.file.impl;

import com.qcloud.cos.model.COSObjectInputStream;
import com.qinjee.masterdata.dao.AttachmentRecordDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.service.file.IFileOperateService;
import com.qinjee.model.request.UserSession;
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
    private UserArchiveDao userArchiveDao;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void putFile(MultipartFile[] files, UserSession userSession) throws Exception {
        File file1 = null;
        for (int i = 0; i < files.length; i++) {
            try {
                String pathUrl=files[i].getOriginalFilename ();
                String s = pathUrl.split ( "#" )[0];
                file1 = FileUploadUtils.multipartFileToFile ( files[i] );
                String name = getName ( pathUrl );
                int i1 = pathUrl.lastIndexOf ( "." );
                String substring = pathUrl.substring ( i1 );
                pathUrl=s+"/"+name+"/"+name+"("+i+")"+substring;
                insertAttachment(files[i], userSession, pathUrl);
                UpAndDownUtil.putFile(file1, pathUrl);
            } finally {
                file1.delete();
            }
        }
    }


    @Override
    public void downLoadFile(HttpServletResponse response, String path) throws Exception {
        try {
            COSObjectInputStream cosObjectInputStream = UpAndDownUtil.downFile(path);
            int i = path.lastIndexOf("#");
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
        String s = getName ( pathUrl );
        List < URL > filePath = getFilePath (userSession,s );
//        //找到应该上传文件的个数
//        Integer fileSize=attachmentRecordDao.selectFileSize(s);
//        if(filePath.size ()>=fileSize){
//            ExceptionCast.cast ( CommonCode.File_NUMBER_WRONG );
//        }
        AttachmentRecord attachmentRecord=new AttachmentRecord();
        //通过groupName找到id
        Integer groupId=attachmentRecordDao.selectGroupId(s);
        attachmentRecord.setGroupId ( groupId );
        attachmentRecord.setCompanyId(userSession.getCompanyId());
        attachmentRecord.setAttachmentName(multipartFile.getOriginalFilename());
        attachmentRecord.setIsDelete((short) 0);
        attachmentRecord.setBusinessModule ( "ARC" );
        String idnumber = multipartFile.getOriginalFilename ().split ( "#" )[0];
        UserArchiveVo userArchiveVo = userArchiveDao.selectByIdNumber ( idnumber );
        attachmentRecord.setBusinessId ( userArchiveVo.getArchiveId () );
        attachmentRecord.setBusinessType ( "archive" );
        attachmentRecord.setBusinessId ( userSession.getArchiveId () );
        attachmentRecord.setAttachmentUrl(pathUrl);
        attachmentRecord.setAttachmentSize((int)(multipartFile.getSize())/1024);
        attachmentRecord.setOperatorId(userSession.getArchiveId());
        attachmentRecordDao.insertSelective(attachmentRecord);
    }

    private String getName(String pathUrl) {
        int i = pathUrl.lastIndexOf ( "#" );
        String substring = pathUrl.substring ( i+1  );
        int i1 = substring.lastIndexOf ( "." );
        return substring.substring ( 0, i1 );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Integer id ,UserSession userSession) {
        attachmentRecordDao.deleteFile (id,userSession.getCompanyId ());
    }

    @Override
    public List<AttachmentRecord> selectAttach(List<Integer> orgIdList,UserSession userSession) {
      return attachmentRecordDao.selectAttach(orgIdList,userSession.getCompanyId ());
    }

    @Override
    public Boolean checkFielName(List<String> fileName, UserSession userSession) {
        Boolean result=false;
        for (String string : fileName) {
            Boolean flag=false;
            String s = string.split ( "#" )[0];
            UserArchiveVo userArchiveVo=userArchiveDao.selectByIdNumber(s);
            List < String > list = attachmentRecordDao.selectGroup ();
            for (String s1 : list) {
                if (fileName.contains ( s1 )) {
                    flag = true;
                    break;
                }
            }
            if (userArchiveVo != null && flag == true) {
               flag=true;
            } else {
               flag=false;
            }
            result=(result&&flag);
        }
        return result;
    }

}
