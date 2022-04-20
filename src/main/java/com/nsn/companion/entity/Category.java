package com.nsn.companion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author jiangqp
 * @since 2022-03-01
 */
@Getter
@Setter
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类别ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 父类别ID
     */
    private String parentid;

    /**
     * 名称
     */
    private String name;

    /**
     * 0：分区；1：笔记本
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;


}
