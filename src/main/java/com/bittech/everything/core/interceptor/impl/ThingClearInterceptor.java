package com.bittech.everything.core.interceptor.impl;

import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.interceptor.ThingInterceptor;
import com.bittech.everything.core.model.Thing;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author: Eve
 * @Date: 2019/2/16 15:22
 * @Version 1.0
 */
public class ThingClearInterceptor implements ThingInterceptor,Runnable {

    private Queue<Thing> queue = new ArrayBlockingQueue<>(1024);
    private final FileIndexDao fileIndexDao;

    public ThingClearInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(Thing thing) {
        this.queue.add(thing);
    }

    @Override
    public void run() {
        while (true){
            Thing thing = this.queue.poll();
            if(thing != null){
                fileIndexDao.delete(thing);
            }
            //批量删除?
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
