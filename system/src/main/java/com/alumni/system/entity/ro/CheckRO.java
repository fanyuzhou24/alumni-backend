package com.alumni.system.entity.ro;

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
public class CheckRO {

    @ApiModelProperty("业务id")
    @NotNull(message = "业务id不能为空")
    private Long businessId;

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    @NotNull(message = "审核状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "审核理由")
    private String remark;
}
