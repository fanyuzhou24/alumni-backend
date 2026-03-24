package com.alumni.system.mapper;

import com.alumni.system.domain.DeptCirclePost;
import com.alumni.system.entity.ro.dept.DeptCirclePageRO;
import com.alumni.system.entity.vo.DeptCirclePostVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 班级动态表 Mapper 接口
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface DeptCirclePostMapper extends BaseMapper<DeptCirclePost> {

    Page<DeptCirclePostVO> pageByCondition(@Param("ro") DeptCirclePageRO ro, Page<Object> objectPage);

    List<DeptCirclePostVO> listByCondition(@Param("ro") DeptCirclePageRO ro);
}
