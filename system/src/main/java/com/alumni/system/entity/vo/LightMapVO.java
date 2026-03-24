package com.alumni.system.entity.vo;

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
@ApiModel(value = "点亮地图列表")
public class LightMapVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "腾讯地图-坐标")
    private String coordinate;

    @ApiModelProperty(value = "点亮地图")
    private Boolean lightMap;

}
