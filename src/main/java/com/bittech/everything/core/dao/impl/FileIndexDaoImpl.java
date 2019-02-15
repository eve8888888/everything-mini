package com.bittech.everything.core.dao.impl;

import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.FileType;
import com.bittech.everything.core.model.Thing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Eve
 * @Date: 2019/2/15 10:57
 * @Version 1.0
 */
public class FileIndexDaoImpl implements FileIndexDao {


    private final DataSource dataSource;

    public FileIndexDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Thing> search(Condition condition) {
        //TODO
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<Thing> list = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            //准备sql语句
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(" select name,path,depth,file_type from file_index ");
            //name匹配,前后模糊
            sqlBuilder.append(" where ")
                    .append(" name like '%")
                    .append(condition.getName())
                    .append("%' ");
            //根据条件中是否给出类型进行对类型的匹配
            if(condition.getFileType() != null){
                sqlBuilder.append(" and file_type = '")
                        .append(condition.getFileType().toUpperCase())
                        .append("' ");
            }
            //limit 和 order by 对查询结果进行限制和排序
            sqlBuilder.append(" order by depth ")
                    .append(condition.getOrderByAsc()?"asc":"desc")
                    .append(" limit ")
                    .append(condition.getLimit())
                    .append(" offset 0 ");

            pst = conn.prepareStatement(sqlBuilder.toString());
            rs = pst.executeQuery();
            while (rs.next()){
                Thing thing = new Thing();
                thing.setName(rs.getString("name"));
                thing.setPath(rs.getString("path"));
                thing.setDepth(rs.getInt("depth"));
                thing.setFileType(FileType.lookupByName(rs.getString("file_type")));
                list.add(thing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        releaseResources(pst,conn,rs);
        return list;
    }

    @Override
    public void insert(Thing thing) {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();

            String sql = "insert into file_index (name, path, depth, file_type) VALUES (?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1,thing.getName());
            pst.setString(2,thing.getPath());
            pst.setInt(3,thing.getDepth());
            pst.setString(4,thing.getFileType().name());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseResources(pst,conn,null);
        }

    }
    private void releaseResources(PreparedStatement pst, Connection conn, ResultSet rs){
        try {
            if(rs!= null){
                rs.close();
            }
            if(pst != null){
                pst.close();
            }
            if(conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        DataSourceFactory.initDatabase();
//        FileIndexDaoImpl fileIndexDao = new FileIndexDaoImpl(DataSourceFactory.dataSource());
//        Thing thing = new Thing();
//        thing.setName("java.txt");
//        thing.setPath("D:\\start\\Cross");
//        thing.setDepth(3);
//        thing.setFileType(FileType.DOC);
//        Thing thing2 = new Thing();
//        thing2.setName("java.exe");
//        thing2.setPath("D:\\start");
//        thing2.setDepth(2);
//        thing2.setFileType(FileType.BIN);
//        fileIndexDao.insert(thing);
//        fileIndexDao.insert(thing2);
//        Condition condition = new Condition();
//        condition.setName("java");
//        condition.setFileType("bin");
//        condition.setOrderByAsc(true);
//        condition.setLimit(5);
//        System.out.println(fileIndexDao.search(condition));
//    }
}
