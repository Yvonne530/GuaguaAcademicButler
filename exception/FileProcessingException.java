package org.example.edumanagementservice.exception;

/**
 * 自定义文件处理异常
 */
public class FileProcessingException extends RuntimeException {

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
