package com.alumni.framework.security.provider;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.core.domain.model.LoginUser;
import com.alumni.common.core.domain.model.MobileLoginBody;
import com.alumni.common.enums.UserTypeEnum;
import com.alumni.framework.security.token.MobileAuthenticationToken;
import com.alumni.framework.web.service.SysPermissionService;
import com.alumni.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MobileAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public Authentication authenticate(Authentication authentication) {

        MobileLoginBody mobileLoginBody = (MobileLoginBody) authentication.getPrincipal();

        SysUser user = userService.selectUserByMobile(mobileLoginBody.getMobile());

        if (user == null) {
            userService.registerUser(buildUser(mobileLoginBody));
            user = userService.selectUserByMobile(mobileLoginBody.getMobile());
        }

        if ("1".equals(user.getStatus())) {
            throw new DisabledException("账号已停用");
        }

        Set<String> permissions = permissionService.getMenuPermission(user);
        LoginUser loginUser = new LoginUser(
                user.getUserId(), user.getDeptId(), user.getDeptIds(), user, permissions
        );

        return new MobileAuthenticationToken(loginUser);
    }

    private SysUser buildUser(MobileLoginBody mobileLoginBody) {
        SysUser sysUser = BeanUtil.copyProperties(mobileLoginBody, SysUser.class);
        sysUser.setPhonenumber(mobileLoginBody.getMobile());
        sysUser.setUserType(UserTypeEnum.TOURIST.getCode());
        return sysUser;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
