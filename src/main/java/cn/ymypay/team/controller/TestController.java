package cn.ymypay.team.controller;

import cn.ymypay.team.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 湮灭网络工作室——amen
 * @date 2025/9/19 上午12:50
 * @description: 测试
 */
@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("test");
    }
    @GetMapping("/test1")
    public Result<String> test1() {
        return Result.success("test1");
    }
}
