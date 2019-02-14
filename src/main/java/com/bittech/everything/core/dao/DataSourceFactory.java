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
        initDatabase();
    }

    public static DataSource dataSource() {
        if (dataSource == null) {
            synchronized (DataSourceFactory.class) {
                if (dataSource == null) {
                    dataSource = new DruidDataSource();
                    dataSource.setDriverClassName("org.h2.Driver");
                    //采用的是H2的嵌入式数据库,数据库以本地文件的方式
                    //存储,只需要提供url接口.
                    String workDir = System.getProperty("user.dir");
                    dataSource.setUrl("jdbc:h2:" + workDir + File.separator + "everything_mini");
                }
            }
        }
        return dataSource;
    }

    public static void initDatabase() {
        Connection conn = null;
        //1.获取数据源
        try {
            conn = dataSource().getConnection();
            //2.获取sql语句
            InputStream in = DataSourceFactory.class
                    .getClassLoader().getResourceAsStream("everything_mini.sql");
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String sql = sb.toString();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        DataSourceFactory df = new DataSourceFactory();
        //System.out.println(DataSourceFactory.dataSource());
    }
}
