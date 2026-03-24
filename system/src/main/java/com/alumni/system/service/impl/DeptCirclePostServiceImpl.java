package com.alumni.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.common.utils.StringUtils;
import com.alumni.system.domain.DeptCircleComment;
import com.alumni.system.domain.DeptCircleLike;
import com.alumni.system.domain.DeptCirclePost;
import com.alumni.system.entity.ro.dept.DeptCircleCommentRO;
import com.alumni.system.entity.ro.dept.DeptCircleLikeRO;
import com.alumni.system.entity.ro.dept.DeptCirclePageRO;
import com.alumni.system.entity.vo.DeptCircleCommentVO;
import com.alumni.system.entity.vo.DeptCirclePostDetailVO;
import com.alumni.system.entity.vo.DeptCirclePostVO;
import com.alumni.system.mapper.DeptCirclePostMapper;
import com.alumni.system.service.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 班级动态表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Service
public class DeptCirclePostServiceImpl extends ServiceImpl<DeptCirclePostMapper, DeptCirclePost> implements IDeptCirclePostService {

    @Resource
    private IDeptCircleLikeService likeService;

    @Resource
    private IDeptCircleCommentService commentService;

    @Resource
    private IDeptAdminService deptAdminService;

    @Resource
    private ISysUserService userService;


    @Override
    public Page<DeptCirclePostVO> pageByCondition(DeptCirclePageRO ro) {
        Long currentUserId = SecurityUtils.getUserId();
        ro.setCurrentUserId(currentUserId);
        return this.baseMapper.pageByCondition(ro, new Page<>(ro.getPageNum(), ro.getPageSize()));
    }

    @Override
    public List<DeptCirclePostVO> listByCondition(DeptCirclePageRO ro) {
        Long currentUserId = SecurityUtils.getUserId();
        ro.setCurrentUserId(currentUserId);
        return this.baseMapper.listByCondition(ro);
    }

    @Override
    public DeptCirclePostDetailVO detail(Long postId) {
        Long currentUserId = SecurityUtils.getUserId();

        DeptCirclePost circlePost = getById(postId);
        Assert.notNull(circlePost, "班级动态不存在");
        DeptCirclePostDetailVO detailVO = BeanUtil.copyProperties(circlePost, DeptCirclePostDetailVO.class);

        DeptCircleLike circleLike = likeService.selectByUserId(currentUserId, postId);
        detailVO.setIsLike(Objects.nonNull(circleLike));

        SysUser user = userService.getById(circlePost.getPublishUserId());
        if (Objects.nonNull(user)) {
            detailVO.setUserName(user.getUserName());
            detailVO.setAvatar(user.getAvatar());
        }

        List<DeptCircleCommentVO> deptCircleCommentVOS = commentService.selectByPostId(postId);
        detailVO.setComments(deptCircleCommentVOS);
        return detailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void comment(DeptCircleCommentRO ro) {
        Long postId = ro.getPostId();
        DeptCirclePost post = this.getById(postId);
        Assert.notNull(post, "动态不存在或已删除");

        // 一级评论自动处理
        if (ro.getParentId() == null) {
            ro.setParentId(0L);
        }

        Long currentUserId = SecurityUtils.getUserId();
        ro.setUserId(currentUserId);

        // 新增评论交给 commentService
        DeptCircleComment comment = BeanUtil.copyProperties(ro, DeptCircleComment.class);
        comment.setUserId(SecurityUtils.getUserId());
        commentService.save(comment);

        // Post 评论数 +1
        this.lambdaUpdate()
                .eq(DeptCirclePost::getId, postId)
                .setSql("comment_count = comment_count + 1")
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void like(DeptCircleLikeRO ro) {

        Long postId = ro.getPostId();
        Long userId = SecurityUtils.getUserId();
        DeptCirclePost post = this.getById(postId);
        Assert.notNull(post, "动态不存在或已删除");

        DeptCircleLike like = likeService.lambdaQuery()
                .eq(DeptCircleLike::getPostId, postId)
                .eq(DeptCircleLike::getUserId, userId)
                .one();

        if (Objects.isNull(like)) {
            // 新增点赞
            like = new DeptCircleLike();
            like.setPostId(postId);
            like.setUserId(userId);
            like.setDelFlag("0");
            likeService.save(like);
        } else {
            // 已点赞则不重复
            return;
        }

        // 点赞数 +1
        this.lambdaUpdate()
                .eq(DeptCirclePost::getId, postId)
                .setSql("like_count = like_count + 1")
                .update();
    }

    @Override
    public void insertCircle(DeptCirclePost deptCirclePost) {
        deptCirclePost.setLikeCount(0);
        deptCirclePost.setCommentCount(0);
        deptCirclePost.setPublishUserId(SecurityUtils.getUserId());
        save(deptCirclePost);
    }

    @Override
    @Transactional
    public void deleteCircle(Long id) {
        DeptCirclePost circlePost = getById(id);
        Assert.notNull(circlePost, "班级动态不存在");
        Long userId = SecurityUtils.getUserId();
        if (!StringUtils.equals(userId.toString(), circlePost.getCreateBy())) {
            deptAdminService.validCheckUser(userId, circlePost.getDeptId());
        }

        removeById(id);
        likeService.remove(Wrappers.<DeptCircleLike>lambdaQuery().eq(DeptCircleLike::getPostId, id));
        commentService.remove(Wrappers.<DeptCircleComment>lambdaQuery().eq(DeptCircleComment::getPostId, id));
    }

}
