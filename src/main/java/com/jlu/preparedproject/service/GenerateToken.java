package com.jlu.preparedproject.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jlu.preparedproject.pojo.UserT;

import java.util.Date;

public class GenerateToken {

    public static final long EXPIRE = 1000 * 60 * 60 * 24 * 7;

    public static String getToken(UserT user) {
        String token = JWT.create().withAudience(user.getUid()+"").withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE))
                .sign(Algorithm.HMAC256(user.getPwd()));
        return token;
    }
}
