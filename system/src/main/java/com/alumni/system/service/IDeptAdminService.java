package com.alumni.system.service;

import com.alumni.system.domain.DeptAdmin;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.dept.DeptAdminPageRO;
import com.alumni.system.entity.ro.dept.DeptAdminRO;
import com.alumni.system.entity.ro.dept.DeptAdminUpdateRO;
import com.alumni.system.entity.vo.DeptAdminVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 班级管理员表 服务类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
public interface IDeptAdminService extends IService<DeptAdmin> {
    IPage<DeptAdminVO> pageByCondition(DeptAdminPageRO ro);

    List<DeptAdminVO> listByCondition(DeptAdminPageRO ro);

    void check(CheckRO ro);

    void applyAdmin(DeptAdminRO ro);

    void updateAdmin(DeptAdminUpdateRO ro);

    void validCheckUser(Long currentUserId, Long deptId);
}
