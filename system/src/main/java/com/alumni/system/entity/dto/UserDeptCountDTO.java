package com.alumni.system.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author lifeng
 * @since 2025-12-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="UserDeptCountDTO", description="用户班级统计")
public class UserDeptCountDTO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "数量")
    private Long countNum;
}
