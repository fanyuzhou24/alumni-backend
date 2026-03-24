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
@ApiModel(value = "角色列表查询入参")
public class SysRolePageRO extends PageDomain {

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
