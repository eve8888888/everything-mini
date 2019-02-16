package com.bittech.everything.core.dao;

import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;

import java.util.List;

public interface FileIndexDao {

    /**
     * 根据condition条件进行数据库检索
     * @param condition
     * @return
     */
    List<Thing> search(Condition condition);

    /**
     * 插入数据thing
     * @param thing
     */
    void insert(Thing thing);

    /**
     * 删除数据thing
     * @param thing
     */
    void delete(Thing thing);
}
