package com.bittech.everything.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Eve
 * @Date: 2019/2/14 11:47
 * @Version 1.0
 */
public enum  FileType {
    ARCHIVE("zip","rar","7z"),
    BIN("exe", "sh", "jar","msi"),
    DOC("doc", "docx", "txt", "pdf"),
    IMG("png", "jpg","gif","ioc","bmp"),
    OTHER;

    private Set<String> extend = new HashSet<>();

    FileType(String... extend) {
        this.extend.addAll(Arrays.asList(extend));
    }

    /**
     * 根据文件拓展名获取文件类型
     *
     * @param extend
     * @return
     */
    public static FileType lookup(String extend) {
        for (FileType filetype :
                FileType.values()) {
            if (filetype.extend.contains(extend)) {
                return filetype;
            }
        }
        return FileType.OTHER;
    }

    /**
     * 根据文件类型名获取文件类型对象
     *
     * @param name
     * @return
     */
    public static FileType lookupByName(String name) {
        for (FileType filetype :
                FileType.values()) {
            if (name.equals(filetype.name())) {
                return filetype;
            }
        }
        return FileType.OTHER;
    }
}
