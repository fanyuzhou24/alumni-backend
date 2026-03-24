package com.alumni.system.service;

import com.alumni.system.domain.DeptCircleComment;
import com.alumni.system.entity.vo.DeptCircleCommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface IDeptCircleCommentService extends IService<DeptCircleComment> {

    List<DeptCircleCommentVO> selectByPostId(Long postId);
}
