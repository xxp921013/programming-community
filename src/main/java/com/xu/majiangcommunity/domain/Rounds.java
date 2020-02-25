package com.xu.majiangcommunity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class Rounds {
    private Integer rid;
    private String articleId;
    private String roundText;
    private String roundCreator;
    private String name;
    private String image;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getRoundText() {
        return roundText;
    }

    public void setRoundText(String roundText) {
        this.roundText = roundText;
    }

    public String getRoundCreator() {
        return roundCreator;
    }

    public void setRoundCreator(String roundCreator) {
        this.roundCreator = roundCreator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Integer getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(Integer thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    private Long gmtCreate;
    private Long gmtModified;
    private Integer thumbsUp;
}
