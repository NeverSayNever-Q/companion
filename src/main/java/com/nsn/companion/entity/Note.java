package com.nsn.companion.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 笔记
 * </p>
 *
 * @author jiangqp
 * @since 2022-02-28
 */
@Getter
@Setter
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 笔记本id
     */
    private Long notebookid;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 排序
     */
    private Integer sort;


}
