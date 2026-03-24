package com.alumni.system.entity.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户对象 sys_user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRO {

    @ApiModelProperty("用户ID")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String nickName;

    @ApiModelProperty("用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @ApiModelProperty("手机号码")
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    private String phonenumber;

    @ApiModelProperty("微信OpenID")
    private String openId;

    @ApiModelProperty("用户性别 0=男,1=女,2=未知")
    private String sex;

    @ApiModelProperty("用户头像")
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

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "腾讯地图-坐标")
    private String coordinate;

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
}
