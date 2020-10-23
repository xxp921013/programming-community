package com.xu.majiangcommunity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 标签dto
 * @author: xxp
 * @create: 2020-10-20 20:31
 */
@Data
public class TagDto implements Serializable {


    private final static String BASE_URL = "http://localhost:8083/?keyWord=";
    private String name;
    private Long score;

    public TagDto() {
    }


    public TagDto(String name, Long score) {
        this.name = name;
        this.url = BASE_URL + name;
        this.score = score;
    }

    public TagDto(String name, String url) {
        this.name = name;
        this.url = url;
    }

    private String url;
}


