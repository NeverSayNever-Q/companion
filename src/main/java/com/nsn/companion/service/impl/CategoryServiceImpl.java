package com.nsn.companion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsn.companion.entity.Category;
import com.nsn.companion.entity.Note;
import com.nsn.companion.mapper.CategoryMapper;
import com.nsn.companion.mapper.NoteMapper;
import com.nsn.companion.service.ICategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jiangqp
 * @since 2022-03-01
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private NoteMapper noteMapper;


    @Override
    public Boolean updateCategoryTree(List<Category> gData) {
        gData.stream().forEach(category -> {
            UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", category.getId());
            Category category2Update = new Category();
            category2Update.setParentid(category.getParentid());
            category2Update.setSort(category.getSort());
            categoryMapper.update(category2Update, updateWrapper);
        });
        return true;
    }

    @Override
    public Boolean delCategoryTree(String categoryid) {
        //1、删除目录自身
        categoryMapper.deleteById(categoryid);
        //2、删除子目录
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parentid", categoryid);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        if (categoryList.size() > 0) {
            categoryList.forEach(category -> {
                delCategoryTree(category.getId());
            });
        }
        //3、删除对应笔记
        QueryWrapper<Note> delWrapper = new QueryWrapper<>();
        delWrapper.eq("notebookid", categoryid);
        noteMapper.delete(delWrapper);

        return true;
    }

    @Override
    public Integer getMaxSort(String parentid) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parentid", parentid).orderByAsc("sort");
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        if (categoryList.size() == 0) {
            return 1;
        } else {
            return categoryList.get(categoryList.size() - 1).getSort() + 1;
        }
    }

    @Override
    public List<Category> getCategoryByNotebookid(String notebookid) {
        List<Category> categoryList = new ArrayList<Category>();
        Category category = categoryMapper.selectById(notebookid);
        categoryList.add(category);
        if (!category.getParentid().equals("0"))
            categoryList.addAll(getCategoryByNotebookid(category.getParentid()));
        Collections.reverse(categoryList);
        return categoryList;
    }
}