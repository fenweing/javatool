package com.tuanbaol.util;

import com.tuanbaol.annotation.I18nMessage;
import com.tuanbaol.spring.RequestUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @description: I18nUtil
 * @author: 120439
 * @date: 2019/12/24 14:35
 * @version: 1.0.0
 */
public final class I18nUtil {
    private static final Logger logger = LoggerFactory.getLogger(I18nUtil.class);
    private final static Locale DEFAULT_LOCALE = Locale.CHINA;
    private final static String DEFAULT_MESSAGE_ZH = "系统处理失败";
    private final static String DEFAULT_MESSAGE_EN = "system processing failed";

    public static String resolveMessage(Integer index) {
        AssertUtil.notNull(index, "解析国际化异常信息，消息标签为空！");
        I18nMessage i18nMessage = getAnnotation();
        if (null == i18nMessage) {
            return getDefaultMessage();
        }
        String lang = getLocale().getLanguage();
        if (lang.equalsIgnoreCase(Locale.CHINA.getLanguage())) {
            return i18nMessage.zh()[index];
        }
        if (lang.equalsIgnoreCase(Locale.ENGLISH.getLanguage())) {
            return i18nMessage.en()[index];
        }
        if (lang.equalsIgnoreCase(Locale.JAPANESE.getLanguage())) {
            return i18nMessage.ja()[index];
        }
        return i18nMessage.en()[index];
    }

    private static String getDefaultMessage() {
        Locale locale = getLocale();
        if (locale.getLanguage().equalsIgnoreCase(DEFAULT_LOCALE.getLanguage())) {
            return DEFAULT_MESSAGE_ZH;
        } else {
            return DEFAULT_MESSAGE_EN;
        }
    }

    private static Locale getLocale() {
        Locale locale = null;
        try {
            locale = RequestUtil.getLocale();
        } catch (Exception e) {
            logger.error("获取当前请求local失败，使用默认值");
        }
        if (null == locale || StringUtils.isBlank(locale.getLanguage())) {
            return DEFAULT_LOCALE;
        }
        return locale;
    }

    public static I18nMessage getAnnotation() {
        Method callingMethod = LogUtil.getCallingMethod(5);
        if (null == callingMethod) {
            return null;
        }
        Annotation[] annotations = callingMethod.getDeclaredAnnotations();
        if (ArrayUtils.isEmpty(annotations)) {
            return null;
        }
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == I18nMessage.class) {
                return (I18nMessage) annotation;
            }
        }
        return null;
    }

}
