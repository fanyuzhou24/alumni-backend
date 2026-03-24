package com.alumni.system.domain;

import com.alumni.common.core.domain.BaseEntity;
import com.alumni.common.core.domain.model.FileDTO;
import com.alumni.common.handler.ListTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 创建班级申请表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "dept_create_apply", autoResultMap = true)
@ApiModel(value="DeptCreateApply对象", description="创建班级申请表")
public class DeptCreateApply extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "申请用户id")
    @TableField("apply_user_id")
    private Long applyUserId;

    @ApiModelProperty(value = "审核用户id")
    @TableField("check_user_id")
    private Long checkUserId;

    @ApiModelProperty(value = "班级名称")
    @TableField("class_name")
    private String className;

    @ApiModelProperty(value = "届")
    @TableField("year_name")
    private String yearName;

    @ApiModelProperty(value = "部门logo")
//    @TableField(value = "dept_logo", typeHandler = ListTypeHandler.class)
    private String deptLogo;

    @ApiModelProperty(value = "部门封面")
//    @TableField(value = "dept_cover", typeHandler = ListTypeHandler.class)
    private String deptCover;

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;

}
