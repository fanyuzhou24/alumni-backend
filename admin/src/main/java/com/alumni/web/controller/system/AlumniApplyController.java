package com.alumni.web.controller.system;

import com.alumni.common.annotation.Log;
import com.alumni.common.core.controller.BaseController;
import com.alumni.common.core.domain.AjaxResult;
import com.alumni.common.core.page.TableDataInfo;
import com.alumni.common.enums.BusinessType;
import com.alumni.system.domain.AlumniApply;
import com.alumni.system.entity.dto.AlumniApplyDTO;
import com.alumni.system.entity.ro.AlumniApplyPageRO;
import com.alumni.system.service.IAlumniApplyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 校友申请
 * 
 * @author lifeng
 */
@Controller
@RequestMapping("/alumni/apply")
@Api(value = "校友申请", tags = "校友申请")
public class AlumniApplyController extends BaseController {
    @Resource
    private IAlumniApplyService alumniApplyService;

    @PostMapping("/page")
    @ResponseBody
    @ApiOperation(value = "校友申请列表", notes = "校友申请列表")
    public TableDataInfo page(@RequestBody AlumniApplyPageRO ro) {
        Page<AlumniApply> page = alumniApplyService.pageByCondition(ro);
        return getDataTable(page);
    }

    @GetMapping("/status")
    @ResponseBody
    @ApiOperation(value = "当前用户审核状态", notes = "当前用户审核状态")
    public AjaxResult status() {
        return AjaxResult.success(alumniApplyService.getUserStatus());
    }

    @Log(title = "校友申请", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "新增校友申请", notes = "新增校友申请")
    public AjaxResult apply(@RequestBody @Validated AlumniApply alumniApply) {
        alumniApplyService.insert(alumniApply);
        return AjaxResult.success();
    }

    @Log(title = "校友申请审批", businessType = BusinessType.UPDATE)
    @PostMapping("/check")
    @ResponseBody
    @ApiOperation(value = "审批校友申请", notes = "审批校友申请")
    public AjaxResult check(@RequestBody @Validated AlumniApplyDTO alumniApplyDTO) {
        alumniApplyService.check(alumniApplyDTO);
        return AjaxResult.success();
    }
}
