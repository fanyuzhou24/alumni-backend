package com.alumni.system.domain;

import com.alumni.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 班级相册表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "dept_album", autoResultMap = true)
@ApiModel(value="DeptAlbum对象", description="班级相册表")
public class DeptAlbum extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "申请用户id")
    @TableField("apply_user_id")
    private Long applyUserId;

    @ApiModelProperty(value = "部门id")
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "审核用户id")
    @TableField("check_user_id")
    private Long checkUserId;

    @ApiModelProperty(value = "文件路径")
//    @TableField(value = "file_url", typeHandler = ListTypeHandler.class)
    private String fileUrl;

    @ApiModelProperty(value = "祝福")
    @TableField("blessings")
    private String blessings;

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;


}
