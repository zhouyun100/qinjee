package com.qinjee.masterdata.service.file;

import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.model.request.UserSession;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    void downLoadFile(HttpServletResponse response, String path) throws Exception;

    /**
     * 获取文件路径
     * @param userSession
     * @return
     */
    List<URL> getFilePath( UserSession userSession,String groupName);
    /**
     * 删除文件(逻辑删除)
     */
    void deleteFile(Integer id,UserSession userSession);
    /**
     * 展示附件列表
     */
    List<AttachmentRecord> selectAttach(List<Integer> orgIdList,UserSession userSession);

    Boolean checkFielName(List<String> fileName, UserSession userSession);
}
