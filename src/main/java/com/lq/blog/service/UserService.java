package com.lq.blog.service;

import com.lq.blog.mapper.CommentMapper;
import com.lq.blog.mapper.MessageMapper;
import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.mapper.UserMapper;
import com.lq.blog.model.*;
import com.lq.blog.util.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private MessageMapper messageMapper;
    public User checkUser(String username,String password){
       User user =  userExtMapper.selectUser(username, MD5Utils.code(password));
        return user;
    }

    public User userCheck(String username, String password) {

        User user = userExtMapper.selectUser(username, MD5Utils.code(password));
        return user;
    }

    public void save(User user) {
        user.setType(0l);
        user.setCreatetime(new Date());
        user.setUpdatetime(new Date());
        userMapper.insert(user);
    }

    public int update(User user, User user1) {
        String nickName = user1.getNickname();
        String email = user1.getEmail();
        String avatar = user1.getAvatar();
        //修改信息完改变评论的头像、昵称、和邮箱
        CommentExample commentExample  = new CommentExample();
        commentExample.createCriteria().andNicknameEqualTo(nickName).andEmailEqualTo(email).andAvatarEqualTo(avatar);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        for (Comment c :comments){
            Comment comment = new Comment();
            BeanUtils.copyProperties(c,comment);
            comment.setAvatar(user.getAvatar());
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
            CommentExample commentExample1  = new CommentExample();
            commentExample1.createCriteria().andIdEqualTo(comment.getId());
            commentMapper.updateByExample(comment,commentExample1);
        }
        //修改信息完改变留言的头像、昵称、和邮箱
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andNicknameEqualTo(nickName).andEmailEqualTo(email).andAvatarEqualTo(avatar);
        List<Message> messages = messageMapper.selectByExample(messageExample);
        for (Message m : messages){
            Message message = new Message();
            BeanUtils.copyProperties(m,message);
            message.setEmail(user.getEmail());
            message.setAvatar(user.getAvatar());
            message.setNickname(user.getNickname());
            MessageExample example= new MessageExample();
            example.createCriteria().andIdEqualTo(m.getId());
            messageMapper.updateByExample(message,example);
        }
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public void editPassword(Long id, String password) {
        User user = userMapper.selectByPrimaryKey(id);
        user.setPassword(MD5Utils.code(password));
        userMapper.updateByPrimaryKey(user);

    }
}
