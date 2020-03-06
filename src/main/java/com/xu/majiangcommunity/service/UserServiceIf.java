package com.xu.majiangcommunity.service;

import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.domain.UserExample;

import java.util.List;

public interface UserServiceIf {
    void addUser(User mainUser);

    User findByToken(String token);

    User findById(String id);

    List<User> findByAccountId(Long id);

    void zeroRound();

    void plusNewRound(String articleId);

    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(User record, UserExample example);

    int updateByExample(User record, UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User getById(Integer id);

    int updateHeadByName(String name, String url);
}
