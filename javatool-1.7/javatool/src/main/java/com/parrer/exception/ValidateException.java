package com.parrer.exception;

import com.parrer.constant.ApiResponseCodeEnum;
import com.parrer.util.StringUtil;

/**
 * 参数校验异常
 *
 * @Author parrer
 * @date 2019年7月31日 上午10:58:05
 */
public class ValidateException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public ValidateException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.VALIDATE_ERROR.getValue();
    }

    public ValidateException(Throwable throwable) {
        super(throwable);
    }

    public ValidateException(Throwable throwable, String template, String... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.VALIDATE_ERROR.getValue();
    }

    public ValidateException(String template, String... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.VALIDATE_ERROR.getValue();
    }

    public ValidateException(String message, Throwable throwable) {
        super(throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.VALIDATE_ERROR.getValue();
    }

}
