package com.lq.blog.dto;

import com.lq.blog.model.Comment;
import com.lq.blog.model.Tag;
import com.lq.blog.model.Type;
import com.lq.blog.model.User;
import lombok.Data;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BlogDTO {
    private Long id;
    private String title;
    private String content;
    private String flags;
    private String firstpicture;
    private String tagIds;
    private String description;
    private Long view;
    private boolean appreciation;
    private boolean publish;
    private boolean sharestatement;
    private boolean commentabled;
    private boolean recommend;
    private Date createtime;
    private Date updatetime;
    private Type type;
    private Long typeid;
    private List<Tag> tags = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private User user;
    public void init(){
        this.tagIds = tagsToIds(this.getTags());
    }
    private String tagsToIds(List<Tag> tags){
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }
}
