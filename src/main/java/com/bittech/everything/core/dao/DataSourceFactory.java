package com.bittech.everything.core.dao;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @Author: Eve
 * @Date: 2019/2/14 12:20
 * @Version 1.0
 */
public class DataSourceFactory {

    private static volatile DruidDataSource dataSource;

    private DataSourceFactory() {

    }

    public static DataSource dataSource() {
        if (dataSource == null) {
            synchronized (DataSourceFactory.class) {
                if (dataSource == null) {
                    dataSource = new DruidDataSource();
                    dataSource.setDriverClassName("org.h2.Driver");
                    //采用的是H2的嵌入式数据库,数据库以本地文件的方式
                    //存储,只需要提供url接口.
                    //获取当前工作路径
                    String workDir = System.getProperty("user.dir");

                    //JDBC规范中关于H2:
                    //jdbc:h2:filepath->存储到本地文件
                    //jdbc:h2://ip:port/databaseName->存储到服务器
                    dataSource.setUrl("jdbc:h2:" + workDir + File.separator + "everything_mini");
                }
            }
        }
        return dataSource;
    }

    public static void initDatabase() {
        Connection conn;
        try {
            try(InputStream in = DataSourceFactory.class
                    .getClassLoader().getResourceAsStream("everything_mini.sql")) {
                if(in == null){
                    throw new RuntimeException("Not read init database script please check it");
                }
                StringBuilder sb = new StringBuilder();
                String line;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in))){
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }
                String sql = sb.toString();
                conn = dataSource().getConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();
                System.out.println("************** 创建表");
                conn.close();
                pst.close();

            }catch (IOException e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
