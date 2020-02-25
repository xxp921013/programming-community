package com.xu.majiangcommunity.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNewRound() {
        return newRound;
    }

    public void setNewRound(Integer newRound) {
        this.newRound = newRound;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private Integer id;

    private String accountId;

    private String name;

    private String token;

    private Long gmtCreate;

    private Long gmtModified;

    private String image;

    private Integer newRound;

    private static final long serialVersionUID = 1L;
}
