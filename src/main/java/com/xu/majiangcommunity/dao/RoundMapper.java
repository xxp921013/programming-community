package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.Rounds;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RoundMapper {
    @Select("select * from rounds where article_id = #{id}")
    List<Rounds> findByArticleId(Integer id);

    @Insert("insert into rounds (article_id,round_text,round_creator,name,image,gmt_create,gmt_modified) values(#{articleId},#{roundText},#{roundCreator},#{name},#{image},#{gmtCreate},#{gmtModified})")
    void addRound(Rounds round);

    @Update("update rounds set thumbs_up = thumbs_up+1 where rid=#{id}")
    int thumpUp(Integer id);

    @Select("SELECT * FROM rounds r inner JOIN (SELECT id  FROM article a WHERE creator = #{id} ) a ON r.`article_id` = a.id")
    List<Rounds> findRoundByUid(String id);

    @Select("SELECT count(*) FROM rounds r inner JOIN (SELECT id  FROM article a WHERE creator = #{id} ) a ON r.`article_id` = a.id")
    int countMyRound(String id);

}
