package cn.ymypay.team.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lvjinfa
 * @date 2025/6/2 下午6:14
 * @description: 自定义异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class YmwlTXTException extends RuntimeException {

    private String message;
    public YmwlTXTException(String message) {
        super(message);
        this.message = message;
    }
    @Override
    public String toString() {
        return "YmwlException{" +
                "message=" + this.getMessage() +
                '}';
    }
}
