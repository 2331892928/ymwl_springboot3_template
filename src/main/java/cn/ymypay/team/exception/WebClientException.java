package cn.ymypay.team.exception;

public class WebClientException extends RuntimeException {
    private final Integer code;

    private final String message;
    /**
     * 通过状态码和错误消息创建异常对象
     */
    public WebClientException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public WebClientException(String message) {
        this.code = 400;
        this.message = message;
    }

    @Override
    public String toString() {
        return "WebClientException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
