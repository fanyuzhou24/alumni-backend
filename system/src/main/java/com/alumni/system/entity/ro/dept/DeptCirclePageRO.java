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
@ApiModel(value = "班级动态列表查询入参")
public class DeptCirclePageRO extends PageDomain {

    @ApiModelProperty(value = "班级")
    private Long deptId;

    @ApiModelProperty(value = "登录用户ID")
    private Long currentUserId;

}
