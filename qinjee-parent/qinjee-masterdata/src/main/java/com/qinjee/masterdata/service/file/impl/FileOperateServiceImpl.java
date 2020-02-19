package com.qinjee.masterdata.service.file.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.AttachmentGroupDao;
import com.qinjee.masterdata.dao.AttachmentRecordDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.vo.staff.AttchmentRecordVo;
import com.qinjee.masterdata.model.vo.staff.ShowAttatchementVo;
import com.qinjee.masterdata.model.vo.staff.DeleteFileVo;
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
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private AttachmentGroupDao attachmentGroupDao;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void putFile(MultipartFile[] files, UserSession userSession) throws Exception {
        File file1 = null;
            for (int i = 0; i < files.length; i++) {
                try {
                    String pathUrl = files[i].getOriginalFilename();
                    String s = pathUrl.split("#")[0];
                    file1 = FileUploadUtils.multipartFileToFile(files[i]);
                    String name = getName(files[i].getOriginalFilename());
                    int i1 = pathUrl.lastIndexOf(".");
                    String substring = pathUrl.substring(i1);
                    //根据层级拼接url
                    pathUrl = s + File.separator + name + File.separator + name + "(" + i + ")" + substring;
                    insertAttachment(files[i], userSession, pathUrl);
                    UpAndDownUtil.putFile(file1, pathUrl);
                } finally {
                    if (file1 != null) {
                        file1.delete();
                    }
                }
            }
        }





    @Transactional(rollbackFor = Exception.class)
    @Override
    public void putPreFile(MultipartFile[] file, Integer preId, Integer groupId, Integer companyId) throws Exception {
           File file1 = null;
          String groupName= attachmentGroupDao.selectGroupName(groupId);
        try {
            for (MultipartFile multipartFile : file) {
                String puthUrl = companyId + File.separator + preId + File.separator + groupName + File.separator + multipartFile.getOriginalFilename();
                file1 = FileUploadUtils.multipartFileToFile(multipartFile);
                //新增上传记录
                insertPreAttachment(multipartFile, preId, groupName, companyId, puthUrl);
                UpAndDownUtil.putFile(file1, puthUrl);
            }
        } finally {
            if(file1!=null) {
                file1.delete ();
            }
        }
    }
    @Override
    public void putArcFile(MultipartFile[] file, Integer groupId, UserSession userSession,Integer archiveId) {
        File file1 = null;
        String groupName= attachmentGroupDao.selectGroupName(groupId);
        try {
            for (MultipartFile multipartFile : file) {
                String puthUrl = userSession.getCompanyId() + File.separator + userSession.getArchiveId() + File.separator + groupName + File.separator + multipartFile.getOriginalFilename();
                file1 = FileUploadUtils.multipartFileToFile(multipartFile);
                //新增上传记录
                insertArcAttachment(multipartFile, groupName, puthUrl, userSession, archiveId);
                UpAndDownUtil.putFile(file1, puthUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(file1!=null) {
                file1.delete ();
            }
        }
    }




    /**
     * 新增预入职模板上传附件记录
     */
    public void insertPreAttachment(MultipartFile file,Integer preId,String groupName,Integer companyId,String pathUrl){
        AttachmentRecord attachmentRecord = getAttachmentRecord ( file, groupName, companyId );
        attachmentRecord.setBusinessId ( preId );
        attachmentRecord.setBusinessModule("PRE");
        attachmentRecord.setBusinessType ( "employment" );
        attachmentRecord.setAttachmentUrl(pathUrl);
        attachmentRecord.setAttachmentSize((int)(file.getSize())/1024);
        attachmentRecord.setOperatorId(preId);
        attachmentRecordDao.insertSelective(attachmentRecord);
    }
    /**
     * 新增预入职模板上传附件记录
     */
    public void insertArcAttachment(MultipartFile file,String groupName,String pathUrl,UserSession userSession,Integer archiveId){
        AttachmentRecord attachmentRecord = getAttachmentRecord ( file, groupName,userSession.getCompanyId()  );
        attachmentRecord.setBusinessId ( archiveId );
        attachmentRecord.setBusinessModule("ARC");
        attachmentRecord.setBusinessType ( "archive" );
        attachmentRecord.setAttachmentUrl(pathUrl);
        attachmentRecord.setAttachmentSize((int)(file.getSize())/1024);
        attachmentRecord.setOperatorId(userSession.getArchiveId());
        attachmentRecordDao.insertSelective(attachmentRecord);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downLoadFile(HttpServletResponse response, List<Integer> list)  {
        List<File> files=new ArrayList <> (  );
        File temp=null;
        try {
            String fileName=null;
                List < AttachmentRecord > attchmentRecordVos = attachmentRecordDao.selectByList ( list );
                for (int i = 0; i < attchmentRecordVos.size (); i++) {
                    String attatchmentUrl = attchmentRecordVos.get ( i ).getAttachmentUrl ();
                    int j = attatchmentUrl.lastIndexOf ( "/" );
                    fileName = attatchmentUrl.substring ( j + 1 );
                    COSObjectInputStream cosObjectInputStream = UpAndDownUtil.downFile ( attatchmentUrl );
                    if (cosObjectInputStream != null) {
                        String s = fileName.split ( "\\." )[1];
                        temp = File.createTempFile ( fileName, "." + s );
                        IOUtils.copy ( cosObjectInputStream, new FileOutputStream ( temp ) );
                        files.add ( temp );
                    } else {
                        ExceptionCast.cast ( CommonCode.TARGET_NOT_EXIST );
                    }
                }
                response.reset ();
                // 将文件输入流写入response的输出流中
            response.setHeader ( "Content-disposition", "attachment; filename=\"" + URLEncoder.encode ( "file.zip", "UTF-8" )   );
            response.setHeader ( "fileName", URLEncoder.encode ( "file.zip", "UTF-8" ) );
            response.setContentType("multipart/form-data");
            CompressFileUtil.toZip ( files, response.getOutputStream () );
        } catch (Exception e) {
            e.printStackTrace ();
           ExceptionCast.cast ( CommonCode.FILE_EMPTY);
        }finally {
            if(temp!=null){
              temp.deleteOnExit ();
            }
        }
    }
    @Override
    public void downLoadInsideFile(HttpServletResponse response, String url) throws IOException {
        COSObjectInputStream cosObjectInputStream = UpAndDownUtil.downFile ( url );
        int j = url.lastIndexOf ( "/" );
        String fileName = url.substring ( j + 1 );
        response.reset ();
        // 将文件输入流写入response的输出流中
        response.setHeader ( "Content-disposition", "attachment; filename=\"" + URLEncoder.encode ( fileName, "UTF-8" )   );
        response.setHeader ( "fileName", URLEncoder.encode ( fileName, "UTF-8" ) );
        response.setContentType("multipart/form-data");
        if (cosObjectInputStream != null) {
            IOUtils.copy ( cosObjectInputStream, response.getOutputStream());
        }else{
            ExceptionCast.cast(CommonCode.FILE_EMPTY);
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
        String s = getName ( multipartFile.getOriginalFilename () );
        List < URL > filePath = getFilePath (userSession,s,userSession.getArchiveId () );
//        //找到应该上传文件的个数
//        Integer fileSize=attachmentRecordDao.selectFileSize(s);
//        if(filePath.size ()>=fileSize){
//            ExceptionCast.cast ( CommonCode.File_NUMBER_WRONG );
//        }
        AttachmentRecord attachmentRecord = getAttachmentRecord ( multipartFile, s, userSession.getCompanyId () );
        String idnumber = multipartFile.getOriginalFilename ().split ( "#" )[0];
        List<UserArchiveVo> list = userArchiveDao.selectByIdNumber ( idnumber,userSession.getCompanyId () );
        attachmentRecord.setBusinessId ( list.get(0).getArchiveId () );
        attachmentRecord.setBusinessType ( "archive" );
        attachmentRecord.setAttachmentUrl(pathUrl);
        attachmentRecord.setAttachmentSize((int)(multipartFile.getSize())/1024);
        attachmentRecord.setOperatorId(userSession.getArchiveId());
        attachmentRecord.setGroupId ( attachmentRecordDao.selectGroupId ( s ) );
        attachmentRecordDao.insertSelective(attachmentRecord);
    }

    private AttachmentRecord getAttachmentRecord(MultipartFile multipartFile, String s, Integer companyId) {
        AttachmentRecord attachmentRecord = new AttachmentRecord ();
        //通过groupName找到id
        Integer groupId = attachmentRecordDao.selectGroupId ( s );
        attachmentRecord.setGroupId ( groupId );
        attachmentRecord.setCompanyId ( companyId );
        String originalFilename = multipartFile.getOriginalFilename ();
        attachmentRecord.setAttachmentName ( originalFilename );
        String s1 = originalFilename.split ( "\\." )[1];
        attachmentRecord.setAttachmentType ( s1 );
        attachmentRecord.setIsDelete ( ( short ) 0 );
        attachmentRecord.setBusinessModule ( "ARC" );
        return attachmentRecord;
    }

    private String getName(String filename) {
        try {
            String s = filename.split ( "#" )[1];
            String s1 = s.split ( "\\." )[0];
            return s1;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(DeleteFileVo deleteFileVo) {
        //删除文件
        List < AttachmentRecord > list = attachmentRecordDao.selectByList (deleteFileVo.getId());
        for (AttachmentRecord attachmentRecord : list) {
            UpAndDownUtil.delFile ( attachmentRecord.getAttachmentUrl ());
        }
        //删除记录
        attachmentRecordDao.deleteFile (deleteFileVo.getId(),deleteFileVo.getCompanyId ());
    }

    @Override
    public PageResult<AttchmentRecordVo> selectAttach(List<Integer> orgIdList, UserSession userSession,Integer pageSize,Integer currentPage) {
        PageHelper.startPage ( currentPage,pageSize );
        List < AttchmentRecordVo > attchmentRecordVos = attachmentRecordDao.selectAttach ( orgIdList, userSession.getCompanyId () );
        return new PageResult <> ( attchmentRecordVos );
    }
    @Override
    public List < AttchmentRecordVo > selectPreAttach(Integer companyId, Integer preId) {
        return attachmentRecordDao.selectByPreIdAndCompanyId(companyId,preId,"employment");
    }

    @Override
    public void updateFileName(String name, Integer attahmentId) {
        attachmentRecordDao.updateFileName(name,attahmentId);
    }

        @Override
        public  List<ShowAttatchementVo> selectMyFile() {
            List<ShowAttatchementVo> attatchementVoList =attachmentGroupDao.selectGroupTop();
            List<ShowAttatchementVo> firstAttatchementList = attatchementVoList.stream().filter(attatchement ->{
                if(attatchement.getParentGroupId() == 0){
                    return true;
                }else{
                    return false;
                }
            }).collect( Collectors.toList());
            handlerAttatchementGroup(attatchementVoList,firstAttatchementList);
            return firstAttatchementList;
        }

        private void handlerAttatchementGroup(List<ShowAttatchementVo> allList,List<ShowAttatchementVo> firstList){
            for(ShowAttatchementVo showAttatchementVo : firstList){
                List<ShowAttatchementVo> tempAttatchement = allList.stream().filter(attatchement ->{
                    if(attatchement.getParentGroupId().equals(showAttatchementVo.getGroupId())){
                        return true;
                    }else{
                        return false;
                    }
                }).collect(Collectors.toList());

//                allList.removeAll(tempAttatchement);
                showAttatchementVo.setList(tempAttatchement);
            }
        }



    @Override
    public List < AttchmentRecordVo > selectMyFileContent(Integer businessId, Integer groupId,Integer companyId) {
       return attachmentRecordDao.selectByBusinessIdAndGroupNameAndBusinessType(businessId,groupId,companyId);
    }

    @Override
    public void moveFile(Integer attachmentId, Integer groupId,Integer companyId) {
      attachmentRecordDao.moveFile(attachmentId,groupId,companyId);
    }

    @Override
    public List < AttchmentRecordVo > selectMyFileContents(Integer businessId, Integer groupId, Integer companyId)  {
        return attachmentRecordDao.selectFileFromPackage(businessId, groupId,companyId);
    }



    @Override
    public String checkFielName(List<String> fileName, UserSession userSession) {
        Map<String,String> map=new HashMap <> (  );
        Integer j=0;
        for (int i = 0; i < fileName.size (); i++) {
            Boolean flag=false;
            String s =fileName.get ( i ).split ( "#" )[0];
            List<UserArchiveVo> userArchiveVos=userArchiveDao.selectByIdNumber(s,userSession.getCompanyId ());
            List < String > list = attachmentRecordDao.selectGroup ();
            String name = null;
            try {
                name = getName ( fileName.get ( i ) );
            } catch (Exception e) {
                map.put ( fileName.get ( i ),"验证不通过" );
            }
            for (String s1 : list) {
                if(null!=name && !"".equals (name )) {
                    if (name.equals ( s1 )) {
                        flag = true;
                        break;
                    }
                }
            }
            if (userArchiveVos.size ()==1 && flag ) {
               flag=true;
            } else {
               flag=false;
            }
            if(!flag) {
                map.put ( fileName.get ( i ), "验证不通过" );
                j++;
            }else{
                map.put ( fileName.get ( i ),"验证通过！" );
            }
        }
        redisClusterService.setex ( userSession.getCompanyId ()+"证件号验证"+userSession.getArchiveId (),2*60*60,
                JSON.toJSONString (map));
        if(j>0){
            map.put ( "flag","false" );
            return JSON.toJSONString ( map );
        }else{
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
                file1.delete ();
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
