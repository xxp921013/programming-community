package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.ArticleDetailDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article (tittle,description,gmt_create,gmt_modified,creator,comment_count,view_count,like_count,tags) values(#{tittle},#{description},#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{viewCount},#{likeCount},#{tags})")
    void addArticle(Article article);

    @Select("select * from article")
    List<Article> findAll();

    List<ArticleDTO> findDTObyID();

    List<ArticleDTO> findDTObyIDWithKeyWord(String keyWord);

    @Select("select * from article where creator = #{id} order by gmt_modified desc ")
    List<Article> findArticleByCreator(String id);

    ArticleDetailDTO findArticleDetailById(String id);

    @Update("UPDATE article SET view_count = view_count +1 WHERE id =#{id}")
    void viewArticle(String id);

    @Update("UPDATE article SET comment" +
            "_count = comment_count +1 WHERE id =#{id}")
    void commentArticle(String articleId);

    @Select("select * from article where id = #{id}")
    Article findById(Integer id);

    @Update("update article set tittle = #{tittle} , description = #{description} , tags = #{tags} , gmt_modified = #{gmtModified} where id = #{id}")
    void updateArticle(Article article);

    @Delete("delete from article where id = #{id}")
    void deleteArticle(Integer id);

    @Select("select * from article where tags REGEXP #{tags} ")
    List<Article> getHotArticle(String tags);

    @Select("select * from article where gmt_modified>#{time} ")
    List<Article> getLastDayArticle(Long time);

    @Select("select count(*) from article where creator=#{accountId}  ")
    int countByCreator(String accountId);
}
