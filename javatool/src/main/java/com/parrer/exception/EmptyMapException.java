package com.parrer.exception;

import com.parrer.constant.ApiResponseCodeEnum;
import com.parrer.util.StringUtil;

/**
 * @Author parrer
 * @version 1.0
 * @description map为空异常类（Map==null||Map.size=0）
 * @since
 */
public class EmptyMapException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public EmptyMapException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyMapException(Throwable throwable) {
        super(throwable);
    }

    public EmptyMapException(Throwable throwable, String template, String... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyMapException(String template, String... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyMapException(String message, Throwable throwable) {
        super(throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyMapException(String code, String message) {
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
