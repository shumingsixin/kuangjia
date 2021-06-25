package com.example.demo.model;

import com.example.demo.constant.Status;
import com.example.demo.hander.BaseException;
import lombok.Data;

@Data
public class ApiResponse {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回内容
     */
    private String message;
    /**
     * 返回数据
     */
    private Object data;


    private ApiResponse() {

    }

    private ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造一个自定义的api返回
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static ApiResponse of(Integer code, String message, Object data) {
        return new ApiResponse(code, message, data);
    }

    /**
     * 构造一个有状态且有数据的api返回
     *
     * @param status
     * @param data
     * @return
     */
    public static ApiResponse ofStatus(Status status, Object data) {
        return of(status.getCode(), status.getMessage(), data);
    }

    /**
     * 构造一个有状态的api返回
     *
     * @param status
     * @return
     */
    public static ApiResponse ofStatus(Status status) {
        return ofStatus(status, null);
    }

    /**
     * 构造一个成功且带数据的api返回
     *
     * @param data
     * @return
     */
    public static ApiResponse ofSuccess(Object data) {
        return ofStatus(Status.OK, data);
    }


    /**
     * 构造一个成功且自定义消息的api返回
     *
     * @param message
     * @return
     */
    public static ApiResponse ofMessage(String message) {
        return of(Status.OK.getCode(), message, null);
    }

    /**
     * 构造一个异常且带数据的api返回
     *
     * @param t    异常
     * @param data
     * @param <T>  BaseException的之类
     * @return
     */
    public static <T extends BaseException> ApiResponse ofException(T t, Object data) {
        return of(t.getCode(), t.getMessage(), data);
    }

    /**
     * 构造一个异常不带数据api返回
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T extends BaseException> ApiResponse ofException(T t) {
        return ofException(t, null);
    }

    /**
     * 运行时错误
     * @param e
     * @return
     */
    public static  ApiResponse ofException(RuntimeException e){
        return of(Status.UNKNOWN_ERROR.getCode(),e.getMessage(),null);
    }
}
