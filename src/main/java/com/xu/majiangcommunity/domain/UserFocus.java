package com.xu.majiangcommunity.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFocus implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer focusId;

    private static final long serialVersionUID = 1L;
}