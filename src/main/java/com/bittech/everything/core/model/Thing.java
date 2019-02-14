package com.bittech.everything.core.model;

import lombok.Data;

/**
 * @Author: Eve
 * @Date: 2019/2/14 11:57
 * @Version 1.0
 */
@Data //getter setter toString生成
public class Thing {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件路径深度
     */
    private Integer depth;

    /**
     * 文件类型
     */
    private FileType fileType;
}
