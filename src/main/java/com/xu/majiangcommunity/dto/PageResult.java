package com.xu.majiangcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private T data;
    private List<Integer> pages;
    private Integer total;
    private Integer pageNum;
    private Boolean showPre;
    private Boolean showFirst;
    private Boolean showNext;
    private Boolean showEndPage;
}
