package com.jlu.preparedproject.dao;

import com.jlu.preparedproject.pojo.UserT;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from users where uid=#{id}")
    UserT findUserById(Integer id);

    @Select("select * from users where uname=#{username}")
    UserT findUserByUsername(String username);
}
