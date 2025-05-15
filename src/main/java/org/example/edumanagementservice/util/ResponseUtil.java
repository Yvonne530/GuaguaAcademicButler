package org.example.edumanagementservice.util;

import org.example.edumanagementservice.dto.DepartmentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;

import static io.micrometer.core.instrument.binder.http.HttpRequestTags.status;

public class ResponseUtil {

    /* ---------------------------- Success Responses ---------------------------- */

    /**
     * 【通用成功】返回数据+自定义消息 (保留你原有的核心逻辑)
     */
    public static <T> ResponseEntity<BaseResponse<T>> success(String msg,T data){
        return ResponseEntity.ok(new BaseResponse<>("",msg,null));
    }

    /**
     * 【快捷成功】只返回数据 (自动填充默认消息)
     */
    public static <T> ResponseEntity<BaseResponse<T>> ok(T data){
        return success("操作成功");
    }

    /**
     * 【无数据成功】仅返回消息 (兼容你原有的success(String))
     */
    public static <T> ResponseEntity<BaseResponse<Void>> ok(String msg){
        return success(msg,null);
    }

    /**
     * 【创建资源成功】HTTP-201 + Location头
     */
    public static <T> ResponseEntity<BaseResponse<T>> created(T data){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>("",msg,null));
    }

    /* ---------------------------- Error Responses ---------------------------- */

    /**
     * 【通用错误】保留你原有的fail方法
     */
    public static <T> ResponseEntity<BaseResponse<T>> fail(int code,String msg){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse<>("",msg,null));
    }

    /**
     * 【语义化错误】快捷返回404
     */
    public static <T> ResponseEntity<BaseResponse<T>> notFound(String msg){
        return status(HttpStatus.NOT_FOUND)
                .body(new BaseResponse<>("",msg,null));
    }

    /**
     * 【冲突错误】快捷返回409
     */
    public static <T> ResponseEntity<BaseResponse<T>> conflict(String msg){
        return status(HttpStatus.CONFLICT)
                .body(new BaseResponse<>("",msg,null));
    }

    private static HttpResponse<Object> status(HttpStatus httpStatus) {
    }

    /* ---------------------------- Backward Compatibility ---------------------------- */

    /**
     * 【向下兼容】保留你对DepartmentDTO的特殊处理
     */
    public static ReponseEntity<BaseReponse<DepartmentDTO>> departmentSuccess(
            String msg,DapartmentDTO data){
        return success(msg,null); //←原有逻辑保持不变
    }
}
