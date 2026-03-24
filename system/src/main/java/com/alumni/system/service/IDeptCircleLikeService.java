package com.alumni.system.service;

import com.alumni.system.domain.DeptCircleLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface IDeptCircleLikeService extends IService<DeptCircleLike> {

    DeptCircleLike selectByUserId(Long currentUserId, Long postId);
}
