package com.alumni.system.entity.ro.dept;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 创建班级申请入参
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "创建班级申请入参")
public class DeptCreateRO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "届")
    private String yearName;

    @ApiModelProperty(value = "logo")
    private String deptLogo;

    @ApiModelProperty(value = "封面")
    private String deptCover;

}
