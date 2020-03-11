package com.xu.majiangcommunity.dto;

import cn.hutool.core.bean.BeanUtil;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.SecurityUser;
import lombok.ToString;

@ToString
public class ArticleDTO {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public SecurityUser getUser() {
        return user;
    }

    public void setUser(SecurityUser user) {
        this.user = user;
    }

    private Integer id;
    private String tittle;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private String creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tags;
    private SecurityUser user;

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
