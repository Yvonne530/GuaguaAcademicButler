package org.example.edumanagementservice.exception;

/**
 * 自定义业务异常，用于处理认证、限流等业务逻辑错误
 */
public class CustomException extends RuntimeException {

    // 错误码（可扩展为枚举）
    private final int errorCode;

    public CustomException(String message) {
        super(message);
        this.errorCode = 400; // 默认400错误码（Bad Request）
    }

    public CustomException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
