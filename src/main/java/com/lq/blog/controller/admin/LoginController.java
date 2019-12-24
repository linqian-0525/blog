package com.lq.blog.controller.admin;

import com.lq.blog.mapper.CodeMapper;
import com.lq.blog.model.Code;
import com.lq.blog.model.User;
import com.lq.blog.service.CNotificationService;
import com.lq.blog.service.MNotificationService;
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
    @Autowired
    private CNotificationService cservice;
    @Autowired
    private MNotificationService mservice;
    @Autowired
    private CodeMapper mapper;
    @GetMapping
    public String loginPage(){
      return "admin/login";
    }

    @PostMapping("/login")
    public String login( @RequestParam String username,
                         @RequestParam String password,
                        @RequestParam String code,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username,password);
        Long nRead = cservice.unreadCount();
        Long mRead = mservice.unreadCount();
        Long unread = nRead + mRead;
        session.setAttribute("unread",unread);
        String verified = getCode();
        if (user != null && user.getType()==1l && verified.equals(code)){//判断用户是否是管理员
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        }
        if (user !=null && user.getType()!=1l) {//不是管理员时给予提示管理员才能够登录
            attributes.addFlashAttribute("message", "管理员才能够登录哦");
            return "redirect:/admin";
        }
        if (!verified.equals(code)){//输入的验证码验证
            attributes.addFlashAttribute("message", "验证码错误");
            return "redirect:/admin";
        }
        else
            {//输入的密码错时给予的提示
              attributes.addFlashAttribute("message","用户名和密码错误");
             return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session){
      session.removeAttribute("user");
      return "redirect:/admin";
    }
    private String getCode() {
        Code code =  mapper.selectByPrimaryKey(1);
        return code.getCode();
    }
}
