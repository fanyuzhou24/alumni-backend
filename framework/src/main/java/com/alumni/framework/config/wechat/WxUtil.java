package com.alumni.framework.config.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class WxUtil {

    @Resource
    private WxMaService wxMaService;

    /**
     * 小程序登录
     * code -> session(openid / unionid / session_key)
     */
    public WxMaJscode2SessionResult login(String code) {
        try {
            return wxMaService
                    .getUserService()
                    .getSessionInfo(code);
        } catch (WxErrorException e) {
            log.error("微信登录失败，code={}", code, e);
            throw new RuntimeException("微信登录失败");
        }
    }

    /**
     * 仅获取 openid（最常用）
     */
    public String getOpenId(String code) {
        return login(code).getOpenid();
    }

    /**
     * 获取 unionid（前提：绑定开放平台）
     */
    public String getUnionId(String code) {
        return login(code).getUnionid();
    }

    /**
     * 获取手机号（新版接口）
     * 前端：wx.getPhoneNumber 拿到 code
     */
    public String getPhoneNumber(String phoneCode) {
        try {
            WxMaPhoneNumberInfo phoneInfo =
                    wxMaService.getUserService()
                            .getPhoneNoInfo(phoneCode);
            return phoneInfo.getPhoneNumber();
        } catch (WxErrorException e) {
            log.error("获取手机号失败", e);
            throw new RuntimeException("获取手机号失败");
        }
    }

    /**
     * 发送订阅消息（主动）
     */
    public void sendSubscribeMessage(
            cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage message) {
        try {
            wxMaService.getMsgService().sendSubscribeMsg(message);
        } catch (WxErrorException e) {
            log.error("发送订阅消息失败", e);
            throw new RuntimeException("发送订阅消息失败");
        }
    }
}
