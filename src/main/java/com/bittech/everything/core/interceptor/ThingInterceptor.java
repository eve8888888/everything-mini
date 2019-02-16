package com.bittech.everything.core.interceptor;

import com.bittech.everything.core.model.Thing;

/**
 * @Author: Eve
 * @Date: 2019/2/16 15:21
 * @Version 1.0
 */

@FunctionalInterface
public interface ThingInterceptor {

    void apply(Thing thing);
}
