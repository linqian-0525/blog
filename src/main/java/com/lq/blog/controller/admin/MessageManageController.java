package com.lq.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.mapper.MessageExtMapper;
import com.lq.blog.mapper.MessageMapper;
import com.lq.blog.model.Message;
import com.lq.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class MessageManageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageMapper messageMapper;
    @GetMapping("/messages")
    public String manageRequest(Model model,
                                @RequestParam(defaultValue = "1",value = "page") Integer page){
        String name= "nickname desc";
        PageHelper.startPage(page,6,name);
        PageInfo<Message> pageInfo = messageService.list();
        model.addAttribute("page",pageInfo);
        return "admin/message";
    }
    @GetMapping("/messages/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        messageMapper.deleteByPrimaryKey(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/messages";
    }
    @GetMapping("/messagemange")
    public String messageMange(Model model,
                                @RequestParam(defaultValue = "1",value = "page") Integer page){
        String name= "nickname desc";
        PageHelper.startPage(page,6,name);
        PageInfo<Message> pageInfo = messageService.listMessageByState();
        model.addAttribute("page",pageInfo);
        return "admin/messageManage";
    }
    @GetMapping("/messagemange/{id}/input")
    public String agree(RedirectAttributes attributes,@PathVariable Long id){
        messageService.update(id,1);
        attributes.addFlashAttribute("message","您已经审核通过了");
        return "redirect:/admin/messagemange";
    }
    @GetMapping("/messagemange/{id}/reject")
    public String reject(RedirectAttributes attributes,@PathVariable Long id){
        messageService.update(id,3);
        attributes.addFlashAttribute("message","你已经设置该评论为不显示状态");
        return "redirect:/admin/messagemange";
    }
}
