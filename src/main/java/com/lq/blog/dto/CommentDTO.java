package com.lq.blog.dto;

import com.lq.blog.model.Blog;
import com.lq.blog.model.Comment;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CommentDTO {
    private Long id;
    private Boolean adminComment;
    private String avatar;
    private String content;
    private Date createTime;
    private String email;
    private String nickname;
    private Long blogId;
    private Long parentCommentId;
    private Integer state;
    private Long userId;
    private Blog blog;
    private List<Comment> replyComments = new ArrayList<>();
    private Comment parentComment;

}
