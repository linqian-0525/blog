package com.lq.blog.controller;

import com.lq.blog.dto.CommentDTO;
import com.lq.blog.model.User;
import com.lq.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    @Value("${comment.avatar}")
    private String avatar;
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(CommentDTO comment, HttpSession session){

         User user = (User) session.getAttribute("user");
         if (user != null ){
             comment.setAvatar(user.getAvatar());
             if (user.getType()==1l) {
                 comment.setAdminComment(true);
             }else {
                 comment.setAdminComment(false);
             }
           comment.setNickname(user.getNickname());
         }
        else if (user == null){
             comment.setAdminComment(false);
             comment.setAvatar(avatar);
            session.setAttribute("manage","没有登录的用户，评论需要提交给管理员回复哦！");
         }
         commentService.saveComment(comment);
        return "redirect:/comments/"+ comment.getBlog().getId();
    }
}

