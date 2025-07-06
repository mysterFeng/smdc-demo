package com.myster.demo.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 统一响应结果类
 * 
 * @author myster
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 成功响应（无数据）
     * 
     * @return 响应结果
     */
    public static <T> Result<T> success() {
        return Result.<T>builder()
                .code(200)
                .message("success")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 成功响应（有数据）
     * 
     * @param data 响应数据
     * @return 响应结果
     */
    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 成功响应（自定义消息）
     * 
     * @param message 响应消息
     * @param data 响应数据
     * @return 响应结果
     */
    public static <T> Result<T> success(String message, T data) {
        return Result.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 失败响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 失败响应（默认400错误码）
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> error(String message) {
        return error(400, message);
    }

    /**
     * 参数错误响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> badRequest(String message) {
        return error(400, message);
    }

    /**
     * 未授权响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 禁止访问响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 资源不存在响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * 服务器内部错误响应
     * 
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> Result<T> internalError(String message) {
        return error(500, message);
    }
} 