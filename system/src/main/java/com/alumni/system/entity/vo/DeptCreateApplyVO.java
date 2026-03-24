package com.alumni.system.entity.vo;

import com.alumni.common.core.domain.model.FileDTO;
import com.alumni.common.handler.ListTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "创建班级申请列表")
public class DeptCreateApplyVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "申请用户id")
    private Long applyUserId;

    @ApiModelProperty(value = "申请用户名称")
    private String applyUserName;

    @ApiModelProperty(value = "审核用户id")
    private Long checkUserId;

    @ApiModelProperty(value = "审核用户名称")
    private String checkUserName;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "届")
    private String yearName;

    @ApiModelProperty(value = "部门logo")
//    @TableField(value = "dept_logo", typeHandler = ListTypeHandler.class)
    private String deptLogo;

    @ApiModelProperty(value = "部门封面")
//    @TableField(value = "dept_cover", typeHandler = ListTypeHandler.class)
    private String deptCover;

    @ApiModelProperty(value = "审核状态（0未审核 1审核通过 2审核拒绝）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
