package com.parrer.util;

import com.parrer.constant.ApiResponseCodeEnum;
import com.parrer.exception.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * @Description 断言器
 * @ClassName AssertUtil
 * @Author 120439
 */
public final class AssertUtil {
    private final static Logger logger = LoggerFactory.getLogger(AssertUtil.class);

    private AssertUtil() {
    }

    /**
     * @param object
     * @param template
     * @param params
     * @Description: 对象为null断言器
     * @return: void
     */
    public static void notNull(Object object, String template, Object... params) {
        if (null == object) {
            // 单独做params为null判断是为了防止params=null时template中含有占位字符导致StringUtil.formatByRegex
            // 方法抛MissingFormatArgumentException异常
            String msg = null == params ? template : StringUtil.formatByRegex(template, params);
            throw new NullException(msg);
        }
    }

    /**
     * @param object
     * @param template
     * @param params
     * @Description: empty断言器
     * @return: void
     */
    public static void notEmpty(Object object, String template, Object... params) {
        String message = StringUtil.formatByRegex(template, params);
        notNull(object, message);
        Class<?> clazz = object.getClass();
        if (String.class.isAssignableFrom(clazz)) {
            if (StringUtils.isEmpty((String) object)) {
                throw new EmptyStringException(message);
            }
        } else if (Collection.class.isAssignableFrom(clazz) && ((Collection) object).size() <= 0) {
            throw new EmptyCollectionException(message);
        } else if (Map.class.isAssignableFrom(clazz) && MapUtils.isEmpty((Map) object)) {
            throw new EmptyMapException(message);
        } else if (Object[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((Object[]) object)) {
            throw new EmptyArrayException(message);
        } else if (byte[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((byte[]) object)) {
            throw new EmptyArrayException(message);
        } else if (short[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((short[]) object)) {
            throw new EmptyArrayException(message);
        } else if (int[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((int[]) object)) {
            throw new EmptyArrayException(message);
        } else if (long[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((long[]) object)) {
            throw new EmptyArrayException(message);
        } else if (float[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((float[]) object)) {
            throw new EmptyArrayException(message);
        } else if (double[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((double[]) object)) {
            throw new EmptyArrayException(message);
        } else if (char[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((char[]) object)) {
            throw new EmptyArrayException(message);
        } else if (boolean[].class.isAssignableFrom(clazz) && ArrayUtils.isEmpty((boolean[]) object)) {
            throw new EmptyArrayException(message);
        }
    }


    /**
     * 断言真（兼容方法）
     *
     * @param boo
     * @param template
     * @param params
     */
    public static void isTrue(Boolean boo, String template, Object... params) {
        if (!boo) {
            String message = StringUtil.formatByRegex(template, params);
            throw new ServiceException(message);
        }
    }

    /**
     * 断言假（兼容方法）
     *
     * @param boo
     * @param template
     * @param params
     */
    public static void isFalse(Boolean boo, String template, Object... params) {
        isTrue(!boo, template, params);
    }

    /**
     * @param object
     * @param index
     * @param params
     * @Description: 对象为null断言器
     * @return: void
     */
    public static void notNullI18(Object object, Integer index, Object... params) {
        if (null == object) {
            String template = I18nUtil.resolveMessage(index);
            // 单独做params为null判断是为了防止params=null时template中含有占位字符导致StringUtil.formatByRegex
            // 方法抛MissingFormatArgumentException异常
            String msg = null == params ? template : StringUtil.formatByRegex(template, params);
            logger.error(getLogMessage(msg));
            throw new NullException(ApiResponseCodeEnum.SERVICE_ERROR.getValue(), getShowMessage(msg));
        }
    }

    /**
     * @param object
     * @param index
     * @param params
     * @Description: empty断言器
     * @return: void
     */
    public static void notEmptyBs(Object object, Integer index, Object... params) {
        String template = I18nUtil.resolveMessage(index);
        String message = StringUtil.formatByRegex(template, params);
        notEmpty(object, message);
    }

    /**
     * 断言真
     *
     * @param boo
     * @param index
     * @param params
     */
    public static void isTrueI18(Boolean boo, Integer index, Object... params) {
        if (!boo) {
            String template = I18nUtil.resolveMessage(index);
            String message = StringUtil.formatByRegex(template, params);
            throw new ServiceException(ApiResponseCodeEnum.SERVICE_ERROR.getValue(), getShowMessage(message));
        }
    }

    public static void isFalseI18(Boolean boo, Integer index, Object... params) {
        if (boo) {
            String template = I18nUtil.resolveMessage(index);
            String message = StringUtil.formatByRegex(template, params);
            throw new ServiceException(ApiResponseCodeEnum.SERVICE_ERROR.getValue(), getShowMessage(message));
        }
    }

    private static String getLogMessage(String msg) {
        String[] splits = msg.split("::");
        return splits.length > 1 ? splits[1] : splits[0];
    }

    private static String getShowMessage(String msg) {
        String[] splits = msg.split("::");
        return splits[0];
    }
}
