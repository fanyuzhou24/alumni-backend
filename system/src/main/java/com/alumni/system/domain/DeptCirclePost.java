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
 * 班级动态表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "dept_circle_post", autoResultMap = true)
@ApiModel(value="DeptCirclePost对象", description="班级动态表")
public class DeptCirclePost extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "发布用户id")
    @TableField("publish_user_id")
    private Long publishUserId;

    @ApiModelProperty(value = "部门id")
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "文件路径")
//    @TableField(value = "file_url", typeHandler = ListTypeHandler.class)
    private String fileUrl;

    @ApiModelProperty(value = "文字内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "话题，多个用逗号分隔，如 #生活,#旅行")
    @TableField("topics")
    private String topics;

    @ApiModelProperty(value = "定位信息")
    @TableField("location")
    private String location;

    @ApiModelProperty(value = "是否公开：1公开 0仅自己可见")
    @TableField("is_public")
    private Integer isPublic;

    @ApiModelProperty(value = "点赞数")
    @TableField("like_count")
    private Integer likeCount;

    @ApiModelProperty(value = "评论数")
    @TableField("comment_count")
    private Integer commentCount;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;


}
