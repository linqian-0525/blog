package com.lq.blog.mapper;

import com.lq.blog.model.Type;
import com.lq.blog.model.TypeExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TypeExtMapper {
    List<Type> list();

    Type findByName(@Param(value = "name") String name);
}