package com.alumni.framework.security.provider;

import cn.hutool.core.bean.BeanUtil;
import com.alumni.common.core.domain.entity.SysUser;
import com.alumni.common.core.domain.model.LoginUser;
import com.alumni.common.core.domain.model.WechatLoginBody;
import com.alumni.common.enums.UserTypeEnum;
import com.alumni.framework.config.wechat.WxUtil;
import com.alumni.framework.security.token.WechatAuthenticationToken;
import com.alumni.framework.web.service.SysPermissionService;
import com.alumni.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class WechatAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Resource
    private WxUtil wxUtil;

    @Override
    public Authentication authenticate(Authentication authentication) {

        WechatLoginBody wechatUser = (WechatLoginBody) authentication.getPrincipal();

        String openId = wxUtil.getOpenId(wechatUser.getCode());
        wechatUser.setOpenId(openId);

        SysUser user = userService.selectUserByOpenId(wechatUser.getOpenId());

        if (user == null) {
            userService.registerUser(buildUser(wechatUser));
            user = userService.selectUserByOpenId(wechatUser.getOpenId());
        }

        if ("1".equals(user.getStatus())) {
            throw new DisabledException("账号已停用");
        }

        Set<String> permissions = permissionService.getMenuPermission(user);
        LoginUser loginUser = new LoginUser(
                user.getUserId(), user.getDeptId(), user.getDeptIds(), user, permissions
        );

        return new WechatAuthenticationToken(loginUser);
    }

    private SysUser buildUser(WechatLoginBody wechatUser) {
        SysUser sysUser = BeanUtil.copyProperties(wechatUser, SysUser.class);
        sysUser.setUserType(UserTypeEnum.TOURIST.getCode());
        return sysUser;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
