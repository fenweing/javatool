package com.parrer.util;

import java.math.BigDecimal;

/**
 * @description: MathUtil
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

    /**
     * 计算增长比值
     * 上期不存在或为0时：本期不存在或为0->0，本期大于0->100
     * 上期大于0：本期不存在或为0->-100，本期大于0->（本期-上期）/上期
     *
     * @param preValue
     * @param curValue
     * @return double
     */
    private static double computeRatio(BigDecimal preValue, BigDecimal curValue) {
        if (null == preValue || preValue.floatValue() == 0) {
            if (null == curValue || curValue.floatValue() == 0) {
                return 0;
            } else {
                return 100;
            }
        } else {
            if (null == curValue || curValue.floatValue() == 0) {
                return -100;
            } else {
                BigDecimal decimalValue = curValue.subtract(preValue).multiply(new BigDecimal(100)).divide(preValue, 2, BigDecimal.ROUND_HALF_UP);
                return decimalValue.doubleValue();
            }
        }
    }
}
