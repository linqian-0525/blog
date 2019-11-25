package com.lq.blog.service;

import com.lq.blog.mapper.DetailsMapper;
import com.lq.blog.model.Details;
import com.lq.blog.model.DetailsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApproveService {
    @Autowired
    private DetailsMapper mapper;
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
}
