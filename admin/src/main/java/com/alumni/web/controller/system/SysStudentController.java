package com.alumni.web.controller.system;


import com.alumni.common.annotation.Log;
import com.alumni.common.core.controller.BaseController;
import com.alumni.common.core.domain.AjaxResult;
import com.alumni.common.core.page.TableDataInfo;
import com.alumni.common.enums.BusinessType;
import com.alumni.common.utils.poi.ExcelUtil;
import com.alumni.system.domain.SysStudent;
import com.alumni.system.domain.excel.SysStudentExcel;
import com.alumni.system.entity.ro.StudentPageRO;
import com.alumni.system.entity.vo.SysStudentVO;
import com.alumni.system.service.ISysStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 学生信息表 前端控制器
 * </p>
 *
 * @author lifeng
 * @since 2025-12-19
 */
@RestController
@RequestMapping("/system/student")
@Api(value = "学生信息", tags = "学生信息")
public class SysStudentController extends BaseController {
    @Resource
    private ISysStudentService studentService;

    @PostMapping("/page")
    @ApiOperation(value = "学生信息列表", notes = "学生信息列表")
    public TableDataInfo page(@RequestBody StudentPageRO ro) {
        IPage<SysStudentVO> page = studentService.pageByCondition(ro);
        return getDataTable(page);
    }

    @PostMapping("/list")
    @ApiOperation(value = "学生信息列表（不分页）", notes = "学生信息列表（不分页）")
    public AjaxResult list(@RequestBody StudentPageRO ro) {
        List<SysStudentVO> list = studentService.listByConditionV2(ro);
        return AjaxResult.success(list);
    }

    @Log(title = "添加学生", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    @ApiOperation(value = "添加学生", notes = "添加学生")
    public AjaxResult apply(@RequestBody @Validated SysStudent student) {
        studentService.insert(student);
        return AjaxResult.success();
    }

    @Log(title = "修改学生", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ApiOperation(value = "修改学生", notes = "修改学生")
    public AjaxResult update(@RequestBody @Validated SysStudent student) {
        studentService.update(student);
        return AjaxResult.success();
    }

    @Log(title = "删除学生", businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    @ApiOperation(value = "删除学生", notes = "删除学生")
    public AjaxResult delete(@RequestParam Long id) {
        studentService.remove(id);
        return AjaxResult.success();
    }

    @Log(title = "批量更新", businessType = BusinessType.UPDATE)
    @GetMapping("/batch/update")
    @ApiOperation(value = "批量更新", notes = "批量更新")
    public AjaxResult batchUpdate(@RequestBody List<SysStudent> studentList) {
        studentService.batchUpdate(studentList);
        return AjaxResult.success();
    }

    @GetMapping("/importTemplate")
    @ApiOperation(value = "导入模板", notes = "导入模板")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysStudentExcel> util = new ExcelUtil<SysStudentExcel>(SysStudentExcel.class);
        util.importTemplateExcel(response, "学生数据");
    }

    @Log(title = "学生管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:student:import')")
    @PostMapping("/importData")
    @ApiOperation(value = "导入学生", notes = "导入学生")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<SysStudentExcel> util = new ExcelUtil<SysStudentExcel>(SysStudentExcel.class);
        List<SysStudentExcel> userList = util.importExcel(file.getInputStream());
        String message = studentService.importStudent(userList);
        return success(message);
    }
}
