package com.lq.blog.controller;

import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.model.User;
import com.lq.blog.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class BlogUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtMapper userExtMapper;
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
   @PostMapping("/user/login")
    public String userLogin(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes attributes){
       User user =  userService.userCheck(username,password);
       if (user != null) {
           session.setAttribute("user",user);
       } else
       {
           attributes.addFlashAttribute("message","用户名和密码错误");
           return "redirect:/login";
       }
        return "welcome";
   }
    @GetMapping("/user/logout")
    public String logOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
    @GetMapping("/user/sign")
    public String profile(){
        return "sign";
    }
     @PostMapping("sign")
    public String sign(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam String nickname,
                       @RequestParam String email,
                       @RequestParam String avatar,
                       Model model){
        User user = new User();
        if (userExtMapper.checkUserName(username) != null)
        {
            model.addAttribute("error","用户名已经存在了，请换一个试试看吧");

        }
        else {
            user.setUsername(username);
            user.setPassword("fcea920f7412b5da7be0cf42b8c93759");
            user.setNickname(nickname);
            user.setEmail(email);
            user.setAvatar(avatar);
            userService.save(user);
            model.addAttribute("error","恭喜你，添加成功，返回登录吧");
        }
         return "sign";
     }
    @GetMapping("/user/edit")
    public String editUser(){
        return "eiditUser";
    }
    @PostMapping("/edit/input")
    public String editInput(@RequestParam String nickname,
                            @RequestParam String email,
                            @RequestParam String avatar,
                            HttpSession session,
                            RedirectAttributes attributes){
       User user = (User) session.getAttribute("user");
       User user1 = new User();
       user1.setAvatar(user.getAvatar());
       user1.setEmail(user.getEmail());
       user1.setNickname(user.getNickname());
       user.setNickname(nickname);
       user.setEmail(email);
       user.setAvatar(avatar);
      int i= userService.update(user,user1);
      if(i==1){
          attributes.addFlashAttribute("message","恭喜你修改成功了");
      }
        return "redirect:/";
    }
}
