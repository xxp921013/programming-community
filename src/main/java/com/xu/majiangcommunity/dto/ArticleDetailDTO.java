package com.xu.majiangcommunity.dto;

import com.xu.majiangcommunity.domain.Rounds;
import com.xu.majiangcommunity.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailDTO {
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
    private List<Rounds> rounds;
}
