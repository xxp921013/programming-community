package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.SecurityUser;
import com.xu.majiangcommunity.domain.SecurityUserExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SecurityUserMapper {
    long countByExample(SecurityUserExample example);

    int deleteByExample(SecurityUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SecurityUser record);

    int insertSelective(SecurityUser record);

    List<SecurityUser> selectByExample(SecurityUserExample example);

    SecurityUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SecurityUser record, @Param("example") SecurityUserExample example);

    int updateByExample(@Param("record") SecurityUser record, @Param("example") SecurityUserExample example);

    int updateByPrimaryKeySelective(SecurityUser record);

    int updateByPrimaryKey(SecurityUser record);

    @Select("select * from security_user where username=#{username} ")
    SecurityUser findByUsername(String username);

    int updateImageByUsername(@Param("updatedImage") String updatedImage, @Param("username") String username);

    @Update("update security_user set newRound = 0 where username=#{username}  ")
    void zeroRound(String username);

    @Update("UPDATE security_user SET newRound = newRound +1 WHERE account_id = (SELECT creator FROM article WHERE id = #{articleId}  );")
    void plusNewRound(String articleId);
}
