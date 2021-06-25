package com.example.demo.hander;

import com.example.demo.constant.Status;
import lombok.Getter;

@Getter
public class JsonException extends BaseException {

    public JsonException(Status status) {
        super(status);
    }

    public JsonException(Integer code, String message) {
        super(code, message);
    }
}
