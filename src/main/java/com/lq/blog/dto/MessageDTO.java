package com.lq.blog.dto;

import com.lq.blog.model.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MessageDTO {
    private Long id;
    private Boolean adminReply;
    private String avatar;
    private String content;
    private Date createTime;
    private String email;
    private String nickname;
    private Long parentMessageId;
    private Integer state;
    private Long userId;
    private Message parentMessage;
    private List<MessageDTO> replyMessage = new ArrayList<>();
}
