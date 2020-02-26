package com.xu.majiangcommunity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
public class BaseResponseBody<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public BaseResponseBody() {
    }

    public BaseResponseBody(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponseBody(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
