package com.bittech.everything.core.index;

import com.bittech.everything.core.interceptor.FileInterceptor;

/**
 * @Author: Eve
 * @Date: 2019/2/15 10:55
 * @Version 1.0
 */
public interface FileScan {

    /**
     * 遍历path
     *
     * @param path
     */
    void index(String path);

    /**
     * 遍历拦截器
     *
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);
}
