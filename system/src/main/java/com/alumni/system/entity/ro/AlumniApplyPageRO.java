package com.alumni.system.entity.ro;

import com.alumni.common.core.page.PageDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "校友申请列表查询入参")
public class AlumniApplyPageRO extends PageDomain {

    @ApiModelProperty(value = "班级")
    private Long deptId;

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    private Integer status;
}
