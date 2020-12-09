package com.jlu.preparedproject.service;

import com.jlu.preparedproject.pojo.UserT;

public interface UserService {

    UserT findUserById(int userId);

    UserT findUserByUserName(String username);
}
