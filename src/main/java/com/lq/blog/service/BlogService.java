package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.Exception.NotFoundException;
import com.lq.blog.dto.BlogDTO;
import com.lq.blog.mapper.BlogExtMapper;
import com.lq.blog.mapper.BlogMapper;
import com.lq.blog.mapper.TypeMapper;
import com.lq.blog.model.Blog;
import com.lq.blog.model.BlogExample;
import com.lq.blog.model.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogExtMapper blogExtMapper;
    @Autowired
    private TypeMapper typeMapper;
   public Blog getBlog(Long id){
     return blogMapper.selectByPrimaryKey(id);
    }
    public  PageInfo<BlogDTO> listBlog(BlogDTO blogDTO){
       BlogExample blogExample = new BlogExample();
       if (StringUtils.isNotBlank(blogDTO.getTitle()) && blogDTO.getTypeid()!= null && blogDTO.isRecommend()==true){
           blogExample.createCriteria().andTitleLike("%"+blogDTO.getTitle()+"%")
                   .andTypeidEqualTo(blogDTO.getTypeid())
                   .andRecommendEqualTo(blogDTO.isRecommend());
       }
        if (StringUtils.isNotBlank(blogDTO.getTitle()) && blogDTO.getTypeid()!= null && blogDTO.isRecommend()==false){
            blogExample.createCriteria().andTitleLike("%"+blogDTO.getTitle()+"%")
                    .andTypeidEqualTo(blogDTO.getTypeid());
        }
        if (StringUtils.isNotBlank(blogDTO.getTitle()) && blogDTO.getTypeid()== null && blogDTO.isRecommend()==false){
            blogExample.createCriteria().andTitleLike("%"+blogDTO.getTitle()+"%")
                    .andRecommendEqualTo(blogDTO.isRecommend());
        }
        if (StringUtils.isNotBlank(blogDTO.getTitle()) && blogDTO.getTypeid()== null && blogDTO.isRecommend()==true){
            blogExample.createCriteria().andTitleLike("%"+blogDTO.getTitle()+"%")
                    .andRecommendEqualTo(blogDTO.isRecommend());
        }

       List<Blog> lists = blogMapper.selectByExample(blogExample);
       List<BlogDTO> dtoList = new ArrayList<>();
       for (Blog blog : lists){
           BlogDTO blogDto = new BlogDTO();
           BeanUtils.copyProperties(blog,blogDto);
           blogDto.setType(typeMapper.selectByPrimaryKey(blogDTO.getTypeid()));
           dtoList.add(blogDto);
       }
       PageInfo<BlogDTO> pageInfo = new PageInfo<>(dtoList);
        return pageInfo;
    }
    @Transactional
    public int saveBlog(BlogDTO blogDTO){
       if (blogDTO.getId()==null){
           Blog blog = new Blog();
           BeanUtils.copyProperties(blogDTO,blog);
           blog.setCreatetime(new Date());
           blog.setUpdatetime(new Date());
           blog.setView(0l);
           blog.setUserid(blogDTO.getUser().getId());
           blog.setTypeid(blogDTO.getType().getId());
           blog.setFlags(blogDTO.getFlags());
           return blogMapper.insert(blog);
       }
          else {
              Blog blog = getBlog(blogDTO.getId());
              blog.setTitle(blogDTO.getTitle());
              blog.setContent(blogDTO.getContent());
              blog.setTypeid(blogDTO.getType().getId());
              blog.setFirstpicture(blogDTO.getFirstpicture());
              blog.setAppreciation(blogDTO.isAppreciation());
              blog.setCommentabled(blogDTO.isCommentabled());
              blog.setSharestatement(blogDTO.isSharestatement());
              blog.setRecommend(blogDTO.isRecommend());
              blog.setFlags(blogDTO.getFlags());
              blog.setUpdatetime(new Date());
              return blogMapper.updateByPrimaryKeySelective(blog);
       }
    }
    public void deleteBlog(Long id){
        blogMapper.deleteByPrimaryKey(id);
    }

    public PageInfo<BlogDTO> listType() {
       List<Blog> blogs = blogExtMapper.list();
       List<BlogDTO> list = new ArrayList<>();
       for (Blog blog : blogs){
           BlogDTO blogDTO = new BlogDTO();
           BeanUtils.copyProperties(blog,blogDTO);
           Type type = typeMapper.selectByPrimaryKey(blog.getTypeid());
           blogDTO.setType(type);
           list.add(blogDTO);
       }
       PageInfo<BlogDTO> pageInfo = new PageInfo<>(list);
       return pageInfo;
    }

    public BlogDTO getBlogDto(Long id) {
       Blog blog = getBlog(id);
       BlogDTO blogDTO = new BlogDTO();

       blogDTO.setTitle(blog.getTitle());
       blogDTO.setContent(blog.getContent());
       blogDTO.setType(typeMapper.selectByPrimaryKey(blog.getTypeid()));
       blogDTO.setFlags(blog.getFlags());
       blogDTO.setFirstpicture(blog.getFirstpicture());
       blogDTO.setAppreciation(blog.getAppreciation());
       BeanUtils.copyProperties(blog,blogDTO);
        blogDTO.init();
       return blogDTO;
    }
}
