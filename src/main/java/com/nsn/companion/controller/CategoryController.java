package com.nsn.companion.controller;

import com.nsn.companion.entity.Category;
import com.nsn.companion.service.ICategoryService;
import com.nsn.companion.util.CommonResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private ICategoryService categoryService;

    @GetMapping("/")
    @ApiOperation("获取目录树")
    public CommonResp list() {
        CommonResp<List<Category>> resp = new CommonResp<>();
        List<Category> list = categoryService.list();
        resp.setContent(list);
        return resp;
    }

    @DeleteMapping("/{categoryid}")
    @ApiOperation("删除目录树")
    public CommonResp delete(@PathVariable String categoryid) {
        CommonResp<List<Category>> resp = new CommonResp<>();
        categoryService.delCategoryTree(categoryid);
        return resp;
    }

    @PatchMapping("/")
    @ApiOperation("更新目录树")
    public CommonResp update(@RequestBody List<Category> categoryList) {
        CommonResp<List<Category>> resp = new CommonResp<>();
        categoryService.updateCategoryTree(categoryList);
        return resp;
    }

    @PatchMapping("/{categoryid}/{title}")
    @ApiOperation("更新目录树")
    public CommonResp update(@PathVariable("categoryid") String categoryid, @PathVariable("title") String title) {
        CommonResp<List<Category>> resp = new CommonResp<>();
        Category category = new Category();
        category.setId(categoryid);
        category.setName(title);
        Boolean isSuccess = categoryService.updateById(category);
        resp.setSuccess(isSuccess);
        return resp;
    }

    @PostMapping("/")
    @ApiOperation("新建目录")
    public CommonResp insert(@RequestBody Category category) {
        CommonResp<Category> resp = new CommonResp<>();
        category.setName("无标题");
        category.setParentid("0");
        category.setSort(categoryService.getMaxSort("0"));
        Boolean isSuccess = categoryService.save(category);
        resp.setSuccess(isSuccess);
        resp.setContent(category);
        return resp;
    }
}

