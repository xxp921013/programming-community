package com.xu.majiangcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String image;
    private Integer roundCount;
    private Integer ArticleCount;

}
