package com.bittech.everything.core.model;

import lombok.Data;

/**
 * @Author: Eve
 * @Date: 2019/2/14 12:01
 * @Version 1.0
 */

@Data
public class Condition {

    private String name;

    private String fileType;

    private Integer limit;

    /**
     * 检索结果的文件信息depth排序规则
     * 默认是true->asc
     * false->desc
     */
    private Boolean orderByAsc;
}
