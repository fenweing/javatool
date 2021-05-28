package com.parrer.exception;

import com.parrer.constant.ApiResponseCodeEnum;
import com.parrer.util.StringUtil;

/**
 * @author 120439
 * @version 1.0
 * @description 服务异常类
 * @since
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(Throwable throwable, String template, Object... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public ServiceException(String template, Object... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
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

