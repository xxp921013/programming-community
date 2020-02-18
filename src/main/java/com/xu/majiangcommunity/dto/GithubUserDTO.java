package com.xu.majiangcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubUserDTO {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;
}
