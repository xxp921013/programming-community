package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.ArticleCollection;
import com.xu.majiangcommunity.domain.ArticleCollectionExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ArticleCollectionMapper {
    long countByExample(ArticleCollectionExample example);

    int deleteByExample(ArticleCollectionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ArticleCollection record);

    int insertSelective(ArticleCollection record);

    List<ArticleCollection> selectByExample(ArticleCollectionExample example);

    ArticleCollection selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ArticleCollection record, @Param("example") ArticleCollectionExample example);

    int updateByExample(@Param("record") ArticleCollection record, @Param("example") ArticleCollectionExample example);

    int updateByPrimaryKeySelective(ArticleCollection record);

    int updateByPrimaryKey(ArticleCollection record);

    List<Integer> findArticleIdByUsername(@Param("username") String username);


    List<ArticleCollection> selectByUsernameLike(@Param("likeUsername")String likeUsername);

    int insertList(@Param("list")List<ArticleCollection> list);









}
