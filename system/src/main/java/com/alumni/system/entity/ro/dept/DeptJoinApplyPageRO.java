package com.alumni.system.entity.ro.dept;

import com.alumni.common.core.page.PageDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "假如班级申请列表查询入参")
public class DeptJoinApplyPageRO extends PageDomain {

    @ApiModelProperty(value = "班级")
    private Long deptId;

    @ApiModelProperty(value = "班级列表")
    private List<Long> deptIds;

    @ApiModelProperty(value = "申请人")
    private Long applyUserId;

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    private Integer status;
}
