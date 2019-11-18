package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.Exception.NotFoundException;
import com.lq.blog.dto.BlogDTO;
import com.lq.blog.dto.TagDto;
import com.lq.blog.mapper.*;
import com.lq.blog.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogExtMapper blogExtMapper;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogMapper blogMapper;
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

    public List<TagDto> tagDTO() {
        List<Tag>  tags = tagExtMapper.list();
        List<TagDto> tagDtoList =  new ArrayList<>();
        for (Tag tag : tags){
            TagDto tagDto = new TagDto();
            BeanUtils.copyProperties(tag,tagDto);
            tagDto.setBlogList(getListBlogById(tag.getId()));
            tagDtoList.add(tagDto);
        }
        return tagDtoList;
    }

    public List<Blog> getListBlogById(Long id) {

        BlogExample example = new BlogExample();
        example.createCriteria();
       List<Blog> blogs = blogMapper.selectByExample(example);
       List<Blog> blogList = new ArrayList<>();
       for (Blog blog : blogs){
           String str[] = StringUtils.split(blog.getTagIds(),",");
           for (int i = 0 ;i<=str.length-1;i++){
               Long s = Long.valueOf(str[i]);
               if (s .equals(id)&&blog.getPublish()==true){
                   blogList.add(blog);
               }
           }
       }
        return blogList;
    }

    public PageInfo<BlogDTO> pageQueryById(Long id) {
        List<Blog> blogList = getListBlogById(id);
        List<BlogDTO> blogDTOS = new ArrayList<>();
        for (Blog blog : blogList){
            BlogDTO blogDTO =  new BlogDTO();
            BeanUtils.copyProperties(blog,blogDTO);
            Type type = typeMapper.selectByPrimaryKey(blog.getTypeid());
            blogDTO.setUser(userMapper.selectByPrimaryKey(blog.getUserid()));
            blogDTO.setType(type);
            blogDTO.setTags(blogService.getTags(blog.getTagIds()));
            if (blog.getPublish()==true){
                blogDTOS.add(blogDTO);
            }

        }
        PageInfo<BlogDTO> pageInfo = new PageInfo<>(blogDTOS);
        return pageInfo;
    }
}
