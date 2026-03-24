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
@ApiModel(value = "班级列表查询入参")
public class SysDeptPageRO extends PageDomain {

    @ApiModelProperty(value = "班级")
    private Long deptId;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "父部门名称")
    private String parentName;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门类型 school-学校 year-届 class-班级")
    private String deptType;
}
