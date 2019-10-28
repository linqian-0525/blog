package com.lq.blog.dto;

import com.lq.blog.model.Blog;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TypeDto {
    private Long id;
    private String name;
    List<Blog> blogList= new ArrayList<>();
}
