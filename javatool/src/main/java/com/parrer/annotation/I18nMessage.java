package com.parrer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: I18nMessage
 * @author: 120439
 * @date: 2019/12/24 14:51
 * @version: 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface I18nMessage {
    String[] zh() default "";

    String[] en() default "";

    String[] ja() default "";
}
