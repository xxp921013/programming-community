package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.domain.UserExample;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,image) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{image})")
    void addUser(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(String token);

    @Select("select * from user where account_id = #{id}")
    User findById(String id);

    @Select("select * from user where account_id = #{id}")
    List<User> findByAccountId(Long id);

    @Update("update user set newRound = 0")
    void zeroRound();

    @Update("UPDATE user SET newRound = newRound +1 WHERE account_id = (SELECT creator FROM article WHERE id = #{articleId} );")
    void plusNewRound(String articleId);

    User getById(@Param("id") Integer id);

    int updateImageByName(@Param("updatedImage") String updatedImage, @Param("name") String name);

    List<User> getByName(@Param("name") String name);


}
