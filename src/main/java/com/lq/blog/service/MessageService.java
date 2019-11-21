package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.dto.MessageDTO;
import com.lq.blog.mapper.MNotificationMapper;
import com.lq.blog.mapper.MessageMapper;
import com.lq.blog.mapper.UserExtMapper;
import com.lq.blog.model.MNotification;
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
    @Autowired
    private MNotificationMapper mapper;
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
        if (messageDTO.getAdminReply()==true){
            message.setState(1);
            message.setNickname(messageDTO.getNickname());
            message.setAvatar(messageDTO.getAvatar());
        }
        else{
            message.setState(0);
            message.setAvatar(messageDTO.getAvatar());
            message.setNickname(messageDTO.getNickname());
        }
        message.setAdminReply(messageDTO.getAdminReply());
        message.setContent(messageDTO.getContent());
        message.setCreateTime(new Date());
        message.setEmail(messageDTO.getEmail());
        notification(message);
        messageMapper.insert(message);
    }

    private void notification(Message message) {
        if (message.getAdminReply()==true){
            return;
        }
        MNotification notification = new MNotification();
        notification.setCreatetime(new Date());
        notification.setNickname(message.getNickname());
        notification.setState(0);
        if (message.getState()==0){
            notification.setType(0);
        }else {
            notification.setType(1);
        }
        mapper.insert(notification);
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

    public PageInfo<Message> list() {
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria();
        List<Message> messages = messageMapper.selectByExample(messageExample);
        PageInfo<Message> pageInfo = new PageInfo<>(messages);
        return pageInfo;
    }

    public PageInfo<Message> listMessageByState() {
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andStateEqualTo(0);
        List<Message> messages = messageMapper.selectByExample(messageExample);
        PageInfo<Message> pageInfo = new PageInfo<>(messages);
        return pageInfo;
    }

    public int update(Long id, int i) {
        Message m = messageMapper.selectByPrimaryKey(id);
        Message message = new Message();
        BeanUtils.copyProperties(m,message);
        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andIdEqualTo(id);
        int x= 0;
        if (i == 1)
        {
            x=1;
            message.setState(1);
            messageMapper.updateByExample(message,messageExample);
        }
        if (i==3){
            x=3;
            message.setState(3);
            messageMapper.updateByExample(message,messageExample);
        }
        return x;
    }
}
