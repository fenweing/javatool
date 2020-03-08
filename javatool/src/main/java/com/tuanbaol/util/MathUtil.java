package com.tuanbaol.util;

import java.math.BigDecimal;

/**
 * @description: MathUtil
 * @author: fw
 * @time: 2019/11/5 12:52
 */
public class MathUtil {
    public static float computeRatio(long numerator, long denominator, int digit) {
        AssertUtil.isTrue(0 == denominator, "denominator can not be zero.");
        BigDecimal numDecimal = BigDecimal.valueOf(numerator);
        BigDecimal denDecimal = BigDecimal.valueOf(denominator);
        return numDecimal.divide(denDecimal, digit, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float computePercentage(long numerator, long denominator, int digit) {
        AssertUtil.isTrue(0 == denominator, "denominator can not be zero.");
        BigDecimal numDecimal = BigDecimal.valueOf(numerator * 100);
        BigDecimal denDecimal = BigDecimal.valueOf(denominator);
        return numDecimal.divide(denDecimal, digit, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float handleDigit(Double db, int digit) {
        AssertUtil.notNull(db, "parameter can not be null.");
        BigDecimal bigDecimal = BigDecimal.valueOf(db);
        return bigDecimal.setScale(digit, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
