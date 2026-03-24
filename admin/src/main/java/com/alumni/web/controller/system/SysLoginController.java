package com.alumni.web.controller.system;

import com.alumni.common.constant.Constants;
import com.alumni.common.core.domain.AjaxResult;
import com.alumni.common.core.domain.entity.SysDept;
import com.alumni.common.core.domain.entity.SysMenu;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.core.domain.model.LoginBody;
import com.alumni.common.core.domain.model.LoginUser;
import com.alumni.common.core.domain.model.MobileLoginBody;
import com.alumni.common.core.domain.model.WechatLoginBody;
import com.alumni.common.core.text.Convert;
import com.alumni.common.utils.DateUtils;
import com.alumni.common.utils.SecurityUtils;
import com.alumni.common.utils.StringUtils;
import com.alumni.framework.web.service.SysLoginService;
import com.alumni.framework.web.service.SysPermissionService;
import com.alumni.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author alumni
 */
@RestController
@Api(value = "登录接口", tags = "登录接口")
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysUserService userService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @ApiOperation(value = "密码登录", notes = "密码登录")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 微信登录
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/wechat/login")
    @ApiOperation(value = "微信登录", notes = "微信登录")
    public AjaxResult wechatLogin(@RequestBody WechatLoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.wechatLogin(loginBody);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 手机号登录
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/mobile/login")
    @ApiOperation(value = "手机号登录", notes = "手机号登录")
    public AjaxResult mobileLogin(@RequestBody MobileLoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.mobileLogin(loginBody);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
//        SysUser user = loginUser.getUser();
        // 获取最新的用户信息
        SysUser user = userService.getById(SecurityUtils.getUserId());
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        // 部门信息
        List<Long> deptIds = user.getDeptIds();
        SysDept dept = null;
        if (!CollectionUtils.isEmpty(deptIds)) {
            List<SysDept> deptList = deptService.listByIds(deptIds);
            if (!CollectionUtils.isEmpty(deptList)) {
                dept = deptList.get(0);
            }
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("dept", dept);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        ajax.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
        ajax.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    @ApiOperation(value = "获取路由信息", notes = "获取路由信息")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    // 检查初始密码是否提醒修改
    public boolean initPasswordIsModify(Date pwdUpdateDate) {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Date pwdUpdateDate) {
        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0) {
            if (StringUtils.isNull(pwdUpdateDate)) {
                // 如果从未修改过初始密码，直接提醒过期
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }
}
