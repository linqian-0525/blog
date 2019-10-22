package com.lq.blog.service;

import com.github.pagehelper.PageInfo;
import com.lq.blog.Exception.NotFoundException;
import com.lq.blog.mapper.TypeExtMapper;
import com.lq.blog.mapper.TypeMapper;
import com.lq.blog.model.Type;
import com.lq.blog.model.TypeExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private TypeExtMapper typeExtMapper;
    @Transactional
   public int save(Type type){
        Type type1  = new Type();
        type1.setName(type.getName());
        return typeMapper.insert(type1);
    }
    @Transactional
    public Type getType(Long id){
        return typeMapper.selectByPrimaryKey(id);
    }
    public Type getTypeByName(String name){
        Type type = typeExtMapper.findByName(name);
        return type;
    }
    @Transactional
    public PageInfo<Type> listType(){

        List<Type> list = typeExtMapper.list();
        PageInfo<Type> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
    @Transactional
    public int  updateType(Long id, Type type){
        Type db = typeMapper.selectByPrimaryKey(id);
        if (db == null){
            throw new NotFoundException("不存在该类型");
        }
        Type update = new Type();
        update.setName(type.getName());
        TypeExample typeExample = new TypeExample();
        typeExample.createCriteria().andIdEqualTo(id);
        int i =  typeMapper.updateByExampleSelective(update,typeExample);
      return i;
    }
    @Transactional
    public void deleteType(Long id){
       typeMapper.deleteByPrimaryKey(id);
    }
}
