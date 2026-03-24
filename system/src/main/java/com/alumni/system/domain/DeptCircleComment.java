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
 * 
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dept_circle_comment")
@ApiModel(value="DeptCircleComment对象", description="")
public class DeptCircleComment extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("post_id")
    private Long postId;

    @TableField("user_id")
    @ApiModelProperty(value = "回复用户")
    private Long userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "回复用户名")
    private String userName;

    @ApiModelProperty(value = "父评论id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "被回复用户id")
    @TableField("reply_user_id")
    private Long replyUserId;

    @TableField(exist = false)
    @ApiModelProperty(value = "被回复用户名")
    private String replyUserName;

    @TableField("content")
    private String content;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;


}
