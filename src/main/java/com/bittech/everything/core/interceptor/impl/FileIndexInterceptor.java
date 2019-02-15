package com.bittech.everything.core.interceptor.impl;

import com.bittech.everything.core.common.FileConvertThing;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.interceptor.FileInterceptor;
import com.bittech.everything.core.model.Thing;

import java.io.File;

/**
 * @Author: Eve
 * @Date: 2019/2/15 15:59
 * @Version 1.0
 */
public class FileIndexInterceptor implements FileInterceptor {

    private final FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(File file) {
        Thing thing = FileConvertThing.convert(file);
        System.out.println("Thing ==>"+thing);
        fileIndexDao.insert(thing);
    }
}
