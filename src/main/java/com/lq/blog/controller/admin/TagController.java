package com.lq.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.model.Tag;
import com.lq.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/tags")
    public String types(Model model,
                        @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
    //    String orderBy = "createtime desc";
        PageHelper.startPage(pageNum,5);
        PageInfo<Tag> pageInfo = tagService.listType();
        model.addAttribute("pageInfo",pageInfo);
        return "admin/tags";
    }
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tag-input";
    }
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getType(id));
        return "admin/tag-input";
    }
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result,
                       RedirectAttributes attributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null){
            result.rejectValue("name","nameError","该标签已经存在啦,换一个试试吧");
        }
        if (result.hasErrors()){
            return "admin/tag-input";
        }
        int i =  tagService.save(tag);
        if(i == 1){
            attributes.addFlashAttribute("message","恭喜你，添加成功啦");
        }else {
            attributes.addFlashAttribute("message","添加失败了，请重新添加");
        }
        return "redirect:/admin/tags";
    }
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result,
                           @PathVariable Long id,RedirectAttributes attributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null){
            result.rejectValue("name","nameError","该分类已经存在啦,换一个试试吧");
        }
        if (result.hasErrors()){
            return "admin/tag-input";
        }
        int i = tagService.updateType(id,tag);
        if(i == 1){
            attributes.addFlashAttribute("message","恭喜你，更新成功啦");
        }else {
            attributes.addFlashAttribute("message","更新失败了啊");
        }
        return "redirect:/admin/tags";
    }
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        tagService.deleteType(id);
        attributes.addFlashAttribute("message","恭喜你，删除成功");
        return "redirect:/admin/tags";
    }
}
