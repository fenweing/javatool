package com.parrer.util;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * @description: LogUtil
 * @author: parrer
 * @time: 2019/11/12 17:33
 */
public class LogUtil {
    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);
    private static DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();

    public static void apiEntry(Object... params) {
        params = CollectionUtil.nullToEmptyString(params);
        try {
            Method callingMethod = getCallingMethod(3);
            if (null == callingMethod) {
                return;
            }
            Object[] printParams = null;
            String defaultNote = callingMethod.getDeclaringClass().getName() + "." + callingMethod.getName() + "接口进入";
            String note = defaultNote + "接口进入";
            if (!ArrayUtils.isEmpty(params)) {
                String[] parameterNames = discover.getParameterNames(callingMethod);
                if (ArrayUtils.isNotEmpty(parameterNames)) {
                    note += "，参数列表：";
                    int min = Math.min(params.length, parameterNames.length);
                    for (int i = 0; i < min; i++) {
                        note += parameterNames[i] + "-【{}】，";
                    }
                    printParams = ArrayUtils.subarray(params, 0, min);
                    note = note.substring(0, note.length() - 1);
                }
            }
            if (ArrayUtils.isNotEmpty(printParams)) {
                logger.info(note, params);
            } else {
                logger.info(note + "，无参。");
            }
        } catch (Exception e) {
            logger.error("打印方法入参日志失败，采用默认打印方式", e);
            if (ArrayUtils.isEmpty(params)) {
                logger.info("方法进入，无参。");
            } else {
                StringBuilder noteBuilder = new StringBuilder("方法进入，");
                for (int i = 0; i < params.length; i++) {
                    noteBuilder.append("参数" + i + "-【{}】，");
                }
                logger.info(noteBuilder.substring(0, noteBuilder.length() - 1), params);
            }
        }
    }

    /**
     * note:get the first method corresponding to the method name
     * if calling method is a lambda method, like this pattern{lambda$truemethodname$1},this method would
     * find normal method name in stackTrace
     *
     * @return
     */
    public static Method getCallingMethod(int depth) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement targetElemet = stackTraceElements[depth];
        String className = targetElemet.getClassName();
        String methodName = targetElemet.getMethodName();
        if (methodName.contains("lambda$")) {
            methodName = getCallingMethodName(stackTraceElements, depth);
        }
        if (null == methodName) {
            return null;
        }
        Class targetClazz = null;
        try {
            targetClazz = ClassUtils.forName(className, null);
//            targetClazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            //ignore exception
            return null;
        }
        Method[] methods = targetClazz.getDeclaredMethods();
        if (methods.length <= 0) {
            return null;
        }
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    private static String getCallingMethodName(StackTraceElement[] stackTraceElements, int depth) {
        String className = stackTraceElements[depth].getClassName();
        for (int dep = depth + 1; dep < stackTraceElements.length; dep++) {
            StackTraceElement element = stackTraceElements[dep];
            if (element.getClassName().equals(className) && !element.getMethodName().contains("lambda$")) {
                return element.getMethodName();
            }
        }
        return null;
    }

}
