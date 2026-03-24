package com.alumni.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.core.domain.entity.SysDept;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.enums.DeptTypeEnum;
import com.alumni.common.enums.StatusEnum;
import com.alumni.common.exception.ServiceException;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.common.utils.StringUtils;
import com.alumni.common.utils.uuid.IdUtils;
import com.alumni.system.domain.DeptAlbum;
import com.alumni.system.domain.DeptCreateApply;
import com.alumni.system.domain.DeptJoinApply;
import com.alumni.system.entity.ro.CheckRO;
import com.alumni.system.entity.ro.dept.DeptCreateApplyPageRO;
import com.alumni.system.entity.ro.dept.DeptCreateRO;
import com.alumni.system.entity.vo.DeptCreateApplyVO;
import com.alumni.system.entity.vo.DeptJoinApplyVO;
import com.alumni.system.mapper.DeptCreateApplyMapper;
import com.alumni.system.service.IDeptCreateApplyService;
import com.alumni.system.service.ISysDeptService;
import com.alumni.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 创建班级申请表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-24
 */
@Service
public class DeptCreateApplyServiceImpl extends ServiceImpl<DeptCreateApplyMapper, DeptCreateApply> implements IDeptCreateApplyService {
    @Resource
    private ISysDeptService deptService;

    @Resource
    private ISysUserService userService;


    @Override
    public IPage<DeptCreateApplyVO> pageByCondition(DeptCreateApplyPageRO ro) {
        Page<DeptCreateApply> applyPage = lambdaQuery()
                .eq(ro.getApplyUserId() != null, DeptCreateApply::getApplyUserId, ro.getApplyUserId())
                .eq(Objects.nonNull(ro.getStatus()), DeptCreateApply::getStatus, ro.getStatus())
                .eq(DeptCreateApply::getDelFlag, "0")
                .orderByDesc(DeptCreateApply::getCreateTime)
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));

        List<DeptCreateApply> records = applyPage.getRecords();

        Map<Long, String> userMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> applyUserIds = records.stream().map(DeptCreateApply::getApplyUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(applyUserIds)) {
                List<SysUser> applyUsers = userService.listByIds(applyUserIds);
                userMap = applyUsers.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
            }
            List<Long> checkUserIds = records.stream().map(DeptCreateApply::getCheckUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(applyUserIds)) {
                List<SysUser> checkUsers = userService.listByIds(checkUserIds);
                Map<Long, String> checkUserMap = checkUsers.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
                userMap.putAll(checkUserMap);
            }
        }

        Map<Long, String> finalUserMap = userMap;
        return applyPage.convert(record -> {
            DeptCreateApplyVO applyVO = BeanUtil.copyProperties(record, DeptCreateApplyVO.class);
            applyVO.setApplyUserName(finalUserMap.get(applyVO.getApplyUserId()));
            applyVO.setCheckUserName(finalUserMap.get(applyVO.getCheckUserId()));
            return applyVO;
        });
    }

    @Override
    public List<DeptCreateApply> listByCondition(DeptCreateApplyPageRO ro) {
        return lambdaQuery()
                .eq(ro.getApplyUserId() != null, DeptCreateApply::getApplyUserId, ro.getApplyUserId())
                .eq(Objects.nonNull(ro.getStatus()), DeptCreateApply::getStatus, ro.getStatus())
                .eq(DeptCreateApply::getDelFlag, "0")
                .list();
    }

    @Override
    @Transactional
    public void check(CheckRO ro) {
        SysDept dept = new SysDept();

        DeptCreateApply createApply = getById(ro.getBusinessId());
        // 查询届是否存在
        SysDept yearDept;
        if (Objects.equals(StatusEnum.CHECK_PASS.getCode(), ro.getStatus())) {
            List<SysDept> yearDeptList = deptService.selectDeptByNameAndType(createApply.getYearName(), DeptTypeEnum.YEAR.getCode(), null);
            if (CollectionUtils.isNotEmpty(yearDeptList)) {
                yearDept = yearDeptList.get(0);
                // 查询班级是否存在
                List<SysDept> classDeptList = deptService.selectDeptByNameAndType(createApply.getClassName(), DeptTypeEnum.CLASS.getCode(), yearDept.getDeptId());
                if (CollectionUtils.isNotEmpty(classDeptList)) {
                    throw new ServiceException("班级已存在");
                }
            } else {
                // 创建届
                yearDept = new SysDept();
                yearDept.setDeptId(IdUtils.nextIdLong());
                yearDept.setParentId(0L);
                yearDept.setAncestors(StringUtils.join(Arrays.asList(yearDept.getParentId(), yearDept.getDeptId()), ","));
                yearDept.setDeptName(createApply.getYearName());
                yearDept.setDeptType(DeptTypeEnum.YEAR.getCode());
                yearDept.setStatus("0");
                deptService.save(yearDept);
            }

            dept.setDeptId(IdUtils.nextIdLong());
            dept.setParentId(yearDept.getDeptId());
            dept.setAncestors(StringUtils.join(Arrays.asList(yearDept.getParentId(), yearDept.getDeptId(), dept.getDeptId()), ","));
            dept.setDeptName(createApply.getClassName());
            dept.setDeptType(DeptTypeEnum.YEAR.getCode());
            dept.setDeptLogo(createApply.getDeptLogo());
            dept.setDeptCover(createApply.getDeptCover());
            dept.setStatus("0");
            deptService.save(dept);
        }
        // 更新审核表
        lambdaUpdate()
                .eq(DeptCreateApply::getId, ro.getBusinessId())
                .set(DeptCreateApply::getStatus, ro.getStatus())
                .set(DeptCreateApply::getCheckUserId, SecurityUtils.getUserId())
                .set(DeptCreateApply::getRemark, ro.getRemark())
                .update()
        ;
    }

    @Override
    public void applyCreate(DeptCreateRO ro) {
        DeptCreateApply createApply = BeanUtil.copyProperties(ro, DeptCreateApply.class);
        // 查询届是否存在
        List<SysDept> yearDeptList = deptService.selectDeptByNameAndType(ro.getYearName(), DeptTypeEnum.YEAR.getCode(), null);
        if (CollectionUtils.isNotEmpty(yearDeptList)) {
            SysDept yearDept = yearDeptList.get(0);
            // 查询班级是否存在
            List<SysDept> classDeptList = deptService.selectDeptByNameAndType(ro.getClassName(), DeptTypeEnum.CLASS.getCode(), yearDept.getDeptId());
            if (CollectionUtils.isNotEmpty(classDeptList)) {
                throw new ServiceException("班级已存在");
            }
        }
        createApply.setApplyUserId(SecurityUtils.getUserId());
        createApply.setStatus(0);
        save(createApply);
    }
}
