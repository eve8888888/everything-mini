package com.bittech.everything.core.common;

import com.bittech.everything.core.model.FileType;
import com.bittech.everything.core.model.Thing;

import java.io.File;

/**
 * @Author: Eve
 * @Date: 2019/2/15 11:21
 * @Version 1.0
 */

/**
 * 辅助工具类
 * 将File对象转换成Thing对象
 */
public final class FileConvertThing {

    private FileConvertThing() {

    }

    /**
     * 将File对象转换成Thing对象
     * @param file
     * @return
     */
    public static Thing convert(File file){
        Thing thing = new Thing();
        thing.setName(file.getName());
        thing.setPath(file.getPath());
        thing.setDepth(computerFileDepth(file));
        thing.setFileType(computerFileType(file));
        return thing;
    }

    /**
     * 计算文件深度
     * @param file
     * @return dept
     */
    private static int computerFileDepth(File file){
        String[] segments = file.getAbsolutePath().split("\\\\");
        return segments.length;
    }

    /**
     * 根据文件名获取文件类型
     * @param file
     * @return
     */
    private static FileType computerFileType(File file){
        if(file.isDirectory()){
            return FileType.OTHER;
        }
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if(index != -1 && index < fileName.length() - 1){
            String extend = fileName.substring(index + 1);
            return FileType.lookup(extend);
        }else {
         return FileType.OTHER;
        }
    }
}
