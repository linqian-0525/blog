package com.lq.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.CommentDTO;
import com.lq.blog.mapper.BlogExtMapper;
import com.lq.blog.mapper.CommentMapper;
import com.lq.blog.model.Blog;
import com.lq.blog.model.Comment;
import com.lq.blog.model.Details;
import com.lq.blog.model.User;
import com.lq.blog.service.ApproveService;
import com.lq.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class ApproveController {
    @Autowired
    private ApproveService service;
    @Autowired
    private BlogExtMapper blogExtMapper;
    @Autowired
    private CommentService commentService;
    @GetMapping("/aprrove/{id}")
    public String approve(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        if (user == null){
            model.addAttribute("id",id);
            attributes.addFlashAttribute("approve","登录后才能行使该功能哦");
          return "redirect:/blogs/"+id;
        }
        if (user !=null){
            Details details=  service.check(user.getId(),id,1);
         if (details==null){
             model.addAttribute("id",id);
             attributes.addFlashAttribute("approve","点赞成功");
          return "redirect:/blogs/"+id;
         }else {
             model.addAttribute("id",id);
             attributes.addFlashAttribute("approve","你已经表达过你的观点了，请不要重复表达");
             return "redirect:/blogs/"+id;
         }
        }
        model.addAttribute("id",id);
        return "redirect:/blogs/"+id;
    }
    @GetMapping("/blogs/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",service.getAndConvert(id));
        model.addAttribute("like_account",service.likeAccount(id));
        model.addAttribute("dislike",service.disAlikeCount(id));
        model.addAttribute("save_account",service.saveCount(id));
        model.addAttribute("id",id);
        Blog blog = blogExtMapper.lastBlog(id);
        if (blog != null){
            model.addAttribute("lastBlog",blog);
        }else {
            model.addAttribute("lastBlog",null);
        }
        Blog nextBlog = blogExtMapper.nextBlog(id);
        if (nextBlog != null){
            model.addAttribute("nextBlog",nextBlog);
        }else {
            model.addAttribute("nextBlog",null);
        }
        return "blog";
    }
    @GetMapping("/disaprrove/{id}")
    public String disapprove(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        if (user == null){
            attributes.addFlashAttribute("approve","登录后才能行使该功能哦");
            return "redirect:/blogs/"+id;
        }
        if (user !=null){
            Details details = service.check(user.getId(),id,0);
            if (details==null){
                attributes.addFlashAttribute("approve","踩一下成功");
                return "redirect:/blogs/"+id;
            }if (details !=null){
                attributes.addFlashAttribute("approve","你已经表达过你的观点了，请不要重复表达");
                return "redirect:/blogs/"+id;
            }
        }
        return "redirect:/blogs/"+id;
    }
    @GetMapping("/save/{id}")
    public String saveAccount(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        if (user == null){
            attributes.addFlashAttribute("approve","登录后才能行使该功能哦");
            return "redirect:/blogs/"+id;
        }
        if (user !=null){
            Details details = service.checkAccount(user.getId(),id,1);
            if (details==null){
                attributes.addFlashAttribute("approve","收藏成功");
                return "redirect:/blogs/"+id;
            }if (details !=null){
                attributes.addFlashAttribute("approve","你已经收藏过该文章了");
                return "redirect:/blogs/"+id;
            }
        }
        return "redirect:/blogs/"+id;
    }
    /**取消点赞功能*/
    @GetMapping("/cancel/approve/{id}")
    public String cancelApprove(@PathVariable Long id,HttpSession session,RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        service.cancelApprove(id,user.getId());
        attributes.addFlashAttribute("message","取消点赞成功");
        return "redirect:/user/profile";
    }
    /**取消收藏功能*/
    @GetMapping("/cancel/save/{id}")
    public String cancelSave(@PathVariable Long id,HttpSession session,RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        service.cancelSave(id,user.getId());
        attributes.addFlashAttribute("message","取消收藏成功");
        return "redirect:/user/mysave";
    }
    /**取消不喜欢功能*/
    @GetMapping("/cancel/dislike/{id}")
    public String cancelDisLike(@PathVariable Long id,HttpSession session,RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        service.cancelDisLike(id,user.getId());
        attributes.addFlashAttribute("message","取消不喜欢成功");
        return "redirect:/user/dislike";
    }
    /**我的评论*/
    @GetMapping("/user/comment")
    public String comment(Model model,
                          @RequestParam(defaultValue = "1",value = "page") Integer page,HttpSession session){
        User user = (User) session.getAttribute("user");
        PageHelper.startPage(page,5);
        PageInfo<Comment> pageInfo = commentService.queryComment(user.getNickname());
        model.addAttribute("page",pageInfo);
        return "mycomment";
    }
}
