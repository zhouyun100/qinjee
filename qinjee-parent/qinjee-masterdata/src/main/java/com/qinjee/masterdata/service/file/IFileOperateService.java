package com.qinjee.masterdata.service.file;

import com.qinjee.masterdata.model.vo.AttchmentRecordVo;
import com.qinjee.masterdata.model.vo.ShowAttatchementVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

public interface IFileOperateService {
    /**
     * 文件上传
     * @param files
     * @return
     */
    void putFile(MultipartFile[]  files, UserSession userSession) throws Exception;

    /**
     * 下载文件
     */
    void downLoadFile(HttpServletResponse response, List<Integer> list) throws Exception;

    /**
     * 获取文件路径
     * @param userSession
     * @return
     */
    List<URL> getFilePath( UserSession userSession,String groupName,Integer id);
    /**
     * 删除文件(逻辑删除)
     */
    void deleteFile(List<Integer> id,Integer companyId);
    /**
     * 展示附件列表
     */
    PageResult <AttchmentRecordVo> selectAttach(List<Integer> orgIdList, UserSession userSession, Integer pageSize, Integer currentPage);

    String checkFielName(List<String> fileName, UserSession userSession);

    void exportCheckFile(UserSession userSession,HttpServletResponse response) throws  IOException;

    String checkImg(MultipartFile[] files, UserSession userSession) throws Exception;

    void putPreFile(MultipartFile file, Integer preId,Integer groupId, Integer companyId) throws Exception;

    List< AttchmentRecordVo> selectPreAttach(Integer companyId, Integer preId);

    void updateFileName(String name, Integer attahmentId);

    List<ShowAttatchementVo>  selectMyFile();

    List< AttchmentRecordVo> selectMyFileContent(Integer businessId,Integer groupId,Integer companyId);

    void moveFile(Integer attachmentId, Integer groupId,Integer companyId);

    List< AttchmentRecordVo> selectMyFileContents(Integer businessId, Integer groupId, Integer companyId) throws UnsupportedEncodingException;
}
