package com.lq.blog.controller;

import com.lq.blog.mapper.BlogExtMapper;
import com.lq.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogExtMapper blogExtMapper;
    @GetMapping("/archives")
    public String archives(Model model){
        model.addAttribute("archiveMap",blogService.archiveBlog());
        model.addAttribute("blogCount",blogExtMapper.count());
        return "archives";
    }
}
