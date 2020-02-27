package com.xu.majiangcommunity.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WebLog implements Serializable {
    private Integer id;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 用户名
     */
    private String name;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Long spendTime;

    /**
     * uri
     */
    private String uri;

    /**
     * url
     */
    private String url;

    /**
     * ip
     */
    private String ip;

    private static final long serialVersionUID = 1L;
}
