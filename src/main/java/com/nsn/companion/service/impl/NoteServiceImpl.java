package com.nsn.companion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsn.companion.entity.Note;
import com.nsn.companion.mapper.NoteMapper;
import com.nsn.companion.service.INoteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public List<Note> getNotesByNoteBookID(String noteBookID){
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notebookid", noteBookID);
        return noteMapper.selectList(queryWrapper );
    }
}
