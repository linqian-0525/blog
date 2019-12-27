package com.lq.blog.controller;

import com.lq.blog.mapper.UserMapper;
import com.lq.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AboutShowController {

    @Autowired
    private UserMapper userMapper;
    @GetMapping("/about")
    public String about(Model model){
       User user = userMapper.selectByPrimaryKey(1l);
       model.addAttribute("user",user);
        return "about";
    }
}
