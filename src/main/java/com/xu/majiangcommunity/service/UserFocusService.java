package com.xu.majiangcommunity.service;

import com.xu.majiangcommunity.domain.UserFocusExample;

import java.util.Collection;
import java.util.List;

import com.xu.majiangcommunity.domain.UserFocus;

public interface UserFocusService {


    long countByExample(UserFocusExample example);

    int deleteByExample(UserFocusExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserFocus record);

    int insertSelective(Integer myId, Integer userId);

    List<UserFocus> selectByExample(UserFocusExample example);

    UserFocus selectByPrimaryKey(Integer id);

    int updateByExampleSelective(UserFocus record, UserFocusExample example);

    int updateByExample(UserFocus record, UserFocusExample example);

    int updateByPrimaryKeySelective(UserFocus record);

    int updateByPrimaryKey(UserFocus record);

    List<Integer> getFocusByUserId(Integer userId);

    List<Integer> getFollowersByUserId(Integer id);

    int removeFocus(Integer userId, Integer focusId);
}
