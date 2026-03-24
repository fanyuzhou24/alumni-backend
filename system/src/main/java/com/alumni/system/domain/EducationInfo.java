package com.alumni.system.domain;

import com.alumni.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教育经历表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("education_info")
@ApiModel(value="EducationInfo对象", description="教育经历表")
public class EducationInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    @ApiModelProperty(value = "学历")
    private String qualification;

    @ApiModelProperty(value = "入校年份")
    private String startYear;

    @ApiModelProperty(value = "离校年份")
    private String endYear;

    @ApiModelProperty(value = "就读学院")
    private String college;

    @ApiModelProperty(value = "就读专业")
    private String major;

    @ApiModelProperty(value = "就读班级")
    private String className;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;


}
