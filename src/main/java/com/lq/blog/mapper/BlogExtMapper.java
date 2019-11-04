package com.lq.blog.mapper;


import com.lq.blog.model.Blog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogExtMapper {

    List<Blog> list();

    List<Blog> listBy();
     Integer count();

    List<Blog> listByQuery(@Param(value="query")String query);

    void updateView(Blog blog);
}