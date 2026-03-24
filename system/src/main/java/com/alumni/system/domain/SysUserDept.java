package com.alumni.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 用户和部门关联 sys_user_dept
 * 
 * @author alumni
 */
@Data
@TableName("sys_user_dept")
@ApiModel(value="SysUserDept对象", description="用户部门关联表")
public class SysUserDept {
    /** 用户ID */
    private Long userId;
    
    /** 部门ID */
    private Long deptId;


}
