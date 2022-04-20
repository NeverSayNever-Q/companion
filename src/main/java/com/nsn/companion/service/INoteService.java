package com.nsn.companion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nsn.companion.entity.Note;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 笔记 服务类
 * </p>
 *
 * @author jiangqp
 * @since 2022-02-28
 */
@Service
public interface INoteService extends IService<Note> {
    //根据noteBookID获取笔记
    public List<Note> getNotesByNoteBookID(String noteBookID);

    //根据noteBookID获取当前最大排序
    public Integer getMaxSort(String noteBookID);

    //根据标题、或内容检索
    public List<Note> getNoteBySearch(String search);

    //取目录结构
    public List<String> getRoutes(String noteid);
}


