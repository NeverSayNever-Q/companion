package com.nsn.companion.controller;

import com.nsn.companion.entity.Note;
import com.nsn.companion.service.ICategoryService;
import com.nsn.companion.service.INoteService;
import com.nsn.companion.util.CommonResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiangqp
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Resource
    private ICategoryService categoryService;
    @Resource
    private INoteService noteService;

    @GetMapping("/{search}")
    @ApiOperation("根据关键字获取笔记列表")
    public CommonResp list(@PathVariable String search) {
        CommonResp<List<Note>> resp = new CommonResp<>();
        List<Note> list = noteService.getNoteBySearch(search);
        resp.setContent(list);
        return resp;
    }
}

