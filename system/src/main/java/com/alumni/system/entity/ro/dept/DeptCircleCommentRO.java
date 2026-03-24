package com.alumni.system.entity.ro.dept;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "班级动态评论入参")
public class DeptCircleCommentRO {

    @ApiModelProperty(value = "班级动态id")
    @NotNull(message = "班级动态id不能为空")
    private Long postId;

    @ApiModelProperty(value = "评论用户id")
    private Long userId;

    @ApiModelProperty(value = "父评论id")
    private Long parentId;

    @ApiModelProperty(value = "被回复用户id")
    private Long replyUserId;

    @ApiModelProperty(value = "回复内容")
    private String content;
}
