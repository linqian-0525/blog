package com.lq.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {
    @GetMapping("/message")
    public String getMessage(){
        return "message";
    }
}
