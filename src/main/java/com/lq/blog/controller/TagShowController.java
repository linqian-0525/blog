package com.lq.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.BlogDTO;
import com.lq.blog.dto.TagDto;
import com.lq.blog.service.BlogService;
import com.lq.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;
    @GetMapping("/tags/{id}")
    public String tags(@PathVariable Long id, Model model, @RequestParam(defaultValue = "1",value = "page") Integer page){
        List<TagDto> tagDtos = tagService.tagDTO();
        Long s = id;
       if (id == -1){
           id = tagDtos.get(0).getId();
           s=id;
       }
        model.addAttribute("tags",tagDtos);
        PageHelper.startPage(page,100);
        PageInfo<BlogDTO> pageInfo=  tagService.pageQueryById(s);
        pageInfo.setPrePage(page-1);
        pageInfo.setNextPage(page+1);
       pageInfo.setPages(3);
        model.addAttribute("page",pageInfo);
        model.addAttribute("activeTypeId",s);
        return "tags";
    }
}
