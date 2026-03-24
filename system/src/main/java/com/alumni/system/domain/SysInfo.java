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
 * 系统信息表
 * </p>
 *
 * @author lifeng
 * @since 2025-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "sys_info", autoResultMap = true)
@ApiModel(value="SysInfo对象", description="系统信息表")
public class SysInfo extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "系统名称")
    @TableField("system_name")
    private String systemName;

    @ApiModelProperty(value = "欢迎语")
    @TableField("welcome_word")
    private String welcomeWord;

    @ApiModelProperty(value = "注册提示")
    @TableField("notice")
    private String notice;

    @ApiModelProperty(value = "logo")
//    @TableField(value = "logo", typeHandler = ListTypeHandler.class)
    private String logo;

    @ApiModelProperty(value = "轮播图")
//    @TableField(value = "carousel", typeHandler = ListTypeHandler.class)
    private String carousel;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;


}
