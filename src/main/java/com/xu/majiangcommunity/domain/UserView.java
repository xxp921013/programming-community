package com.xu.majiangcommunity.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    private String username;
    private Integer id;
    private String image;
}
