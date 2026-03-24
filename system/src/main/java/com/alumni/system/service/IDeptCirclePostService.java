package com.alumni.system.service;

import com.alumni.system.domain.DeptCirclePost;
import com.alumni.system.entity.ro.dept.DeptCircleCommentRO;
import com.alumni.system.entity.ro.dept.DeptCircleLikeRO;
import com.alumni.system.entity.ro.dept.DeptCirclePageRO;
import com.alumni.system.entity.vo.DeptCirclePostDetailVO;
import com.alumni.system.entity.vo.DeptCirclePostVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 班级动态表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface IDeptCirclePostService extends IService<DeptCirclePost> {

    Page<DeptCirclePostVO> pageByCondition(DeptCirclePageRO ro);

    List<DeptCirclePostVO> listByCondition(DeptCirclePageRO ro);

    DeptCirclePostDetailVO detail(Long postId);

    void comment(DeptCircleCommentRO ro);

    void like(DeptCircleLikeRO ro);

    void insertCircle(DeptCirclePost deptCirclePost);

    void deleteCircle(Long id);

}
