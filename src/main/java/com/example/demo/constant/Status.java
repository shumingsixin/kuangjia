package com.example.demo.constant;

import lombok.Getter;

//枚举类
@Getter
public enum Status {
    OK(200, "操作成功"), UNKNOWN_ERROR(500, "系统出错");


    private Integer code;
    private String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
