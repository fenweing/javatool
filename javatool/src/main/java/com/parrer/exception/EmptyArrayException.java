package com.parrer.exception;

import com.parrer.constant.ApiResponseCodeEnum;
import com.parrer.util.StringUtil;

/**
 * @author fw
 * @version 1.0
 * @description 数组为null或array.length=0异常类
 * @since
 */
public class EmptyArrayException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public EmptyArrayException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyArrayException(Throwable throwable) {
        super(throwable);
    }

    public EmptyArrayException(Throwable throwable, String template, String... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyArrayException(String template, String... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyArrayException(String message, Throwable throwable) {
        super(throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyArrayException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
