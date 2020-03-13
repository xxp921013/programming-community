package com.xu.majiangcommunity.service;

import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.domain.ArticleEs;
import com.xu.majiangcommunity.domain.ArticleExample;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.ArticleDetailDTO;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.dto.PageResult;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface ArticleServiceIf {
    void addArticle(Article article);

    PageResult<List<ArticleDTO>> findAll(Integer page, String keyWord);

    PageResult<List<Article>> findArticleByCreator(String id, Integer page);

    ArticleDetailDTO getArticleDetail(String id);

    void viewArticle(String id);

    void commentArticle(String articleId);

    Article findById(Integer id);

    void updateArticle(Article article);

    void deleteArticle(Integer id);

    List<Article> getHotArticle(String tags);

    List<Article> getLastDayArticle(Long time);


    Set<Serializable> getRecent();

    int countMyArticle(String accountId);

    PageResult<List<ArticleEs>> findAllByEs(Integer page, String keyWord, String sortType);

    long countByExample(ArticleExample example);

    int deleteByExample(ArticleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    List<Article> selectByExample(ArticleExample example);

    Article selectByPrimaryKey(Integer id);

    int updateByExampleSelective(Article record, ArticleExample example);

    int updateByExample(Article record, ArticleExample example);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    ArticleDTO findOneById(Integer id);

    Integer getViewCount(Integer id);

    PageResult<List<ArticleEs>> findByIds(List<Integer> ids, Integer page);
}
