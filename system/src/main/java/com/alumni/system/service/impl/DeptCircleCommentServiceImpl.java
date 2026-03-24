package com.alumni.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.system.domain.DeptCircleComment;
import com.alumni.system.entity.vo.DeptCircleCommentVO;
import com.alumni.system.mapper.DeptCircleCommentMapper;
import com.alumni.system.service.IDeptCircleCommentService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Service
public class DeptCircleCommentServiceImpl extends ServiceImpl<DeptCircleCommentMapper, DeptCircleComment> implements IDeptCircleCommentService {


    @Override
    public List<DeptCircleCommentVO> selectByPostId(Long postId) {
        Assert.notNull(postId, "班级动态id不能为空");
        // 查询所有有效评论
//        List<DeptCircleComment> commentList = lambdaQuery()
//                .eq(DeptCircleComment::getPostId, postId)
//                .eq(DeptCircleComment::getDelFlag, "0")
//                .orderByAsc(DeptCircleComment::getCreateTime)
//                .list();
        List<DeptCircleComment> commentList = this.baseMapper.listByPostId(postId);

        if (CollectionUtils.isEmpty(commentList)) {
            return Collections.emptyList();
        }

        // 转为VO
        List<DeptCircleCommentVO> voList = BeanUtil.copyToList(commentList, DeptCircleCommentVO.class);

        // 按 parent_id 构建评论树
        Map<Long, DeptCircleCommentVO> map = voList.stream()
                .collect(Collectors.toMap(DeptCircleCommentVO::getId, v -> v));

        List<DeptCircleCommentVO> rootList = new ArrayList<>();

        voList.forEach(vo -> {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                // 一级评论
                rootList.add(vo);
            } else {
                // 子评论挂载到父节点
                DeptCircleCommentVO parent = map.get(vo.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(vo);
                }
            }
        });

        // 一级评论展示按时间倒序（越新越前）
        rootList.sort(Comparator.comparing(DeptCircleCommentVO::getCreateTime).reversed());
        return rootList;
    }

}
