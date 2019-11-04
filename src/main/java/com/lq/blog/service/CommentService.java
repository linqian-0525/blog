package com.lq.blog.service;

import com.lq.blog.dto.CommentDTO;
import com.lq.blog.mapper.BlogMapper;
import com.lq.blog.mapper.CommentExtMapper;
import com.lq.blog.mapper.CommentMapper;
import com.lq.blog.model.Comment;
import com.lq.blog.model.CommentExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        comment.setNickname(commentDTO.getNickname());
        comment.setAvatar(commentDTO.getAvatar());
        comment.setCreateTime(new Date());
        comment.setAdminComment(commentDTO.getAdminComment());
        return commentMapper.insert(comment);
    }

    public  List<CommentDTO> listCommentByBlogId(Long blogId){
      CommentExample commentExample = new CommentExample();
      commentExample.createCriteria().andBlogIdEqualTo(blogId);
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
      return commentDTOList;
    }



}
