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
 * 学生信息表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_student")
@ApiModel(value="SysStudent对象", description="学生信息表")
public class SysStudent extends BaseEntity {

    @ApiModelProperty(value = "学生ID")
    @TableId(value = "student_id", type = IdType.AUTO)
    private Long studentId;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentNo;

    @ApiModelProperty(value = "所属班级ID（sys_dept）")
    private Long deptId;

    @ApiModelProperty(value = "所属用户ID（sys_user）")
    private Long userId;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

}
