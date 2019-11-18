package com.lq.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.BlogDTO;
import com.lq.blog.mapper.BlogExtMapper;
import com.lq.blog.mapper.TagExtMapper;
import com.lq.blog.mapper.TypeExtMapper;
import com.lq.blog.service.BlogService;
import com.lq.blog.service.TagService;
import com.lq.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogExtMapper blogExtMapper;
    @GetMapping("/")
    public String index( Model model,
                         @RequestParam(defaultValue = "1",value = "page") Integer page){
            PageHelper.startPage(page,6);
            PageInfo<BlogDTO> pageInfo =  blogService.listType(1);
            Integer i = blogExtMapper.count();
            if (page!=1){
                pageInfo.setPrePage(page-1);
            }
            pageInfo.setNextPage(page+1);
            pageInfo.setPages(i/6+1);
            model.addAttribute("page",pageInfo);
            model.addAttribute("size",i);
            PageHelper.startPage(page,6);
            model.addAttribute("types",typeService.listTypeDTO());
            String oeder = "view desc";
            PageHelper.startPage(page,5,oeder);
            model.addAttribute("tags",blogService.listByView());
            String oderBy = "updatetime desc";
            PageHelper.startPage(page,5,oderBy);
            model.addAttribute("recommendBlogs",blogService.list());
            return "index";
    }
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }

    @PostMapping("/search")
    public String search( Model model,
                         @RequestParam String query,
                         @RequestParam(defaultValue = "1",value = "page") Integer page) {
        PageHelper.startPage(page,6);
        PageInfo<BlogDTO> pageInfo =  blogService.listBlogQuery("%"+query+"%");
        if (page!=1){
            pageInfo.setPrePage(page-1);
        }
        pageInfo.setNextPage(page+1);
        pageInfo.setPages(3);
        Long i =pageInfo.getTotal();
        model.addAttribute("query",query);
        model.addAttribute("page",pageInfo);
        model.addAttribute("size",i);
    return "search";
    }
    @GetMapping("/footer/newblog")
    public String newBlogs(Model model, @RequestParam(defaultValue = "1",value = "page") Integer page){
        String orderBy = "updatetime desc";
        PageHelper.startPage(page,3,orderBy);
        model.addAttribute("newblogs",blogExtMapper.list());
        return "_fragments :: newblogList";
    }
}
