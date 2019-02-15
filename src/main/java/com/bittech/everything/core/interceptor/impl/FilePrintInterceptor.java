package com.bittech.everything.core.interceptor.impl;

import com.bittech.everything.core.interceptor.FileInterceptor;

import java.io.File;

/**
 * @Author: Eve
 * @Date: 2019/2/15 12:08
 * @Version 1.0
 */
public class FilePrintInterceptor implements FileInterceptor {
    @Override
    public void apply(File file) {
        System.out.println(file.getAbsolutePath());
    }
}
