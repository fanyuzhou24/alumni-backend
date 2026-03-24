package com.alumni.system.mapper;

import com.alumni.system.domain.DeptCircleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface DeptCircleCommentMapper extends BaseMapper<DeptCircleComment> {

    List<DeptCircleComment> listByPostId(Long postId);
}
