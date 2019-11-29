package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.page.PostPageVo;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月23日 11:07:00
 */
public interface PostService {
  /**
   * 分页查询岗位列表
   *
   * @param userSession
   * @param postPageVo
   * @return
   */
  PageResult<Post> getPostConditionPage(UserSession userSession, PostPageVo postPageVo);

  /**
   * 根据岗位id查询员工档案岗位关系表
   *
   * @param pageSize
   * @param currentPage
   * @param postId
   * @return
   */
  ResponseResult<PageResult<UserArchivePostRelation>> getUserArchivePostRelationList(Integer pageSize, Integer currentPage, Integer postId);

  /**
   * 新增岗位
   *
   * @param postVo
   * @return
   */
  ResponseResult addPost(PostVo postVo, UserSession userSession);

  /**
   * 编辑岗位
   *
   * @param postVo
   * @param userSession
   * @return
   */
  ResponseResult editPost(PostVo postVo, UserSession userSession);

  /**
   * 删除岗位
   *
   * @param userSession
   * @param postIds
   * @return
   */
  ResponseResult deletePost(UserSession userSession, List<Integer> postIds);

  /**
   * 解封/封存机构
   *
   * @param postIds
   * @param isEnable
   * @param userSession
   * @return
   */
  ResponseResult sealPostByIds(List<Integer> postIds, Short isEnable, UserSession userSession);


  ResponseResult sortPorts(List<Integer> postIds, UserSession userSession);

  /**
   * 复制岗位
   *
   * @param postIds
   * @param userSession
   * @return
   */
  ResponseResult copyPost(List<Integer> postIds, UserSession userSession, Integer orgId);

  /**
   * 获取公司所有的岗位
   *
   * @param userSession
   * @param orgId
   * @return
   */
  ResponseResult<List<Post>> getAllPost(UserSession userSession, Integer orgId, Short isEnable);

  /**
   * 下载模板
   *
   * @return
   */
  ResponseResult downloadTemplate(HttpServletResponse response);

  /**
   * 根据查询条件导出Excel
   *
   * @param postPageVo
   * @param userSession
   * @return
   */
  ResponseResult downloadExcelByCondition(PostPageVo postPageVo, UserSession userSession, HttpServletResponse response);

  /**
   * 根据选择的岗位id导出Excel
   *
   * @param postIds
   * @param userSession
   * @param response
   * @return
   */
  ResponseResult downloadExcelByPostId(List<Integer> postIds, UserSession userSession, HttpServletResponse response);

  /**
   * 查看岗位历任
   * @param userSession
   * @param orgId
   * @return
   */
  ResponseResult<List<UserArchivePostRelation>> getPostSuccessive(Integer postId);

}
