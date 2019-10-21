package com.lq.blog.service;

import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.model.User;
import com.lq.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserExtMapper userExtMapper;

    public User checkUser(String username,String password){

       User user =  userExtMapper.selectUser(username, MD5Utils.code(password));
        return user;
    }
}
