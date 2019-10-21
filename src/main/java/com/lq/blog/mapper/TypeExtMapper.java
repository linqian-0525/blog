package com.lq.blog.mapper;

import com.lq.blog.model.Type;
import com.lq.blog.model.TypeExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface TypeExtMapper {
    List<Type> list();
}