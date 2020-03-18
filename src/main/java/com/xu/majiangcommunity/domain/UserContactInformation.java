package com.xu.majiangcommunity.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContactInformation implements Serializable {
    private Integer id;

    private String username;

    private String mail;

    private Integer phone;

    private static final long serialVersionUID = 1L;
}