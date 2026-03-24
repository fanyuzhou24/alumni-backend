package com.alumni.web.controller.system;


import com.alumni.common.annotation.Log;
import com.alumni.common.core.controller.BaseController;
import com.alumni.common.core.domain.AjaxResult;
import com.alumni.common.core.page.TableDataInfo;
import com.alumni.common.enums.BusinessType;
import com.alumni.system.domain.EducationInfo;
import com.alumni.system.entity.ro.EducationInfoPageRO;
import com.alumni.system.service.IEducationInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 学生信息表 前端控制器
 * </p>
 *
 * @author lifeng
 * @since 2025-12-19
 */
@RestController
@RequestMapping("/education/info")
@Api(value = "教育经历", tags = "教育经历")
public class EducationInfoController extends BaseController {
    @Resource
    private IEducationInfoService educationInfoService;

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation(value = "教育经历列表", notes = "教育经历列表")
    public TableDataInfo page(@RequestBody EducationInfoPageRO ro) {
        Page<EducationInfo> page = educationInfoService.pageByCondition(ro);
        return getDataTable(page);
    }

    @Log(title = "添加教育经历", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "添加教育经历", notes = "添加教育经历")
    public AjaxResult apply(@RequestBody @Validated EducationInfo educationInfo) {
        educationInfoService.save(educationInfo);
        return AjaxResult.success();
    }

    @Log(title = "修改教育经历", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation(value = "修改教育经历", notes = "修改教育经历")
    public AjaxResult update(@RequestBody @Validated EducationInfo educationInfo) {
        educationInfoService.updateById(educationInfo);
        return AjaxResult.success();
    }

    @Log(title = "删除教育经历", businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除教育经历", notes = "删除教育经历")
    public AjaxResult delete(@RequestParam Long id) {
        educationInfoService.removeById(id);
        return AjaxResult.success();
    }

}
