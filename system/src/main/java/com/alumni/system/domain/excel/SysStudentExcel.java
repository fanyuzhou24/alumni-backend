package com.alumni.system.domain.excel;

import com.alumni.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 学生信息导入
 * </p>
 *
 * @author lifeng
 * @since 2025-12-19
 */
@Data
@ApiModel(value="SysStudentExcel", description="学生信息导入")
public class SysStudentExcel {

    @ApiModelProperty(value = "届")
    @Excel(name = "届")
    private String yearName;

    @ApiModelProperty(value = "班级名称")
    @Excel(name = "班级名称")
    private String className;

    @ApiModelProperty(value = "学生姓名")
    @Excel(name = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    @Excel(name = "学号")
    private String studentNo;

    @ApiModelProperty(value = "性别")
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    private String gender;

}
