package com.alumni.system.entity.vo;

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
@ApiModel(value = "校友列表")
public class SysUserVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty("部门ID列表")
    private List<Long> deptIds;

    @ApiModelProperty("用户名称")
    private String nickName;

    @ApiModelProperty("用户类型 tourist-游客 alumni-校友")
    private String userType;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("手机号码")
    private String phonenumber;

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

    @ApiModelProperty(value = "班级信息")
    private String className;

    @ApiModelProperty(value = "届信息")
    private String yearName;

    @ApiModelProperty(value = "腾讯地图-坐标")
    private String coordinate;

    @ApiModelProperty(value = "点亮地图")
    private Boolean lightMap;

    @ApiModelProperty(value = "距离")
    private String distance;

}
