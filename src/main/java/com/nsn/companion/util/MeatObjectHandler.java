package com.nsn.companion.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: jiangqp
 * Date: 2022/09/08
 */
@Component
public class MeatObjectHandler implements MetaObjectHandler {


    @Override
    //使用MybatisPlus实现添加操作时，该方法会执行
    public void insertFill(MetaObject metaObject) {
        //参数：需要设置的属性；设置的 时间；元数据
        this.strictInsertFill(metaObject, "createtime", LocalDateTime.class, LocalDateTime.now());

    }

    @Override
    //使用MybatisPlus实现修改操作时，该方法会执行
    public void updateFill(MetaObject metaObject) {
    }
}
