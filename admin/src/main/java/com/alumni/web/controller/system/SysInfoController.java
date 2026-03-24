package com.alumni.web.controller.system;


import com.alumni.common.annotation.Anonymous;
import com.alumni.common.annotation.Log;
import com.alumni.common.core.controller.BaseController;
import com.alumni.common.core.domain.AjaxResult;
import com.alumni.common.enums.BusinessType;
import com.alumni.system.domain.SysInfo;
import com.alumni.system.service.ISysInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统信息表 前端控制器
 * </p>
 *
 * @author lifeng
 * @since 2025-12-19
 */
@RestController
@RequestMapping("/system/info")
@Api(value = "系统信息", tags = "系统信息")
public class SysInfoController extends BaseController {
    @Resource
    private ISysInfoService sysInfoService;

    @PostMapping("/list")
    @ApiOperation(value = "系统信息列表（不分页）", notes = "系统信息列表（不分页）")
    @Anonymous
    public AjaxResult list() {
        List<SysInfo> list = sysInfoService.list();
        return AjaxResult.success(list);
    }

    @Log(title = "添加系统信息", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    @ApiOperation(value = "添加系统信息", notes = "添加系统信息")
    public AjaxResult apply(@RequestBody @Validated SysInfo sysInfo) {
        sysInfoService.save(sysInfo);
        return AjaxResult.success();
    }

    @Log(title = "修改系统信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ApiOperation(value = "修改系统信息", notes = "修改系统信息")
    public AjaxResult update(@RequestBody @Validated SysInfo sysInfo) {
        sysInfoService.updateById(sysInfo);
        return AjaxResult.success();
    }

    @Log(title = "删除系统信息", businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    @ApiOperation(value = "删除系统信息", notes = "删除系统信息")
    public AjaxResult delete(@RequestParam Long id) {
        sysInfoService.removeById(id);
        return AjaxResult.success();
    }
}
