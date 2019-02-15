package com.bittech.everything.core.index;

import com.bittech.everything.core.dao.DataSourceFactory;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.dao.impl.FileIndexDaoImpl;
import com.bittech.everything.core.index.impl.FileScanImpl;
import com.bittech.everything.core.interceptor.FileInterceptor;
import com.bittech.everything.core.interceptor.impl.FileIndexInterceptor;
import com.bittech.everything.core.interceptor.impl.FilePrintInterceptor;
import com.bittech.everything.core.model.Thing;

/**
 * @Author: Eve
 * @Date: 2019/2/15 10:55
 * @Version 1.0
 */
public interface FileScan {

    /**
     * 遍历path
     * @param path
     */
    void index(String path);

    /**
     * 遍历拦截器
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);

    public static void main(String[] args) {
        DataSourceFactory.initDatabase();
        FileScan scan = new FileScanImpl();
        FileInterceptor print = new FilePrintInterceptor();
        scan.interceptor(print);
        FileInterceptor index = new FileIndexInterceptor(new FileIndexDaoImpl(DataSourceFactory.dataSource()));
        //scan.interceptor(index);
        scan.index("E:\\Document\\PDF");
    }
}
