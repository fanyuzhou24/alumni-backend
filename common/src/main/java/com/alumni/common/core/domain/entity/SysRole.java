package com.alumni.common.core.domain.entity;

import com.alumni.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 角色表 sys_role
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "角色对象")
@TableName("sys_role")
public class SysRole extends BaseEntity {

    @ApiModelProperty("角色ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @ApiModelProperty("角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    @TableField("role_name")
    private String roleName;

    @ApiModelProperty("角色权限")
    @NotBlank(message = "权限字符不能为空")
    @Size(max = 100, message = "权限字符长度不能超过100个字符")
    @TableField("role_key")
    private String roleKey;

    @ApiModelProperty("角色排序")
    @NotBlank(message = "显示顺序不能为空")
    @TableField("role_sort")
    private String roleSort;

    @ApiModelProperty("数据范围 1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    @TableField("data_scope")
    private String dataScope;

    @ApiModelProperty("角色状态 0正常 1停用")
    @TableField("status")
    private String status;

    @ApiModelProperty("删除标志 0代表存在 2代表删除")
    @TableField("del_flag")
    private String delFlag;

    @ApiModelProperty("用户是否存在此角色标识 默认false")
    @TableField(exist = false)
    private boolean flag = false;

    @ApiModelProperty("菜单组")
    @TableField(exist = false)
    private Long[] menuIds;

    @ApiModelProperty("部门组（数据权限）")
    @TableField(exist = false)
    private Long[] deptIds;

    @ApiModelProperty("角色菜单权限")
    @TableField(exist = false)
    private Set<String> permissions;

    @ApiModelProperty("菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）")
    @TableField(exist = false)
    private boolean menuCheckStrictly;

    @ApiModelProperty("部门树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）")
    @TableField(exist = false)
    private boolean deptCheckStrictly;

    public SysRole() {}

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }
}
