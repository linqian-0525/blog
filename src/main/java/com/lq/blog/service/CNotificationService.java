package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.mapper.CNotificationMapper;
import com.lq.blog.model.CNotification;
import com.lq.blog.model.CNotificationExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CNotificationService {
    @Autowired
    private CNotificationMapper mapper;
    public PageInfo<CNotification> list() {
        CNotificationExample example = new CNotificationExample();
        example.createCriteria().andStatusEqualTo(0);
        List<CNotification> list = mapper.selectByExample(example);
        PageInfo<CNotification> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    public void updateStatus(Long id) {
        CNotification cNotification = mapper.selectByPrimaryKey(id);
        cNotification.setStatus(1);
        mapper.updateByPrimaryKey(cNotification);
    }
    public Long unreadCount(){
        CNotificationExample example = new CNotificationExample();
        example.createCriteria().andStatusEqualTo(0);
        return mapper.countByExample(example);
    }
}
