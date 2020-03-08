package com.tuanbaol.exception;

import com.tuanbaol.constant.ApiResponseCodeEnum;
import com.tuanbaol.util.StringUtil;

/**
 * @author fw
 * @version 1.0
 * @description 集合为null或Collection.size=0异常类
 * @since
 */
public class EmptyCollectionException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public EmptyCollectionException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyCollectionException(Throwable throwable) {
        super(throwable);
    }

    public EmptyCollectionException(Throwable throwable, String template, String... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyCollectionException(String template, String... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyCollectionException(String message, Throwable throwable) {
        super(throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.SERVICE_ERROR.getValue();
    }

    public EmptyCollectionException(String code, String message) {
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
