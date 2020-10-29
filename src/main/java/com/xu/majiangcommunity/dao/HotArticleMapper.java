package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.HotArticle;
import com.xu.majiangcommunity.domain.HotArticleExample;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HotArticleMapper {
    long countByExample(HotArticleExample example);

    int deleteByExample(HotArticleExample example);

    int insert(HotArticle record);

    int insertSelective(HotArticle record);

    List<HotArticle> selectByExample(HotArticleExample example);

    int updateByExampleSelective(@Param("record") HotArticle record, @Param("example") HotArticleExample example);

    int updateByExample(@Param("record") HotArticle record, @Param("example") HotArticleExample example);

    List<HotArticle> findAllByIdIn(@Param("nameCollection") Collection<Integer> nameCollection);
}
