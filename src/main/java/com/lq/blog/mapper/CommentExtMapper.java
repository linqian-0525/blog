package com.lq.blog.mapper;

import com.lq.blog.model.Comment;
import com.lq.blog.model.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
  List<Comment> findAll(@Param(value = "id") Long id);

    int count();
}