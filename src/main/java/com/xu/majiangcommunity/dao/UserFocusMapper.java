package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.UserFocus;
import com.xu.majiangcommunity.domain.UserFocusExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserFocusMapper {
    long countByExample(UserFocusExample example);

    int deleteByExample(UserFocusExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserFocus record);

    int insertSelective(UserFocus record);

    List<UserFocus> selectByExample(UserFocusExample example);

    UserFocus selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserFocus record, @Param("example") UserFocusExample example);

    int updateByExample(@Param("record") UserFocus record, @Param("example") UserFocusExample example);

    int updateByPrimaryKeySelective(UserFocus record);

    int updateByPrimaryKey(UserFocus record);

    List<Integer> selectFocusIdByUserId(@Param("userId") Integer userId);

    List<Integer> selectUserIdByFocusId(@Param("focusId") Integer focusId);

    int deleteByUserIdAndFocusId(@Param("userId") Integer userId, @Param("focusId") Integer focusId);


}
