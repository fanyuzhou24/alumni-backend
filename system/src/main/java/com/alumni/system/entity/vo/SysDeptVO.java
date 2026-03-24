package com.alumni.system.entity.vo;

import com.alumni.common.core.domain.model.FileDTO;
import com.alumni.system.domain.SysStudent;
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
@ApiModel(value = "班级列表")
public class SysDeptVO {

    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty("父部门ID")
    private Long parentId;

    @ApiModelProperty("父部门名称")
    private String parentName;

    @ApiModelProperty("祖级列表")
    private String ancestors;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门类型 school-学校 year-届 class-班级")
    private String deptType;

    @ApiModelProperty(value = "班级人数")
    private Long countNum;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("负责人")
    private String leader;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty(value = "部门logo")
    private String deptLogo;

    @ApiModelProperty(value = "部门封面")
    private String deptCover;

    @ApiModelProperty(value = "申请状态 not_join-未加入 apply_join 已申请加入 joined-已加入")
    private String joinStatus;

    @ApiModelProperty(value = "管理员列表（逗号拼接）")
    private String deptAdminNameList;

    @ApiModelProperty(value = "当前用户是否是管理员")
    private Boolean isAdmin;

    @ApiModelProperty(value = "学生列表")
    private List<SysStudent> studentList;
}
