package com.nsn.companion.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nsn.companion.entity.Note;
import com.nsn.companion.service.impl.NoteServiceImpl;
import com.nsn.companion.util.CommonResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    private NoteServiceImpl noteService;

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
    public CommonResp update(@RequestBody Map<String,String> note){
        CommonResp resp = new CommonResp<>();
        if(!note.isEmpty() && (note.containsKey("id"))){
            String id = note.get("id");
            //自动匹配传参与实体类字段
            UpdateWrapper<Note> noteUpdateWrapper = new UpdateWrapper<>();
            Field[] fields = Note.class.getDeclaredFields();
            List<Field> fieldList = Arrays.asList(fields);
            note.forEach((k, v) ->{
                fieldList.forEach((field) ->{
                    if(k != "id" && field.getName() == k){
                        noteUpdateWrapper.set(k, v);
                    }
                });
            });
            //更新
            if(!noteUpdateWrapper.getSqlSet().isEmpty()){
                noteUpdateWrapper.eq("id", id);
                Boolean isSuccess = noteService.update(null, noteUpdateWrapper);
                resp.setSuccess(isSuccess);
            }
        }
        else{
            resp.setSuccess(false);
        }

        return resp;
    }
}