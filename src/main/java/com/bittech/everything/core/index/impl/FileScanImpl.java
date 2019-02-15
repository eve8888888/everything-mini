package com.bittech.everything.core.index.impl;

import com.bittech.everything.config.EverythingMiniConfig;
import com.bittech.everything.core.index.FileScan;
import com.bittech.everything.core.interceptor.FileInterceptor;

import java.io.File;
import java.util.LinkedList;

/**
 * @Author: Eve
 * @Date: 2019/2/15 10:57
 * @Version 1.0
 */
public class FileScanImpl implements FileScan {

    EverythingMiniConfig config = EverythingMiniConfig.getInstance();
    private LinkedList<FileInterceptor> interceptors = new LinkedList<>();
    @Override
    public void index(String path) {
        File file = new File(path);
        if(file.isFile()){
            if(config.getExcludePath().contains(file.getPath())){
                return;
            }
        }else {
            if(config.getExcludePath().contains(path)){
                return;
            }else {
                File[] files = file.listFiles();
                if(files != null){
                    for (File f :
                            files) {
                        index(f.getAbsolutePath());
                    }
                }
            }

        }
        for (FileInterceptor interceptor : this.interceptors){
            interceptor.apply(file);
        }
    }
    @Override
    public void interceptor(FileInterceptor interceptor){
        this.interceptors.add(interceptor);
    }

}
