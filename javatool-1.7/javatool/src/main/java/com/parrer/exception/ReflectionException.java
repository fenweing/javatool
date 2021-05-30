package com.parrer.exception;

/**
 * @description: ReflectException
 * @author: fw
 * @time: 2019/11/4 18:53
 */
public class ReflectionException extends RuntimeException {
    private Integer code;

    public ReflectionException(String msg) {
        super(msg);
    }

    public ReflectionException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public ReflectionException(String msg, Integer code, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public ReflectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
