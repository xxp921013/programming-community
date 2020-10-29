package com.xu.majiangcommunity.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticle implements Serializable {
    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 文章标题
     */
    private String tittle;

    /**
     * 权重
     */
    private Long weights;

    private static final long serialVersionUID = 1L;
}