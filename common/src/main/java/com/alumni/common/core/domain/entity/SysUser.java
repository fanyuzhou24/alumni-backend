package com.alumni.common.core.domain.entity;

import com.alumni.common.annotation.Excel;
import com.alumni.common.annotation.Excel.ColumnType;
import com.alumni.common.annotation.Excel.Type;
import com.alumni.common.annotation.Excels;
import com.alumni.common.core.domain.BaseEntity;
import com.alumni.common.handler.ListTypeHandler;
import com.alumni.common.xss.Xss;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户对象")
@TableName(value = "sys_user", autoResultMap = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUser extends BaseEntity {

    @ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    @ApiModelProperty("部门ID")
    @TableField("dept_id")
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    @ApiModelProperty("部门ID列表")
    @TableField(value = "dept_ids", typeHandler = ListTypeHandler.class)
    @Excel(name = "部门编号列表", type = Type.IMPORT)
    private List<Long> deptIds;

    @ApiModelProperty("用户名称")
    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    @TableField("nick_name")
    @Excel(name = "用户昵称")
    private String nickName;

    @ApiModelProperty("用户类型 tourist-游客 alumni-校友")
    @TableField("user_type")
    private String userType;

    @ApiModelProperty("用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @TableField("email")
    @Excel(name = "用户邮箱")
    private String email;

    @ApiModelProperty("手机号码")
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    @TableField("phonenumber")
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    private String phonenumber;

    @ApiModelProperty("微信OpenID")
    @TableField("open_id")
    private String openId;

    @ApiModelProperty("用户性别 0=男,1=女,2=未知")
    @TableField("sex")
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    @ApiModelProperty("用户头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "入校年份")
    private String startYear;

    @ApiModelProperty(value = "离校年份")
    private String endYear;

    @ApiModelProperty(value = "地区")
    private String area;

    @ApiModelProperty(value = "工作单位")
    private String employer;

    @ApiModelProperty(value = "工作职位")
    private String job;

    @ApiModelProperty(value = "校友卡号")
    private String alumniCardNo;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "腾讯地图-坐标")
    private String coordinate;

    @ApiModelProperty(value = "点亮地图")
    private Boolean lightMap;

    @ApiModelProperty(value = "是否公开定位信息")
    private Boolean showCoordinate;

    @ApiModelProperty(value = "是否展示手机号码")
    private Boolean showPhone;

    @ApiModelProperty(value = "是否展示个人照片")
    private Boolean showPicture;

    @ApiModelProperty(value = "是否展示工作职位")
    private Boolean showJob;

    @ApiModelProperty(value = "是否展示工作单位")
    private Boolean showEmployer;

    @ApiModelProperty(value = "是否展示邮箱")
    private Boolean showEmail;

    @ApiModelProperty("密码")
    @TableField("password")
    @JsonIgnore
    private String password;

    @ApiModelProperty("账号状态 0=正常 1=停用")
    @TableField("status")
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    @ApiModelProperty("删除标志 0=存在 2=删除")
    @TableField("del_flag")
    private String delFlag;

    @ApiModelProperty("最后登录IP")
    @TableField("login_ip")
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    @ApiModelProperty("最后登录时间")
    @TableField("login_date")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    @ApiModelProperty("密码最后更新时间")
    @TableField("pwd_update_date")
    private Date pwdUpdateDate;


    @ApiModelProperty(value = "距离")
    @TableField(exist = false)
    private String distance;

    @ApiModelProperty("部门对象")
    @TableField(exist = false)
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    @JsonIgnore
    private SysDept dept;

    @ApiModelProperty("角色列表")
    @TableField(exist = false)
    @JsonIgnore
    private List<SysRole> roles;

    @ApiModelProperty("角色组")
    @TableField(exist = false)
    @JsonIgnore
    private Long[] roleIds;

    @ApiModelProperty("岗位组")
    @TableField(exist = false)
    @JsonIgnore
    private Long[] postIds;

    public SysUser() {}

    public SysUser(Long userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    @JsonIgnore
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
