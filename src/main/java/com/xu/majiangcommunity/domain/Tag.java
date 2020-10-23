package com.xu.majiangcommunity.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Serializable {
    private Integer id;

    /**
     * 权重
     */
    private Long weight;

    /**
     * 标签名
     */
    private String name;

    private static final long serialVersionUID = 1L;
}
