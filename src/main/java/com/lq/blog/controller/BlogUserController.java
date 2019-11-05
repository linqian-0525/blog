package com.lq.blog.controller;

import com.lq.blog.model.User;
import com.lq.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class BlogUserController {
    @Autowired
    private UserService userService;
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
                     RedirectAttributes attributes){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setAvatar(avatar);
        userService.save(user);
        attributes.addFlashAttribute("message","恭喜你注册成功了，登录吧");
        return "redirect:/login";
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
       user.setNickname(nickname);
       user.setEmail(email);
       user.setAvatar(avatar);
      int i= userService.update(user);
      if(i==1){
          attributes.addFlashAttribute("message","恭喜你修改成功了");
      }
        return "redirect:/";
    }
}
