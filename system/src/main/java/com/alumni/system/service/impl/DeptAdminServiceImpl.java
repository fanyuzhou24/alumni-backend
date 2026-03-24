package com.alumni.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.core.domain.entity.SysDept;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.enums.StatusEnum;
import com.alumni.common.enums.UserTypeEnum;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.system.domain.DeptAdmin;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.dept.DeptAdminPageRO;
import com.alumni.system.entity.ro.dept.DeptAdminRO;
import com.alumni.system.entity.ro.dept.DeptAdminUpdateRO;
import com.alumni.system.entity.vo.DeptAdminVO;
import com.alumni.system.mapper.DeptAdminMapper;
import com.alumni.system.mapper.SysDeptMapper;
import com.alumni.system.service.IDeptAdminService;
import com.alumni.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 班级管理员表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Service
public class DeptAdminServiceImpl extends ServiceImpl<DeptAdminMapper, DeptAdmin> implements IDeptAdminService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private ISysUserService userService;

    @Override
    public IPage<DeptAdminVO> pageByCondition(DeptAdminPageRO ro) {
        Page<DeptAdminVO> page = this.baseMapper.pageByCondition(ro, new Page<>(ro.getPageNum(), ro.getPageSize()));

        Map<Long, SysDept> deptMap = new HashMap<>();
        Map<Long, String> parentMap = new HashMap<>();
        List<Long> deptIds = page.getRecords().stream().map(DeptAdminVO::getDeptId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deptIds)) {
            List<SysDept> deptList = sysDeptMapper.selectBatchIds(deptIds);
            deptMap = deptList.stream().collect(Collectors.toMap(SysDept::getDeptId, a -> a));

            // 父部门名称
            List<Long> parentIds = deptList.stream().map(SysDept::getParentId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(parentIds)) {
                List<SysDept> parentDept = Optional.ofNullable(sysDeptMapper.selectBatchIds(parentIds)).orElse(new ArrayList<>());
                parentMap = parentDept.stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));
            }
        }
        Map<Long, SysDept> finalDeptMap = deptMap;
        Map<Long, String> finalParentMap = parentMap;
        return page.convert(record -> {
            SysDept dept = finalDeptMap.get(record.getDeptId());
            if (Objects.nonNull(dept)) {
                record.setClassName(dept.getDeptName());
                record.setYearName(finalParentMap.get(dept.getParentId()));
            }
            return record;
        });
    }

    @Override
    public List<DeptAdminVO> listByCondition(DeptAdminPageRO ro) {
        return this.baseMapper.listByCondition(ro);
    }

    @Override
    public void check(CheckRO ro) {
        DeptAdmin deptAdmin = getById(ro.getBusinessId());
        Assert.notNull(deptAdmin, "申请不存在");
        validCheckUser(SecurityUtils.getUserId(), deptAdmin.getDeptId());

        lambdaUpdate()
                .eq(DeptAdmin::getId, ro.getBusinessId())
                .set(DeptAdmin::getStatus, ro.getStatus())
                .set(DeptAdmin::getCheckUserId, SecurityUtils.getUserId())
                .update();
    }

    @Override
    public void applyAdmin(DeptAdminRO ro) {
        Long userId = SecurityUtils.getUserId();
        ro.setUserId(userId);

        DeptAdmin deptAdmin = lambdaQuery()
                .eq(DeptAdmin::getUserId, ro.getUserId())
                .eq(DeptAdmin::getDeptId, ro.getDeptId())
                .one();
        if (Objects.nonNull(deptAdmin)) {
            Assert.isTrue(Objects.equals(deptAdmin.getStatus(), 1), "当前用户已经是班级管理员！");
            deptAdmin.setStatus(0);
            deptAdmin.setFileUrl(ro.getFileUrl());
            deptAdmin.setApplyReason(ro.getApplyReason());
            deptAdmin.setCheckUserId(null);
            updateById(deptAdmin);
        } else {
            DeptAdmin insert = BeanUtil.copyProperties(ro, DeptAdmin.class);
            insert.setStatus(0);
            save(insert);
        }
    }

    @Override
    public void updateAdmin(DeptAdminUpdateRO ro) {
        Long currentUserId = SecurityUtils.getUserId();
        Assert.isTrue(!Objects.equals(ro.getUserId(), currentUserId), "不能操作自己");

        validCheckUser(currentUserId, ro.getDeptId());

        DeptAdmin deptAdmin = lambdaQuery().eq(DeptAdmin::getUserId, ro.getUserId())
                .eq(DeptAdmin::getDeptId, ro.getDeptId())
                .eq(DeptAdmin::getStatus, StatusEnum.CHECK_PASS.getCode())
                .one();
        if (Objects.equals(ro.getOperation(), 0)) {
            if (Objects.nonNull(deptAdmin)) {
                lambdaUpdate()
                        .eq(DeptAdmin::getId, deptAdmin.getId())
                        .set(DeptAdmin::getCheckUserId, currentUserId)
                        .set(DeptAdmin::getDelFlag, "0")
                        .update();
            } else {
                DeptAdmin insert = new DeptAdmin();
                insert.setUserId(ro.getUserId());
                insert.setDeptId(ro.getDeptId());
                insert.setStatus(StatusEnum.CHECK_PASS.getCode());
                insert.setCheckUserId(currentUserId);
                insert.setDelFlag("0");
                save(insert);
            }
        } else {
            lambdaUpdate()
                    .eq(DeptAdmin::getId, deptAdmin.getId())
                    .set(DeptAdmin::getCheckUserId, currentUserId)
                    .set(DeptAdmin::getDelFlag, "2")
                    .update();
        }
    }

    @Override
    public void validCheckUser(Long currentUserId, Long deptId) {
        SysUser user = userService.getById(currentUserId);
        Assert.notNull(user, "用户不存在");

        if (!StringUtils.equals(user.getUserType(), UserTypeEnum.ADMIN.getCode())) {
            DeptAdmin deptAdmin = lambdaQuery().eq(DeptAdmin::getUserId, currentUserId)
                    .eq(DeptAdmin::getDeptId, deptId)
                    .eq(DeptAdmin::getStatus, StatusEnum.CHECK_PASS.getCode())
                    .eq(DeptAdmin::getDelFlag, 0)
                    .one();
            Assert.notNull(deptAdmin, "非班级管理员不可操作");
        }
    }

}
