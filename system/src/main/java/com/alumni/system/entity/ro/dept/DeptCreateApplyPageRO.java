package com.alumni.system.entity.ro.dept;

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
@ApiModel(value = "创建班级申请列表查询入参")
public class DeptCreateApplyPageRO extends PageDomain {

    @ApiModelProperty(value = "申请人id")
    private Long applyUserId;

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    private Integer status;
}
