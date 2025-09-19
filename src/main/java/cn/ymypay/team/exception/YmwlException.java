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
public class YmwlException extends RuntimeException {
    private Integer code;

    private String message;
    public YmwlException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public YmwlException(String message) {
        super(message);
        this.code = 400;
        this.message = message;
    }
    @Override
    public String toString() {
        return "YmwlException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
