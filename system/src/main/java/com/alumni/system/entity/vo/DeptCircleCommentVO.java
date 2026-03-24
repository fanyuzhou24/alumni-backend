package com.alumni.system.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("朋友圈评论返回对象")
public class DeptCircleCommentVO {

    @ApiModelProperty("评论ID")
    private Long id;

    @ApiModelProperty("评论所属的帖子ID")
    private Long postId;

    @ApiModelProperty("评论用户ID")
    private Long userId;

    @ApiModelProperty("评论用户昵称，可选")
    private String userName;

    @ApiModelProperty("评论用户头像，可选")
    private String avatar;

    @ApiModelProperty("父评论ID（为0或null表示一级评论）")
    private Long parentId;

    @ApiModelProperty("被回复的用户ID（@用户时使用）")
    private Long replyUserId;

    @ApiModelProperty("被回复的用户昵称")
    private String replyUserName;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;

    @ApiModelProperty("子评论列表")
    private List<DeptCircleCommentVO> children;

}
