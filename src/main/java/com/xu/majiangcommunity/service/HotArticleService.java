package com.xu.majiangcommunity.service;

import java.util.Collection;
import java.util.List;

import com.xu.majiangcommunity.domain.HotArticleExample;
import com.xu.majiangcommunity.domain.HotArticle;

public interface HotArticleService {


    long countByExample(HotArticleExample example);

    int deleteByExample(HotArticleExample example);

    int insert(HotArticle record);

    int insertSelective(HotArticle record);

    List<HotArticle> selectByExample(HotArticleExample example);

    int updateByExampleSelective(HotArticle record, HotArticleExample example);

    int updateByExample(HotArticle record, HotArticleExample example);

    List<HotArticle> selectByArticleIdIn(List<Integer> ids);
}
