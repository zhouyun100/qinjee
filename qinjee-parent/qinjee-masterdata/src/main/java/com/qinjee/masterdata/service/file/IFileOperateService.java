package com.qinjee.masterdata.service.file;

import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.vo.staff.AttachmentVo;
import com.qinjee.model.request.UserSession;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.List;

public interface IFileOperateService {
    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    void putFile(MultipartFile multipartFile, AttachmentVo attachmentVo, UserSession userSession) throws Exception;

    /**
     * 下载文件
     */
    void downLoadFile(HttpServletResponse response, String path) throws Exception;

    /**
     * 获取文件路径
     * @param attachmentVo
     * @param userSession
     * @return
     */
    List<URL> getFilePath(AttachmentVo attachmentVo, UserSession userSession);
    /**
     * 删除文件(逻辑删除)
     */
    void deleteFile(AttachmentVo attachmentVo,UserSession userSession);
    /**
     * 展示附件列表
     */
    List<AttachmentRecord> selectAttach(AttachmentVo attachmentVo,UserSession userSession);

}
