package com.xu.majiangcommunity.dto;

import cn.hutool.core.bean.BeanUtil;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.java2d.pipe.AAShapePipe;

@Data
public class ArticleDTO {
    private Integer id;
    private String tittle;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tags;
    private User user;

    public ArticleDTO() {
    }

    public ArticleDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtil.copyProperties(article, articleDTO);
    }

    public static ArticleDTO getArticleDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtil.copyProperties(article, articleDTO);
        return articleDTO;
    }

}
