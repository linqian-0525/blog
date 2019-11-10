package com.lq.blog.mapper;


import com.lq.blog.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserExtMapper {
  User selectUser(@Param(value="username")String username,@Param(value="password") String password);

    String checkUserName(@Param(value = "username") String username);

  User findByNameAndEmain(@Param(value = "username")String nickname, @Param(value = "email")String email);
}