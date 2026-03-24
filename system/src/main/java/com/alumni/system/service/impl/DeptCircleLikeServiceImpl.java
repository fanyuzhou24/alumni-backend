package com.alumni.system.service.impl;

import com.alumni.system.domain.DeptCircleLike;
import com.alumni.system.mapper.DeptCircleLikeMapper;
import com.alumni.system.service.IDeptCircleLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Service
public class DeptCircleLikeServiceImpl extends ServiceImpl<DeptCircleLikeMapper, DeptCircleLike> implements IDeptCircleLikeService {

    @Override
    public DeptCircleLike selectByUserId(Long currentUserId, Long postId) {
        DeptCircleLike result = new DeptCircleLike();
        if (Objects.nonNull(currentUserId) && Objects.nonNull(postId)) {
            result = lambdaQuery()
                    .eq(DeptCircleLike::getPostId, postId)
                    .eq(DeptCircleLike::getUserId, currentUserId)
                    .eq(DeptCircleLike::getDelFlag, "0")
                    .one();
        }
        return result;
    }
}
