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
 * 职业发展表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_info")
@ApiModel(value="WorkInfo对象", description="职业发展表")
public class WorkInfo extends BaseEntity {

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "单位名称")
    private String employer;

    @ApiModelProperty(value = "工作职位")
    private String job;

    @ApiModelProperty(value = "单位地址")
    private String address;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "单位性质")
    private String unitNature;

    @ApiModelProperty(value = "入校年份")
    private String startYear;

    @ApiModelProperty(value = "离校年份")
    private String endYear;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

}
