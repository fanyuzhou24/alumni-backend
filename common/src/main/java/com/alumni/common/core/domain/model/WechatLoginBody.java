package com.alumni.common.core.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录对象
 *
 * @author alumni
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatLoginBody {

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("code")
    private String code;

}
