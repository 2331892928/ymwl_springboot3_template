package cn.ymypay.team.common;

import lombok.Data;

/**
 * @author lvjinfa
 * @date 2025/5/29 上午1:59
 * @description: 返回实体
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("success");
        return result;
    }
    public static <T> Result<T> success(Integer code) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg("success");
        return result;
    }
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }
    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.setCode(201);
        result.setMsg("fail");
        return result;
    }
    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setCode(400);
        result.setMsg(message);
        return result;
    }
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }
}
