package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.mapper.MNotificationMapper;
import com.lq.blog.model.MNotification;
import com.lq.blog.model.MNotificationExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MNotificationService {
    @Autowired
    private MNotificationMapper mapper;
    public PageInfo<MNotification> list() {
        MNotificationExample example = new MNotificationExample();
        example.createCriteria().andStateEqualTo(0);
        List<MNotification> list =mapper.selectByExample(example);
        PageInfo<MNotification> pageInfo =  new PageInfo<>(list);
        return pageInfo;
    }

    public void updateStatus(Long id) {
        MNotification mNotification = mapper.selectByPrimaryKey(id);
        mNotification.setState(1);
        mapper.updateByPrimaryKeySelective(mNotification);
    }

    public Long unreadCount() {
        MNotificationExample example = new MNotificationExample();
        example.createCriteria().andStateEqualTo(0);
        return mapper.countByExample(example);
    }
}
