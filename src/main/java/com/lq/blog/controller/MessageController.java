package com.lq.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.MessageDTO;
import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.model.Message;
import com.lq.blog.model.User;
import com.lq.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MessageController {
    @Value("${comment.avatar}")
    private String avatar;
    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private MessageService messageService;
    @GetMapping("/messages")
    public String getMessage(Model model, @RequestParam(defaultValue = "1",value = "page") Integer page,HttpSession session){
        String order = "create_time desc";
        PageHelper.startPage(page,5,order);
        PageInfo<MessageDTO> pageInfo = new PageInfo<>(messageService.listMessage());
            pageInfo.setNextPage(page+1);
            pageInfo.setPrePage(page-1);
        model.addAttribute("page",pageInfo);
        String manage = (String) session.getAttribute("manage");
        if (manage !=null){
            model.addAttribute("message",manage);
            session.removeAttribute("manage");
        }
        return "message";
    }
    @PostMapping("/messages")
    public String post(MessageDTO message, HttpSession session){
       User user = (User) session.getAttribute("user");//从session里面取出User
        User user1 =  userExtMapper.findByNameAndEmain(message.getNickname(),message.getEmail());
        if (user != null ){//判断是有登录状态的用户
            message.setAvatar(user.getAvatar());
            if (user.getType()==1l) {//判断留言的人是否时管理员
                message.setAdminReply(true);
            }else {
                message.setAdminReply(false);//在留言成功后提示信息
                session.setAttribute("manage","你已经留言成功了，管理员审核通过后，即可显示哦");
            }message.setNickname(user.getNickname());
        } else if (user == null && user1==null){//当User的值为null时即为匿名游客
            message.setAdminReply(false);
            message.setAvatar(avatar);
            message.setNickname("匿名游客");//将用户名设置为匿名游客
            session.setAttribute("manage","你已经留言成功了，管理员审核通过后，即可显示哦");
        } else if (user1 != null){
            if (user1.getType()==1l)
            {
                message.setAdminReply(true);
            }else {
                message.setAdminReply(false);
            }
            message.setAvatar(user1.getAvatar());
        }
        messageService.save(message);
        return "redirect:/messages";
    }

}
