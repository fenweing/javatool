package com.tuanbaol.util;

/**
 * @description: Predicate
 * @author: fw
 * @date: 2019/12/17 11:11
 * @version: 1.0.0
 */
public final class Predicate {
    private Predicate() {
    }


    public static void isTrue(boolean condition, VoidOperator operator) {
        if (condition) {
            operator.operate();
        }
    }

    public static void isTrueElse(boolean condition, VoidOperator trueOperator, VoidOperator falseOperator) {
        if (condition) {
            trueOperator.operate();
        } else {
            falseOperator.operate();
        }
    }

    public static void isFalse(boolean condition, VoidOperator operator) {
        if (!condition) {
            operator.operate();
        }
    }

    public static <T> T isTrueReturn(boolean condition, Class<T> clazz, ReturnOperator operator) {
        if (condition) {
            return (T) operator.operate();
        }
        return null;
    }

    public static <T> T isFalseReturn(boolean condition, Class<T> clazz, ReturnOperator operator) {
        if (!condition) {
            return (T) operator.operate();
        }
        return null;
    }

    public interface VoidOperator {
        void operate();
    }

    public interface ReturnOperator {
        Object operate();
    }
}
