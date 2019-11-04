package com.lq.blog.controller.admin;

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
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;
    @GetMapping
    public String loginPage(){
      return "admin/login";
    }

    @PostMapping("/login")
    public String login( @RequestParam String username,
                         @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username,password);
        if (user != null && user.getType()==1l){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        }
        if (user !=null && user.getType()!=1l){
            attributes.addFlashAttribute("message","管理员才能够登录哦");
            return "redirect:/admin";
        }
        else
            {
              attributes.addFlashAttribute("message","用户名和密码错误");
             return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session){
      session.removeAttribute("user");
      return "redirect:/admin";
    }
}
