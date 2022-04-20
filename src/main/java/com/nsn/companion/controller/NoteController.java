package com.nsn.companion.controller;

import com.nsn.companion.entity.Note;
import com.nsn.companion.service.INoteService;
import com.nsn.companion.util.CommonResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 笔记 前端控制器
 * </p>
 *
 * @author jiangqp
 * @since 2022-02-28
 */
@RestController
@RequestMapping("/note")
public class NoteController {
    @Resource
    private INoteService noteService;

    @GetMapping("/")
    @ApiOperation("通过notebookid获取笔记列表")
    public CommonResp list(@RequestParam("noteBookId") String noteBookId) {
        CommonResp<List<Note>> resp = new CommonResp<>();
        List<Note> list = noteService.getNotesByNoteBookID(noteBookId);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/{noteId}")
    @ApiOperation("获取笔记")
    public CommonResp note(@PathVariable String noteId) {
        CommonResp<Note> resp = new CommonResp<>();
        Note note = noteService.getById(noteId);
        resp.setContent(note);
        return resp;
    }

    @PatchMapping("/")
    @ApiOperation("更新笔记")
    public CommonResp update(@RequestBody List<Note> noteList){
        CommonResp resp = new CommonResp<>();
        noteService.updateBatchById(noteList);
        return resp;
    }

    @DeleteMapping("/{noteId}")
    @ApiOperation("删除笔记")
    public CommonResp delete(@PathVariable String noteId) {
        CommonResp resp = new CommonResp<>();
        Boolean isSucess = noteService.removeById(noteId);
        resp.setSuccess(isSucess);
        return resp;
    }

    @PostMapping("/")
    @ApiOperation("新增笔记")
    public CommonResp Insert(@RequestBody  Note note) {
        CommonResp<Note> resp = new CommonResp<>();
        note.setTitle("无标题");
        note.setContent("");
        note.setSort(noteService.getMaxSort(note.getNotebookid()));
        Boolean isSuccess = noteService.save(note);
        resp.setContent(note);
        resp.setSuccess(isSuccess);
        return resp;
    }

    @GetMapping("/category/{noteId}")
    @ApiOperation("获取笔记目录结构")
    public CommonResp categoies(@PathVariable String noteId) {
        CommonResp<List<String>> resp = new CommonResp<>();
        List<String> routes = noteService.getRoutes(noteId);
        resp.setContent(routes);
        return resp;
    }
}