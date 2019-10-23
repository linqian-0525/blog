package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.Exception.NotFoundException;
import com.lq.blog.mapper.TagExtMapper;
import com.lq.blog.mapper.TagMapper;
import com.lq.blog.model.Tag;
import com.lq.blog.model.TagExample;
import com.lq.blog.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagExtMapper tagExtMapper;
    @Autowired
    private TagMapper tagMapper;
    public PageInfo<Tag> listType() {
        List<Tag> list = tagExtMapper.list();
        PageInfo<Tag> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    public Tag getType(Long id) {
        Tag tag = tagMapper.selectByPrimaryKey(id);
        return  tag;
    }
    public Tag getTagByName(String name) {
       Tag tag = tagExtMapper.findByName(name);
        return tag;
    }
    public int save(Tag tag) {
        Tag tag1  = new Tag();
        tag1.setName(tag.getName());
        return tagMapper.insert(tag1);
    }
    @Transactional
    public int  updateType(Long id, Tag tag){
        Tag db = tagMapper.selectByPrimaryKey(id);
        if (db == null){
            throw new NotFoundException("不存在该类型");
        }
        Tag update = new Tag();
        update.setName(tag.getName());
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andIdEqualTo(id);
        int i =  tagMapper.updateByExampleSelective(update,tagExample);
        return i;
    }
   @Transactional
    public void deleteType(Long id) {
        tagMapper.deleteByPrimaryKey(id);
    }
}
