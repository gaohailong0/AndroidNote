package com.gaohailong.app.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qingw on 2016/11/13 0013.
 */

public class MathUtil {
    /***
     * 四舍五入，保留两位小数
     *double d = 0.200;
     * DecimalFormat df = new DecimalFormat("0.00");
     * System.out.println(df.format(d));
     * @param f
     * @return
     */
    public static String get2(double f) {
        if (Double.isNaN(f)) {
            return "0.00";
        } else {
            double d = Double.parseDouble(String.valueOf(f));
            if (Double.isInfinite(d) || Double.isNaN(d)) {
                return "0.00";
            } else {
                DecimalFormat df = new DecimalFormat("0.00");
                return df.format(d);

            }
        }
    }

    /***
     * 保留一位小数、 Math.round(12.5)
     * @param f
     * @return
     */
    public static String get1(double f) {

        if (Double.isNaN(f)) {
            return "0.0";
        } else {
            double d = Double.parseDouble(String.valueOf(f));
            if (Double.isInfinite(d) || Double.isNaN(d)) {
                return "0.0";
            } else {
                DecimalFormat df = new DecimalFormat("0.0");
                return df.format(d);

            }
        }
    }

    /***
     * 如果double的小数点后最后一位是有0，则去掉0
     *
     * @return
     */
    public static String doubleTrans(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }

    /**
     * 去0并且去除科学记数法
     *
     * @param d
     * @return
     */
    public static String getBigDecimalStrTrans(double d) {
        d = Double.valueOf(doubleTrans(d));
        if (d > 0.0) {
            BigDecimal bd = new BigDecimal(String.valueOf(d));
            return bd.stripTrailingZeros().toPlainString();
        }
        return "0";
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if (pattern != null) {
            if (!TextUtils.isEmpty(str)) {
                str = str.trim();
                Matcher isNum = pattern.matcher(str);
                if (!isNum.matches()) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    /*
     *
     * 除法保留两位小数
     *
     */
    public static float divide(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return (float) b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /*
     *
     * 返回两位小数的double类型
     *
     * */
    public static double getDouble(Double d) {
        BigDecimal big = BigDecimal.valueOf(d);
        d = big.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    /*
     * 产生两个数之间的随机数，请注意左开右闭原则
     * */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        int cha = Math.abs(max - min);
        int result = 0;
        if (cha <= 1) {
            return min;
        } else {
            if (min > max) {
                result = random.nextInt(cha) + max;
                if (max < result) {
                    return result;
                }
            }
            if (min < max) {
                result = random.nextInt(cha) + min;
                if (result > min) {
                    return result;
                }
            }
        }
        return result;
    }

}
