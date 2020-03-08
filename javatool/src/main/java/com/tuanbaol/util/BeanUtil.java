package com.tuanbaol.util;


import com.tuanbaol.exception.ReflectionException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: BeanUtils
 * @author: 120439
 * @time: 2019/11/4 18:08
 */
public class BeanUtil {
    public static <S, T> T copy(S src, T target) {
        BeanUtils.copyProperties(src, target);
        return target;
    }

    public static <S, T> T copy(S src, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReflectionException("Initial instance failed,calss type-【" + clazz + "】", e);
        }
        BeanUtils.copyProperties(src, t);
        return t;
    }

    public static <S, T> List<T> copyList(List<S> sList, Class<T> clazz) {
        List<T> tList = new ArrayList<>();
        for (S s : sList) {
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ReflectionException("Initial instance failed,calss type-【" + clazz + "】", e);
            }
            BeanUtils.copyProperties(s, t);
            tList.add(t);
        }
        return tList;
    }

}
