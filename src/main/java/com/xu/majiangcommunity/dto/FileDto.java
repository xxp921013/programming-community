package com.xu.majiangcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private Integer success;
    private String message;
    private String url;
}
