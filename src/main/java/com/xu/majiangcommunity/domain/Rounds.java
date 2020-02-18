package com.xu.majiangcommunity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rounds {
    private Integer rid;
    private Integer articleId;
    private String roundText;
    private Integer roundCreator;
    private String name;
    private String image;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer thumbsUp;
}
