package com.qinjee.masterdata.service.organation.impl;

import com.qinjee.masterdata.dao.PostInstructionsDao;
import com.qinjee.masterdata.model.entity.PostInstructions;
import com.qinjee.masterdata.service.organation.PostInstructionsService;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月24日 15:07:00
 */
@Service
public class PostInstructionsImpl implements PostInstructionsService {

    @Autowired
    private PostInstructionsDao postInstructionsDao;

    @Override
    public ResponseResult<PostInstructions> showPostInstructions(Integer postId) {
        PostInstructions postInstructions = postInstructionsDao.getPostInstructionsByPostId(postId);
        return new ResponseResult<>(postInstructions);
    }
}
