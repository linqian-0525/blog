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
public class NotificationController {

    @Autowired
    private MNotificationService mservice;
    @Autowired
    private CNotificationService service;
    @GetMapping("/notification")
    public String getRequest(HttpSession session, Model model,
                             @RequestParam(defaultValue = "1",value = "page") Integer page){
        PageHelper.startPage(page,10);
        model.addAttribute("page",service.list());
        Long nRead = service.unreadCount();
        Long mRead = mservice.unreadCount();
        Long unread = nRead + mRead;
        session.setAttribute("unread",unread);
        return "admin/notification";
    }
    @GetMapping("/notification/{id}/input")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        service.updateStatus(id);
        attributes.addFlashAttribute("message","标记成功");
        return "redirect:/admin/notification";
    }
}
