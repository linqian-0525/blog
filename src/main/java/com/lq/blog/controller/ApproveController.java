package com.lq.blog.controller;

import com.lq.blog.model.Details;
import com.lq.blog.model.User;
import com.lq.blog.service.ApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class ApproveController {
    @Autowired
    private ApproveService service;
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
}
