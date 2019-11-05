package com.lq.blog.service;

import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.mapper.UserMapper;
import com.lq.blog.model.User;
import com.lq.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private UserMapper userMapper;
    public User checkUser(String username,String password){
       User user =  userExtMapper.selectUser(username, MD5Utils.code(password));
        return user;
    }

    public User userCheck(String username, String password) {

        User user = userExtMapper.selectUser(username, MD5Utils.code(password));
        return user;
    }

    public void save(User user) {
        user.setType(0l);
        user.setCreatetime(new Date());
        user.setUpdatetime(new Date());
        userMapper.insert(user);
    }

    public int update(User user) {

        return userMapper.updateByPrimaryKeySelective(user);
    }
}
