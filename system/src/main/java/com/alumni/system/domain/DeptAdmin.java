package com.alumni.system.domain;

import com.alumni.common.core.domain.BaseEntity;
import com.alumni.common.core.domain.model.FileDTO;
import com.alumni.common.handler.ListTypeHandler;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 班级管理员表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "dept_admin", autoResultMap = true)
@ApiModel(value="DeptAdmin对象", description="班级管理员表")
public class DeptAdmin extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "部门id")
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "审核用户id")
    @TableField(value = "check_user_id", updateStrategy = FieldStrategy.IGNORED)
    private Long checkUserId;

    @ApiModelProperty(value = "文件路径")
//    @TableField(value = "file_url", typeHandler = ListTypeHandler.class)
    private String fileUrl;

    @ApiModelProperty(value = "申请理由")
    @TableField("apply_reason")
    private String applyReason;

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;


}
