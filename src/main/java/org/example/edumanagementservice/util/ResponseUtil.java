package org.example.edumanagementservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<BaseResponse<T>> ok(T data) {
        return ResponseEntity.ok(new BaseResponse<>(null, 200, "操作成功", data));
    }

    public static ResponseEntity<BaseResponse<Void>> ok(String msg) {
        return ResponseEntity.ok(new BaseResponse<>(null, 200, msg, null));
    }

    public static <T> ResponseEntity<BaseResponse<T>> ok(String msg, T data) {
        return ResponseEntity.ok(new BaseResponse<>(null, 200, msg, data));
    }

    public static <T> ResponseEntity<BaseResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(null, 201, "创建成功", data));
    }

    public static <T> ResponseEntity<BaseResponse<T>> fail(int code, String msg) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse<>(null, code, msg, null));
    }

    public static <T> ResponseEntity<BaseResponse<T>> notFound(String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new BaseResponse<>(null, 404, msg, null));
    }

    public static <T> ResponseEntity<BaseResponse<T>> conflict(String msg) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new BaseResponse<>(null, 409, msg, null));
    }
}
