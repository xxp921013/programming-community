package com.xu.majiangcommunity.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Tag implements Serializable {
    private final static String BASE_URL = "http://localhost:8080/?keyWord=";
    private String name;

    public Tag() {
    }


    public Tag(String name) {
        this.name = name;
        this.url = BASE_URL + name;
    }

    public Tag(String name, String url) {
        this.name = name;
        this.url = url;
    }

    private String url;
}
