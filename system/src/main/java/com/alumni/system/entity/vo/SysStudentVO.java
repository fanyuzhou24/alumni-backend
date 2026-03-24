package com.alumni.system.entity.vo;

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
@ApiModel(value = "校友列表")
public class SysStudentVO {

    @ApiModelProperty(value = "学生ID")
    private Long studentId;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentNo;

    @ApiModelProperty(value = "所属班级ID（sys_dept）")
    private Long deptId;

    @ApiModelProperty(value = "所属用户ID（sys_user）")
    private Long userId;

    @ApiModelProperty(value = "是否为班级管理员")
    private Boolean isAdmin;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty("用户头像")
    private String avatar;
}
