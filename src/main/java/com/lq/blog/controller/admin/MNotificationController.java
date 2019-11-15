package com.lq.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.lq.blog.service.CNotificationService;
import com.lq.blog.service.MNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class MNotificationController {
    @Autowired
    private MNotificationService service;
    @Autowired
    private CNotificationService cservice;
    @GetMapping("/motification")
    public String request(Model model, HttpSession session,
                           @RequestParam(defaultValue = "1",value = "page") Integer page){
        PageHelper.startPage(page,5);
        model.addAttribute("page",service.list());
        Long nRead = cservice.unreadCount();
        Long mRead = service.unreadCount();
        Long unread = nRead + mRead;
        session.setAttribute("unread",unread);
        return "admin/motification";
    }
    @GetMapping("/motification/{id}/input")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        service.updateStatus(id);
        attributes.addFlashAttribute("message","标记成功");
        return "redirect:/admin/motification";
    }
}
