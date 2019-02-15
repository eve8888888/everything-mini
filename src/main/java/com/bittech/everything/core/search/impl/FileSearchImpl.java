package com.bittech.everything.core.search.impl;

import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;
import com.bittech.everything.core.search.FileSearch;

import java.util.List;

/**
 * @Author: Eve
 * @Date: 2019/2/15 9:35
 * @Version 1.0
 */
public class FileSearchImpl implements FileSearch {
    private final FileIndexDao fileIndexDao;

    public FileSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public List<Thing> search(Condition condition) {
        //数据库处理逻辑
        return fileIndexDao.search(condition);
    }
}
