package com.alumni.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.core.domain.entity.SysDept;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.enums.StatusEnum;
import com.alumni.common.exception.ServiceException;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.system.domain.SysStudent;
import com.alumni.system.domain.excel.SysStudentExcel;
import com.alumni.system.entity.ro.StudentPageRO;
import com.alumni.system.entity.ro.dept.DeptAdminPageRO;
import com.alumni.system.entity.vo.DeptAdminVO;
import com.alumni.system.entity.vo.SysStudentVO;
import com.alumni.system.mapper.SysStudentMapper;
import com.alumni.system.mapper.SysUserMapper;
import com.alumni.system.service.IDeptAdminService;
import com.alumni.system.service.ISysDeptService;
import com.alumni.system.service.ISysStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生信息表 服务实现类
 * </p>
 *
 * @author lifeng
 * @since 2025-12-19
 */
@Service
public class SysStudentServiceImpl extends ServiceImpl<SysStudentMapper, SysStudent> implements ISysStudentService {
    @Resource
    private SysUserMapper userMapper;

    @Resource
    private IDeptAdminService deptAdminService;

    @Resource
    private ISysDeptService deptService;


    @Override
    public IPage<SysStudentVO> pageByCondition(StudentPageRO ro) {
        Page<SysStudent> page = lambdaQuery()
                .eq(ro.getDeptId() != null, SysStudent::getDeptId, ro.getDeptId())
                .eq(StringUtils.isNotBlank(ro.getStudentName()), SysStudent::getStudentName, ro.getStudentName())
                .like(StringUtils.isNotBlank(ro.getStudentNameStr()), SysStudent::getStudentName, ro.getStudentNameStr())
                .eq(SysStudent::getDelFlag, "0")
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));

        return page.convert(record -> BeanUtil.copyProperties(record, SysStudentVO.class));
    }

    @Override
    public List<SysStudent> listByCondition(StudentPageRO ro) {
        return lambdaQuery()
                .eq(ro.getDeptId() != null, SysStudent::getDeptId, ro.getDeptId())
                .eq(StringUtils.isNotBlank(ro.getStudentName()), SysStudent::getStudentName, ro.getStudentName())
                .like(StringUtils.isNotBlank(ro.getStudentNameStr()), SysStudent::getStudentName, ro.getStudentNameStr())
                .eq(SysStudent::getDelFlag, "0")
                .list();
    }

    @Override
    public List<SysStudentVO> listByConditionV2(StudentPageRO ro) {

        // 查学生记录
        List<SysStudent> students = lambdaQuery()
                .eq(ro.getDeptId() != null, SysStudent::getDeptId, ro.getDeptId())
                .eq(StringUtils.isNotBlank(ro.getStudentName()), SysStudent::getStudentName, ro.getStudentName())
                .like(StringUtils.isNotBlank(ro.getStudentNameStr()), SysStudent::getStudentName, ro.getStudentNameStr())
                .eq(SysStudent::getDelFlag, "0")
                .list();

        List<SysStudentVO> resultList = BeanUtil.copyToList(students, SysStudentVO.class);

        // 已经绑定的 userId
        Set<Long> studentBoundUserIds = students.stream()
                .map(SysStudent::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (Objects.nonNull(ro.getDeptId())) {
            // 查班级所有用户
            List<SysUser> deptUsers = userMapper.selectUserListByDeptId(ro.getDeptId());

            if (CollectionUtils.isNotEmpty(deptUsers)) {
                // 构建 userId -> SysUser map 方便取数据
                Map<Long, SysUser> userMap = deptUsers.stream()
                        .collect(Collectors.toMap(SysUser::getUserId, u -> u));

                // 给学生绑定的用户填充用户信息
                resultList.forEach(vo -> {
                    Long userId = vo.getUserId();
                    if (userId != null && userMap.containsKey(userId)) {
                        SysUser user = userMap.get(userId);
                        vo.setAvatar(user.getAvatar());
                    }
                });

                // 添加未绑定学生记录的用户 → 虚拟学生
                deptUsers.stream()
                        .filter(u -> !studentBoundUserIds.contains(u.getUserId()))
                        .forEach(u -> {
                            SysStudentVO vo = new SysStudentVO();
                            vo.setUserId(u.getUserId());
                            vo.setAvatar(u.getAvatar());
                            vo.setStudentId(null);
                            vo.setUserId(u.getUserId());
                            vo.setStudentName(u.getUserName());
                            vo.setDeptId(ro.getDeptId());
                            resultList.add(vo);
                        });

                // 查询管理员
                List<DeptAdminVO> deptAdmins = Optional.ofNullable(deptAdminService.listByCondition(DeptAdminPageRO.builder()
                        .deptId(ro.getDeptId())
                        .status(StatusEnum.CHECK_PASS.getCode())
                        .build()))
                        .orElse(new ArrayList<>());
                List<Long> adminUserIds = deptAdmins.stream().map(DeptAdminVO::getUserId).collect(Collectors.toList());
                for (SysStudentVO sysStudentVO : resultList) {
                    if (Objects.nonNull(sysStudentVO.getUserId())) {
                        sysStudentVO.setIsAdmin(adminUserIds.contains(sysStudentVO.getUserId()));
                    }
                }
            }
        }

        Long currentUserId = SecurityUtils.getUserId();

        resultList.sort((a, b) -> {
            // 当前用户永远最前（且唯一）
            if (Objects.equals(a.getUserId(), currentUserId)) return -1;
            if (Objects.equals(b.getUserId(), currentUserId)) return 1;

            // 已注册学生优先
            boolean aRegistered = a.getUserId() != null;
            boolean bRegistered = b.getUserId() != null;
            if (aRegistered != bRegistered) {
                return aRegistered ? -1 : 1;
            }

            // 同类型按姓名（中文）升序
            return Collator.getInstance(Locale.CHINA)
                    .compare(StringUtils.defaultString(a.getStudentName()),
                            StringUtils.defaultString(b.getStudentName()));
        });

        return resultList;
    }


    @Override
    public SysStudent getOne(Long studentId) {
        return lambdaQuery()
                .eq(SysStudent::getStudentId, studentId)
                .eq(SysStudent::getDelFlag, "0")
                .one();
    }

    @Override
    public void insert(SysStudent sysStudent) {
        save(sysStudent);
    }

    @Override
    public void update(SysStudent sysStudent) {
        updateById(sysStudent);
    }

    @Override
    public void remove(Long studentId) {
        lambdaUpdate()
                .set(SysStudent::getDelFlag, "2")
                .eq(SysStudent::getStudentId, studentId)
                .update()
        ;
    }

    @Override
    @Transactional
    public void batchUpdate(List<SysStudent> studentList) {
        if (studentList == null || studentList.isEmpty()) {
            return;
        }

        // 按班级分组
        Map<Long, List<SysStudent>> deptGroup = studentList.stream()
                .collect(Collectors.groupingBy(SysStudent::getDeptId));

        for (Map.Entry<Long, List<SysStudent>> entry : deptGroup.entrySet()) {
            Long deptId = entry.getKey();
            List<SysStudent> students = entry.getValue();

            // 查询该班级现有学生（未删除的）
            List<SysStudent> existStudents = lambdaQuery()
                    .eq(SysStudent::getDeptId, deptId)
                    .eq(SysStudent::getDelFlag, "0")
                    .list();

            Set<Long> incomingIds = students.stream()
                    .filter(s -> s.getStudentId() != null)
                    .map(SysStudent::getStudentId)
                    .collect(Collectors.toSet());

            List<SysStudent> deleteList = new ArrayList<>();

            // 删除：现有学生不在 incomingIds 列表中的
            for (SysStudent exist : existStudents) {
                if (!incomingIds.contains(exist.getStudentId())) {
                    exist.setDelFlag("2"); // 标记删除
                    deleteList.add(exist);
                }
            }

            // 批量操作
            if (!students.isEmpty()) {
                saveOrUpdateBatch(students, 100);
            }

            if (!deleteList.isEmpty()) {
                updateBatchById(deleteList, 100);
            }
        }
    }

    @Override
    public void bindStudents(List<Long> studentIds, Long userId) {
        if (CollectionUtils.isEmpty(studentIds)) {
            return;
        }
        lambdaUpdate()
                .set(SysStudent::getUserId, userId)
                .in(SysStudent::getStudentId, studentIds)
                .update()
        ;
    }

    @Override
    @Transactional
    public String importStudent(List<SysStudentExcel> studentExcels) {
        if (CollectionUtils.isEmpty(studentExcels)) {
            throw new ServiceException("导入学生数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        // 获取届和班级
        List<String> yearNameList = studentExcels.stream().map(SysStudentExcel::getYearName).collect(Collectors.toList());
        List<SysDept> yearDeptList = deptService.selectDeptByYearNames(yearNameList);
        Map<String, SysDept> yearDeptMap = yearDeptList.stream().collect(Collectors.toMap(SysDept::getDeptName, a -> a));

        List<SysStudent> sysStudents = new ArrayList<>();
        for (int i = 0; i < studentExcels.size(); i++) {
            try {
                SysStudentExcel studentExcel = studentExcels.get(i);
                SysStudent sysStudent = BeanUtil.copyProperties(studentExcel, SysStudent.class);

                // 查询届是否存在
                String yearName = studentExcel.getYearName();
                SysDept yearDept = yearDeptMap.get(yearName);
                if (Objects.isNull(yearDept) || CollectionUtils.isEmpty(yearDept.getChildren())) {
                    throw new ServiceException("届不存在");
                }

                List<SysDept> classList = yearDept.getChildren().stream().filter(child -> StringUtils.equals(child.getDeptName(), studentExcel.getClassName())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(classList)) {
                    throw new ServiceException("班级不存在");
                }
                SysDept dept = classList.get(0);
                sysStudent.setDeptId(dept.getDeptId());
                sysStudents.add(sysStudent);
            } catch (Exception e) {
                failureNum++;
                int index = i + 2;
                String msg = "<br/> 第" + index + "行，校验失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }

        Map<Long, List<SysStudent>> classStudentMap = sysStudents.stream().collect(Collectors.groupingBy(SysStudent::getDeptId));
        Set<Long> deptIds = classStudentMap.keySet();
        if (CollectionUtils.isNotEmpty(deptIds)) {
            lambdaUpdate().in(SysStudent::getDeptId, deptIds).set(SysStudent::getDelFlag, 2).update();
        }
        for (Map.Entry<Long, List<SysStudent>> entry : classStudentMap.entrySet()) {
            List<SysStudent> students = entry.getValue();
            saveBatch(students);
            successNum += students.size();
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }


}
