package com.parrer.exception;

import com.parrer.constant.ApiResponseCodeEnum;
import com.parrer.util.StringUtil;

/**
 * @Author parrer
 * @version 1.0
 * @description 转换异常类
 * @since
 */
public class ParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public ParseException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public ParseException(Throwable throwable) {
        super(throwable);
    }

    public ParseException(Throwable throwable, String template, String... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public ParseException(String template, String... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public ParseException(String message, Throwable throwable) {
        super(throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public ParseException(String code, String message) {
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
