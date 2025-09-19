package cn.ymypay.team.exception;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
import cn.ymypay.team.common.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author lvjinfa
 * @date 2025/6/1 下午6:55
 * @description: 全局异常类
 */
@Slf4j
@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    /**
     * 自定义txt异常
     */
    @ExceptionHandler(YmwlTXTException.class)
    public String handleRuntimeException(YmwlTXTException e) {
        return "fail";
    }
    /**
     * 自定义异常
     */
    @ExceptionHandler(YmwlException.class)
    public Result<String> handleRuntimeException(YmwlException e) {
//        e.printStackTrace();
        return Result.fail(e.getMessage());
    }
    /**
     * 处理其他运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
//        e.printStackTrace();
        return Result.fail(e.getMessage());
    }
    /**
     * 处理未知异常
     */
    @ExceptionHandler(Exception.class)
    public static Result<String> handleException(Exception e) {
//        e.printStackTrace();
        return Result.fail(500, "系统错误");
    }
    /**
     * 处理404异常
     *
     * @param e NoHandlerFoundException
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<String> noHandlerFoundException(HttpServletRequest req, Exception e) {
        return Result.fail(404, "页面错误");
    }
    /**
     * 处理请求方式错误(405)异常
     *
     * @param e HttpRequestMethodNotSupportedException
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> httpRequestMethodNotSupportedException(HttpServletRequest req, Exception e) {
        return Result.fail(405, "不支持的方法");
    }
    /**
     * 处理空指针的异常
     *
     * @param e NullPointerException
     * @return
     * @description 空指针异常定义为前端传参错误，返回400
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result<String> nullPointerException(NullPointerException e) {
        return Result.fail(500, "服务器错误");
    }
    /**
     * 未登录异常
     *
     * @param e NotLoginException
     * @return
     * @description 未登录异常，返回401
     */
    @ExceptionHandler(value = NotLoginException.class)
    public Result<String> notLoginException(NotLoginException e) {
        return Result.fail(401, "未登录");
    }
    /**
     * 权限异常
     *
     * @param e NotRoleException
     * @return
     * @description 权限异常，返回403
     */
    @ExceptionHandler(value = NotRoleException.class)
    public Result<String> notRoleException(NotRoleException e) {
        return Result.fail(403, "你没有操作此接口的权限");
    }
    /**
     * 丢失了必须带参
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result<String> notRoleException(HttpMessageNotReadableException e) {
        return Result.fail(400, "丢失参数");
    }
    /**
     * Servle原生异常
     * @param e ServletException
     * @return
     */
    @ExceptionHandler(value = ServletException.class)
    public Result<String> notRoleException(ServletException e) {
//        e.printStackTrace();
        return Result.fail(500, "服务器错误");
    }
}
