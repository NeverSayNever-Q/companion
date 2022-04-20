package com.nsn.companion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nsn.companion.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiangqp
 * @since 2022-03-01
 */
@Service
public interface ICategoryService extends IService<Category> {
    //更新目录树排序
    public Boolean updateCategoryTree(List<Category> gData);

    //删除目录和笔记
    public Boolean delCategoryTree(String categoryid);

    //新增目录：分区后者笔记
    public Integer getMaxSort(String parentid);

    //获取目录结构
    public List<Category> getCategoryByNotebookid(String notebookid);
}
