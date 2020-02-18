package com.xu.majiangcommunity.domain;

import kotlin.jvm.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticle implements Serializable {
    private Integer articleId;
    private String tittle;
    private Long weights;
}
