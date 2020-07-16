package com.bianyi.item.service;

import com.bianyi.item.mapper.CategoryMapper;
import com.bianyi.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> getCategoryById(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categories = categoryMapper.select(category);
        return categories;
    }

    public List<String> SelectByIdList(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        ArrayList<String> names = new ArrayList<>();
        for (Category category : list) {
            names.add(category.getName());
        }
        return names;
    }

    public List<String> queryNamesByIds(List<Long> ids) {
        List<String> list=new ArrayList<>();
        for (Long id : ids) {
            Category category = categoryMapper.selectByPrimaryKey(id);
            list.add(category.getName());
        }

        return list;
    }
}
