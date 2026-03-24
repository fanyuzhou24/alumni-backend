package com.alumni.system.entity.ro.dept;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 审核入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptAdminUpdateRO {

    @ApiModelProperty(value = "操作类型 0-设置管理员 1-取消管理员")
    @NotNull(message = "操作类型不能为空")
    private Integer operation;

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "班级id")
    @NotNull(message = "班级id不能为空")
    private Long deptId;
}
