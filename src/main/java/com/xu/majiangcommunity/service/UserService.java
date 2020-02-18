package com.xu.majiangcommunity.service;

import com.xu.majiangcommunity.dao.UserMapper;
import com.xu.majiangcommunity.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public void addUser(User mainUser) {
        userMapper.addUser(mainUser);
    }

    public User findByToken(String token) {
        User byToken = userMapper.findByToken(token);
        return byToken;
    }

    public User findById(int id) {
        User byId = userMapper.findById(id);
        return byId;
    }

    public List<User> findByAccountId(Long id) {
        List<User> users = userMapper.findByAccountId(id);
        return users;
    }

    public void zeroRound() {
        userMapper.zeroRound();
    }

    public void plusNewRound(Integer articleId) {
        userMapper.plusNewRound(articleId);
    }
}
