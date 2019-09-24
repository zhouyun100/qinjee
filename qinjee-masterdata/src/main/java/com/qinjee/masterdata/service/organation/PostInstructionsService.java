package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PostInstructions;
import com.qinjee.model.response.ResponseResult;

/**
 * @author 高雄
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
}
