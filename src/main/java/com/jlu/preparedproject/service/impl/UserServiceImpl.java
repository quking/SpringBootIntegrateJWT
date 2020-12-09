package com.jlu.preparedproject.service.impl;

import com.jlu.preparedproject.dao.UserDao;
import com.jlu.preparedproject.pojo.UserT;
import com.jlu.preparedproject.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Override
    public UserT findUserById(int userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public UserT findUserByUserName(String username) {
        return userDao.findUserByUsername(username);
    }
}
