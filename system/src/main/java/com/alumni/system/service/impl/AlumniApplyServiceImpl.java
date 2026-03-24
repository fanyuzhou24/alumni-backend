package com.alumni.system.service.impl;

import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.enums.StatusEnum;
import com.alumni.common.enums.UserTypeEnum;
import com.alumni.common.exception.ServiceException;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.common.utils.bean.BeanUtils;
import com.alumni.system.domain.AlumniApply;
import com.alumni.system.domain.SysStudent;
import com.alumni.system.entity.dto.AlumniApplyDTO;
import com.alumni.system.entity.ro.AlumniApplyPageRO;
import com.alumni.system.entity.ro.StudentPageRO;
import com.alumni.system.mapper.AlumniApplyMapper;
import com.alumni.system.mapper.SysUserDeptMapper;
import com.alumni.system.service.IAlumniApplyService;
import com.alumni.system.service.ISysStudentService;
import com.alumni.system.service.ISysUserService;
import com.alumni.system.service.TokenService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 校友申请表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-22
 */
@Service
public class AlumniApplyServiceImpl extends ServiceImpl<AlumniApplyMapper, AlumniApply> implements IAlumniApplyService {
    @Resource
    private ISysUserService userService;

    @Resource
    private ISysStudentService studentService;

    @Resource
    private TokenService tokenService;

    @Resource
    private SysUserDeptMapper userDeptMapper;

    @Override
    public Page<AlumniApply> pageByCondition(AlumniApplyPageRO ro) {
        return lambdaQuery()
                .eq(ro.getDeptId() != null, AlumniApply::getDeptId, ro.getDeptId())
                .eq(Objects.nonNull(ro.getStatus()), AlumniApply::getStatus, ro.getStatus())
                .eq(AlumniApply::getDelFlag, "0")
                .orderByDesc(AlumniApply::getCreateTime)
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));
    }

    @Override
    public List<AlumniApply> listByCondition(AlumniApplyPageRO ro) {
        return lambdaQuery()
                .eq(ro.getDeptId() != null, AlumniApply::getDeptId, ro.getDeptId())
                .eq(Objects.nonNull(ro.getStatus()), AlumniApply::getStatus, ro.getStatus())
                .eq(AlumniApply::getDelFlag, "0")
                .list();
    }

    @Override
    public AlumniApply getOne(Long id) {
        return lambdaQuery()
                .eq(AlumniApply::getId, id)
                .eq(AlumniApply::getDelFlag, "0")
                .one();
    }

    @Override
    public void insert(AlumniApply alumniApply) {
        Long userId = SecurityUtils.getUserId();
        alumniApply.setUserId(userId);
        AlumniApply exist = lambdaQuery().eq(AlumniApply::getUserId, userId).ne(AlumniApply::getStatus, StatusEnum.CHECK_REFUSED.getCode()).one();
        if (Objects.nonNull(exist)) {
            if (StatusEnum.CHECKING.getCode().equals(exist.getStatus())) {
                throw new ServiceException("已经发起申请");
            }
            if (StatusEnum.CHECK_PASS.getCode().equals(exist.getStatus())) {
                throw new ServiceException("已经申请通过");
            }
        }
        save(alumniApply);
    }

    @Override
    public void update(AlumniApply alumniApply) {
        updateById(alumniApply);
    }

    @Override
    public void delete(Long id) {
        lambdaUpdate()
                .set(AlumniApply::getDelFlag, "2")
                .eq(AlumniApply::getId, id)
                .update();
    }

    @Override
    @Transactional
    public void check(AlumniApplyDTO alumniApplyDTO) {
        AlumniApply exist = getOne(alumniApplyDTO.getId());
        Assert.notNull(exist, "申请单不存在！");

        AlumniApply update = new AlumniApply();
        BeanUtils.copyProperties(alumniApplyDTO, update);
        update(update);

        if (Objects.equals(alumniApplyDTO.getStatus(), 1)) {
            // 查询学生
            List<SysStudent> sysStudents = studentService.listByCondition(StudentPageRO.builder().deptId(exist.getDeptId()).studentName(exist.getUserName()).build());
            if (CollectionUtils.isNotEmpty(sysStudents)) {
                SysStudent sysStudent = sysStudents.get(0);
                alumniApplyDTO.setStudentIds(Collections.singletonList(sysStudent.getStudentId()));
            }

            studentService.bindStudents(alumniApplyDTO.getStudentIds(), alumniApplyDTO.getUserId());

            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(exist, sysUser);
            sysUser.setUserType(UserTypeEnum.ALUMNI.getCode());
            if (Objects.nonNull(exist.getDeptId())) {
                sysUser.setDeptIds(Collections.singletonList(exist.getDeptId()));
            }
            userService.updateUser(sysUser);
            userService.updateUserDeptIds(sysUser.getUserId(), sysUser.getDeptId());

            // 更新缓存用户
            tokenService.refreshUserCache(userService.getById(sysUser.getUserId()));
        }
    }

    @Override
    public Integer getUserStatus() {
        Integer status = StatusEnum.NOT_APPLY.getCode();
        Long userId = SecurityUtils.getUserId();
        AlumniApply alumniApply = lambdaQuery().eq(AlumniApply::getUserId, userId).ne(AlumniApply::getStatus, StatusEnum.CHECK_REFUSED.getCode()).one();
        if (Objects.nonNull(alumniApply)) {
            status = alumniApply.getStatus();
        }
        return status;
    }
}
