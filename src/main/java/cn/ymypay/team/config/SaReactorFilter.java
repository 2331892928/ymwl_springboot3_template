//package cn.ymypay.team.config;
//
//import org.springframework.context.annotation.Bean;
//
///**
// * @author 湮灭网络工作室——amen
// * @date 2025/9/19 上午12:59
// * @description: SaToken拦截器
// */
//public class SaReactorFilter1 {
//    @Bean
//    public SaReactorFilter getSaReactorFilter() {
//        return new SaReactorFilter()
//                // 拦截地址
//                .addInclude("/**")    /* 拦截全部path */
//                // 鉴权方法：每次访问进入
//                .setAuth(obj -> {
//                    // 登录校验
//                    SaRouter.match("/**") // 拦截所有路由
//                            .notMatch("/auth/user/login") // 排除登录接口
//                            .notMatch("/auth/verification/code/send") // 排除验证码发送接口
//                            .check(r -> StpUtil.checkLogin()) // 校验是否登录
//                    ;
//                })
//                // 异常处理方法：每次setAuth函数出现异常时进入
//                .setError(e -> {
//                    if (e instanceof NotLoginException) { // 未登录异常
//                        throw new NotLoginException(e.getMessage(), null, null);
//                    } else if (e instanceof NotPermissionException || e instanceof NotRoleException) { // 权限不足，或不具备角色，统一抛出权限不足异常
//                        throw new NotPermissionException(e.getMessage());
//                    } else { // 其他异常，则抛出一个运行时异常
//                        throw new RuntimeException(e.getMessage());
//                    }
//                });
//    }
//
//}
