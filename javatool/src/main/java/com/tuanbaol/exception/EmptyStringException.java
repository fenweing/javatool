package com.tuanbaol.exception;

import com.tuanbaol.constant.ApiResponseCodeEnum;
import com.tuanbaol.util.StringUtil;

/**
 * @author fw
 * @version 1.0
 * @description 字符串为空异常类（string==null||String.length=0）
 * @since
 */
public class EmptyStringException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public EmptyStringException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyStringException(Throwable throwable) {
        super(throwable);
    }

    public EmptyStringException(Throwable throwable, String template, String... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyStringException(String template, String... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyStringException(String message, Throwable throwable) {
        super(throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyStringException(String code, String message) {
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
