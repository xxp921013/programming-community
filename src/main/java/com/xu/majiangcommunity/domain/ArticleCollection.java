package com.xu.majiangcommunity.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCollection implements Serializable {
    private Integer id;

    private Integer articleId;

    private String username;

    private static final long serialVersionUID = 1L;
}