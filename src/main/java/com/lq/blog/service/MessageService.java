package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.MessageDTO;
import com.lq.blog.mapper.MessageMapper;
import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.model.Message;
import com.lq.blog.model.MessageExample;
import com.lq.blog.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private MessageMapper messageMapper;
    public void save(MessageDTO messageDTO) {
        Long parentCommentId = messageDTO.getParentMessage().getId();
        Message message = new Message();
        if (parentCommentId == -1){
            message.setParentMessageId(null);
        }
        else {
            message.setParentMessageId(parentCommentId);
        }
        User user =  userExtMapper.findByNameAndEmain(messageDTO.getNickname(),messageDTO.getEmail());
        if (user == null) {
            message.setState(0);
            message.setAvatar(messageDTO.getAvatar());
            message.setNickname("匿名游客");
        }else {
            message.setState(1);
            message.setNickname(messageDTO.getNickname());
            message.setAvatar(messageDTO.getAvatar());
        }
        message.setAdminReply(messageDTO.getAdminReply());
        message.setContent(messageDTO.getContent());
        message.setCreateTime(new Date());
        message.setEmail(messageDTO.getEmail());
        messageMapper.insert(message);
    }

    public List<MessageDTO> listMessage() {
        List<MessageDTO> messageDTOList = new ArrayList<>();
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andParentMessageIdIsNull().andStateEqualTo(1);
        List<Message> messageList = messageMapper.selectByExample(messageExample);
        for (Message message : messageList){
            MessageDTO messageDTO =  new MessageDTO();
            BeanUtils.copyProperties(message,messageDTO);
            messageDTO.setParentMessage(null);
            messageDTO.setReplyMessage(ListMessageByParent(message.getId()));
            messageDTOList.add(messageDTO);
        }
        return messageDTOList;
    }
    //获得所有有parentId为该条的id的 message
    private List<MessageDTO> ListMessageByParent(Long  id)
    {
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andParentMessageIdEqualTo(id).andStateEqualTo(1);
        List<Message> messages = messageMapper.selectByExample(messageExample);
        List<MessageDTO> messageDTOList = new ArrayList<>();
        for (Message m : messages){
            MessageDTO messageDTO1 =  new MessageDTO();
            BeanUtils.copyProperties(m,messageDTO1);
            messageDTO1.setParentMessage(m);
            if (m.getParentMessageId()!=null){
               messageDTO1.setReplyMessage(ListMessageByParent(m.getId()));
            }
            messageDTOList.add(messageDTO1);
        }
        return messageDTOList;
    }
}
