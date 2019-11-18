package com.lq.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.BlogDTO;
import com.lq.blog.mapper.BlogExtMapper;
import com.lq.blog.mapper.TagExtMapper;
import com.lq.blog.mapper.TypeExtMapper;
import com.lq.blog.model.User;
import com.lq.blog.service.BlogService;
import com.lq.blog.service.TagService;
import com.lq.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeExtMapper typeExtMapper;
    @Autowired
    private BlogExtMapper blogExtMapper;
    @Autowired
    private TagExtMapper tagExtMapper;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     *
     * 管理博客列表的展示
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(Model model,
                        @RequestParam(defaultValue = "1",value = "page") Integer page){
        String oderby="updatetime desc";
        PageHelper.startPage(page,5,oderby);
        PageInfo<BlogDTO> pageInfo =  blogService.listType(0);
        if (page!=1){
            pageInfo.setPrePage(page-1);
        }
        pageInfo.setNextPage(page+1);
        pageInfo.setPages(blogExtMapper.count()/5+1);
        model.addAttribute("page",pageInfo);
        model.addAttribute("types",typeExtMapper.list());
        return "admin/blogs";
    }

    /**
     * 博客条件的查询
     * @param model
     * @param blogDTO
     * @param pageNum
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(Model model, BlogDTO blogDTO,
                        @RequestParam(defaultValue = "1",value = "page") Integer pageNum){

        PageHelper.startPage(pageNum,10);
        PageInfo<BlogDTO> pageInfo =  blogService.listBlog(blogDTO);
        model.addAttribute("page",pageInfo);
        return "admin/blogs :: blogList";
    }

    /**
     * 博客的修改的界面
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model){
        model.addAttribute("blog",new BlogDTO());
        model.addAttribute("types",typeExtMapper.list());
        model.addAttribute("tags",tagExtMapper.list());
        return "error/blogs-input";
    }

    /**
     * 博客内容的修改
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(Model model,@PathVariable Long id){
        BlogDTO blogDTO = blogService.getBlogDto(id);
        model.addAttribute("blog",blogDTO);
        model.addAttribute("types",typeExtMapper.list());
        model.addAttribute("tags",tagExtMapper.list());
        return "error/blogs-input";
    }

    /**
     * 博客的新增
     * @param blogDTO
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/blogs")
    public String post(BlogDTO blogDTO, HttpSession session,
                       RedirectAttributes attributes){
        blogDTO.setUser((User) session.getAttribute("user"));
        blogDTO.setType(typeService.getType(blogDTO.getType().getId()));
        blogDTO.setTags(tagService.listTag(blogDTO.getTagIds()));
        int i = blogService.saveBlog(blogDTO);
        if(i == 1){
            attributes.addFlashAttribute("message","恭喜你，操作成功啦");
        }else {
            attributes.addFlashAttribute("message","操作失败了，请再试一次吧");
        }

        return "redirect:/admin/blogs";
    }
    /**
     * 博客的删除
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }
}
