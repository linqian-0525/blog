package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.Exception.NotFoundException;
import com.lq.blog.dto.BlogDTO;
import com.lq.blog.mapper.*;
import com.lq.blog.model.*;
import com.lq.blog.util.MarkdownUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogExtMapper blogExtMapper;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentMapper commentMapper;
   public Blog getBlog(Long id){
     return blogMapper.selectByPrimaryKey(id);
    }
    //查询博客
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
           blogDTO.setUser(userMapper.selectByPrimaryKey(blog.getUserid()));
           dtoList.add(blogDto);
       }
       PageInfo<BlogDTO> pageInfo = new PageInfo<>(dtoList);
        return pageInfo;
    }
    @Transactional
    public int saveBlog(BlogDTO blogDTO){
       //判断是blogId为空时，是新增博客
       if (blogDTO.getId()==null){
           Blog blog = new Blog();
           BeanUtils.copyProperties(blogDTO,blog);
           blog.setCreatetime(new Date());
           blog.setUpdatetime(new Date());
           blog.setView(0l);
           blog.setUserid(blogDTO.getUser().getId());
           blog.setTypeid(blogDTO.getType().getId());
           blog.setFlags(blogDTO.getFlags());
           blog.setDescription(blogDTO.getDescription());
           blog.setTagIds(blogDTO.getTagIds());
           blog.setPublish(blogDTO.isPublish());
           return blogMapper.insert(blog);
       }
       //这是修改博客的内容
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
              blog.setDescription(blogDTO.getDescription());
              blog.setUpdatetime(new Date());
               blog.setPublish(blogDTO.isPublish());
              blog.setTagIds(blogDTO.getTagIds());
              return blogMapper.updateByPrimaryKeySelective(blog);
       }
    }
    //删除博客的内容
    public void deleteBlog(Long id){
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andBlogIdEqualTo(id);
       commentMapper.deleteByExample(commentExample);
        blogMapper.deleteByPrimaryKey(id);
    }
    //根据分类的类型  查询相关的博客
    public PageInfo<BlogDTO> listType() {
       List<Blog> blogs = blogExtMapper.list();
       List<BlogDTO> list = new ArrayList<>();
       for (Blog blog : blogs){
           BlogDTO blogDTO = new BlogDTO();
           BeanUtils.copyProperties(blog,blogDTO);
           Type type = typeMapper.selectByPrimaryKey(blog.getTypeid());
           blogDTO.setUser(userMapper.selectByPrimaryKey(blog.getUserid()));
           blogDTO.setType(type);
           list.add(blogDTO);
       }
       PageInfo<BlogDTO> pageInfo = new PageInfo<>(list);
       return pageInfo;
    }
//根据页面所需要的内容传给界面
    public BlogDTO getBlogDto(Long id) {
       Blog blog = getBlog(id);
       BlogDTO blogDTO = new BlogDTO();
        BeanUtils.copyProperties(blog,blogDTO);
       blogDTO.setTitle(blog.getTitle());
       blogDTO.setContent(blog.getContent());
       blogDTO.setType(typeMapper.selectByPrimaryKey(blog.getTypeid()));
       blogDTO.setFlags(blog.getFlags());
       blogDTO.setFirstpicture(blog.getFirstpicture());
       blogDTO.setAppreciation(blog.getAppreciation());
       blogDTO.setTagIds(blog.getTagIds());
       blogDTO.setUser(userMapper.selectByPrimaryKey(blog.getUserid()));
       blogDTO.setTags(getTags(blog.getTagIds()));
       blogDTO.setPublish(blog.getPublish());

        blogDTO.init();
       return blogDTO;
    }
    //显示所有的博客
    public PageInfo<Blog> list() {
      List<Blog>  blog = blogExtMapper.listBy();
      return new PageInfo<>(blog);
    }
    //这是博客首页根据 博客标题和内容的一个查询
    public PageInfo<BlogDTO> listBlogQuery(String query) {
        List<Blog>  blogs = blogExtMapper.listByQuery(query);
        List<BlogDTO> list = new ArrayList<>();
        for (Blog blog : blogs){
            BlogDTO blogDTO = new BlogDTO();
            BeanUtils.copyProperties(blog,blogDTO);
            Type type = typeMapper.selectByPrimaryKey(blog.getTypeid());
            blogDTO.setUser(userMapper.selectByPrimaryKey(blog.getUserid()));
            blogDTO.setType(type);
            list.add(blogDTO);
        }
        PageInfo<BlogDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
   }
   //通过tagids  获取每个tags
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
   //获取博客的详情
    public BlogDTO getAndConvert(Long id) {

        Blog blog1 =  new Blog();
        blog1.setId(id);
        blog1.setView(1l);
        blogExtMapper.updateView(blog1);
        Blog blog = getBlog(id);
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
   //首页中统计每个type类中  所含的文章
    public PageInfo<BlogDTO> getBlogByTypeId(Long id) {
       BlogExample blogExample = new BlogExample();
       blogExample.createCriteria().andTypeidEqualTo(id);
       List<Blog> blogs = blogMapper.selectByExample(blogExample);
       List<BlogDTO> blogDTOS = new ArrayList<>();
       for (Blog blog :blogs){
           BlogDTO blogDTO = new BlogDTO();
           BeanUtils.copyProperties(blog,blogDTO);
           Type type = typeMapper.selectByPrimaryKey(blog.getTypeid());
           blogDTO.setUser(userMapper.selectByPrimaryKey(blog.getUserid()));
           blogDTO.setType(type);
           blogDTOS.add(blogDTO);
       }
       PageInfo<BlogDTO> pageInfo =  new PageInfo<>(blogDTOS);
       return pageInfo;
    }
     //归档博客的显示
      public   Map<String,List<Blog>> archiveBlog(){
            List<String> years = blogExtMapper.stringListYear();
            Map<String,List<Blog>> map = new HashMap<>();
            for (String year : years ){
                map.put(year,blogExtMapper.listByYear(year));
            }
            return map;
    }
   //热门文章的显示
    public PageInfo<Blog> listByView() {
       List<Blog> blogs = blogExtMapper.list();
       PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
       return pageInfo;
    }
}
