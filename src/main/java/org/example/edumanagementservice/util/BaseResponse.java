package org.example.edumanagementservice.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BaseResponse<T> {

    private String requestId;   // （可选）用于请求链路追踪（如UUID）

    private int code;           // HTTP状态码或业务状态码

    private String message;     // 对结果的描述

    private T data;             // API返回的核心数据（泛型）


    // ==== 【新增】静态工厂方法（推荐使用）====

    /**
     * 【成功】构造默认成功的响应 (code=200)
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(null, HttpStatus.OK.value(), "Success", data);
    }

    /**
     * 【成功】构造带自定义消息的成功响应 (code=200)
     */
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(null, HttpStatus.OK.value(), message, data);
    }

    /**
     * 【失败】构造错误响应 (需指定错误码和消息)
     */
    public static <T> BaseResponse<T> fail(int code, String message) {
        return new BaseResponse<>(null, code, message, null);
    }

    /**
     * 【失败】直接传入HttpStatus构造错误响应 (自动提取code和reason)
     */
    public static <T> BaseResponse<T> fail(HttpStatus status, String detailMessage) {
        return new BaseResponse<>(null, status.value(), status.getReasonPhrase() + ": " + detailMessage, null);
    }


    // ==== 【保留】原有构造方法 ====
    public BaseResponse(String requestId, int code, String message, T data) {
        this.requestId = requestId;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
