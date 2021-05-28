/*
 * @(#)SpringContextUtil.java 2018-4-8下午04:20:04 ebcp-commons Copyright 2018
 * Thuisoft, Inc. All rights reserved. THUNISOFT PROPRIETARY/CONFIDENTIAL. Use
 * is subject to license terms.
 */
package com.parrer.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * SpringContextUtil
 *
 * @author fw
 * @version 1.0
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        return (T) applicationContext.getBeansOfType(clazz);
    }

    public static Map getBeansOfType(Class type) {
        return applicationContext.getBeansOfType(type);
    }

    public static <T> List<T> getBeanListOfType(Class<T> type) {
        List<T> callbacks = new ArrayList<T>();
        Map beans = getBeansOfType(type);
        Iterator itor = beans.entrySet().iterator();
        while (itor.hasNext()) {
            Entry entry = (Entry) itor.next();
            callbacks.add((T) entry.getValue());
        }
        return callbacks;
    }

}
