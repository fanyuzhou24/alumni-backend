package com.alumni.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.annotation.DataScope;
import com.alumni.common.constant.UserConstants;
import com.alumni.common.core.domain.entity.SysDept;
import com.alumni.common.core.domain.entity.SysRole;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.enums.DeptTypeEnum;
import com.alumni.common.enums.SearchTypeEnum;
import com.alumni.common.enums.UserTypeEnum;
import com.alumni.common.exception.ServiceException;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.common.utils.StringUtils;
import com.alumni.common.utils.bean.BeanValidators;
import com.alumni.common.utils.spring.SpringUtils;
import com.alumni.common.utils.uuid.CardNoGenerator;
import com.alumni.system.domain.SysPost;
import com.alumni.system.domain.SysUserDept;
import com.alumni.system.domain.SysUserPost;
import com.alumni.system.domain.SysUserRole;
import com.alumni.system.entity.ro.SysUserPageRO;
import com.alumni.system.entity.ro.SysUserRO;
import com.alumni.system.entity.vo.LightMapVO;
import com.alumni.system.entity.vo.SysUserVO;
import com.alumni.system.mapper.*;
import com.alumni.system.service.ISysConfigService;
import com.alumni.system.service.ISysDeptService;
import com.alumni.system.service.ISysUserService;
import com.alumni.system.service.TokenService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author alumni
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    protected Validator validator;

    @Autowired
    private SysUserDeptMapper userDeptMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public SysUser selectUserByOpenId(String openId) {
        return lambdaQuery()
                .eq(SysUser::getOpenId, openId)
                .one();
    }

    @Override
    public SysUser selectUserByMobile(String mobile) {
        return lambdaQuery()
                .eq(SysUser::getPhonenumber, mobile)
                .one();
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user) {
        return save(user);
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        updateById(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUserStatus(user.getUserId(), user.getStatus());
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(Long userId, String avatar) {
        return userMapper.updateUserAvatar(userId, avatar) > 0;
    }

    /**
     * 更新用户登录信息（IP和登录时间）
     *
     * @param userId    用户ID
     * @param loginIp   登录IP地址
     * @param loginDate 登录时间
     * @return 结果
     */
    public void updateLoginInfo(Long userId, String loginIp, Date loginDate) {
        userMapper.updateLoginInfo(userId, loginIp, loginDate);
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.resetUserPwd(user.getUserId(), user.getPassword());
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(Long userId, String password) {
        return userMapper.resetUserPwd(userId, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    deptService.checkDeptDataScope(user.getDeptId());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    deptService.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setDeptId(u.getDeptId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    @Transactional
    public SysUser updateInfo(SysUserRO ro) {
        Long currentUserId = SecurityUtils.getUserId();
        Assert.isTrue(Objects.equals(currentUserId, ro.getUserId()), "非当前用户");

        SysUser user = BeanUtil.copyProperties(ro, SysUser.class);
        updateById(user);

        return getById(user.getUserId());
    }

    @Override
    public IPage<SysUserVO> pageByCondition(SysUserPageRO ro) {
        Page<SysUser> result;
        if (StringUtils.equals(SearchTypeEnum.LOCATION.getCode(), ro.getSearchType())) {
            result = locationPage(ro);
        } else {
            result = commonPage(ro);
        }
        return fillPage(result);
    }

    private Page<SysUser> locationPage(SysUserPageRO ro) {
        Integer radius = ro.getRadius();
        String location = ro.getLocation();
        Assert.isTrue(radius != null, "范围不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(location), "坐标不能为空");

        String[] arr = location.split(",");
        double lat = Double.parseDouble(arr[0]);
        double lng = Double.parseDouble(arr[1]);

        validateCoordinates(lat, lng);

        Page<SysUser> page = new Page<>(ro.getPageNum(), ro.getPageSize());

        return this.baseMapper.pageNearbyUsers(page, lat, lng, radius * 1000);
    }

    private void validateCoordinates(double latitude, double longitude) {
        // 纬度校验
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException(
                    String.format("纬度必须在 -90 到 90 之间，当前值：%.6f", latitude)
            );
        }

        // 经度校验
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException(
                    String.format("经度必须在 -180 到 180 之间，当前值：%.6f", longitude)
            );
        }
    }


    private Page<SysUser> commonPage(SysUserPageRO ro) {
        // 获取deptIds
//        List<Long> deptIds = SecurityUtils.getDeptIds();
        Long deptId = SecurityUtils.getDeptId();

        SysUser user = Optional.ofNullable(SecurityUtils.getLoginUser().getUser()).orElse(new SysUser());
        String searchIndustry = null;
        List<Long> searchDeptIds = Objects.nonNull(ro.getDeptId()) ? Collections.singletonList(ro.getDeptId()) : new ArrayList<>();
        if (StringUtils.isNotEmpty(ro.getSearchType())) {
            ro.setUserType(UserTypeEnum.ALUMNI.getCode());
//            if (CollectionUtils.isEmpty(deptIds)) {
//                return new Page<>();
//            }
//            Long deptId = deptIds.get(0);
            if (Objects.isNull(deptId)) {
//                return new Page<>();
            }
            if (StringUtils.equals(SearchTypeEnum.YEAR.getCode(), ro.getSearchType())) {
//                List<SysDept> deptList = Optional.ofNullable(deptService.listByIds(deptIds)).orElse(new ArrayList<>());
//                List<Long> parentIds = deptList.stream().map(SysDept::getParentId).distinct().collect(Collectors.toList());
//                if (!CollectionUtils.isEmpty(parentIds)) {
//                    List<SysDept> searchDeptList = deptService.lambdaQuery().in(SysDept::getParentId, parentIds).list();
//                    searchDeptIds = searchDeptList.stream().map(SysDept::getDeptId).distinct().collect(Collectors.toList());
//                }
                SysDept sysDept = deptService.getById(deptId);
                if (Objects.nonNull(sysDept)) {
                    List<SysDept> searchDeptList = deptService.lambdaQuery().eq(SysDept::getParentId, sysDept.getParentId()).list();
                    searchDeptIds = searchDeptList.stream().map(SysDept::getDeptId).distinct().collect(Collectors.toList());
                }
            }

            if (StringUtils.equals(SearchTypeEnum.CLASS.getCode(), ro.getSearchType())) {
//                searchDeptIds = deptIds;
                searchDeptIds = Collections.singletonList(deptId);
            }

            if (StringUtils.equals(SearchTypeEnum.INDUSTRY.getCode(), ro.getSearchType())) {
                searchIndustry = user.getIndustry();
            }
        }

        Page<SysUser> result = lambdaQuery()
                .eq(Objects.nonNull(ro.getStartYear()), SysUser::getStartYear, ro.getStartYear())
                .eq(Objects.nonNull(ro.getEndYear()), SysUser::getEndYear, ro.getEndYear())
                .eq(StringUtils.isNotBlank(searchIndustry), SysUser::getIndustry, searchIndustry)
                .eq(StringUtils.isNotBlank(ro.getUserType()), SysUser::getUserType, ro.getUserType())
                .like(StringUtils.isNotBlank(ro.getUserName()), SysUser::getUserName, ro.getUserName())
                .eq(SysUser::getDelFlag, "0")
                .in(!CollectionUtils.isEmpty(searchDeptIds), SysUser::getDeptId, searchDeptIds)
//                .apply(!CollectionUtils.isEmpty(searchDeptIds), buildJsonContainsSql(searchDeptIds))
                .orderByDesc(SysUser::getCreateTime)
                .page(new Page<>(ro.getPageNum(), ro.getPageSize()));
        return result;
    }

    private IPage<SysUserVO> fillPage(Page<SysUser> result) {
        Map<Long, SysDept> deptMap = new HashMap<>();
        Map<Long, String> parentMap = new HashMap<>();
        List<Long> userDeptIds = result.getRecords().stream().map(SysUser::getDeptId).collect(Collectors.toList());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(userDeptIds)) {
            List<SysDept> deptList = deptService.listByIds(userDeptIds);
            deptMap = deptList.stream().collect(Collectors.toMap(SysDept::getDeptId, a -> a));

            // 父部门名称
            List<Long> parentIds = deptList.stream().map(SysDept::getParentId).distinct().collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(parentIds)) {
                List<SysDept> parentDept = Optional.ofNullable(deptService.listByIds(parentIds)).orElse(new ArrayList<>());
                parentMap = parentDept.stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));
            }
        }

        Map<Long, SysDept> finalDeptMap = deptMap;
        Map<Long, String> finalParentMap = parentMap;
        return result.convert(record -> {
            SysUserVO sysUserVO = BeanUtil.copyProperties(record, SysUserVO.class);
            SysDept dept = finalDeptMap.get(sysUserVO.getDeptId());
            if (Objects.nonNull(dept)) {
                sysUserVO.setClassName(dept.getDeptName());
                sysUserVO.setYearName(finalParentMap.get(dept.getParentId()));
            }
            return sysUserVO;
        });
    }

    private String buildJsonContainsSql(List<Long> deptIds) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < deptIds.size(); i++) {
            if (i > 0) sb.append(" OR ");
            sb.append("JSON_CONTAINS(dept_ids, JSON_ARRAY(").append(deptIds.get(i)).append("))");
        }
        sb.append(")");
        return sb.toString();
    }


    @Override
    public void updateUserDeptIds(Long userId, Long newDeptId) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        List<Long> deptIds = user.getDeptIds();
        if (deptIds == null) {
            deptIds = new ArrayList<>();
        }

        // 已存在则不再添加
        if (!deptIds.contains(newDeptId)) {
            deptIds.add(newDeptId);
        }

        // 先检查是否存在关联
        Long count = userDeptMapper.selectCount(
                Wrappers.<SysUserDept>lambdaQuery()
                        .eq(SysUserDept::getUserId, userId)
                        .eq(SysUserDept::getDeptId, newDeptId)
        );

        // 用户部门关联表
        if (count == 0) {
            SysUserDept relation = new SysUserDept();
            relation.setUserId(userId);
            relation.setDeptId(newDeptId);

            userDeptMapper.insert(relation);
        }


        // 更新数据库
        SysUser updateUser = new SysUser();
        updateUser.setUserId(userId);
        updateUser.setDeptIds(deptIds);

        this.updateById(updateUser);



        // 更新缓存用户
        tokenService.refreshUserCache(getById(userId));
    }

    @Override
    public SysUserVO detail(Long userId) {
        SysUser sysUser = getById(userId);
        Assert.notNull(sysUser, "用户不存在");

        SysUserVO sysUserVO = BeanUtil.copyProperties(sysUser, SysUserVO.class);
//        List<Long> deptIds = sysUser.getDeptIds();
//        Long deptId = sysUser.getDeptId();
//        if (!CollectionUtils.isEmpty(deptIds)) {
//            Long deptId = deptIds.get(0);
//            SysDept classDept = deptService.getById(deptId);
//            if (Objects.nonNull(classDept)) {
//                SysDept yearDept = deptService.getById(classDept.getParentId());
//
//                sysUserVO.setClassName(classDept.getDeptName());
//                sysUserVO.setYearName(yearDept.getDeptName());
//            }
//        }

        Long deptId = sysUser.getDeptId();

        SysDept classDept = deptService.getById(deptId);
        if (Objects.nonNull(classDept) && DeptTypeEnum.CLASS.getCode().equals(classDept.getDeptType())) {
            SysDept yearDept = deptService.getById(classDept.getParentId());

            sysUserVO.setClassName(classDept.getDeptName());
            sysUserVO.setYearName(yearDept.getDeptName());
        }
        return sysUserVO;
    }

    @Override
    public void light(String coordinate) {
        Long userId = SecurityUtils.getUserId();
        SysUser sysUser = getById(userId);
        if (Objects.nonNull(sysUser)) {
            lambdaUpdate().set(SysUser::getLightMap, Boolean.TRUE)
                    .set(SysUser::getCoordinate, coordinate)
                    .eq(SysUser::getUserId, userId)
                    .update();
        }
    }

    @Override
    public List<LightMapVO> lightUserList() {
        List<LightMapVO> result = new ArrayList<>();
        List<SysUser> users = lambdaQuery().eq(SysUser::getLightMap, Boolean.TRUE)
                .eq(SysUser::getDelFlag, "0")
                .list();
        if (!CollectionUtils.isEmpty(users)) {
            result = BeanUtil.copyToList(users, LightMapVO.class);
        }
        return result;
    }

    @Override
    public void applyCard() {
        Long userId = SecurityUtils.getUserId();
        SysUser user = getById(userId);
        Assert.isTrue(StringUtils.isEmpty(user.getAlumniCardNo()), "当前用户已经申请校友卡");
        String cardNo = CardNoGenerator.generateCardNo();
        lambdaUpdate().set(SysUser::getAlumniCardNo, cardNo)
                .eq(SysUser::getUserId, userId)
                .update();
    }

}
