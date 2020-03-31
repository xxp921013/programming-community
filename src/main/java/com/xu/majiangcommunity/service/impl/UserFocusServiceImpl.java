package com.xu.majiangcommunity.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xu.majiangcommunity.constant.RedisPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xu.majiangcommunity.domain.UserFocusExample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xu.majiangcommunity.domain.UserFocus;
import com.xu.majiangcommunity.dao.UserFocusMapper;
import com.xu.majiangcommunity.service.UserFocusService;

@Service
public class UserFocusServiceImpl implements UserFocusService {
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Resource
    private UserFocusMapper userFocusMapper;

    @Override
    public long countByExample(UserFocusExample example) {
        return userFocusMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(UserFocusExample example) {
        return userFocusMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userFocusMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserFocus record) {
        return userFocusMapper.insert(record);
    }

    @Override
    public int insertSelective(Integer myId, Integer userId) {
        UserFocus userFocus = new UserFocus();
        userFocus.setUserId(myId);
        userFocus.setFocusId(userId);
        int i = 0;
        try {
            i = userFocusMapper.insertSelective(userFocus);
            if (i != 0) {
                ValueOperations<String, Serializable> stringSerializableValueOperations = redisTemplate.opsForValue();
                stringSerializableValueOperations.set(RedisPrefix.USER_FOLLOWERS_PRE + userId, new ArrayList<>());
                stringSerializableValueOperations.set(RedisPrefix.USER_FOCUS_PRE + myId, new ArrayList<>());
            }
        } catch (Exception e) {
            i = 0;
        }
        return i;
    }

    @Override
    public List<UserFocus> selectByExample(UserFocusExample example) {
        return userFocusMapper.selectByExample(example);
    }

    @Override
    public UserFocus selectByPrimaryKey(Integer id) {
        return userFocusMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(UserFocus record, UserFocusExample example) {
        return userFocusMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(UserFocus record, UserFocusExample example) {
        return userFocusMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(UserFocus record) {
        return userFocusMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserFocus record) {
        return userFocusMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Integer> getFocusByUserId(Integer userId) {
        ValueOperations<String, Serializable> stringSerializableValueOperations = redisTemplate.opsForValue();
        ArrayList<Integer> list = (ArrayList) stringSerializableValueOperations.get(RedisPrefix.USER_FOCUS_PRE + userId);
        if (CollectionUtil.isEmpty(list)) {
            list = new ArrayList<>(userFocusMapper.selectFocusIdByUserId(userId));
            stringSerializableValueOperations.set(RedisPrefix.USER_FOCUS_PRE + userId, new ArrayList<Integer>(list));
        }
        return list;
    }

    @Override
    public List<Integer> getFollowersByUserId(Integer id) {
        ValueOperations<String, Serializable> stringSerializableValueOperations = redisTemplate.opsForValue();
        ArrayList<Integer> list = (ArrayList) stringSerializableValueOperations.get(RedisPrefix.USER_FOLLOWERS_PRE + id);
        if (CollectionUtil.isEmpty(list)) {
            list = new ArrayList<>(userFocusMapper.selectUserIdByFocusId(id));
            stringSerializableValueOperations.set(RedisPrefix.USER_FOLLOWERS_PRE + id, new ArrayList<Integer>(list));
        }
        return list;
    }

    @Override
    public int removeFocus(Integer userId, Integer focusId) {
        int i = userFocusMapper.deleteByUserIdAndFocusId(userId, focusId);
        if (i != 0) {
            ValueOperations<String, Serializable> stringSerializableValueOperations = redisTemplate.opsForValue();
            stringSerializableValueOperations.set(RedisPrefix.USER_FOLLOWERS_PRE + focusId, new ArrayList<>());
            stringSerializableValueOperations.set(RedisPrefix.USER_FOCUS_PRE + userId, new ArrayList<>());
        }
        return i;

    }

}
