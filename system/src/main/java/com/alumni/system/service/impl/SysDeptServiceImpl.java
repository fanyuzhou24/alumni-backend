package com.alumni.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.annotation.DataScope;
import com.alumni.common.constant.UserConstants;
import com.alumni.common.core.domain.TreeSelect;
import com.alumni.common.core.domain.entity.SysDept;
import com.alumni.common.core.domain.entity.SysRole;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.core.text.Convert;
import com.alumni.common.enums.DeptTypeEnum;
import com.alumni.common.enums.JoinStatusEnum;
import com.alumni.common.enums.StatusEnum;
import com.alumni.common.exception.ServiceException;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.common.utils.StringUtils;
import com.alumni.common.utils.spring.SpringUtils;
import com.alumni.system.domain.DeptJoinApply;
import com.alumni.system.domain.SysStudent;
import com.alumni.system.entity.dto.UserDeptCountDTO;
import com.alumni.system.entity.ro.StudentPageRO;
import com.alumni.system.entity.ro.SysDeptPageRO;
import com.alumni.system.entity.ro.dept.DeptAdminPageRO;
import com.alumni.system.entity.ro.dept.DeptJoinApplyPageRO;
import com.alumni.system.entity.vo.DeptAdminVO;
import com.alumni.system.entity.vo.SysDeptVO;
import com.alumni.system.mapper.SysDeptMapper;
import com.alumni.system.mapper.SysRoleMapper;
import com.alumni.system.mapper.SysUserDeptMapper;
import com.alumni.system.service.IDeptAdminService;
import com.alumni.system.service.IDeptJoinApplyService;
import com.alumni.system.service.ISysDeptService;
import com.alumni.system.service.ISysStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 * 
 * @author alumni
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService
{
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private IDeptJoinApplyService joinApplyService;

    @Autowired
    private IDeptAdminService deptAdminService;

    @Resource
    private SysUserDeptMapper userDeptMapper;

    @Resource
    private ISysStudentService studentService;

    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept)
    {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 查询部门树结构信息
     * 
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDept dept)
    {
        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts)
    {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        for (SysDept dept : depts)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts)
    {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     * 
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId)
    {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId)
    {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId)
    {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId)
    {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId)
    {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept)
    {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     * 
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()) && StringUtils.isNotNull(deptId))
        {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts))
            {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept)
    {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept)
    {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     * 
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept)
    {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     * 
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors)
    {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children)
        {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId)
    {
        return deptMapper.deleteDeptById(deptId);
    }

    @Override
    public List<TreeSelect> listClass() {
        List<SysDept> sysDeptList = lambdaQuery().ne(SysDept::getParentId, 0)
                .eq(SysDept::getDelFlag, "0")
                .list();
        return buildDeptTreeSelect(sysDeptList);
    }

    @Override
    public IPage<SysDeptVO> pageByCondition(SysDeptPageRO ro) {
        if (StringUtils.isNotEmpty(ro.getParentName())) {
            List<SysDept> parentList = lambdaQuery().eq(SysDept::getDelFlag, "0")
                    .eq(SysDept::getDeptName, ro.getParentName() + "届")
                    .list();
            if (CollectionUtils.isNotEmpty(parentList)) {
                SysDept parentDept = parentList.get(0);
                ro.setParentId(parentDept.getDeptId());
            } else {
                return new Page<>();
            }
        }

        Page<SysDept> deptPage = lambdaQuery().eq(SysDept::getDelFlag, "0")
                .eq(Objects.nonNull(ro.getDeptId()), SysDept::getDeptId, ro.getDeptId())
                .eq(Objects.nonNull(ro.getParentId()), SysDept::getParentId, ro.getParentId())
                .like(StringUtils.isNotEmpty(ro.getDeptName()), SysDept::getDeptName, ro.getDeptName())
                .eq(StringUtils.isNotEmpty(ro.getDeptType()), SysDept::getDeptType, ro.getDeptType())
                .orderByAsc( SysDept::getParentId, SysDept::getOrderNum)
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));

        if (CollectionUtils.isEmpty(deptPage.getRecords())) {
            return new Page<>();
        }


        // 父部门名称
        Map<Long, String> parentMap = new HashMap<>();
        List<Long> parentIds = deptPage.getRecords().stream().map(SysDept::getParentId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(parentIds)) {
            List<SysDept> parentDept = Optional.ofNullable(listByIds(parentIds)).orElse(new ArrayList<>());
            parentMap = parentDept.stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));
        }

        // 获取用户申请信息
        List<Long> deptIds = Optional.ofNullable(SecurityUtils.getDeptIds()).orElse(new ArrayList<>());

        List<Long> searchDeptIds = deptPage.getRecords().stream().map(SysDept::getDeptId).collect(Collectors.toList());
        List<DeptJoinApply> deptJoinApplies = joinApplyService.listByCondition(DeptJoinApplyPageRO.builder().applyUserId(SecurityUtils.getUserId()).deptIds(searchDeptIds).status(StatusEnum.CHECKING.getCode()).build());
        List<Long> deptJoinIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deptJoinApplies)) {
            deptJoinIds = deptJoinApplies.stream().map(DeptJoinApply::getDeptId).collect(Collectors.toList());
        }

        Map<Long, String> adminMap = new HashMap<>();
        List<DeptAdminVO> deptAdmins = deptAdminService.listByCondition(DeptAdminPageRO.builder()
                .deptIds(searchDeptIds)
                .status(StatusEnum.CHECK_PASS.getCode())
                .build());
        if (CollectionUtils.isNotEmpty(deptAdmins)) {
            adminMap = deptAdmins.stream()
                    .collect(Collectors.groupingBy(
                            DeptAdminVO::getDeptId,
                            Collectors.mapping(
                                    vo -> vo.getUserName(),
                                    Collectors.joining(",")
                            )
                    ));
        }

        // 获取班级人数
        List<UserDeptCountDTO> userDeptCountDTOS = Optional.of(userDeptMapper.countUserByDeptIds(searchDeptIds)).orElse(new ArrayList<>());
        Map<Long, Long> userCountMap = userDeptCountDTOS.stream().
                collect(Collectors.toMap(UserDeptCountDTO::getDeptId, UserDeptCountDTO::getCountNum));

        // 获取班级学生
        Map<Long, List<SysStudent>> studentMap = new HashMap<>();
        List<SysStudent> sysStudents = studentService.listByCondition(StudentPageRO.builder().deptIds(searchDeptIds).build());
        if (CollectionUtils.isNotEmpty(sysStudents)) {
            studentMap = sysStudents.stream()
                    .collect(Collectors.groupingBy(
                            SysStudent::getDeptId));
        }


        List<Long> finalDeptJoinIds = deptJoinIds;
        Map<Long, String> finalAdminMap = adminMap;
        Map<Long, String> finalParentMap = parentMap;
        Map<Long, List<SysStudent>> finalStudentMap = studentMap;
        return deptPage.convert(dept -> {
            SysDeptVO sysDeptVO = BeanUtil.copyProperties(dept, SysDeptVO.class);
            sysDeptVO.setJoinStatus(JoinStatusEnum.NOT_JOIN.getCode());
            sysDeptVO.setParentName(finalParentMap.get(sysDeptVO.getParentId()));
            sysDeptVO.setCountNum(userCountMap.get(sysDeptVO.getDeptId()));
            sysDeptVO.setStudentList(finalStudentMap.get(sysDeptVO.getDeptId()));

            if (deptIds.contains(sysDeptVO.getDeptId())) {
                sysDeptVO.setJoinStatus(JoinStatusEnum.JOINED.getCode());
                sysDeptVO.setDeptAdminNameList(finalAdminMap.get(sysDeptVO.getDeptId()));
            }
            if (finalDeptJoinIds.contains(sysDeptVO.getDeptId())){
                sysDeptVO.setJoinStatus(JoinStatusEnum.APPLY_JOIN.getCode());
            }


            return sysDeptVO;
        });
    }

    @Override
    public List<SysDeptVO> listByCondition(SysDeptPageRO ro) {
        List<SysDept> deptList = lambdaQuery().eq(SysDept::getDelFlag, "0")
                .eq(Objects.nonNull(ro.getDeptId()), SysDept::getDeptId, ro.getDeptId())
                .eq(Objects.nonNull(ro.getParentId()), SysDept::getParentId, ro.getParentId())
                .like(StringUtils.isNotEmpty(ro.getDeptName()), SysDept::getDeptName, ro.getDeptName())
                .eq(StringUtils.isNotEmpty(ro.getDeptType()), SysDept::getDeptType, ro.getDeptType())
                .orderByAsc( SysDept::getParentId, SysDept::getOrderNum)
                .list();

        // 获取用户申请信息
        Long userId = SecurityUtils.getUserId();
        List<Long> deptIds = Optional.ofNullable(SecurityUtils.getDeptIds()).orElse(new ArrayList<>());

        List<Long> searchDeptIds = deptList.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        List<DeptJoinApply> deptJoinApplies = joinApplyService.listByCondition(DeptJoinApplyPageRO.builder().applyUserId(userId).deptIds(searchDeptIds).status(StatusEnum.CHECKING.getCode()).build());
        List<Long> deptJoinIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deptJoinApplies)) {
            deptJoinIds = deptJoinApplies.stream().map(DeptJoinApply::getDeptId).collect(Collectors.toList());
        }

        Map<Long, List<DeptAdminVO>> adminMap = new HashMap<>();
        List<DeptAdminVO> deptAdmins = deptAdminService.listByCondition(DeptAdminPageRO.builder()
                .deptIds(searchDeptIds)
                .status(StatusEnum.CHECK_PASS.getCode())
                .build());
        if (CollectionUtils.isNotEmpty(deptAdmins)) {
            adminMap = deptAdmins.stream()
                    .collect(Collectors.groupingBy(DeptAdminVO::getDeptId));
        }

        List<Long> finalDeptJoinIds = deptJoinIds;
        Map<Long, List<DeptAdminVO>> finalAdminMap = adminMap;

        List<SysDeptVO> result = new ArrayList<>();
        deptList.forEach(dept -> {
            SysDeptVO sysDeptVO = BeanUtil.copyProperties(dept, SysDeptVO.class);
            sysDeptVO.setJoinStatus(JoinStatusEnum.NOT_JOIN.getCode());

            if (deptIds.contains(sysDeptVO.getDeptId())) {
                sysDeptVO.setJoinStatus(JoinStatusEnum.JOINED.getCode());
                List<DeptAdminVO> deptAdminVOS = finalAdminMap.get(sysDeptVO.getDeptId());
                if (CollectionUtils.isNotEmpty(deptAdminVOS)) {
                    String deptNames = deptAdminVOS.stream().map(DeptAdminVO::getUserName).collect(Collectors.joining(","));
                    sysDeptVO.setDeptAdminNameList(deptNames);
                    List<Long> adminIds = deptAdminVOS.stream().map(DeptAdminVO::getUserId).collect(Collectors.toList());
                    sysDeptVO.setIsAdmin(adminIds.contains(userId));
                }
            }
            if (finalDeptJoinIds.contains(sysDeptVO.getDeptId())){
                sysDeptVO.setJoinStatus(JoinStatusEnum.APPLY_JOIN.getCode());
            }
            result.add(sysDeptVO);
        });
        return result;
    }

    @Override
    public List<SysDept> selectDeptByNameAndType(String deptName, String type, Long parentId) {
        if (StringUtils.isEmpty(deptName) || StringUtils.isEmpty(type)) {
            return new ArrayList<>();
        }
        return lambdaQuery()
                .eq(SysDept::getDeptName, deptName)
                .eq(SysDept::getDeptType, type)
                .eq(Objects.nonNull(parentId), SysDept::getParentId, parentId)
                .list();
    }

    @Override
    public List<SysDept> selectDeptByYearNames(List<String> deptNames) {
        if (CollectionUtils.isEmpty(deptNames)) {
            return new ArrayList<>();
        }
        List<SysDept> yearDeptList = lambdaQuery()
                .in(SysDept::getDeptName, deptNames)
                .list();

        if (CollectionUtils.isNotEmpty(yearDeptList)) {
            // 查询班级
            List<Long> yearIds = yearDeptList.stream().map(SysDept::getDeptId).collect(Collectors.toList());
            List<SysDept> classDeptList = lambdaQuery()
                    .in(SysDept::getParentId, yearIds)
                    .list();

            Map<Long, List<SysDept>> classDeptMap = classDeptList.stream().collect(Collectors.groupingBy(SysDept::getParentId));
            for (SysDept yearDept : yearDeptList) {
                List<SysDept> children = classDeptMap.get(yearDept.getDeptId());
                yearDept.setChildren(children);
            }
        }

        return yearDeptList;
    }

    @Override
    public SysDeptVO detail(Long deptId) {

        Long userId = SecurityUtils.getUserId();
        List<Long> deptIds = SecurityUtils.getDeptIds();

        SysDept dept = getById(deptId);
        Assert.notNull(dept, "班级不存在");

        SysDeptVO sysDeptVO = BeanUtil.copyProperties(dept, SysDeptVO.class);

        if (StringUtils.equals(dept.getDeptType(), DeptTypeEnum.CLASS.getCode())) {
            Long parentId = dept.getParentId();
            SysDept parent = getById(parentId);
            Assert.notNull(parent, "父级不存在");
            sysDeptVO.setParentName(parent.getDeptName());
            sysDeptVO.setJoinStatus(JoinStatusEnum.NOT_JOIN.getCode());
            List<DeptJoinApply> deptJoinApplies = joinApplyService.listByCondition(DeptJoinApplyPageRO.builder().applyUserId(SecurityUtils.getUserId()).deptId(deptId).status(StatusEnum.CHECKING.getCode()).build());
            if (CollectionUtils.isNotEmpty(deptJoinApplies)) {
                sysDeptVO.setJoinStatus(JoinStatusEnum.APPLY_JOIN.getCode());
            }

            if (CollectionUtils.isNotEmpty(deptIds) && deptIds.contains(deptId)) {
                sysDeptVO.setJoinStatus(JoinStatusEnum.JOINED.getCode());
            }

            List<DeptAdminVO> deptAdmins = deptAdminService.listByCondition(DeptAdminPageRO.builder()
                    .deptId(deptId)
                    .status(StatusEnum.CHECK_PASS.getCode())
                    .build());
            if (CollectionUtils.isNotEmpty(deptAdmins)) {
                String deptNames = deptAdmins.stream().map(DeptAdminVO::getUserName).collect(Collectors.joining(","));
                sysDeptVO.setDeptAdminNameList(deptNames);
                List<Long> adminIds = deptAdmins.stream().map(DeptAdminVO::getUserId).collect(Collectors.toList());
                sysDeptVO.setIsAdmin(adminIds.contains(userId));
            }
        }
        return sysDeptVO;
    }

    @Override
    public List<SysDeptVO> myList() {
        // 获取用户申请信息
        Long userId = SecurityUtils.getUserId();
        return this.baseMapper.myList(userId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t)
    {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t)
    {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext())
        {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t)
    {
        return getChildList(list, t).size() > 0;
    }
}
