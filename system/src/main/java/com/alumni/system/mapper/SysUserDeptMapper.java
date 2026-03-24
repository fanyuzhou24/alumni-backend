package com.alumni.system.mapper;

import com.alumni.system.domain.SysUserDept;
import com.alumni.system.entity.dto.UserDeptCountDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与部门关联表 数据层
 *
 * @author alumni
 */
public interface SysUserDeptMapper extends BaseMapper<SysUserDept> {

    List<UserDeptCountDTO> countUserByDeptIds(@Param("deptIds") List<Long> deptIds);
}
