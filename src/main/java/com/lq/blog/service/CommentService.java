package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.CommentDTO;
import com.lq.blog.mapper.*;
import com.lq.blog.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private CNotificationMapper cNotificationMapper;
    @Transactional
    public int saveComment (CommentDTO commentDTO){
        Long parentCommentId = commentDTO.getParentComment().getId();
        Comment comment = new Comment();
        if (parentCommentId != -1){
            comment.setParentCommentId(parentCommentId);
        }else {
            commentDTO.setParentComment(null);
        }
        comment.setContent(commentDTO.getContent());
        comment.setBlogId(commentDTO.getBlog().getId());
        comment.setEmail(commentDTO.getEmail());
        comment.setCreateTime(new Date());
        comment.setAdminComment(commentDTO.getAdminComment());
        User user =  userExtMapper.findByNameAndEmain(commentDTO.getNickname(),commentDTO.getEmail());
        if (commentDTO.getAdminComment()==true)
        {
            comment.setState(1);
            comment.setNickname(commentDTO.getNickname());
            comment.setAvatar(commentDTO.getAvatar());
        }
       else  {
            comment.setState(0);
            comment.setAvatar(commentDTO.getAvatar());
            comment.setNickname(commentDTO.getNickname());
        }
        notification(comment);
        return commentMapper.insert(comment);
    }

    private void notification(Comment comment) {
        if(comment.getAdminComment()==true){
            return;
        }
        CNotification notification = new CNotification();
        if (comment.getState()==0){
            notification.setType(0);
        }else {
            notification.setType(1);
        }
        notification.setBlogId(comment.getBlogId());
        notification.setCreatetime(new Date());
        Blog blog = blogMapper.selectByPrimaryKey(comment.getBlogId());
        notification.setBlogTitle(blog.getTitle());
        notification.setStatus(0);
        notification.setNotifier(comment.getNickname());
        notification.setRecevier("管理员");
        cNotificationMapper.insert(notification);
    }

    public  List<CommentDTO> listCommentByBlogId(Long blogId){
      CommentExample commentExample = new CommentExample();//查找出没有审核过的评论信息
      commentExample.createCriteria().andBlogIdEqualTo(blogId).andStateEqualTo(1);
      List<Comment> comments = commentMapper.selectByExample(commentExample);
      List<CommentDTO> commentDTOList = new ArrayList<>();
      for (Comment comment :comments){
          CommentDTO commentDTO = new CommentDTO();
          if (comment.getParentCommentId()==null){
              commentDTO.setReplyComments(commentExtMapper.findAll(comment.getId()));
              BeanUtils.copyProperties(comment,commentDTO);
              commentDTOList.add(commentDTO);
          }
      }
      return commentDTOList;//将结果返回给界面
    }


    public PageInfo<CommentDTO> listComment() {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        CommentExample example = new CommentExample();
        example.createCriteria();
        List<Comment> comments = commentMapper.selectByExample(example);
        for (Comment comment : comments){
            CommentDTO commentDTO =  new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setBlog(blogMapper.selectByPrimaryKey(comment.getBlogId()));
            commentDTOS.add(commentDTO);
        }
        PageInfo<CommentDTO> pageInfo = new PageInfo<>(commentDTOS);
        return pageInfo;
    }

    public PageInfo<CommentDTO> listCommentByState() {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        CommentExample example = new CommentExample();
        example.createCriteria().andStateEqualTo(0);
        List<Comment> comments = commentMapper.selectByExample(example);
        for (Comment comment : comments){
            CommentDTO commentDTO =  new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setBlog(blogMapper.selectByPrimaryKey(comment.getBlogId()));
            commentDTOS.add(commentDTO);
        }
        PageInfo<CommentDTO> pageInfo = new PageInfo<>(commentDTOS);
        return pageInfo;
    }

    public int update(Long id, int i) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        Comment comment1 = new Comment();
        BeanUtils.copyProperties(comment,comment1);
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andIdEqualTo(id);
        int x=0;
        if (i == 1)
        {
            x=1;
            comment1.setState(1);
            commentMapper.updateByExample(comment1,commentExample);
        }
        if (i==3){
            x=3;
            comment1.setState(3);
            commentMapper.updateByExample(comment1,commentExample);
        }
        return x;
    }
}
