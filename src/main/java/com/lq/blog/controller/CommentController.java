package com.lq.blog.controller;

import com.lq.blog.dto.CommentDTO;
import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.model.User;
import com.lq.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserExtMapper userExtMapper;
    @Value("${comment.avatar}")
    private String avatar;
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model,HttpSession session){

        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        //这里可以使用这个方法来判断  然后添加一个message 让在前台显示
        String manage = (String) session.getAttribute("manage");
        if (manage !=null){
            model.addAttribute("message",manage);
            session.removeAttribute("manage");
        }
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(CommentDTO comment, HttpSession session){

         User user = (User) session.getAttribute("user");
        User user1 =  userExtMapper.findByNameAndEmain(comment.getNickname(),comment.getEmail());
         if (user != null ){
             comment.setAvatar(user.getAvatar());
             if (user.getType()==1l) {
                 comment.setAdminComment(true);
             }else {
                 comment.setAdminComment(false);
             }
           comment.setNickname(user.getNickname());
         }
         else if (user == null && user1==null){
             comment.setAdminComment(false);
             comment.setAvatar(avatar);
             session.setAttribute("manage","没有注册的用户，评论需要提交给管理员审核哦！");
         }
        else if (user1 != null){
             if (user1.getType()==1l)
             {
                 comment.setAdminComment(true);
             }else {
                 comment.setAdminComment(false);
             }
             comment.setAvatar(user1.getAvatar());
         }
         commentService.saveComment(comment);
        return "redirect:/comments/"+ comment.getBlog().getId();
    }
}

