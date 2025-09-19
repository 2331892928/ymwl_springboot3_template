package cn.ymypay.team.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.ymypay.team.vo.UserInfoVo ;

/**
 * @author lvjinfa
 * @date 2025/6/1 下午5:14
 * @description: 用户工具包
 */
public class UserUtils {
    public static UserInfoVo getUserInfo() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        return (UserInfoVo) StpUtil.getTokenSession().get("userInfo");
    }
    public static void setUserInfo(UserInfoVo userInfo) {
        StpUtil.getTokenSession().set("userInfo", userInfo);
    }
}
