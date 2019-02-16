package com.bittech.everything.core;

import com.bittech.everything.config.EverythingMiniConfig;
import com.bittech.everything.core.dao.DataSourceFactory;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.dao.impl.FileIndexDaoImpl;
import com.bittech.everything.core.index.FileScan;
import com.bittech.everything.core.index.impl.FileScanImpl;
import com.bittech.everything.core.interceptor.ThingInterceptor;
import com.bittech.everything.core.interceptor.impl.FileIndexInterceptor;
import com.bittech.everything.core.interceptor.impl.ThingClearInterceptor;
import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;
import com.bittech.everything.core.search.FileSearch;
import com.bittech.everything.core.search.impl.FileSearchImpl;

import javax.sql.DataSource;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: Eve
 * @Date: 2019/2/16 14:56
 * @Version 1.0
 */
public class EverythingMiniManager {

    private static volatile EverythingMiniManager manager;
    private FileSearch fileSearch;
    private FileScan fileScan;


    /**
     * 清理删除的文件
     */
    private ThingClearInterceptor thingClearInterceptor;
    private Thread backgroundClearThread;
    private ExecutorService executorService;
    private AtomicBoolean backgroundClearThreadStatus = new AtomicBoolean(false);

    private EverythingMiniManager(){
        initComponent();
    }
    public static EverythingMiniManager getInstance(){
        if(manager == null){
            synchronized (EverythingMiniManager.class){
                if(manager == null){
                    manager =  new EverythingMiniManager();
                }
            }
        }
        return manager;
    }

    private void initComponent(){

        //数据源
        DataSource dataSource = DataSourceFactory.dataSource();
        //检查数据库
        checkDatabase();
        //业务层的对象
        FileIndexDao fileIndexDao = new FileIndexDaoImpl(dataSource);
        this.fileScan = new FileScanImpl();
        this.fileSearch = new FileSearchImpl(fileIndexDao);
        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));
        this.thingClearInterceptor = new ThingClearInterceptor(fileIndexDao);
        this.thingClearInterceptor = new ThingClearInterceptor(fileIndexDao);
        this.backgroundClearThread = new Thread(thingClearInterceptor);
        backgroundClearThread.setName("Clear-Thread");
        backgroundClearThread.setDaemon(true);


    }
    private void checkDatabase(){
        String fileName = EverythingMiniConfig.getInstance().getH2IndexPath()+".mv.db";
        File file = new File(fileName);
        if(file.isFile() && !file.exists()){
            DataSourceFactory.initDatabase();
        }
    }

    public List<Thing> search(Condition condition){
        return this.fileSearch.search(condition).stream().filter(thing -> {
            String path = thing.getPath();
            File f = new File(path);
            boolean flag = f.exists();
            if(!flag){
                //做删除
                //TODO
                thingClearInterceptor.apply(thing);
            }
            return flag;
        }).collect(Collectors.toList());
    }
    public void buildIndex(){
        Set<String> directories =
                EverythingMiniConfig.getInstance().getIncludePath();
        if(this.executorService == null){
            this.executorService =
                    Executors.newFixedThreadPool(directories.size(), new ThreadFactory() {
                private final AtomicInteger threadId = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Thread-Scan-"+threadId.getAndIncrement());
                    return thread;
                }
            });
        }
        CountDownLatch countDownLatch = new CountDownLatch(directories.size());
        System.out.println("Build index start...");
        for(String path : directories){
            this.executorService.submit(() -> {
                EverythingMiniManager.this.fileScan.index(path);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Build index complete...");

    }

    /**
     * 启动清理线程
     */
    public void startBackgroundClearThread() {
        if (this.backgroundClearThreadStatus.compareAndSet(false,true)){
            this.backgroundClearThread.start();
        }else {
            System.out.println("Can not repeat start BackgroundClearThread");
        }
    }
}
