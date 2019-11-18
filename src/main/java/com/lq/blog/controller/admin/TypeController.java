package com.lq.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.model.Type;
import com.lq.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;
    @GetMapping("/types")
    public String types(Model model,
                        @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        String orderBy = "id desc";
        PageHelper.startPage(pageNum,5,orderBy);
        PageInfo<Type> pageInfo = typeService.listType();
        model.addAttribute("pageInfo",pageInfo);
        return "admin/types";
    }
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/type-input";
    }
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result,
                       RedirectAttributes attributes){
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","该分类已经存在啦,换一个试试吧");
        }
      if (result.hasErrors()){
          return "admin/type-input";
      }
       int i =  typeService.save(type);
       if(i == 1){
           attributes.addFlashAttribute("message","恭喜你，添加成功啦");
        }else {
           attributes.addFlashAttribute("message","添加失败了，请重新添加");
       }
        return "redirect:/admin/types";
    }
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,
                      @PathVariable Long id,RedirectAttributes attributes){
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","该分类已经存在啦,换一个试试吧");
        }
        if (result.hasErrors()){
            return "admin/type-input";
        }
      int i = typeService.updateType(id,type);
        if(i == 1){
            attributes.addFlashAttribute("message","恭喜你，更新成功啦");
        }else {
            attributes.addFlashAttribute("message","更新失败了啊");
        }
        return "redirect:/admin/types";
    }
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
      int  i = typeService.deleteType(id);
      if (i == 1){
          attributes.addFlashAttribute("message","恭喜你，删除成功");
      }
      if (i == 0){
          attributes.addFlashAttribute("message","删除失败，有博客是该分类，请先修改该博客的分类吧");
      }
      return "redirect:/admin/types";
    }
}
