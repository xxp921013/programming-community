package com.xu.majiangcommunity.dto;

import lombok.Data;


@Data

public class FileResponseBody extends BaseResponseBody {
    public FileResponseBody() {
    }

    public FileResponseBody(int code, String message, String url) {
        super(code, message);
        this.url = url;
    }

    public FileResponseBody(int code, String message, String url, Integer success) {
        super(code, message);
        this.url = url;
        this.success = success;
    }

    private String url;
    private Integer success;
}
