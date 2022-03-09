package com.nsn.companion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nsn.companion.entity.Note;

import java.util.List;

/**
 * <p>
 * 笔记 服务类
 * </p>
 *
 * @author jiangqp
 * @since 2022-02-28
 */
public interface INoteService extends IService<Note> {

    //根据noteBookID获取笔记
    public List<Note> getNotesByNoteBookID(String noteBookID);
}
