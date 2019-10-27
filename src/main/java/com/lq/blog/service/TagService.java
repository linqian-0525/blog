package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.Exception.NotFoundException;
import com.lq.blog.mapper.TagExtMapper;
import com.lq.blog.mapper.TagMapper;
import com.lq.blog.model.Tag;
import com.lq.blog.model.TagExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
   public List<Tag> listTag(String ids){//123
        List<Tag> list = new ArrayList<>();
        int s = convertToList(ids).size();
        List<Long> longs =  convertToList(ids);
        for (int i =0;i<s;i++){
            Tag tag  = getType(longs.get(i));
            list.add(tag);
        }
        return list;
    }
    private List<Long> convertToList(String ids){
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null){
            String [] idarray = ids.split(",");
            for (int i = 0 ;i<idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
