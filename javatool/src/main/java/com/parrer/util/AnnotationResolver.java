package com.parrer.util;

import com.parrer.exception.ServiceException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ValidationException;
import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * @description: AnnotationResolver
 * @author: parrer
 * @date: 2019/12/9 19:54
 * @version: 1.0.0
 */
public class AnnotationResolver {
    private final static Logger logger = LoggerFactory.getLogger(AssertUtil.class);
    private final static Class[] ANNOATATIONS = {Null.class, NotNull.class, NotEmpty.class, NotBlank.class, Min.class, Max.class};

    public static boolean resolve2Boolean(Object target) {
        if (null == target) {
            return true;
        }
        Class clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtils.isEmpty(fields)) {
            return true;
        }
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (ArrayUtils.isEmpty(annotations)) {
                continue;
            }
            for (Annotation annotation : annotations) {
                for (Class annoClazz : ANNOATATIONS) {
                    if (annotation.annotationType() == annoClazz) {
                        String methodName = annoClazz.getSimpleName().toLowerCase();
                        Method method = getMethod(methodName);
                        field.setAccessible(true);
                        boolean legal;
                        try {
                            legal = (boolean) method.invoke(null, target, field, annotation, false);
                        } catch (Exception e) {
                            throw new ServiceException(e, "解析注解时，反射执行解析方法失败，方法名-【{}】", methodName);
                        }
                        if (!legal) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static boolean notnull(Object target, Field field, Annotation annotation, Boolean exception) {
        NotNull notNull = (NotNull) annotation;
        Object value;
        try {
            value = field.get(target);
        } catch (IllegalAccessException e) {
            throw new ServiceException(e, "反射获取字段值失败，字段名称-【{}】", field.getName());
        }
        if (null == value) {
            if (exception) {
                throw new ValidationException(notNull.message());
            }
            logger.error(getMessage(target, notNull.message()) + "不能为null");
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否为blank，等价于stringObject==null||stingObject.length==0||
     * for(int i = 0; i < strLen; ++i) {
     * if (!Character.isWhitespace(cs.charAt(i))) {
     * return true;
     * }
     * }
     * 只对String类型字段判断
     *
     * @param target
     * @param field
     * @param annotation
     * @param exception
     * @return
     */
    private static boolean notblank(Object target, Field field, Annotation annotation, Boolean exception) {
        NotBlank notBlank = (NotBlank) annotation;
        Object value;
        try {
            value = field.get(target);
        } catch (IllegalAccessException e) {
            throw new ServiceException(e, "反射获取字段值失败，字段名称-【{}】", field.getName());
        }
        if (field.getType() == String.class && StringUtils.isBlank((String) value)) {
            if (exception) {
                throw new ValidationException(notBlank.message());
            }
            logger.error(getMessage(target, notBlank.message()) + "不能为blank");
            return false;
        }
        return true;
    }

    /**
     * 判断字段为null或为空，目前判断为空只支持：String,? implements Collection,? implements Map
     *
     * @param target
     * @param field
     * @param annotation
     * @param exception
     * @return
     */
    private static boolean notempty(Object target, Field field, Annotation annotation, Boolean exception) {
        NotEmpty notEmpty = (NotEmpty) annotation;
        Object value;
        try {
            value = field.get(target);
        } catch (IllegalAccessException e) {
            throw new ServiceException(e, "反射获取字段值失败，字段名称-【{}】", field.getName());
        }
        Class<?> clazz = field.getType();
        boolean invalid = false;
        if (null != value) {
            if (String.class.isAssignableFrom(clazz)) {
                invalid = StringUtils.isEmpty((String) value);
            } else if (Collection.class.isAssignableFrom(clazz)) {
                invalid = ((Collection) value).size() <= 0;
            } else if (Map.class.isAssignableFrom(clazz)) {
                invalid = MapUtils.isEmpty((Map) value);
            }
        } else {
            invalid = true;
        }
        if (invalid) {
            if (exception) {
                throw new ValidationException(notEmpty.message());
            }
            logger.error(getMessage(target, notEmpty.message()) + "不能为empty");
            return false;
        }
        return true;
    }

    private static String getMessage(Object target, String fieldName) {
        return target.getClass().getSimpleName() + "中参数【" + fieldName + "】";
    }

    private static Method getMethod(String name) {
        try {
            return AnnotationResolver.class.getDeclaredMethod(name, Object.class, Field.class, Annotation.class, Boolean.class);
        } catch (NoSuchMethodException e) {
            throw new ServiceException(e, "解析注解时，未找到对应解析方法，注解名称-【{}】", name);
        }
    }
}
