package com.jlu.preparedproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.jlu.preparedproject.annotations.PassToken;
import com.jlu.preparedproject.annotations.UserLoginToken;
import com.jlu.preparedproject.service.GenerateToken;
import com.jlu.preparedproject.service.UserService;
import com.jlu.preparedproject.pojo.UserT;
import com.jlu.preparedproject.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    UserService userService;

    //登录
    @PostMapping("/login")
    public Object login(UserVO user){
        JSONObject jsonObject=new JSONObject();
        UserT userForBase=userService.findUserByUserName(user.getUsername());
        if(userForBase==null){
            jsonObject.put("message","登录失败,用户不存在");
            return jsonObject;
        }else {
            if (!userForBase.getPwd().equals(user.getPassword())){
                jsonObject.put("message","登录失败,密码错误");
                return jsonObject;
            }else {
                String token = GenerateToken.getToken(userForBase);
                jsonObject.put("token", token);
                jsonObject.put("user", userForBase);
                return jsonObject;
            }
        }
    }
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }

    @PassToken
    @GetMapping(name = "/index")
    public String index(){
        return "login";
    }
}
