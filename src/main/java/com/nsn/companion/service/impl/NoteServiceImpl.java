package com.nsn.companion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsn.companion.entity.Category;
import com.nsn.companion.entity.Note;
import com.nsn.companion.mapper.NoteMapper;
import com.nsn.companion.service.ICategoryService;
import com.nsn.companion.service.INoteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 笔记 服务实现类
 * </p>
 *
 * @author jiangqp
 * @since 2022-02-28
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {
    @Resource
    private NoteMapper noteMapper;
    @Resource
    private ICategoryService categoryService;

    @Override
    public List<Note> getNotesByNoteBookID(String noteBookID){
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notebookid", noteBookID).orderByAsc("sort");
        return noteMapper.selectList(queryWrapper );
    }

    @Override
    public Integer getMaxSort(String noteBookID){
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notebookid", noteBookID).orderByAsc("sort");
        List<Note> noteList = noteMapper.selectList(queryWrapper );
        if(noteList.size() == 0){
            return 1;
        }
        else{
            return noteList.get(noteList.size()-1).getSort() + 1;
        }
    }

    @Override
    public List<Note> getNoteBySearch(String search){
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", search).or().like("content", search);
        List<Note> noteList = noteMapper.selectList(queryWrapper);
        return noteList;
    }

    @Override
    public List<String> getRoutes(String noteid){
        List<String> categoryTitleList = new ArrayList<>();
        //1、取noteBookID
        Note note = noteMapper.selectById(noteid);
        String noteBookID = note.getNotebookid();
        String title = note.getTitle();
        //2、取目录结构
        List<Category> categoryList = categoryService.getCategoryByNotebookid(noteBookID);
        categoryList.forEach((category) -> {categoryTitleList.add(category.getName());});

        return categoryTitleList;
    }
}
