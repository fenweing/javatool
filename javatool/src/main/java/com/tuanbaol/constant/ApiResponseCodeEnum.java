package com.tuanbaol.constant;

/**
 * @author 120439
 * @version 1.0
 * @description api接口返回码枚举
 */
public enum ApiResponseCodeEnum {
    SUCCESS("0000", "请求成功"), VALIDATE_ERROR("0001", "校验错误"), SERVICE_ERROR("0002", "服务错误"), SYSTEM_ERROR("0003", "系统错误");
    private String value;
    private String name;

    ApiResponseCodeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


}
