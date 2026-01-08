package cn.ymypay.team.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.ymypay.team.utils.UserUtils;
import cn.ymypay.team.vo.UserInfoVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 湮灭网络工作室——amen
 * @date 2025/11/26 下午1:41
 * @description: 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }
    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        UserInfoVo userInfo = UserUtils.getUserInfo();
        if (userInfo == null){
            return List.of();
        }
        String role = userInfo.getRole();
        List<String> list = new ArrayList<String>();
        list.add(role);
        return list;
    }
}
