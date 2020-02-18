package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,image) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{image})")
    void addUser(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(String token);

    @Select("select * from user where account_id = #{id}")
    User findById(int id);

    @Select("select * from user where account_id = #{id}")
    List<User> findByAccountId(Long id);

    @Update("update user set newRound = 0")
    void zeroRound();

    @Update("UPDATE user SET newRound = newRound +1 WHERE account_id = (SELECT creator FROM article WHERE id = #{articleId} );")
    void plusNewRound(Integer articleId);

}
