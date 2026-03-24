package com.alumni.web.controller.system;


import com.alumni.common.annotation.Log;
import com.alumni.common.core.controller.BaseController;
import com.alumni.common.core.domain.AjaxResult;
import com.alumni.common.core.page.TableDataInfo;
import com.alumni.common.enums.BusinessType;
import com.alumni.system.domain.WorkInfo;
import com.alumni.system.entity.ro.WorkInfoPageRO;
import com.alumni.system.service.IWorkInfoService;
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
@RequestMapping("/work/info")
@Api(value = "工作经历", tags = "工作经历")
public class WorkInfoController extends BaseController {
    @Resource
    private IWorkInfoService workInfoService;

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation(value = "工作经历列表", notes = "工作经历列表")
    public TableDataInfo page(@RequestBody WorkInfoPageRO ro) {
        Page<WorkInfo> page = workInfoService.pageByCondition(ro);
        return getDataTable(page);
    }

    @Log(title = "添加工作经历", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "添加工作经历", notes = "添加工作经历")
    public AjaxResult apply(@RequestBody @Validated WorkInfo workInfo) {
        workInfoService.save(workInfo);
        return AjaxResult.success();
    }

    @Log(title = "修改工作经历", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation(value = "修改工作经历", notes = "修改工作经历")
    public AjaxResult update(@RequestBody @Validated WorkInfo workInfo) {
        workInfoService.updateById(workInfo);
        return AjaxResult.success();
    }

    @Log(title = "删除工作经历", businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除工作经历", notes = "删除工作经历")
    public AjaxResult delete(@RequestParam Long id) {
        workInfoService.removeById(id);
        return AjaxResult.success();
    }

}
