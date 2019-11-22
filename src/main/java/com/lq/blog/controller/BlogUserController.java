package com.lq.blog.controller;

import com.lq.blog.dto.VerifyCode;
import com.lq.blog.mapper.CodeMapper;
import com.lq.blog.mapper.IVerifyCodeGen;
import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.model.Code;
import com.lq.blog.model.User;
import com.lq.blog.service.ProblemService;
import com.lq.blog.service.SimpleCharVerifyCodeGenImpl;
import com.lq.blog.service.UserService;
import com.lq.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller

public class BlogUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private CodeMapper mapper;
    @Autowired
    private ProblemService service;
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
   @PostMapping("/user/login")
    public String userLogin(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String code,
                            HttpSession session,
                            RedirectAttributes attributes){
        String verified = getCode();
        if (!verified.equals(code)){
            attributes.addFlashAttribute("message", "验证码错误");
            return "redirect:/login";
        }
       User user =  userService.userCheck(username,password);
       if (user != null) {
           session.setAttribute("user",user);
           return "welcome";
       }
       else {
               attributes.addFlashAttribute("message", "用户名和密码错误");
               return "redirect:/login";
       }

   }

    private String getCode() {
      Code code =  mapper.selectByPrimaryKey(1);
      return code.getCode();
    }

    @GetMapping("/user/logout")
    public String logOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
    @GetMapping("/user/sign")
    public String profile(Model model){
       model.addAttribute("problems",service.list());
        return "sign";
    }
     @PostMapping("sign")
    public String sign(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam String nickname,
                       @RequestParam String email,
                       @RequestParam String avatar,
                       @RequestParam int problemId,
                       @RequestParam String answer,
                       Model model){
        User user = new User();
        if (userExtMapper.checkUserName(username) != null)
        {
            model.addAttribute("error","用户名已经存在了，请换一个试试看吧");

        }
        else {
            user.setUsername(username);
            user.setPassword(MD5Utils.code(password));
            user.setNickname(nickname);
            user.setEmail(email);
            user.setAvatar(avatar);
            user.setProblemid(problemId);
            user.setAnswer(answer);
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
    //修改密码功能
    @GetMapping("/user/password")
    public String editPassword(){
        return "eiditpassword";
    }
    @PostMapping("/edit/password")
    public String password(@RequestParam String oldPassword,
                           @RequestParam String password,
                           HttpSession session,Model model,RedirectAttributes attributes){
       User user = (User) session.getAttribute("user");
       String s = MD5Utils.code(oldPassword);
       if (!user.getPassword().equals(s)){
           model.addAttribute("error","输入的旧密码错误");
           return "eiditpassword";
       }
       if (oldPassword .equals(password)){
           model.addAttribute("error","新密码不能和旧密码一致");
           return "eiditpassword";
       }
       else {
           userService.editPassword(user.getId(),password);
           attributes.addFlashAttribute("message","恭喜你，修改密码成功后需要重新登录哦！");
           session.removeAttribute("user");
           return "redirect:/";
       }
    }

    /**获取验证码功能*/

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {

            VerifyCode verifyCode=  iVerifyCodeGen.generate(80, 28);
           String code = verifyCode.getCode();
           save(code);
            //将VerifyCode绑定sessions
            request.getSession().setAttribute("VerifyCode", code);
            //设置响应头
           response.setHeader("Pragma", "no-cache");
            //设置响应头
            response.setHeader("Cache-Control", "no-cache");
            //在代理服务器端防止缓冲
            response.setDateHeader("Expires", 0);
            //设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save(String code) {
        Code code1 = mapper.selectByPrimaryKey(1);
        if (code1!=null){
            code1.setCode(code);
            mapper.updateByPrimaryKey(code1);
        }
        else {
            code1.setId(1);
            code1.setCode(code);
            mapper.insert(code1);
        }
    }
    /**忘记密码的功能*/
    @GetMapping("/user/forgot")
    public String forget(Model model){
        model.addAttribute("problems",service.list());
        return "forgot";
    }
    @PostMapping("/find/password")
    public String findPassWord(@RequestParam String password,
                               @RequestParam String username,
                               @RequestParam int problemId,
                               @RequestParam String answer,
                               Model model,RedirectAttributes attributes){
     User user=  service.checkUserName(username);
     if (user==null){
         attributes.addFlashAttribute("message","你输入的该用户不存在");
         return "redirect:/user/forgot";

     }
     if (user.getProblemid()!=problemId) {
         attributes.addFlashAttribute("message", "密保问题选择不正确");
         return "redirect:/user/forgot";
     }
     if (!user.getAnswer().equals(answer)) {
         attributes.addFlashAttribute("message", "你输入的答案错了哦");
         return "redirect:/user/forgot";
        }
        else {
        user.setPassword(MD5Utils.code(password));
        service.updatePassword(user);
         model.addAttribute("error", "你已经找回密码了，请返回登录吧");
         return "forgot";
     }

    }
}
