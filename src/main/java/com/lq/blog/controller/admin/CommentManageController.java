package com.lq.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.CommentDTO;
import com.lq.blog.mapper.CommentExtMapper;
import com.lq.blog.mapper.CommentMapper;
import com.lq.blog.service.CommentService;
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
public class CommentManageController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @GetMapping("/manage")
    public String manageRequest(Model model,
                                @RequestParam(defaultValue = "1",value = "page") Integer page){
        String name= "blog_id desc";
        PageHelper.startPage(page,6,name);
        PageInfo<CommentDTO> pageInfo = commentService.listCommentByState();
        if (page!=1 && page>1){
            pageInfo.setPrePage(page-1);
        }
        pageInfo.setNextPage(page+1);
        pageInfo.setPages(commentExtMapper.count()/6+1);
        model.addAttribute("page",pageInfo);
        return "admin/commentManage";
    }
    @GetMapping("/manage/{id}/input")
    public String agree(RedirectAttributes attributes,@PathVariable Long id){
        commentService.update(id,1);
        attributes.addFlashAttribute("message","您已经审核通过了");
        return "redirect:/admin/manage";
    }
    @GetMapping("/manage/{id}/reject")
    public String reject(RedirectAttributes attributes,@PathVariable Long id){
        commentService.update(id,3);
        attributes.addFlashAttribute("message","你已经设置该评论为不显示状态");
        return "redirect:/admin/manage";
    }
    @GetMapping("/comments")
    public String comment(Model model,
                          @RequestParam(defaultValue = "1",value = "page") Integer page){
        String name= "blog_id desc";
        PageHelper.startPage(page,6,name);
        PageInfo<CommentDTO> pageInfo = commentService.listComment();
        if (page!=1 && page>1){
            pageInfo.setPrePage(page-1);
        }
        pageInfo.setNextPage(page+1);
        pageInfo.setPages(commentExtMapper.count()/6+1);
        model.addAttribute("page",pageInfo);
        return "admin/comment";
    }
    @GetMapping("/comment/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        commentMapper.deleteByPrimaryKey(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/comments";
    }
}
