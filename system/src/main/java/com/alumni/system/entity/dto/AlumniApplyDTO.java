package com.alumni.system.entity.dto;

import com.alumni.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 校友申请表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="AlumniApplyDTO", description="校友申请审核入参")
public class AlumniApplyDTO extends BaseEntity {

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

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

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    private Integer status;

    @ApiModelProperty(value = "绑定学生id列表")
    private List<Long> studentIds;
}
