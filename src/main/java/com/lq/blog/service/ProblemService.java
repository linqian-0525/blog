package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.mapper.ProblemMapper;
import com.lq.blog.mapper.UserMapper;
import com.lq.blog.model.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {
    @Autowired
    private ProblemMapper mapper;
    @Autowired
    private UserMapper userMapper;
    public List<Problem> list(){
        ProblemExample example = new ProblemExample();
        example.createCriteria();
        List<Problem> list = mapper.selectByExample(example);
        return list;
    }

    public User checkUserName(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);
        if (users.size()!=0)
        {
            return users.get(0);
        }
       else {
           return null;
        }
    }

    public void updatePassword(User user) {
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(user.getId());
        userMapper.updateByPrimaryKeySelective(user);
    }

}
