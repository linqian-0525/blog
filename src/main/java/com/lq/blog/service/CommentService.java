package com.lq.blog.service;

import com.lq.blog.dto.CommentDTO;
import com.lq.blog.mapper.BlogMapper;
import com.lq.blog.mapper.CommentExtMapper;
import com.lq.blog.mapper.CommentMapper;
import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.model.Comment;
import com.lq.blog.model.CommentExample;
import com.lq.blog.model.User;
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
        if (user == null) {
            comment.setState(0);
            comment.setAvatar(commentDTO.getAvatar());
            comment.setNickname("匿名游客");
        }else {
            comment.setState(1);
            comment.setNickname(commentDTO.getNickname());
        }
        return commentMapper.insert(comment);
    }

    public  List<CommentDTO> listCommentByBlogId(Long blogId){
      CommentExample commentExample = new CommentExample();
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
      return commentDTOList;
    }



}
