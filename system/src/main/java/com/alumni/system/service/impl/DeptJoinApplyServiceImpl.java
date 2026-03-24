package com.alumni.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.core.domain.entity.SysDept;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.enums.StatusEnum;
import com.alumni.common.exception.ServiceException;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.system.domain.DeptJoinApply;
import com.alumni.system.domain.SysStudent;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.StudentPageRO;
import com.alumni.system.entity.ro.dept.DeptJoinApplyPageRO;
import com.alumni.system.entity.ro.dept.DeptJoinRO;
import com.alumni.system.entity.vo.DeptJoinApplyVO;
import com.alumni.system.mapper.DeptJoinApplyMapper;
import com.alumni.system.mapper.SysDeptMapper;
import com.alumni.system.service.IDeptJoinApplyService;
import com.alumni.system.service.ISysStudentService;
import com.alumni.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 加入班级申请表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Service
public class DeptJoinApplyServiceImpl extends ServiceImpl<DeptJoinApplyMapper, DeptJoinApply> implements IDeptJoinApplyService {

    @Resource
    private ISysUserService userService;

    @Resource
    private ISysStudentService studentService;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public IPage<DeptJoinApplyVO> pageByCondition(DeptJoinApplyPageRO ro) {
        Page<DeptJoinApply> applyPage = lambdaQuery()
                .eq(ro.getDeptId() != null, DeptJoinApply::getDeptId, ro.getDeptId())
                .eq(ro.getApplyUserId() != null, DeptJoinApply::getApplyUserId, ro.getApplyUserId())
                .in(CollectionUtils.isNotEmpty(ro.getDeptIds()), DeptJoinApply::getDeptId, ro.getDeptIds())
                .eq(Objects.nonNull(ro.getStatus()), DeptJoinApply::getStatus, ro.getStatus())
                .eq(DeptJoinApply::getDelFlag, "0")
                .orderByDesc(DeptJoinApply::getCreateTime)
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));

        List<DeptJoinApply> records = applyPage.getRecords();

        Map<Long, String> userMap = new HashMap<>();
        Map<Long, SysDept> deptMap = new HashMap<>();
        Map<Long, String> parentMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> applyUserIds = records.stream().map(DeptJoinApply::getApplyUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(applyUserIds)) {
                List<SysUser> applyUsers = userService.listByIds(applyUserIds);
                userMap = applyUsers.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
            }
            List<Long> checkUserIds = records.stream().map(DeptJoinApply::getCheckUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(applyUserIds)) {
                List<SysUser> checkUsers = userService.listByIds(checkUserIds);
                Map<Long, String> checkUserMap = checkUsers.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
                userMap.putAll(checkUserMap);
            }

            List<Long> deptIds = records.stream().map(DeptJoinApply::getDeptId).collect(Collectors.toList());
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
        }

        Map<Long, String> finalUserMap = userMap;
        Map<Long, SysDept> finalDeptMap = deptMap;
        Map<Long, String> finalParentMap = parentMap;
        return applyPage.convert(record -> {
            DeptJoinApplyVO deptJoinApplyVO = BeanUtil.copyProperties(record, DeptJoinApplyVO.class);
            deptJoinApplyVO.setApplyUserName(finalUserMap.get(deptJoinApplyVO.getApplyUserId()));
            deptJoinApplyVO.setCheckUserName(finalUserMap.get(deptJoinApplyVO.getCheckUserId()));

            SysDept dept = finalDeptMap.get(deptJoinApplyVO.getDeptId());
            if (Objects.nonNull(dept)) {
                deptJoinApplyVO.setClassName(dept.getDeptName());
                deptJoinApplyVO.setYearName(finalParentMap.get(dept.getParentId()));
            }
            return deptJoinApplyVO;
        });
    }

    @Override
    public List<DeptJoinApply> listByCondition(DeptJoinApplyPageRO ro) {
        return lambdaQuery()
                .eq(ro.getDeptId() != null, DeptJoinApply::getDeptId, ro.getDeptId())
                .eq(ro.getApplyUserId() != null, DeptJoinApply::getApplyUserId, ro.getApplyUserId())
                .in(CollectionUtils.isNotEmpty(ro.getDeptIds()), DeptJoinApply::getDeptId, ro.getDeptIds())
                .eq(Objects.nonNull(ro.getStatus()), DeptJoinApply::getStatus, ro.getStatus())
                .eq(DeptJoinApply::getDelFlag, "0")
                .list();
    }

    @Override
    @Transactional
    public void check(CheckRO ro) {
        DeptJoinApply joinApply = getById(ro.getBusinessId());
        Assert.notNull(joinApply, "申请单不存在");
        Long userId = joinApply.getApplyUserId();
        SysUser user = userService.getById(userId);
        String username = user.getUserName();
        List<Long> deptIds = user.getDeptIds();

        if (Objects.equals(StatusEnum.CHECK_PASS.getCode(), ro.getStatus())) {
            if (CollectionUtils.isNotEmpty(deptIds) && deptIds.contains(joinApply.getDeptId())) {
                throw new ServiceException("已加入当前班级");
            }

            // 绑定班级和学生
            List<SysStudent> sysStudents = studentService.listByCondition(StudentPageRO.builder().studentName(username).deptId(joinApply.getDeptId()).build());
            if (CollectionUtils.isNotEmpty(sysStudents)) {
                List<Long> studentIds = sysStudents.stream().map(SysStudent::getStudentId).collect(Collectors.toList());
                studentService.bindStudents(studentIds, userId);
            }

            userService.updateUserDeptIds(userId, joinApply.getDeptId());
        }

        lambdaUpdate()
                .eq(DeptJoinApply::getId, ro.getBusinessId())
                .set(DeptJoinApply::getStatus, ro.getStatus())
                .set(DeptJoinApply::getCheckUserId, SecurityUtils.getUserId())
                .set(DeptJoinApply::getRemark, ro.getRemark())
                .update()
        ;


    }

    @Override
    public void applyJoin(DeptJoinRO ro) {
        Long userId = SecurityUtils.getUserId();
        List<Long> deptIds = SecurityUtils.getDeptIds();
        if (CollectionUtils.isNotEmpty(deptIds) && deptIds.contains(ro.getDeptId())) {
            throw new ServiceException("已加入当前班级");
        }

        DeptJoinApply joinApply = BeanUtil.copyProperties(ro, DeptJoinApply.class);
        joinApply.setApplyUserId(userId);
        joinApply.setStatus(0);
        save(joinApply);
    }
}
