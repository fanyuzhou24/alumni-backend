package com.alumni.system.entity.ro;

import com.alumni.common.core.page.PageDomain;
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
@ApiModel(value = "校友列表查询入参")
public class SysUserPageRO extends PageDomain {

    @ApiModelProperty(value = "班级")
    private Long deptId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty("用户类型 tourist-游客 alumni-校友")
    private String userType;

    @ApiModelProperty(value = "入校年份")
    private String startYear;

    @ApiModelProperty(value = "离校年份")
    private String endYear;

    @ApiModelProperty(value = "搜索类型 同届-year 同班-class 同行-industry 附近-location")
    private String searchType;

    @ApiModelProperty(value = "位置坐标 lng,lat")
    private String location;

    @ApiModelProperty(value = "范围(单位千米)")
    private Integer radius;
}
