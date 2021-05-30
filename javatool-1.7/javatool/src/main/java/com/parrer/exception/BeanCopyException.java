package com.parrer.exception;

/**
 * @description: BeanCopyException
 * @author: fw
 * @time: 2019/11/4 18:50
 */
public class BeanCopyException extends RuntimeException {
    private Integer code;

    public BeanCopyException(String msg) {
        super(msg);
    }

    public BeanCopyException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public BeanCopyException(String msg, Integer code, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

}
