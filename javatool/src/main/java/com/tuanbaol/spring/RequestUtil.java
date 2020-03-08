package com.tuanbaol.spring;

import com.tuanbaol.util.AssertUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @description: RequestUtil
 * @author: fw
 * @time: 2019/11/4 18:53
 */
public class RequestUtil {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        AssertUtil.notNull(attrs, "当前线程未绑定request上下文");
        return attrs.getRequest();
    }

    public static Locale getLocale() {
        return getRequest().getLocale();
    }
}
