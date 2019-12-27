package com.lq.blog.mapper;


import com.lq.blog.model.Blog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogExtMapper {

    List<Blog> list();

    List<Blog> listBy();
     Integer count();
    List<Blog> listByQuery(@Param(value="query")String query);

    void updateView(Blog blog);

    @Select("SELECT DATE_FORMAT(b.createTime,'%Y')as year FROM blogs b GROUP BY year DESC")
    List<String> stringListYear();

    @Select("SELECT * FROM blogs b WHERE DATE_FORMAT(b.createTime,'%Y')= #{year} and publish=true")
    List<Blog> listByYear(String year);

    Blog lastBlog(@Param(value ="id") Long id);

    Blog nextBlog(@Param(value ="id") Long id);
}