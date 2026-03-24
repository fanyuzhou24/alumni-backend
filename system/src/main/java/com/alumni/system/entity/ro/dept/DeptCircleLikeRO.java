package com.alumni.system.entity.ro.dept;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "班级动态点赞入参")
public class DeptCircleLikeRO {

    @ApiModelProperty(value = "班级动态id")
    private Long postId;

    @ApiModelProperty(value = "评论用户id")
    private Long userId;

    @ApiModelProperty(value = "是否点赞")
    private Boolean isLike;

}
