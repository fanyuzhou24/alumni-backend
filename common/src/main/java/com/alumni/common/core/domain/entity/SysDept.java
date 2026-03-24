package com.alumni.common.core.domain.entity;

import com.alumni.common.core.domain.BaseEntity;
import com.alumni.common.core.domain.model.FileDTO;
import com.alumni.common.handler.ListTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 部门表 sys_dept
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "部门对象")
@TableName(value = "sys_dept", autoResultMap = true)
public class SysDept extends BaseEntity {

    @ApiModelProperty("部门ID")
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    @ApiModelProperty("父部门ID")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty("祖级列表")
    @TableField("ancestors")
    private String ancestors;

    @ApiModelProperty("部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty("部门类型 school-学校 year-届 class-班级")
    @NotBlank(message = "部门类型不能为空")
    @Size(max = 30, message = "部门类型长度不能超过30个字符")
    @TableField("dept_type")
    private String deptType;

    @ApiModelProperty("显示顺序")
    @NotNull(message = "显示顺序不能为空")
    @TableField("order_num")
    private Integer orderNum;

    @ApiModelProperty("负责人")
    @TableField("leader")
    private String leader;

    @ApiModelProperty("联系电话")
    @Size(max = 11, message = "联系电话长度不能超过11个字符")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @TableField("email")
    private String email;

    @ApiModelProperty("部门状态 0正常,1停用")
    @TableField("status")
    private String status;

    @ApiModelProperty("删除标志 0代表存在 2代表删除")
    @TableField("del_flag")
    private String delFlag;

    @ApiModelProperty(value = "部门logo")
//    @TableField(value = "dept_logo", typeHandler = ListTypeHandler.class)
    private String deptLogo;

    @ApiModelProperty(value = "部门封面")
//    @TableField(value = "dept_cover", typeHandler = ListTypeHandler.class)
    private String deptCover;

    @ApiModelProperty("父部门名称")
    @TableField(exist = false)
    private String parentName;

    @ApiModelProperty("排除编号")
    @TableField(exist = false)
    @JsonIgnore
    private Long excludeId;

    @ApiModelProperty("子部门")
    @TableField(exist = false)
    private List<SysDept> children;

}
