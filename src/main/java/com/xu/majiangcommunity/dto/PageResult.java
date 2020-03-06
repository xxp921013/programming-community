package com.xu.majiangcommunity.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> extends BaseResponseBody<T> {
    private List<Integer> pages;
    private Integer total;
    private Integer pageNum;
    private Boolean showPre;
    private Boolean showFirst;
    private Boolean showNext;
    private Boolean showEndPage;

    public PageResult() {
    }

    public PageResult(int code, String message, List<Integer> pages, Integer total, Integer pageNum, Boolean showPre, Boolean showFirst, Boolean showNext, Boolean showEndPage) {
        super(code, message);
        this.pages = pages;
        this.total = total;
        this.pageNum = pageNum;
        this.showPre = showPre;
        this.showFirst = showFirst;
        this.showNext = showNext;
        this.showEndPage = showEndPage;
    }

    public PageResult(int code, String message, T data) {
        super(code, message, data);
    }

    public PageResult(int code, String message, T data, List<Integer> pages, Integer total, Integer pageNum, Boolean showPre, Boolean showFirst, Boolean showNext, Boolean showEndPage) {
        super(code, message, data);
        this.pages = pages;
        this.total = total;
        this.pageNum = pageNum;
        this.showPre = showPre;
        this.showFirst = showFirst;
        this.showNext = showNext;
        this.showEndPage = showEndPage;
    }
}
