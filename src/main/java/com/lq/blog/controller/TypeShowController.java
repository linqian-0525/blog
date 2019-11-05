package com.lq.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.BlogDTO;
import com.lq.blog.dto.TypeDto;
import com.lq.blog.mapper.TypeExtMapper;
import com.lq.blog.model.Type;
import com.lq.blog.service.BlogService;
import com.lq.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeShowController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;
    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id, Model model, @RequestParam(defaultValue = "1",value = "page") Integer page){
        List<TypeDto> typeDtos = typeService.typeDTO();
       if (id == -1){
           id = typeDtos.get(0).getId();
       }
        model.addAttribute("types",typeDtos);
        PageHelper.startPage(page,6);
        PageInfo<BlogDTO> pageInfo= blogService.getBlogByTypeId(id);
        pageInfo.setPrePage(page-1);
        pageInfo.setNextPage(page+1);
        pageInfo.setPages(pageInfo.getSize()/6);
        model.addAttribute("page",pageInfo);
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
