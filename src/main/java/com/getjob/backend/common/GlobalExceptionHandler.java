package com.getjob.backend.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理JSON解析错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("JSON解析错误", e);
        String message = "请求体格式错误，请检查JSON格式";
        if (e.getMessage() != null && e.getMessage().contains("JSON parse error")) {
            message = "JSON格式错误，请检查请求体中的特殊字符是否正确转义";
        }
        return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理参数验证错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数验证错误", e);
        String message = "请求参数验证失败";
        if (e.getBindingResult() != null && e.getBindingResult().getFieldError() != null) {
            message = e.getBindingResult().getFieldError().getDefaultMessage();
        }
        return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("服务器内部错误", e);
        return ApiResponse.error(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "服务器内部错误: " + e.getMessage());
    }
}

