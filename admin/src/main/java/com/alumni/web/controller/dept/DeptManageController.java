package com.alumni.web.controller.dept;


import com.alumni.common.core.controller.BaseController;
import com.alumni.common.core.domain.AjaxResult;
import com.alumni.common.core.page.TableDataInfo;
import com.alumni.system.domain.DeptAlbum;
import com.alumni.system.domain.DeptCirclePost;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.dept.*;
import com.alumni.system.entity.vo.*;
import com.alumni.system.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 班级管理表 前端控制器
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@RestController
@RequestMapping("/dept/manage")
@Api(value = "班级管理", tags = "班级管理")
public class DeptManageController extends BaseController {
    @Resource
    private IDeptAdminService adminService;

    @Resource
    private IDeptJoinApplyService joinApplyService;

    @Resource
    private IDeptCreateApplyService createApplyService;

    @Resource
    private IDeptAlbumService albumService;

    @Resource
    private IDeptCirclePostService circlePostService;

    @Resource
    private IDeptCircleLikeService circleLikeService;

    @Resource
    private IDeptCircleCommentService circleCommentService;

    // ----------------------------------------  班级管理员模块  -------------------------------------------------------
    @PostMapping("/admin/page")
    @ResponseBody
    @ApiOperation(value = "班级管理员申请列表", notes = "班级管理员申请列表")
    public TableDataInfo adminPage(@RequestBody DeptAdminPageRO ro) {
        IPage<DeptAdminVO> page = adminService.pageByCondition(ro);
        return getDataTable(page);
    }

    @PostMapping("/admin/apply")
    @ResponseBody
    @ApiOperation(value = "班级管理员申请", notes = "班级管理员申请")
    public AjaxResult applyAdmin(@RequestBody @Validated DeptAdminRO ro) {
        adminService.applyAdmin(ro);
        return AjaxResult.success();
    }

    @PostMapping("/admin/check")
    @ResponseBody
    @ApiOperation(value = "审核班级管理员申请", notes = "审核班级管理员申请")
    public AjaxResult checkAdmin(@RequestBody @Validated CheckRO ro) {
        adminService.check(ro);
        return AjaxResult.success();
    }

    @PostMapping("/admin/update")
    @ResponseBody
    @ApiOperation(value = "设置班级管理员", notes = "设置班级管理员")
    public AjaxResult updateAdmin(@RequestBody @Validated DeptAdminUpdateRO ro) {
        adminService.updateAdmin(ro);
        return AjaxResult.success();
    }

    // ----------------------------------------  创建/加入班级模块  -----------------------------------------------------

    @PostMapping("/join/page")
    @ResponseBody
    @ApiOperation(value = "加入班级申请列表", notes = "加入班级申请列表")
    public TableDataInfo joinPage(@RequestBody DeptJoinApplyPageRO ro) {
        IPage<DeptJoinApplyVO> page = joinApplyService.pageByCondition(ro);
        return getDataTable(page);
    }

    @PostMapping("/join/apply")
    @ResponseBody
    @ApiOperation(value = "加入班级申请", notes = "加入班级申请")
    public AjaxResult applyJoin(@RequestBody DeptJoinRO ro) {
        joinApplyService.applyJoin(ro);
        return AjaxResult.success();
    }

    @PostMapping("/join/check")
    @ResponseBody
    @ApiOperation(value = "审核加入班级申请", notes = "审核加入班级申请")
    public AjaxResult checkJoin(@RequestBody @Validated CheckRO ro) {
        joinApplyService.check(ro);
        return AjaxResult.success();
    }

    @PostMapping("/create/page")
    @ResponseBody
    @ApiOperation(value = "创建班级申请列表", notes = "创建班级申请列表")
    public TableDataInfo createPage(@RequestBody DeptCreateApplyPageRO ro) {
        IPage<DeptCreateApplyVO> page = createApplyService.pageByCondition(ro);
        return getDataTable(page);
    }

    @PostMapping("/create/apply")
    @ResponseBody
    @ApiOperation(value = "创建班级申请", notes = "创建班级申请")
    public AjaxResult applyCreate(@RequestBody DeptCreateRO ro) {
        createApplyService.applyCreate(ro);
        return AjaxResult.success();
    }

    @PostMapping("/create/check")
    @ResponseBody
    @ApiOperation(value = "审核创建班级申请", notes = "审核创建班级申请")
    public AjaxResult checkCreate(@RequestBody @Validated CheckRO ro) {
        createApplyService.check(ro);
        return AjaxResult.success();
    }

    // ----------------------------------------  班级相册/动态模块  -----------------------------------------------------
    @PostMapping("/album/page")
    @ResponseBody
    @ApiOperation(value = "班级相册申请列表", notes = "班级相册申请列表")
    public TableDataInfo albumPage(@RequestBody DeptAlbumPageRO ro) {
        Page<DeptAlbum> page = albumService.pageByCondition(ro);
        return getDataTable(page);
    }

    @PostMapping("/album/apply")
    @ResponseBody
    @ApiOperation(value = "班级相册申请", notes = "班级相册申请")
    public AjaxResult applyAlbum(@RequestBody DeptAlbum deptAlbum) {
        albumService.applyAlbum(deptAlbum);
        return AjaxResult.success();
    }

    @PostMapping("/album/check")
    @ResponseBody
    @ApiOperation(value = "审核班级相册", notes = "审核班级相册")
    public AjaxResult checkAlbum(@RequestBody @Validated CheckRO ro) {
        albumService.check(ro);
        return AjaxResult.success();
    }

    @GetMapping("/album/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除班级相册", notes = "删除班级相册")
    public AjaxResult deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return AjaxResult.success();
    }

    @PostMapping("/circle/page")
    @ResponseBody
    @ApiOperation(value = "班级动态列表", notes = "班级动态列表")
    public TableDataInfo circlePage(@RequestBody DeptCirclePageRO ro) {
        Page<DeptCirclePostVO> page = circlePostService.pageByCondition(ro);
        return getDataTable(page);
    }

    @PostMapping("/circle/insert")
    @ResponseBody
    @ApiOperation(value = "发布班级动态", notes = "发布班级动态")
    public AjaxResult insertCircle(@RequestBody DeptCirclePost deptCirclePost) {
        circlePostService.insertCircle(deptCirclePost);
        return AjaxResult.success();
    }

    @GetMapping("/circle/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除班级动态", notes = "删除班级动态")
    public AjaxResult deleteCircle(@PathVariable Long id) {
        circlePostService.deleteCircle(id);
        return AjaxResult.success();
    }

    @GetMapping("/circle/post")
    @ResponseBody
    @ApiOperation(value = "班级动态", notes = "班级动态")
    public AjaxResult queryPost(@RequestParam Long postId) {
        DeptCirclePostDetailVO detail = circlePostService.detail(postId);
        return AjaxResult.success(detail);
    }

    @PostMapping("/circle/comment")
    @ResponseBody
    @ApiOperation(value = "班级动态评论", notes = "班级动态评论")
    public AjaxResult comment(@RequestBody DeptCircleCommentRO ro) {
        circlePostService.comment(ro);
        return AjaxResult.success();
    }

    @PostMapping("/circle/like")
    @ResponseBody
    @ApiOperation(value = "班级动态点赞", notes = "班级动态点赞")
    public AjaxResult like(@RequestBody DeptCircleLikeRO ro) {
        circlePostService.like(ro);
        return AjaxResult.success();
    }


}
