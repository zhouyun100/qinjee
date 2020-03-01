package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PostInstructions;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月24日 15:07:00
 */
public interface PostInstructionsService {
    /**
     * 根据岗位id查询岗位说明书
     * @param postId
     * @return
     */
    ResponseResult<PostInstructions> showPostInstructions(Integer postId);


    /**
     * 下载岗位说明书
     * @param instructionId
     * @param response
     * @return
     */
    ResponseResult downloadInstructions(Integer instructionId, HttpServletResponse response);
}
