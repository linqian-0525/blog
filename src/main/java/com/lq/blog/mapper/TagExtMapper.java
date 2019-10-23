package com.lq.blog.mapper;

import com.lq.blog.model.Tag;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagExtMapper {


    List<Tag> list();
    Tag findByName(@Param(value = "name") String name);
}