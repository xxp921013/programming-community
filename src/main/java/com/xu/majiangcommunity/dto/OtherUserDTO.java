package com.xu.majiangcommunity.dto;

import com.xu.majiangcommunity.domain.ArticleEs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherUserDTO {
    private String username;
    private String image;
    private int isFocus;
    private int userId;
    private List<ArticleEs> articles;
}
