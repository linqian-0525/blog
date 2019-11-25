package com.lq.blog.service;

import com.lq.blog.Exception.NotFoundException;
import com.lq.blog.dto.BlogDTO;
import com.lq.blog.mapper.DetailsMapper;
import com.lq.blog.mapper.TypeMapper;
import com.lq.blog.mapper.UserMapper;
import com.lq.blog.model.Blog;
import com.lq.blog.model.Details;
import com.lq.blog.model.DetailsExample;
import com.lq.blog.model.Tag;
import com.lq.blog.util.MarkdownUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApproveService {
    @Autowired
    private DetailsMapper mapper;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TagService tagService;
    public Details check(Long userId, Long blogId, int i){
        DetailsExample example = new DetailsExample();
        example.createCriteria().andBlogIdEqualTo(blogId).andUserIdEqualTo(userId);
        List<Details> list = mapper.selectByExample(example);
        if (list.size()==0){
            //进行插入数据
            Details details = new Details();
            details.setBlogId(blogId);
            details.setUserId(userId);
            if (i==1){
                details.setLikeAccount(1);
            }else {
                details.setDisalikeCount(1);
            }

            mapper.insert(details);
        return null;}
        else {
            return list.get(0);
        }
    }

    public Long likeAccount(Long id) {
        DetailsExample  example = new DetailsExample();
        example.createCriteria().andBlogIdEqualTo(id).andLikeAccountIsNotNull();
       Long count =  mapper.countByExample(example);
       return count;
    }

    public Long disAlikeCount(Long id) {
        DetailsExample  example = new DetailsExample();
        example.createCriteria().andBlogIdEqualTo(id).andDisalikeCountIsNotNull();
        Long count =  mapper.countByExample(example);
        return count;
    }

    public Long saveCount(Long id) {
        DetailsExample  example = new DetailsExample();
        example.createCriteria().andBlogIdEqualTo(id).andSaveAccountIsNotNull();
        Long count = mapper.countByExample(example);
        return count;
    }

    public BlogDTO getAndConvert(Long id) {
        Blog blog = blogService.getBlog(id);
        if (blog == null){
            throw new NotFoundException("该博客不存在");
        }
        String content = blog.getContent();
        BlogDTO blogDTO = new BlogDTO();
        BeanUtils.copyProperties(blog,blogDTO);
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setContent( MarkdownUtils.markdownToHtmlExtensions(content));
        blogDTO.setType(typeMapper.selectByPrimaryKey(blog.getTypeid()));
        blogDTO.setFlags(blog.getFlags());
        blogDTO.setFirstpicture(blog.getFirstpicture());
        blogDTO.setAppreciation(blog.getAppreciation());
        blogDTO.setTagIds(blog.getTagIds());
        blogDTO.setUpdatetime(blog.getUpdatetime());
        blogDTO.setUser(userMapper.selectByPrimaryKey(blog.getUserid()));
        blogDTO.setTags(getTags(blog.getTagIds()));
        blogDTO.init();

        return blogDTO;
    }
    public List<Tag> getTags(String tagIds){
        List<Tag> list = new ArrayList<>();
        String str[] = StringUtils.split(tagIds,",");
        for (int i =0;i<str.length;i++){
            Tag tag = tagService.getType(Long.valueOf(str[i]));
            if (tag != null){
                list.add(tag);
            }
        }
        return list;
    }

    public Details checkAccount(Long userId, Long blogId, int i) {
        DetailsExample example = new DetailsExample();
        example.createCriteria().andBlogIdEqualTo(blogId).andUserIdEqualTo(userId);
        List<Details> list = mapper.selectByExample(example);
        if (list.size()==0){
            //进行插入数据
            Details details = new Details();
            details.setBlogId(blogId);
            details.setUserId(userId);
            if (i==1){
                details.setSaveAccount(1);
            }else {
                details.setSaveAccount(0);
            }

            mapper.insert(details);
            return null;}
        else {
            return list.get(0);
        }
    }
}
