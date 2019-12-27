package com.qinjee.masterdata.service.file.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.AttachmentRecordDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.vo.AttchmentRecordVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.file.IFileOperateService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.utils.CompressFileUtil;
import com.qinjee.utils.FileUploadUtils;
import com.qinjee.utils.UpAndDownUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
public class FileOperateServiceImpl implements IFileOperateService {
    @Autowired
    private AttachmentRecordDao attachmentRecordDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private RedisClusterService redisClusterService;


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
                //根据层级拼接url
                pathUrl=s+File.separator+name+File.separator+name+"("+i+")"+substring;
                insertAttachment(files[i], userSession, pathUrl);
                UpAndDownUtil.putFile(file1, pathUrl);
            } finally {
                if(file1!=null) {
                    file1.deleteOnExit ();
                }
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downLoadFile(HttpServletResponse response, List<Integer> list) throws Exception {
        List<File> files=new ArrayList <> (  );
        File temp=null;
        try {
            String fileName=null;
            List < AttachmentRecord > attchmentRecordVos = attachmentRecordDao.selectByList ( list );
            for (int i = 0; i < attchmentRecordVos.size (); i++) {
                String attatchmentUrl = attchmentRecordVos.get ( i ).getAttachmentUrl ();
                String attachmentName = attchmentRecordVos.get ( i ).getAttachmentName ();
                int j =attachmentName.lastIndexOf ( "#" );
                fileName= attachmentName.substring ( j + 1 );
                COSObjectInputStream cosObjectInputStream = UpAndDownUtil.downFile ( attatchmentUrl );
                if (cosObjectInputStream != null) {
                    String s = fileName.split ( "\\." )[1];
                    temp = File.createTempFile(fileName, "."+s);
                    IOUtils.copy ( cosObjectInputStream, new FileOutputStream ( temp ) );
                    files.add ( temp );
                } else {
                    ExceptionCast.cast ( CommonCode.TARGET_NOT_EXIST );
                }
            }
            // 将文件输入流写入response的输出流中
            response.setHeader ( "content-disposition", "attachment;fileName=" + URLEncoder.encode ( fileName+".zip", "UTF-8" ) );
            CompressFileUtil.toZip ( files,response.getOutputStream () );
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("下载失败!");
        }finally {
            if(temp!=null){
              temp.deleteOnExit ();
            }
        }
    }

    @Override
    public List<URL> getFilePath(UserSession userSession, String groupName, Integer id) {
        List<URL> stringList=new ArrayList<>();
        List<String> list = attachmentRecordDao.selectFilePath(groupName,id,userSession.getCompanyId());
        for (String s : list) {
            stringList.add(UpAndDownUtil.getPath(s));
        }
        return stringList;
    }
    @Transactional(rollbackFor = Exception.class)
    public void insertAttachment(MultipartFile multipartFile, UserSession userSession,String pathUrl) {
        String s = getName ( pathUrl );
        List < URL > filePath = getFilePath (userSession,s,userSession.getArchiveId () );
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
    public void deleteFile(List<Integer> id ,UserSession userSession) {
        attachmentRecordDao.deleteFile (id,userSession.getCompanyId ());
        List < AttachmentRecord > list = attachmentRecordDao.selectByList ( id );
        for (AttachmentRecord attachmentRecord : list) {
            UpAndDownUtil.delFile ( attachmentRecord.getAttachmentUrl ());
        }
    }

    @Override
    public PageResult<AttchmentRecordVo> selectAttach(List<Integer> orgIdList, UserSession userSession,Integer pageSize,Integer currentPage) {
        PageHelper.startPage ( currentPage,pageSize );
        List < AttchmentRecordVo > attchmentRecordVos = attachmentRecordDao.selectAttach ( orgIdList, userSession.getCompanyId () );
        return new PageResult <> ( attchmentRecordVos );
    }

    @Override
    public String checkFielName(List<String> fileName, UserSession userSession) {
        Map<String,String> map=new HashMap <> (  );
        for (int i = 0; i < fileName.size (); i++) {
            Boolean flag=false;
            String s =fileName.get ( i ).split ( "#" )[0];
            UserArchiveVo userArchiveVo=userArchiveDao.selectByIdNumber(s);
            List < String > list = attachmentRecordDao.selectGroup ();
            String name = getName ( fileName.get ( i ) );
            for (String s1 : list) {
                if (s1.contains ( name )) {
                    flag = true;
                    break;
                }
            }
            if (userArchiveVo != null && flag ) {
               flag=true;
            } else {
               flag=false;
            }
            if(!flag) {
                map.put ( fileName.get ( i ), "验证不通过" );
            }else{
                map.put ( fileName.get ( i ),"验证通过！" );
            }
                redisClusterService.setex ( userSession.getCompanyId ()+"证件号验证"+userSession.getArchiveId (),2*60*60,
                        JSON.toJSONString (map));
        }
        if(map.size ()>0){
            map.put ( "flag","false" );
            return JSON.toJSONString ( map );
        }else {
            map.put ( "flag","true" );
            return JSON.toJSONString ( map );
        }
    }


    @Override
    public void exportCheckFile(UserSession userSession,HttpServletResponse response) throws IOException {
        String value=null;
        String s = userSession.getCompanyId () + "证件号验证" + userSession.getArchiveId ();
        if (redisClusterService.exists ( s )) {
            value = redisClusterService.get ( s );
        } else {
            ExceptionCast.cast ( CommonCode.REDIS_KEY_EXCEPTION );
        }
        Map < String, String > parse = ( Map < String, String > ) JSONArray.parse ( value );
        response.setContentType("application/x-msdownload;charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=\"" + URLEncoder.encode("checkFile.txt", "UTF-8") + "\"");
        response.setHeader("fileName", URLEncoder.encode("checkFile.txt", "UTF-8"));
        creatCheckResponse ( response, parse );
    }

    @Override
    public String checkImg(MultipartFile[] files, UserSession userSession) throws Exception {
        File file1 = null;
        Map<String,String> map=new HashMap <> (  );
        try {
            for (int i = 0; i < files.length; i++) {
                    file1 = FileUploadUtils.multipartFileToFile ( files[i] );
                    boolean b = checkImageScale ( file1 );
                    if(!b){
                        map.put ( files[i].getOriginalFilename (),"图片校验不通过" );
                }
            }
            if(map.size ()>0){
                redisClusterService.setex ( userSession.getCompanyId ()+"图片验证"+userSession.getArchiveId (),2*60*60,
                        JSON.toJSONString (map));
                return JSON.toJSONString ( map );
            }
            else {
                return "校验通过！";
            }
        } finally {
            if(file1!=null) {
                file1.deleteOnExit ();
            }
        }
    }

    private void creatCheckResponse(HttpServletResponse response, Map < String, String > parse) throws IOException {
        StringBuffer stringBuffer=new StringBuffer (  );
        ServletOutputStream outputStream = response.getOutputStream ();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream ( outputStream );
        for (Map.Entry < String, String > entry : parse.entrySet ()) {
            stringBuffer.append ( entry.getKey () ).append ( "\t" )
                    .append ( entry.getValue () ).append ( "\r\n" );
        }
        bufferedOutputStream.write ( stringBuffer.toString ().getBytes ("UTF-8") );
        bufferedOutputStream.flush ();
        bufferedOutputStream.close ();
    }
    public static boolean checkImageScale(File file) throws IOException {
        Boolean result = false;
        if (!file.exists()) {
            return false;
        }
        BufferedImage bufferedImage = ImageIO.read(file);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if(width>0){
            return true;
        }else{
            return false;
        }
    }

}
