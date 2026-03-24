package com.alumni.system.mapper;

import com.alumni.system.domain.DeptAdmin;
import com.alumni.system.entity.ro.dept.DeptAdminPageRO;
import com.alumni.system.entity.vo.DeptAdminVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 班级管理员表 Mapper 接口
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface DeptAdminMapper extends BaseMapper<DeptAdmin> {

    Page<DeptAdminVO> pageByCondition(@Param("ro") DeptAdminPageRO ro, Page<Object> objectPage);
    List<DeptAdminVO> listByCondition(@Param("ro") DeptAdminPageRO ro);
}
