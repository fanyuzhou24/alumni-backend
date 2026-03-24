package com.alumni.system.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "班级动态列表")
public class DeptCirclePostDetailVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "发布用户id")
    private Long publishUserId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

    @ApiModelProperty(value = "文字内容")
    private String content;

    @ApiModelProperty(value = "话题，多个用逗号分隔，如 #生活,#旅行")
    private String topics;

    @ApiModelProperty(value = "定位信息")
    private String location;

    @ApiModelProperty(value = "是否公开：1公开 0仅自己可见")
    private Integer isPublic;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentCount;

    @ApiModelProperty(value = "发布时间")
    private Date createTime;

    @ApiModelProperty(value = "是否点赞")
    private Boolean isLike;

    @ApiModelProperty(value = "发布人姓名")
    private String userName;

    @ApiModelProperty(value = "发布人头像")
    private String avatar;

    @ApiModelProperty(value = "评论")
    private List<DeptCircleCommentVO> comments;
}
