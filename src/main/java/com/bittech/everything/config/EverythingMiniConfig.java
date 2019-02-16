package com.bittech.everything.config;

import lombok.Getter;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Eve
 * @Date: 2019/2/15 11:32
 * @Version 1.0
 */
@Getter
public class EverythingMiniConfig {

    private static volatile EverythingMiniConfig config;

    /**
     * 建立索引的路径
     */
    private Set<String> includePath = new HashSet<>();

    /**
     * 排除索引的路径
     */
    private Set<String> excludePath = new HashSet<>();

    /**
     * H2数据库文件路径
     */
    private String h2IndexPath = System.getProperty("user.dir") + File.separator + "everythingmini";

    private EverythingMiniConfig(){

    }

    private void initDefaultPathsConfig(){
        //获取文件系统
        FileSystem fileSystem = FileSystems.getDefault();
        //遍历的目录
        Iterable<Path> iterator = fileSystem.getRootDirectories();
        iterator.forEach(path -> config.includePath.add(path.toString()));
        /**
         * 排除的目录
         * windows ： C:\Windows C:\Program Files (x86)
         * C:\Program Files  C:\ProgramData
         *
         * Linux: /tmp /etc
         */
        String osName = System.getProperty("os.name");
        if(osName.startsWith("Windows")){
            config.getExcludePath().add("C:\\Windows");
            config.getExcludePath().add("C:\\Program Files (x86)");
            config.getExcludePath().add("C:\\Program Files");
            config.getExcludePath().add("C:\\ProgramData");
        }else {
            config.getExcludePath().add("/tmp");
            config.getExcludePath().add("/etc");
            config.getExcludePath().add("/root");
        }
    }

    public static EverythingMiniConfig getInstance(){
        if(config == null){
            synchronized (EverythingMiniConfig.class){

                if(config == null){
                    config = new EverythingMiniConfig();
                    config.initDefaultPathsConfig();
                }
            }
        }
        return config;
    }
}
