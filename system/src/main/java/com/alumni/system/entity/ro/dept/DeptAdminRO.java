package com.alumni.system.entity.ro.dept;

import com.alumni.common.core.domain.model.FileDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 班级管理员申请入参
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "班级管理员申请入参")
public class DeptAdminRO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "部门id")
    @NotNull(message = "班级不能为空")
    private Long deptId;

    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

    @ApiModelProperty(value = "申请理由")
    private String applyReason;

}
