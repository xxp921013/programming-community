package com.xu.majiangcommunity.service;

import java.util.List;

import com.xu.majiangcommunity.domain.ArticleCollection;
import com.xu.majiangcommunity.domain.ArticleCollectionExample;

public interface ArticleCollectionService {


    long countByExample(ArticleCollectionExample example);

    int deleteByExample(ArticleCollectionExample example, String username);

    int deleteByPrimaryKey(Integer id);

    int insert(ArticleCollection record);

    int insertSelective(ArticleCollection record);

    List<ArticleCollection> selectByExample(ArticleCollectionExample example);

    ArticleCollection selectByPrimaryKey(Integer id);

    int updateByExampleSelective(ArticleCollection record, ArticleCollectionExample example);

    int updateByExample(ArticleCollection record, ArticleCollectionExample example);

    int updateByPrimaryKeySelective(ArticleCollection record);

    int updateByPrimaryKey(ArticleCollection record);

    List<Integer> getUserCollectionArticleIds(String username);
}
