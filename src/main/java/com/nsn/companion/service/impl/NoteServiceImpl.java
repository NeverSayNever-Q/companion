package com.nsn.companion.service.impl;

import com.nsn.companion.entity.Note;
import com.nsn.companion.mapper.NoteMapper;
import com.nsn.companion.service.INoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 笔记 服务实现类
 * </p>
 *
 * @author jiangqp
 * @since 2022-02-08
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {

}
