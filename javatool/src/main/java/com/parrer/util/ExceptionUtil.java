package com.parrer.util;

import com.parrer.constant.ApiResponseCodeEnum;
import com.parrer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: ExceptionUtil
 * @author: 120439
 * @date: 2019/12/24 15:47
 * @version: 1.0.0
 */
public class ExceptionUtil {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    public static void doThrow(Integer index, Object... params) {
        AssertUtil.notNull(index, "异常标签不能为空！");
        String message = I18nUtil.resolveMessage(index);
        message = StringUtil.formatByRegex(message, params);
        logger.error(getLogMessage(message));
        throw new ServiceException(ApiResponseCodeEnum.SERVICE_ERROR.getValue(), getShowMessage(message));
    }

    /**
     * @param cause
     * @param index
     * @param params
     */
    public static void doThrow(Throwable cause, Integer index, Object... params) {
        AssertUtil.notNull(index, "异常标签不能为空！");
        String message = I18nUtil.resolveMessage(index);
        message = StringUtil.formatByRegex(message, params);
        throw new ServiceException(ApiResponseCodeEnum.SERVICE_ERROR.getValue(), getShowMessage(message), cause);
    }

    private static String getLogMessage(String msg) {
        String[] splits = msg.split("::");
        return splits.length > 1 ? splits[1] : splits[0];
    }

    private static String getShowMessage(String msg) {
        String[] splits = msg.split("::");
        return splits[0];
    }
}
